package com.cjhxfund.foundation.util.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.AppConfig;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.web.context.SpringContext;

public class NIOServer {
	// 用于检测所有Channel状态的Selector
	private Selector selector = null;
	// 定义实现编码、解码的字符集对象
	private Charset charset = Charset.forName("UTF-8");

	private static  JLogger logger = SysLogger.getLogger();
	
	//消息处理类
	private ISocketMsg msg;

	public void init(ISocketMsg msg) throws IOException {
		this.setMsg(msg);
		selector = Selector.open();
		// 通过open方法来打开一个未绑定的ServerSocketChannel实例
		ServerSocketChannel server = ServerSocketChannel.open();
		String ip = SpringContext.getLocalIp();
//		ip = "10.201.224.162";
		String webSocketPort = AppConfig.getProperty(PathUtil.getSrcPath("jdbc.properties"), "websocket.port");
		InetSocketAddress isa = new InetSocketAddress(ip, Integer.valueOf(webSocketPort.trim()));
		logger.info("socket address :"+ip+":"+webSocketPort);
		// 将该ServerSocketChannel绑定到指定ip地址
		server.socket().bind(isa);
		// 设置ServerSocket以非阻塞方式工作
		server.configureBlocking(false);
		// 将server注册到指定Selector对象
		server.register(selector, SelectionKey.OP_ACCEPT);
		String content = "";
		
		while (selector.select() > 0) {
			// 依次处理selector上的每个已选择的SelectionKey
			for (SelectionKey sk : selector.selectedKeys()) {
				// 从selector上的已选择Key集中删除正在处理的SelectionKey
				selector.selectedKeys().remove(sk);
				// 如果sk对应的通信包含客户端的连接请求
				if (sk.isAcceptable()) {
					// 调用accept方法接受连接，产生服务器端对应的SocketChannel
					SocketChannel sc = server.accept();
					// 设置采用非阻塞模式
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
					// 将sk对应的Channel设置成准备接受其他请求
					sk.interestOps(SelectionKey.OP_ACCEPT);
				}
				// 如果sk对应的通道有数据需要读取
				if (sk.isReadable()) {
					// 获取该SelectionKey对应的Channel，该Channel中有可读的数据
					SocketChannel sc = (SocketChannel) sk.channel();
					// 定义准备之星读取数据的ByteBuffer
					ByteBuffer buff = ByteBuffer.allocate(1024);
					
					// 开始读取数据
					try {
						while (sc.read(buff) > 0) {
							buff.flip();
							content += charset.decode(buff);
						}
						// 打印从该sk对应的Channel里读到的数据
//						System.out.println("Client receive:" + content);
						msg.dealMsg(content);
						// 将sk对应的Channel设置成准备下一次读取
						sk.interestOps(SelectionKey.OP_READ);
						// 如果捕捉到该sk对应的channel出现异常，即表明该channel对应的client出现了
						// 异常，所以从selector中取消sk的注册
					} catch (IOException e) {
						// 从Selector中删除指定的SelectionKey
						sk.cancel();
						if (sk.channel() != null) {
							sk.channel().close();
						}
					}
					
					content = msg.getSendMsg();
					// 如果content的长度大于0，即聊天信息不为空
					if (content.length() > 0) {
						// 遍历该selector里注册的所有SelectKey
						for (SelectionKey key : selector.keys()) {
							// 选取该key对应的Channel
							Channel targetChannel = key.channel();
							// 如果该channel是SocketChannel对象
							if (targetChannel instanceof SocketChannel) {
								// 将独到的内容写入该Channel中
								SocketChannel dest = (SocketChannel) targetChannel;
								dest.write(charset.encode(content));
//								System.out.println("server send:" + content);
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new NIOServer().init(new TestMsg());
	}

	public ISocketMsg getMsg() {
		return msg;
	}

	public void setMsg(ISocketMsg msg) {
		this.msg = msg;
	}
}
