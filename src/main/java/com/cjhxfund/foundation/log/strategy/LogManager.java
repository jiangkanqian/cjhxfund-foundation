package com.cjhxfund.foundation.log.strategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cjhxfund.foundation.log.constants.Constant;
import com.cjhxfund.foundation.log.utils.CommUtil;
import com.cjhxfund.foundation.log.utils.TimeUtil;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.model.WSSession;

/**
 * 日志管理线程
 * @version 2015/10/31
 */
public class LogManager extends Thread {
	
	/** 单例 */
	private static LogManager instance = null;
	
	/** 日志文件列表 */
	private static Map<String,LogFileItem> logFileMap = new ConcurrentHashMap<String,LogFileItem>();
	
	/** 日志写入的间隔时间 */
	public final static long WRITE_LOG_INV_TIME = CommUtil.getConfigByLong("WRITE_LOG_INV_TIME", 1000);
	
	/** 单个日志文件的大小(默认为10M) */
	public final static long SINGLE_LOG_FILE_SIZE = CommUtil.getConfigByLong("SINGLE_LOG_FILE_SIZE", 1024*1024*10);
	
	/** 缓存大小(默认10KB) */
	public final static long SINGLE_LOG_CACHE_SIZE = CommUtil.getConfigByLong("SINGLE_LOG_CACHE_SIZE", 1024*10);
	
	/** 是否运行 */
	private boolean bIsRun = true ;
	
	private final static String SEP = File.separator;
	
	public LogManager(){
		
	}
	
	/**
	 * 获得日志管理类单例
	 */
	public synchronized static LogManager getInstance(){
		if(instance == null){
			instance = new LogManager();
			instance.setName("FLogger");
			instance.start();
		}
		return instance;
	}
	
	public void removeStart(){
		for(Entry<String, LogFileItem> entry: logFileMap.entrySet()){
			if(entry.getKey().contains("StartLogger-")){
				logFileMap.remove(entry.getKey());
//				System.out.println("移除："+entry.getKey());
			}
		}
	}
	
	/**
	 * 添加日志
	 * @param logFileName  日志文件名称
	 * @param logMsg      日志内容
	 * @param class1 
	 */
	public void addLog(String logFileName,StringBuffer logMsg){
		//获得单个日志文件的信息
		LogFileItem lfi = logFileMap.get(logFileName);
		if(lfi == null){
			synchronized(this){
				lfi = logFileMap.get(logFileName);
				if(lfi == null){
					lfi = new LogFileItem();
					lfi.logFileName = logFileName;
					lfi.nextWriteTime = System.currentTimeMillis() + WRITE_LOG_INV_TIME;
					logFileMap.put(logFileName, lfi);
				}
			}
		}
		//同步单个文件的日志
		synchronized(lfi){
			if(lfi.currLogBuff == 'A'){
				lfi.alLogBufA.add(logMsg);
			}else{
				lfi.alLogBufB.add(logMsg);
			}
			lfi.currCacheSize += CommUtil.StringToBytes(logMsg.toString()).length;
		}
		dealMsg(logMsg.toString());
	}
	
	private void dealMsg(String msg) {
		// 遍历服务器关联的所有客户端
		Set<Entry<String, WSSession>> set = CommonCache.sessionMap.entrySet();
		WSSession session = null;
		for (Entry<String, WSSession> entry : set) {
			session = entry.getValue();
			try {
				if (!session.isOpen()) {
					CommonCache.sessionMap.remove(entry.getKey());
				} else {
					session.sendText(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 线程方法 */
	public void run(){
//		int i = 0 ;
		while(bIsRun){
			try{
				//输出到文件
				flush(true);
                Thread.sleep(WRITE_LOG_INV_TIME);
			}catch(Exception e){
				System.out.println("开启日志服务错误...");
	            e.printStackTrace();
			}
		}
	}
	
	/** 关闭方法 */
    public void close(){
    	bIsRun = false;
    	try{
    		flush(true);
    	}catch(Exception e){
    		System.out.println("关闭日志服务错误...");
            e.printStackTrace();
    	}
    }
    
    /**
     * 输出缓存的日志到文件
     * @param bIsForce 是否强制将缓存中的日志输出到文件
     */ 
    private void flush(boolean bIsForce) throws IOException{
//    	long currTime = System.currentTimeMillis();
    	Iterator<String> iter = logFileMap.keySet().iterator();
    	while(iter.hasNext()){
    		LogFileItem lfi = logFileMap.get(iter.next());
    		//获得需要进行输出的缓存列表
    		ArrayList<StringBuffer> alWrtLog = null;
    		synchronized(lfi){
    			if(lfi.currLogBuff == 'A'){
    				alWrtLog = lfi.alLogBufA;
    				lfi.currLogBuff = 'B';
    			}else{
    				alWrtLog = lfi.alLogBufB;
    				lfi.currLogBuff = 'A';
    			}
//    			lfi.currCacheSize = 0;
    		}
    		//创建日志文件
    		createLogFile(lfi);
    		//输出日志，如果缓存中有数据
    		if( lfi.currCacheSize > 0){
//    			System.out.println(lfi.currCacheSize);
    			int iWriteSize = writeToFile(lfi.fullLogFileName,alWrtLog);
    			lfi.currLogSize += iWriteSize;
    			//写完之后再清除
    			lfi.currCacheSize = 0;
    		}
    	}
    }
    
    /**
     * 创建日志文件
     * @param lfi
     */
    private void createLogFile(LogFileItem lfi){
    	//当前系统日期
    	String currPCDate = TimeUtil.getPCDate('-');
    	
    	//判断日志root路径是否存在，不存在则先创建
    	File rootDir = new File(Constant.CFG_LOG_PATH);
    	if(!rootDir.exists() || !rootDir.isDirectory()){
    		rootDir.mkdirs();
    	}
    	
    	//如果超过单个文件大小，则拆分文件
    	if(lfi.fullLogFileName != null && lfi.fullLogFileName.length() > 0 && lfi.currLogSize >= LogManager.SINGLE_LOG_FILE_SIZE ){
    		File oldFile = new File(lfi.fullLogFileName);
    		if(oldFile.exists()){
        		String newFileName = Constant.CFG_LOG_PATH + SEP + lfi.lastPCDate + SEP + lfi.logFileName + "_" + TimeUtil.getPCDate() + "_"+ TimeUtil.getCurrTime() + ".log";
    			File newFile = new File(newFileName);
    			boolean flag = oldFile.renameTo(newFile);
    			System.out.println("日志已自动备份为 " + newFile.getName() + ( flag ? "成功!" : "失败!" ) );
    			lfi.fullLogFileName = "";
    			lfi.currLogSize = 0;
    			//等待1s中，让文件操作成功
    			try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    	//创建文件
    	if ( lfi.fullLogFileName == null || lfi.fullLogFileName.length() <= 1 || !lfi.lastPCDate.equals(currPCDate)){
    		String sDir = Constant.CFG_LOG_PATH + SEP + currPCDate ;
    		File file = new File(sDir);
    		if(!file.exists()){
    			file.mkdirs();
    		}
    		lfi.fullLogFileName = sDir + SEP + lfi.logFileName + ".log";
    		lfi.lastPCDate = currPCDate;
    		file = new File(lfi.fullLogFileName);
    		if(file.exists()){
    			lfi.currLogSize = file.length();
    		}else{
    			lfi.currLogSize = 0;
    		}
    	}
    }
    
    /**
     * 输出日志到文件
     * @param sFullFileName 完整的日志文件名称
     * @param sbLogMsg      日志文件内容
     * @return 返回输出内容大小
     */
    private int writeToFile(String sFullFileName,ArrayList<StringBuffer> sbLogMsg) throws IOException{
    	int size = 0;
    	OutputStream fout = null;
    	try{
    	    fout = new FileOutputStream(sFullFileName, true);	
    	    for(int i = 0; i < sbLogMsg.size(); i++){
                StringBuffer logMsg = sbLogMsg.get(i);
                byte[] tmpBytes = CommUtil.StringToBytes(logMsg.toString());
                fout.write(tmpBytes);
                size += tmpBytes.length;
             }
            fout.flush();
            sbLogMsg.clear();
    	}catch(Exception e){
    	    e.printStackTrace();
    	}finally{
    		if(fout != null){
    			fout.close();
    		}
    	}
    	return size;
    }
    
}
