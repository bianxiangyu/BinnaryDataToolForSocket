package com.centling.radio.socket;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.socket.model.ByteArray;

public class SocketClient {
    private final static Logger Log = LoggerFactory.getLogger(SocketClient.class);
    public final static String HOST_STRING = "localhost";
    public final static Integer INSTRUCTION_CLIENT_PORT_INT = 4000;
    public final static Integer INSTRUCTION_STATION_PORT_INT = 3000;
    public final static Integer HEART_BEAT_PORT = 7000;
    public final static Integer WARNING_PORT_INT = 2000;
    public static final Integer TASK_ADD_SOCKET_CLIENT_PORT = 5000;
    public static final Integer DATA_IMPORT_CLIENT_PORT = 6000;
    private SocketExtend socketExtend;

    public boolean connect(String hostStr, int port, int msTimeout, int times) {
	msTimeout = msTimeout < 0 ? 0 : msTimeout;
	times = times < 1 ? 1 : times;
	for (int i = 0; i < times; i++) {
	    Log.info("第[{}]次连接", i + 1);
	    Socket socket = new Socket();
	    try {
		SocketAddress inetAddress = new InetSocketAddress(InetAddress.getByName(hostStr), port);
		socket.connect(inetAddress, msTimeout);
		Log.info("客户端[{}]:[{}]连接到服务器[{}]:[{}]", new Object[] { socket.getLocalAddress(), socket.getLocalPort(),
			socket.getInetAddress(), socket.getPort() });
		socketExtend = new SocketExtend(socket);
		socketExtend.setInfo(SocketExtend.SOCKET_CLIENT);
		return true;
	    } catch (UnknownHostException e) {
		Log.error("主机地址[{}]异常", hostStr, e);
		return false;
	    } catch (ConnectException e) {
		Log.error("服务器Socket服务[{}]异常", hostStr + ":" + port, e);
		Integer wait = 1000 * (i + 1) * (i + 1);
		try {
		    Thread.sleep(wait);
		    if (i + 2 <= times) {
			Log.info("[{}]ms后尝试[{}]连接", wait, i + 2);
		    }
		} catch (InterruptedException e1) {
		    e1.printStackTrace();
		}
	    } catch (SocketTimeoutException e) {
		Log.info("第[{}]次连接超时(limit:[{}])", i + 1, msTimeout);
		Log.warn("警告信息:", e);
	    } catch (BindException e) {
		Log.error("客户端端口号被其他程序占用", e);
		return false;
	    } catch (IOException e) {
		Log.error("其他错误", e);
		return false;
	    }finally{
		try {
		    if (!socket.isConnected()) {
			Log.info("socketClient:未能连接到服务端[{}]:[{}],即将关闭连接！",hostStr,port);
			socket.close();
		    }
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	    }
	}
	return false;
    }

    // 从服务端接收数据
    public byte[] receiveDataFromServer (final int timeWaitBegin, final int timeWaitEnd) throws IOException{
	return socketExtend.receiveData(timeWaitBegin, timeWaitEnd);
}
    public boolean receiveDataFromServerToBuff(final int timeWaitBegin) throws IOException{
	return socketExtend.receiveDataToBuff(timeWaitBegin);
    }
    public ConcurrentLinkedQueue<ByteArray> getRecBuff(){
	return socketExtend.getRecBuff();
    }
    // 从客户端发送数据
    public boolean sendDataToServer(byte[] data) throws IOException {
	return socketExtend.sendData(data);
    }

    // 是否正在连接
    public boolean isConnecting() {
	return socketExtend!=null&&socketExtend.isConnecting();
    }

    // 连接被摧毁
    public boolean destoryConnect() {
	return socketExtend.destoryConnect();
    }

    /**
     * ConcurrentLinkedQueue<ByteArray>
     * @return
     */
    public ConcurrentLinkedQueue<ByteArray> getImpBuff() {
	
	return socketExtend.getImpBuff();
    }
}
