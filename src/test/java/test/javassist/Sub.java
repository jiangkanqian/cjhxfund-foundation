package test.javassist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cjhxfund.foundation.util.io.FileOpUtil;
import com.cjhxfund.foundation.util.str.JsonUtil;
import com.cjhxfund.foundation.web.model.FileInfo;

public class Sub extends TestSubject{

	public static void main(String[] args) {
		String folder = "E:/workspace/cjhxfund/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/cjhxfund-ips-deploy/admin";
		List<File> fileList = FileOpUtil.getListFiles(folder, "", false);
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		for(File file : fileList){
			FileInfo fi = new FileInfo(file.getName(), file.length(), file.lastModified(), file.getPath());
			fileInfoList.add(fi);
			System.out.println(JsonUtil.toJson(fi));
		}
		
	}
}
