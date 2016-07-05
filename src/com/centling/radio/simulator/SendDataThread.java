package com.centling.radio.simulator;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.socket.SocketExtend;
import com.centling.radio.socket.SocketServer;

public class SendDataThread extends Thread {
    private final static Logger Log = LoggerFactory.getLogger(SendDataThread.class);
    private SocketExtend socket;
    private int timeOut = Integer.valueOf(Simulator.TIME_PER_MSG);
    private volatile boolean wait = false;
    private volatile boolean end = false;
    private volatile Integer funcid;
    private SocketServer socketServer;

    public void end() {
	this.end = true;
    }


    public SendDataThread(SocketServer socketServer, SocketExtend socket) {
	this.socket = socket;
	this.socketServer = socketServer;
    }

    public void pauseSend() {
	this.wait = true;
    }

    public synchronized void  resumeOrStartSend(Integer funcid) {
	this.funcid = funcid;
	this.wait = false;
    }

    public boolean sendDataOnce(byte[] data) throws IOException {
	return socketServer.sendDataToClient(data, socket);
    }
    @Override
    public synchronized void start() {
	this.pauseSend();
	super.start();
    };

    @Override
    public void run() {
	do {
	    try {
		Thread.sleep(timeOut);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    if (wait) {
		try {
		    Thread.sleep(100);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		continue;
	    }
	    if (end) {
		try {
		    Thread.sleep(Integer.valueOf(Simulator.TIME_SEND_DEATH_AFTER_MACHINE_CHECK));
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		return;
	    }
	    byte[] repData = Simulator.responseForRequest.responseForRequest(funcid.toString());
	    Log.info("======[{}]============响应报文[{}]==================", getName(), funcid);
	    System.out
		    .println(new Date().toString() + "::=======响应报文============" + getName() + "=========>>" + funcid);
	    try {
		if (!socketServer.sendDataToClient(repData, socket)) {
		    break;
		}
	    } catch (IOException e) {
		Log.error("网络连接异常，无法向客户端发送数据!");
		System.out.println(new Date().toString() + "::网络连接异常,无法发送数据，即将返回");
		socketServer.destoryConnect(socket);
		break;
	    }
	} while (true);
    }


}
