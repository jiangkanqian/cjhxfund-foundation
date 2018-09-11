package com.cjhxfund.foundation.util.thread;

import com.cjhxfund.foundation.util.io.CmdUtil;

public class Windows {

	public static void main(String[] args) {
//		String systemInfo = CmdUtil.cmd("systeminfo");
//		String cpuBrief = CmdUtil.cmd("wmic cpu list brief");
		String tasklist = CmdUtil.cmd("tasklist");
		System.out.println(tasklist);
	}
}
