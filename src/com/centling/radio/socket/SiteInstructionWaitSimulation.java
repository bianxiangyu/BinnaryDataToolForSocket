package com.centling.radio.socket;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.socket.code.BaseTcpMesgDecode;
import com.centling.radio.socket.model.RequestParameter;
import com.centling.radio.utils.PropertyUtils;

public class SiteInstructionWaitSimulation implements Runnable {
    private Logger Log = LoggerFactory.getLogger(SiteInstructionWaitSimulation.class);
    RequesResponsetMap responseForRequest = new RequesResponsetMap();
    private int serverPort;
    private int receiveTimeOut;
    private SocketServer socketServer;
    private int receiveEndOut;
    boolean ifHeartBeat = false;
    private AtomicInteger funcid = new AtomicInteger();
    private SendDataThread oldSendDataThread;
    private ReceiveDataThread oldReceiveDataThread;

    public SiteInstructionWaitSimulation(int serverPort, int receiveTimeOut, int receiveEndWait) {
	this.serverPort = serverPort;
	this.receiveTimeOut = receiveTimeOut;
	this.receiveEndOut = receiveEndWait;
	socketServer = new SocketServer(this.serverPort);
    }

    public SiteInstructionWaitSimulation(int serverPort, int receiveTimeOut, int receiveEndWait, boolean ifHeartBeat) {
	this.serverPort = serverPort;
	this.receiveTimeOut = receiveTimeOut;
	this.receiveEndOut = receiveEndWait;
	this.ifHeartBeat = ifHeartBeat;
	socketServer = new SocketServer(this.serverPort);
    }

    private class ReceiveDataThread extends Thread {
	private SocketExtend socket;
	private int timeOut = 10;
	private volatile boolean end = false;

	public ReceiveDataThread(SocketExtend socket) {
	    this.socket = socket;
	}

	public void end() {
	    this.end = true;
	}

	@Override
	public void run() {
	    while (!ifHeartBeat && !end) {
		byte[] instrByte = null;
		try {
		    instrByte = socket.receiveData(7000, 5);
		} catch (IOException e1) {
		    Log.info("网络连接异常,无法读取数据，即将返回");
		    e1.printStackTrace();
		    return;
		}
		if (instrByte == null) {
		    continue;
		}
		Integer funcidCpy = BaseTcpMesgDecode.getFunctionInt(instrByte);
		Log.info("收到新的指令[{}]<<<<任务切换", funcidCpy);
		if (!funcidCpy.toString().equals("2") && !funcidCpy.toString().equals("3")) {
		    byte[] repFirst = responseForRequest.responseForRequest("1");
		    try {
			Log.info("监测请求应答For[{}]<<<<任务切换", funcidCpy);
			Thread.sleep(timeOut);
			// timeOut = timeOut + 1000;
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		    if (funcidCpy.toString().equals("1")) {
			Log.info("收到监测终止指令<<<<任务切换", getName());
			if (oldSendDataThread != null) {
			    oldSendDataThread.end();
			    Log.info("旧任务已经结束，新任务即将开始<<<<任务切换");
			}
			try {
			    if (oldSendDataThread.sendDataOnce(repFirst)) {
				Log.info("监测终止响应数据[{}]到客户端<<<<任务切换", repFirst.length);
			    } else {
				Log.info("监测终止响应数据到客户端------失败<<<<任务切换");
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    } else {
			try {
			    oldSendDataThread.sendDataOnce(repFirst);
			    funcid.set(funcidCpy);
			    oldSendDataThread.runForNewTask();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }

		}
	    }
	}
    }

    private class SendDataThread extends Thread {
	private SocketExtend socket;
	private int timeOut = 30;
	private AtomicBoolean end = new AtomicBoolean(false);

	public SendDataThread(SocketExtend socket) {
	    this.socket = socket;
	}

	public void end() {
	    this.end = new AtomicBoolean(true);
	}

	public void runForNewTask() {
	    this.end = new AtomicBoolean(false);
	}
	public boolean sendDataOnce(byte[] data) throws IOException{
	    if (end.get()) {
		 return socketServer.sendDataToClient(data, socket);
	    }
	   return false;
	}
	@Override
	public void run() {
	    do {
		final String funcStr = funcid.toString();
		if (funcStr.equals("1")) {
		    continue;
		}
		if (end.get()) {
		    if (ifHeartBeat) {
			break;
		    }
		    continue;
		}
		// 收到指令数据，解码，执行命令
		try {
		    /* Log.info("解码,指令执行。。。"); */
		    Thread.sleep(timeOut);
		    // timeOut = timeOut + 1000;
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		byte[] repData = responseForRequest.responseForRequest(funcStr);
		// 指令执行完毕，生成响应，合成tcp报文,准备发送回服务端
		/*
		 * Log.info("指令执行完毕，生成tcp响应报文[{}]，发送响应tcp报文[{}]到客户端",
		 * repData.length, ByteArrayTool.byteArrayToString(repData));
		 */
		Log.info("======[{}]============响应报文[{}]==================", getName(), funcid);
		try {
		    socketServer.sendDataToClient(repData, socket);
		} catch (IOException e) {
		    Log.error("网络连接异常，无法向客户端发送数据!");
		    break;
		}
	    } while (!ifHeartBeat);
	}

    }

    @Override
    public void run() {
	while (true) {
	    SocketExtend socket = socketServer.acceptSocket();
	    Log.info("[{}]收到客户端[{}][{}]连接请求", new Object[] { serverPort, socket.getInetAddress(), socket.getPort() });
	    byte[] data = null;
	    try {
		data = socketServer.receiveDataFromClient(socket, receiveTimeOut, receiveEndOut);
	    } catch (IOException e1) {
		e1.printStackTrace();
		return;
	    }
	    if (data == null || data.length <= 0) {
		continue;
	    }
	    // Log.info("从客户端收到指令数据[{}]-bytes:[{}]", data.length,
	    // ByteArrayTool.byteArrayToString(data));
	    funcid.set(BaseTcpMesgDecode.getFunctionInt(data));
	    Log.info("收到客户端指令[{}]", funcid);
	    if (!funcid.toString().equals("2") && !funcid.toString().equals("3")) {
		byte[] repFirst = responseForRequest.responseForRequest("1");
		try {
		    Log.info("监测请求应答For[{}]指令", funcid);
		    Thread.sleep(10);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		try {
		    Log.info("发送监测响应数据到客户端");
		    socketServer.sendDataToClient(repFirst, socket);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (oldReceiveDataThread != null) {
		oldReceiveDataThread.end();
	    }
	    if (oldSendDataThread != null) {
		oldSendDataThread.end();
	    }
	    oldReceiveDataThread = new ReceiveDataThread(socket);
	    oldReceiveDataThread.start();
	    oldSendDataThread = new SendDataThread(socket);
	    oldSendDataThread.start();
	}
    }

    public static void main(String[] args) {
	ByteArrayTool.setPrintStreamToFile(ByteArrayTool.FILE_STATION);
	String heartPortStr = PropertyUtils.getProperty("start_heart_beat_port");
	String instrPortStr = PropertyUtils.getProperty("start_instruction_port");
	String numberStr = PropertyUtils.getProperty("number_of_simulator");
	RequestParameter parameter = RequestParameter.getInstanceByConfFile();
	for (int i = 0; i < Integer.valueOf(numberStr); i++) {
	    new Thread(new SiteInstructionWaitSimulation(Integer.valueOf(instrPortStr) + i, parameter.getResponseTimeOut(), parameter.getResponseEndWait())).start();
	    new Thread(new SiteInstructionWaitSimulation(Integer.valueOf(heartPortStr) + i, parameter.getResponseTimeOut(), parameter.getResponseEndWait(), true)).start();
	}

    }
}
