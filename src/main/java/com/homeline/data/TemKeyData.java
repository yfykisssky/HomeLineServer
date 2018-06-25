package com.homeline.data;

import java.util.ArrayList;
import java.util.List;

public class TemKeyData {

	private static List<KeyData> listKeys = new ArrayList<KeyData>();

	private static final long TIMEOUT = 7 * 24 * 60 * 60 * 1000;

	static class KeyData {

		private String username;

		private String priKey;

		private String aesKey;

		private String token;

		private long timeStamp;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPriKey() {
			return priKey;
		}

		public void setPriKey(String priKey) {
			this.priKey = priKey;
		}

		public String getAesKey() {
			return aesKey;
		}

		public void setAesKey(String aesKey) {
			this.aesKey = aesKey;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public long getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}

	}

	public static KeyData getUserByName(String username) {

		for (int u = 0; u < listKeys.size(); u++) {
			if (listKeys.get(u).getUsername().equals(username)) {
				return listKeys.get(u);
			}
		}

		return null;

	}

	public static void addToKeyList(String username, String priKey) {

		KeyData user = getUserByName(username);

		if (user == null) {
			
			KeyData keyData = new KeyData();

			keyData.setUsername(username);

			keyData.setPriKey(priKey);

			synchronized (listKeys) {
				listKeys.add(keyData);
			}
			
		} else {
			synchronized (listKeys) {
				user.setPriKey(priKey);
			}
		}

	}

	public static String getPriKey(String username) {

		synchronized (listKeys) {
			for (int u = 0; u < listKeys.size(); u++) {
				if (listKeys.get(u).getUsername().equals(username)) {
					return listKeys.get(u).getPriKey();
				}
			}
		}

		return null;

	}

	public static void updateAesKey(String username, String aesKey) {

		synchronized (listKeys) {
			for (int u = 0; u < listKeys.size(); u++) {
				if (listKeys.get(u).getUsername().equals(username)) {
					listKeys.get(u).setAesKey(aesKey);
				}
			}
		}

	}

	public static String getAesKey(String username) {

		synchronized (listKeys) {
			for (int u = 0; u < listKeys.size(); u++) {
				if (listKeys.get(u).getUsername().equals(username)) {
					return listKeys.get(u).getAesKey();
				}
			}
		}

		return null;

	}

	public static void removeFromList(String username) {

		synchronized (listKeys) {
			for (int u = 0; u < listKeys.size(); u++) {
				if (listKeys.get(u).getUsername().equals(username)) {
					listKeys.remove(u);
				}
			}
		}

	}

	public static void removePriFromList(String username) {

		synchronized (listKeys) {
			for (int u = 0; u < listKeys.size(); u++) {
				if (listKeys.get(u).getUsername().equals(username)) {
					listKeys.get(u).setPriKey(null);
				}
			}
		}

	}

	public static void setTokenFromList(String username, String token) {

		synchronized (listKeys) {
			for (int u = 0; u < listKeys.size(); u++) {
				if (listKeys.get(u).getUsername().equals(username)) {
					listKeys.get(u).setToken(token);
					listKeys.get(u).setTimeStamp(System.currentTimeMillis());
				}
			}
		}

	}

	public static String getTokenFromList(String username) {

		synchronized (listKeys) {
			for (int u = 0; u < listKeys.size(); u++) {
				if (listKeys.get(u).getUsername().equals(username)) {
					if (listKeys.get(u).getTimeStamp() + TIMEOUT <= System.currentTimeMillis()) {
						return listKeys.get(u).getToken();
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

}
