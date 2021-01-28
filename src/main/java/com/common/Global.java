package com.common;

import com.corundumstudio.socketio.SocketIOServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 通用类
 *
 * @author Tim
 */
public class Global {
	public static final String FILE_UPLOADPATH = "/FileCenter/upload";//文件上传路径
	public static final String FILE_UPLOADTPL = "/FileCenter/uploadTpl";// 文件上传模板路径
	public static final String FILE_UPTEMPPATH = "/FileCenter/temp";//文件临时上传路径
	public static final String NULLSTRING = "";//""字符串

	public static final String MIDDATAFORMULATEXT = "辅助数据";//辅助数据公式中文。
	public static Map<String,String> isCreatedTable = new HashMap<>();//记录当年是否已经创建了表
	public static final String VERSION = "1.0.1.BUILD-SNAPSHOT";
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期format
	public static final DateFormat year_month = new SimpleDateFormat("yyyy-MM");//日期format
	public static final DateFormat year_month_day = new SimpleDateFormat("yyyy-MM-dd");//日期format
	public static final DateFormat dfpath = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//日期format
	public static final DateFormat year_month_day_no_ = new SimpleDateFormat("yyyyMMdd");//日期format
	public static final DateFormat year_month_day_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期format
	public static final DateFormat year_month_day_time_no_ = new SimpleDateFormat("yyyyMMddHHmmss");//日期format
	public static final DateFormat dtimecode = new SimpleDateFormat("mm_sss");//日期format
	public static final String RESFROM_ROLE = "1";
	public static final String RESFROM_USER = "2";
	//加密次数
    public static final int HASHITERATIONS = 2;
	public static final String  SQLMODEL   = "1";
	public static final String  JSONMODEL  = "2";
	public static final String  DATEMODEL  = "3";
	public static final String FORMULAJSONTOP = "JSON:";
	public static final String FORMULADATETOP = "DATE:";
	public static final int TABLE_MAX_RECORD = 9000000;
	//SocketIO
	public static SocketIOServer NETTY_SOCKET_SERVER;

	/**
	 * 创建UUID
	 *
	 * @return UUID
	 */
    public static String createUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.toUpperCase();
		uuid = uuid.replaceAll("-", Global.NULLSTRING);
		return uuid;
	}

	/**
	 * 关闭JDBC
	 *
	 * @param conn Connection
	 * @param stmt Statement
	 * @param rs ResultSet
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(conn != null) {
				conn.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//记录备份信息，防止重复备份，注意线程安全 记录内容如下  2018-09,true  表示2019-09月份已经备份
	private static Map<String,Boolean> DEPTEMPBACKINFO = null;
	public static synchronized Map<String,Boolean> getDeptEmpRelaMonthBackupInfo() {
		return Global.DEPTEMPBACKINFO;
	}
	public static synchronized void setDeptEmpRelaMonthBackupInfo(Map<String,Boolean> map) {
		Global.DEPTEMPBACKINFO = map;
	}


}
