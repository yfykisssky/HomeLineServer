package com.homeline.data;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class JdbcPool implements DataSource {

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DATA_BASE = "home";
	private static final String URL = "jdbc:mysql://localhost:3306/" + DATA_BASE
			+ "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "yfy991830yfy";
	private static final int POOL_INIT_SIZE = 10;

	private static JdbcPool instance;
	/**
	 * @Field: listConnections 使用LinkedList集合来存放数据库链接，
	 *         由于要频繁读写List集合，所以这里使用LinkedList存储数据库连接比较合适
	 */
	private static LinkedList<Connection> listConnections = new LinkedList<Connection>();

	JdbcPool() {

	}

	public static JdbcPool getInstance() {

		if (instance == null) {
			synchronized (JdbcPool.class) {
				if (instance == null) {
					instance = new JdbcPool();
				}
			}
		}

		return instance;

	}

	static {

		String driver = DRIVER;
		String url = URL;
		String username = USERNAME;
		String password = PASSWORD;
		// 数据库连接池的初始化连接数大小
		int jdbcPoolInitSize = POOL_INIT_SIZE;
		// 加载数据库驱动
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < jdbcPoolInitSize; i++) {
			Connection conn;
			try {
				conn = DriverManager.getConnection(url, username, password);
				// 将获取到的数据库连接加入到listConnections集合中，listConnections集合此时就是一个存放了数据库连接的连接池
				listConnections.add(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * 获取数据库连接
	 * 
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {

		// 如果数据库连接池中的连接对象的个数大于0
		if (listConnections.size() > 0) {
			// 从listConnections集合中获取一个数据库连接
			synchronized (listConnections) {
				final Connection conn = listConnections.removeFirst();
				// 返回Connection对象的代理对象
				return (Connection) Proxy.newProxyInstance(JdbcPool.class.getClassLoader(),
						conn.getClass().getInterfaces(), new InvocationHandler() {
							@Override
							public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
								if (!method.getName().equals("close")) {
									return method.invoke(conn, args);
								} else {
									// 如果调用的是Connection对象的close方法，就把conn还给数据库连接池
									listConnections.add(conn);
									return null;
								}
							}
						});
			}
		} else {
			throw new RuntimeException("对不起，数据库忙");
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
