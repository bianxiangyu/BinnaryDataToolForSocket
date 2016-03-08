package com.centling.radio.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author paul
 *
 */
public class SocketServer {
    volatile boolean timeReceiveTimeOut = false;
    private final static Logger Log = LoggerFactory.getLogger(SocketServer.class);
    public static final Integer INSTRUCTION_SOCKET_STATION_PORT = 3000;
    public final static Integer TASK_STATE_SOCKET_CLIENT_PORT = 4000;
    public static final Integer TASK_ADD_SOCKET_CLIENT_PORT = 5000;
    public static final Integer WARNING_SOCKET_STATION_PORT = 2000;
    public static final Integer DATA_IMPORT_CLIENT_PORT = 6000;
    public final static Integer HEART_BEAT_PORT = 7000;
    private ServerSocket serverSocket = null;

    // ServerSocket初始化
    public SocketServer(Integer port) {
	try {
	    serverSocket = new ServerSocket(port);
	} catch (IOException e) {
	    Log.error("socketServer:[{}]服务无法建立，检查端口是否被占用", port, e);
	}
    }

    // 捕获客户端的socket代理
    public SocketExtend acceptSocket() {
	if (serverSocket == null) {
	    Log.error("socketServer:[{}]服务未建立", serverSocket);
	    return null;
	}
	try {
	    Socket socket = serverSocket.accept();
	    Log.info("[{}]捕获客户端[{}]:[{}]连接请求",
		    new Object[] { socket.getLocalPort(), socket.getInetAddress(), socket.getPort() });
	    SocketExtend socketExtend = new SocketExtend(socket);
	    socketExtend.setInfo(SocketExtend.SOCKET_SERVER);
	    return socketExtend;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    // 向客户端发送数据
    public boolean sendDataToClient(byte[] data, SocketExtend socketExtend) throws IOException {
	return socketExtend.sendData(data);
    }

    // 从客户端接收数据

    /**
     * 
     * @param socket
     * @param timeWaitBegin
     * @param timeWaitEnd
     * @return byte[]
     */
    public byte[] receiveDataFromClient(SocketExtend socketExtend, final int timeWaitBegin, final int timeWaitEnd)throws IOException{
	return socketExtend.receiveData(timeWaitBegin, timeWaitEnd);
    }

    public boolean isConnecting(SocketExtend socketExtend) {
	Log.info("Server");
	return socketExtend.isConnecting();
    }

    public void close() {
	if (serverSocket != null) {
	    try {
		serverSocket.close();
		Log.info("SocketServer[{}]:[{}]:服务关闭!",serverSocket.getInetAddress(),serverSocket.getLocalPort());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    public boolean destoryConnect(SocketExtend socketExtend) {
	return socketExtend.destoryConnect();
    }
}
