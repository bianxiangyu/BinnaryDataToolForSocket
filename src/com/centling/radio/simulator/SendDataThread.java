package com.centling.radio.simulator;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.socket.SocketExtend;
import com.centling.radio.socket.SocketServer;

public class SendDataThread implements Runnable {
    private final static Logger Log = LoggerFactory.getLogger(SendDataThread.class);
    private SocketExtend socket;
    private int timeOut = Integer.valueOf(Simulator.TIME_PER_MSG);
    private volatile boolean wait = false;
    private volatile boolean end = false;
    private volatile Integer funcid;
    private SocketServer socketServer;
    private int hassend =0;
    public void end() {
	this.end = true;
    }

    public SendDataThread(SocketServer socketServer, SocketExtend socket) {
	this.socket = socket;
	this.socketServer = socketServer;
    }

    public synchronized void pauseSend() {
	this.wait = true;
    }

    public synchronized void resumeOrStartSend(Integer funcid) {
	this.funcid = funcid;
	if (wait) {
	    this.notify();
	}
	this.wait = false;
    }

    public boolean sendDataOnce(byte[] data) throws IOException {
	return socketServer.sendDataToClient(data, socket);
    }

    

    @Override
    public void run() {
	do {
	    try {
		Thread.sleep(timeOut);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    synchronized (this) {
		if (wait) {
		    /*try {
			Thread.sleep(100);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }*/
		    try {
			this.wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		} else {
		    byte[] repData = Simulator.responseForRequest.responseForRequestPressure(funcid.toString());
		    if (hassend%1000==0) {
			 Log.info("======[{}]============响应报文[{}]==========[{}]========", Thread.currentThread().getName(), funcid,hassend);
		    }
		   /* System.out.println(
			    new Date().toString() + "::=======响应报文============" + getName() + "=========>>" + funcid);*/
		    try {
			if (!socketServer.sendDataToClient(repData, socket)) {
			    Log.info("[{}]响应报文[{}]发送失败",Thread.currentThread().getName(),funcid);
			    break;
			}
			hassend++;
		    } catch (IOException e) {
			Log.error("网络连接异常，无法向客户端发送数据!");
			System.out.println(new Date().toString() + "::网络连接异常,无法发送数据，即将返回");
			socketServer.destoryConnect(socket);
			break;
		    }
		}

	    }
	} while (true);
    }

}
