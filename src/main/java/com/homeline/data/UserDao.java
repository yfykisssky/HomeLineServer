package com.homeline.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.http.util.TextUtils;

public class UserDao extends BaseDao {

	private static final String TABLE_NAME = "users";

	public UserDao() throws SQLException, ClassNotFoundException {
	
	}

	public int checkPswd(String user, String pswd) throws ClassNotFoundException, SQLException {

		int stateCode = -1;
		String sql = "select pswd from users where name=\"" + user + "\";";
		ResultSet rs = exeSqlQuery(sql);

		String password = null;
		while (rs.next()) {
			password = rs.getString("pswd");
		}

		if (!TextUtils.isEmpty(password)) {
			if (password.equals(pswd)) {
				stateCode = 0;
			} else {
				stateCode = 2;
			}
		}

		releaseAll();

		return stateCode;
	}

	public void addUser(String user, String pswd) throws SQLException, ClassNotFoundException {

		String sql = "insert into " + TABLE_NAME + " values(\"" + user + "\",\"" + pswd + "\");";
		exeSql(sql);
		releaseAll();

	}

	public void removeUser(String user) throws SQLException, ClassNotFoundException {

		String sql = "delete from users where name=\"" + user + "\";";
		exeSql(sql);
		releaseAll();

	}

	public boolean checkUserExit(String user) throws ClassNotFoundException, SQLException {

		String sql = "select name from users where name=\"" + user + "\";";
		ResultSet rs = exeSqlQuery(sql);

		boolean hasUser = false;

		if (rs.next()) {
			hasUser = true;
		}

		releaseAll();
		return hasUser;
	}

	public void changePswd(String user, String pswd) throws SQLException, ClassNotFoundException {

		String sql = "update users set pswd=\"" + pswd + "\" where name=\"" + user + "\";";
		exeSql(sql);
		releaseAll();

	}

}
