package com.homeline.data;

import java.sql.*;

public class BaseDao {

	private Statement st;
	private ResultSet rs;
	private Connection conn;

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		return JdbcPool.getInstance().getConnection();
	}

	protected Statement exeSql(String sql) throws SQLException, ClassNotFoundException {
		conn = getConnection();
		st = conn.createStatement();
		st.execute(sql);
		return st;
	}

	protected ResultSet exeSqlQuery(String sql) throws ClassNotFoundException {
		try {
			conn = getConnection();
			st = conn.createStatement();
			st.execute(sql);
			rs = st.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void releaseAll() throws SQLException {
		release(conn, st, rs);
	}

	public static void release(Connection conn, Statement st, ResultSet rs) throws SQLException {
		if (rs != null) {
			// 关闭存储查询结果的ResultSet对象
			rs.close();
		}
		if (st != null) {
			// 关闭负责执行SQL命令的Statement对象
			st.close();
		}

		if (conn != null) {
			// 关闭Connection数据库连接对象
			conn.close();
		}
	}

}
