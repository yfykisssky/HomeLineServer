package com.homeline.data;

import com.homeline.tool.PropertiesUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class JdbcPool implements DataSource {

    private static String DRIVER;
    private static String DATA_BASE;
    private static String URL;
    private static String URLSET;
    private static String USERNAME;
    private static String PASSWORD;
    private static int POOL_INIT_SIZE;

    private static JdbcPool instance;
    /**
     * @Field: listConnections 使用LinkedList集合来存放数据库链接，
     * 由于要频繁读写List集合，所以这里使用LinkedList存储数据库连接比较合适
     */
    private static LinkedList<Connection> listConnections = new LinkedList<Connection>();

    JdbcPool() {

        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.JDBC, this);
            DRIVER = prop.getProperty("DRIVER");
            DATA_BASE = prop.getProperty("DATA_BASE");
            URLSET = prop.getProperty("URLSET");
            URL = prop.getProperty("URL") + DATA_BASE + URLSET;
            USERNAME = prop.getProperty("USERNAME");
            PASSWORD = prop.getProperty("PASSWORD");
            POOL_INIT_SIZE = Integer.parseInt(prop.getProperty("POOL_INIT_SIZE"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 数据库连接池的初始化连接数大小
        int jdbcPoolInitSize = POOL_INIT_SIZE;
        // 加载数据库驱动
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jdbcPoolInitSize; i++) {
            Connection conn;
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                // 将获取到的数据库连接加入到listConnections集合中，listConnections集合此时就是一个存放了数据库连接的连接池
                listConnections.add(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

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

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }

    @Override
    public Connection getConnection() {

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
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
