package com.homeline.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.java_websocket.WebSocket;

import com.homeline.Debug;
import com.homeline.data.TemKeyData;
import com.homeline.tool.AESHelper;

public class WsPool {

    private static final Map<WsServer, String> wsUserMap = new HashMap<WsServer, String>();

    //通过websocket连接获取其对应的用户
    public static String getUserByWs(WsServer conn) {
        return wsUserMap.get(conn);
    }

    //因为有可能多个websocket对应一个userName（但一般是只有一个，因为在close方法中，我们将失效的websocket连接去除了）
    public static WsServer getWsByUser(String userName) {
        Set<WsServer> keySet = wsUserMap.keySet();
        synchronized (keySet) {
            for (WsServer conn : keySet) {
                String cuser = wsUserMap.get(conn);
                if (cuser.equals(userName)) {
                    return conn;
                }
            }
        }
        return null;
    }

    //向连接池中添加连接
    public static void addUser(String userName, WsServer conn) {
        wsUserMap.put(conn, userName); // 添加连接
    }

    //获取所有连接池中的用户，因为set是不允许重复的，所以可以得到无重复的user数组
    public static Collection<String> getOnlineUser() {
        List<String> setUsers = new ArrayList<String>();
        Collection<String> setUser = wsUserMap.values();
        for (String u : setUser) {
            setUsers.add(u);
        }
        return setUsers;
    }

    //向特定的用户发送数据
    public static void sendMessageToUser(String username, String message) throws IOException {
        WsServer ws = getWsByUser(username);
        if (null != ws) {
            ws.session.getBasicRemote().sendText(message);
        }
    }

    //向所有的用户发送消息
    public static void sendMessageToAll(String message) throws IOException {
        Set<WsServer> keySet = wsUserMap.keySet();
        synchronized (keySet) {
            for (WsServer conn : keySet) {
                String user = wsUserMap.get(conn);
                if (user != null) {
                    conn.session.getBasicRemote().sendText(message);
                }
            }
        }
    }

    public static boolean removeUser(WsServer conn) {
        if (wsUserMap.containsKey(conn)) {
            wsUserMap.remove(conn); // 移除连接
            return true;
        } else {
            return false;
        }
    }
}
