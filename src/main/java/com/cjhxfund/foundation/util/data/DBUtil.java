package com.cjhxfund.foundation.util.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBUtil {

	private static Log log = LogFactory.getLog(DBUtil.class);

	// 获取连接
	public static Connection getCon(Map<String, String> dbBean) {
		Connection con = null;
		try {
			Class.forName(dbBean.get("driver"));
			con = DriverManager.getConnection(dbBean.get("url"), dbBean.get("user"), dbBean.get("password"));
			log.debug("连接成功……");
			return con;
		} catch (SQLException e) {
			log.error("连接失败……");
			e.printStackTrace();
			return null;

		} catch (ClassNotFoundException e) {
			log.error("驱动类没找到！");
			e.printStackTrace();
			return null;
		}
	}

	

	// 释放资源
	public static void release(Object o) {
		if (o == null) {
			return;
		}
		if (o instanceof ResultSet) {
			try {
				((ResultSet) o).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (o instanceof Statement) {
			try {
				((Statement) o).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (o instanceof Connection) {
			Connection c = (Connection) o;
			try {
				if (!c.isClosed()) {
					c.close();
					log.debug("成功关闭数据库连接！");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("关闭数据库连接失败！");
			}
		}

	}

	public static void close(Connection con, Statement stmt, ResultSet rs) {
		if (con != null) {
			release(con);
		}
		if (stmt != null) {
			release(stmt);
		}
		if (rs != null) {
			release(rs);
		}
	}
	
	public static void close(Connection con, Statement stmt) {
		if (con != null) {
			release(con);
		}
		if (stmt != null) {
			release(stmt);
		}
	}


	public static void main(String[] args) throws SQLException {

//		Dbsource db1 = new Dbsource();
//		db1.setDbType("mysql");
//		db1.setSid("wechat");
//		db1.setUserName("root");
//		db1.setPwd("root");
//		db1.setUrl("localhost:3306");
		

	}

}
