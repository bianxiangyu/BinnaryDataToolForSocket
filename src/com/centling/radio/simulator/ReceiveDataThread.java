package com.centling.radio.simulator;

import java.io.IOException;
import java.util.Date;

import javax.management.loading.PrivateClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.socket.SocketExtend;
import com.centling.radio.socket.SocketServer;
import com.centling.radio.socket.code.BaseTcpMesgDecode;

public class ReceiveDataThread extends Thread {
    private final static Logger Log = LoggerFactory.getLogger(ReceiveDataThread.class);
    private SocketExtend socket;
    private int timeToResponseRecNewInstr = 100;
    private volatile boolean end = false;
    private int receiveTimeOut;
    private int receiveEndOut;
    private SendDataThread sendDataThread;
    public ReceiveDataThread(SocketServer socketServer,SocketExtend socket,int receiveTimeout,int receiveEndOut) {
        this.socket = socket;
        this.receiveTimeOut = receiveTimeout;
        this.receiveEndOut = receiveEndOut;
        this.sendDataThread = new SendDataThread(socketServer, socket);
        this.sendDataThread.start();
    }

    public void end() {
        this.end = true;
    }

    @Override
    public void run() {
        while (!end) {
    	byte[] instrByte = null;
    	try {
    	    instrByte = socket.receiveData(receiveTimeOut, receiveEndOut);
    	} catch (IOException e1) {
    	    socket.destoryConnect();
    	    // Log.info("网络连接异常,无法读取数据，即将返回");
    	    System.out.println(new Date().toString() + "::网络连接异常,无法读取数据，即将返回");
    	    e1.printStackTrace();
    	    return;
    	}
    	if (instrByte == null) {
    	    continue;
    	}
    	Integer funcid = BaseTcpMesgDecode.getFunctionInt(instrByte);
    	Log.info("收到新的指令[{}]", funcid);
    	System.out.println(new Date().toString() + "::收到新的指令，任务切换/设备自检---task->" + funcid);
    	if (funcid.toString().equals("2")) {
    	    Log.info("收到设备自检指令<<<<设备自检");
    	    System.out.println(new Date().toString() + "::收到设备自检指令<<<<设备自检");
    	    byte[] mStatus = Simulator.responseForRequest.responseForRequest("2");
    	    Log.info("设备即将停止发送数据<<<<设备自检");
    	    System.out.println(new Date().toString() + "::设备即将停止发送数据<<<<设备自检");
    	    try {
    		Thread.sleep(timeToResponseRecNewInstr);
    		sendDataThread.sendDataOnce(mStatus);
    		// socketServer.sendDataToClient(mStatus, socketExtend);
    		Log.info("发送设备自检响应数据[{}]<<<<设备自检--data->", mStatus.length);
    		System.out.println(new Date().toString() + "::发送设备自检响应数据<<<<设备自检---data->" + mStatus.length);
    	    } catch (IOException e) {
    		e.printStackTrace();
    	    } catch (InterruptedException e) {
    		e.printStackTrace();
    	    }

    	}
    	if (funcid.toString().equals("3")) {
    	    //sendDataThread.pauseSend();
    	    byte[] data = Simulator.responseForRequest.responseForRequest(funcid.toString());
    	    try {
    		Thread.sleep(5);
    		sendDataThread.sendDataOnce(data);
    		/*if (!funcid.toString().equals("3")&&!funcid.toString().equals("1")&&!funcid.toString().equals("4")&&!funcid.toString().equals("2")) {
    		    sendDataThread.resumeSend();
    		}*/
    	    } catch (IOException e) {
    		e.printStackTrace();
    		return;
    	    } catch (InterruptedException e) {
    		e.printStackTrace();
    	    }
    	}
    	if (!funcid.toString().equals("2") && !funcid.toString().equals("3")) {
    	    byte[] repFirst = Simulator.responseForRequest.responseForRequest("1");
    	    try {
    		Log.info("监测请求应答For[{}]<<<<任务切换", funcid);
    		System.out.println(new Date().toString() + "::监测请求应答<<<<任务切换---task->" + funcid);
    		Thread.sleep(timeToResponseRecNewInstr);
    		// timeOut = timeOut + 1000;
    	    } catch (InterruptedException e) {
    		e.printStackTrace();
    	    }
    	    if (funcid.toString().equals("1")) {
    		Log.info("收到监测终止指令<<<<任务切换", getName());
    		System.out.println(new Date().toString() + "::收到监测终止指令<<<<任务切换----thread->" + getName());
    		if (sendDataThread != null) {
    		    sendDataThread.pauseSend();
    		    Log.info("旧任务已经结束，新任务即将开始<<<<任务切换");
    		    System.out.println(new Date().toString() + "::旧任务已经结束，新任务即将开始<<<<任务切换");
    		}
    		try {
    		    if (sendDataThread.sendDataOnce(repFirst)) {
    			Log.info("监测终止响应数据[{}]到客户端<<<<任务切换", repFirst.length);
    			System.out.println(new Date().toString() + "::监测终止响应--数据--到客户端<<<<任务切换----data->"
    				+ repFirst.length + "bytes");
    		    } else {
    			Log.info("监测终止响应数据到客户端------失败<<<<任务切换");
    			System.out.println(new Date().toString() + "::监测终止响应数据到客户端------失败<<<<任务切换");
    		    }
    		} catch (IOException e) {
    		    e.printStackTrace();
    		}
    	    } else {
    		try {
    		    sendDataThread.sendDataOnce(repFirst);
    		    sendDataThread.resumeOrStartSend(funcid);
    		} catch (IOException e) {
    		    e.printStackTrace();
    		}
    	    }

    	}
        }
    }
}


