/**
 * 
 */
package com.centling.radio.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.socket.code.BaseTcpMesgDecode;
import com.centling.radio.socket.model.ByteArray;

/**
 * @author lenovo
 *
 */
public class SocketExtend {
    private Socket socket;
    public final static String SOCKET_CLIENT = "SocketClient";
    public final static String SOCKET_SERVER = "SocketServer";
    private String info = "Socket";
    private ByteArrayOutputStream byteBuff = new ByteArrayOutputStream(0);
    private final static Logger Log = LoggerFactory.getLogger(SocketExtend.class);
    private ConcurrentLinkedQueue<ByteArray> receive = new ConcurrentLinkedQueue<ByteArray>();
    private ConcurrentLinkedQueue<ByteArray> impRecBuff = new ConcurrentLinkedQueue<ByteArray>();

    public SocketExtend(Socket socket) {
	try {
	    socket.setKeepAlive(true);
	} catch (SocketException e) {
	    e.printStackTrace();
	}
	this.socket = socket;
    }

    public ConcurrentLinkedQueue<ByteArray> getRecBuff() {
	return this.receive;
    }

    public boolean receiveDataToBuff(int timeWaitBegin) throws IOException {
	if (!this.isConnecting()) {
	    Log.error("网络连接未建立，无法接受数据");
	    return false;
	}
	/*
	 * Log.info("{}--[{}][{}]:准备读取数据,waitFirst[{}]ms,waitEnd[{}]ms", new
	 * Object[] { info, socket.getInetAddress(), socket.getPort(),
	 * timeWaitBegin, timeWaitEnd });
	 */
	socket.setSoTimeout(timeWaitBegin);
	// ByteArrayOutputStream byteBuff = new ByteArrayOutputStream(0);
	InputStream inputStream = socket.getInputStream();
	while (true) {
	    try {
		byte[] resultPic = new byte[1024];
		int hasRead = inputStream.read(resultPic);
		if (hasRead > 0) {
		    byteBuff.write(resultPic, 0, hasRead);
		    this.takeUseData();
		}
	    } catch (SocketTimeoutException timeout) {
		return false;
	    }
	}
    }

    private void takeUseData() {
	BaseTcpMesgDecode baseTcpMesgDecode = new BaseTcpMesgDecode(byteBuff.toByteArray());
	byteBuff.reset();
	byte[] res = baseTcpMesgDecode.getOnePiece();
	while (res != null) {
	    if (res.length < 50) {
		impRecBuff.add(ByteArray.valueOf(res));
	    } else {
		receive.add(ByteArray.valueOf(res));
	    }
	    res = baseTcpMesgDecode.getOnePiece();

	}
	byte[] remain = baseTcpMesgDecode.getRemain();
	if (remain != null) {
	    try {
		byteBuff.write(remain);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    // 从服务端接收数据
    public byte[] receiveData(final int timeWaitBegin, final int timeWaitEnd) throws IOException {
	if (!this.isConnecting()) {
	    Log.error("网络连接未建立，无法接受数据");
	    throw new IOException();
	}
	/*
	 * Log.info("{}--[{}][{}]:准备读取数据,waitFirst[{}]ms,waitEnd[{}]ms", new
	 * Object[] { info, socket.getInetAddress(), socket.getPort(),
	 * timeWaitBegin, timeWaitEnd });
	 */
	socket.setSoTimeout(timeWaitBegin);
	// ByteArrayOutputStream byteBuff = new ByteArrayOutputStream(0);
	InputStream inputStream = socket.getInputStream();
	ByteArrayOutputStream byteBuffOnce = new ByteArrayOutputStream();
	while (true) {
	    try {
		byte[] resultPic = new byte[1024];
		int hasRead = inputStream.read(resultPic);
		if (hasRead > 0) {
		    byteBuffOnce.write(resultPic, 0, hasRead);
		    byteBuffOnce.write(resultPic, 0, resultPic.length);
		    this.takeUseData();
		    if (timeWaitBegin <= 0) {
			return byteBuffOnce.toByteArray();
		    }
		    if (timeWaitEnd <= 0) {
			if (inputStream.available() == 0) {
			    return byteBuffOnce.toByteArray();
			}
			socket.setSoTimeout(1);
		    } else {
			socket.setSoTimeout(timeWaitEnd);
		    }

		}
	    } catch (SocketTimeoutException timeout) {
		try {
		    if (socket.getSoTimeout() < timeWaitBegin) {
			/*
			 * Log.info("{}:已经等待更多的响应字节[{}]ms，即将返回", info,
			 * timeWaitEnd);
			 */
			BaseTcpMesgDecode baseTcpMesgDecode = new BaseTcpMesgDecode(byteBuffOnce.toByteArray());
			byteBuffOnce.reset();
			byte[] res = baseTcpMesgDecode.getUsePieces();
			return res;
		    } else {
			// Log.error("{}:已经等待响应[{}]ms，未收到任何响应", new Object[] {
			// info, timeWaitBegin }, timeout);
			return null;
		    }

		} catch (SocketException e) {
		    e.printStackTrace();
		    return null;
		}
	    }
	}
    }

    // 从客户端发送数据
    public boolean sendData(byte[] data) throws IOException {
	if (!this.isConnecting()) {
	    Log.error("连接未建立，无法发送数据");
	    throw new IOException("连接未建立，无法发送数据");
	}
	if (data == null || data.length <= 0) {
	    Log.error("所发送数据不能为空！！");
	    return false;
	}
	/*
	 * Log.info("{}[{}]:[{}]---数据[{}]bytes---->{}[{}]:[{}]", new Object[] {
	 * info, socket.getLocalAddress(), socket.getLocalPort(), data.length,
	 * info, socket.getInetAddress(), socket.getPort() });
	 */
	OutputStream outputStream = socket.getOutputStream();
	outputStream.write(data);
	outputStream.flush();
	return true;

    }

    // 是否正在连接
    public boolean isConnecting() {
	if (socket == null) {
	    Log.error("尚未初始化socket");
	} else {
	    /*
	     * Log.info("{}[{}][{}]状态:connected:[{}]---closed:[{}]", new
	     * Object[] { info, socket.getInetAddress(), socket.getPort(),
	     * socket.isConnected(), socket.isClosed() });
	     */
	}
	return (socket != null) && socket.isConnected() && !socket.isClosed();
    }

    // 连接被摧毁
    public boolean destoryConnect() {
	if (socket != null) {
	    try {
		socket.close();
		Log.info("{}:中断连接！", info);
		return true;
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return false;
    }

    public InetAddress getInetAddress() {
	return socket.getInetAddress();
    }

    public int getPort() {
	return socket.getPort();
    }

    public InetAddress getLocalAddress() {
	return socket.getLocalAddress();
    }

    public int getLocalPort() {
	return socket.getLocalPort();
    }

    public Socket getSocket() {
	return this.socket;
    }

    public void setInfo(String info) {
	this.info = info;
    }

    /**
     * ConcurrentLinkedQueue<ByteArray>
     * 
     * @return
     */
    public ConcurrentLinkedQueue<ByteArray> getImpBuff() {

	return this.impRecBuff;
    }

}
