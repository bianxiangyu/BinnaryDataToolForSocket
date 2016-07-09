package com.centling.radio.simulator;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.socket.SocketExtend;
import com.centling.radio.socket.SocketServer;
import com.centling.radio.socket.model.RequestParameter;
import com.centling.radio.utils.PropertyUtils;

public class Simulator implements Runnable {
    Integer count = 0;
    public final static String TIME_PER_MSG = PropertyUtils.getProperty("time_per_mesg");
    public final static String TIME_SEND_DEATH_AFTER_MACHINE_CHECK = PropertyUtils
	    .getProperty("time_send_death_after_check");
    private Logger Log = LoggerFactory.getLogger(Simulator.class);
    public static RequesResponsetMap responseForRequest = new RequesResponsetMap();
    public static ThreadPool threadPool = new ThreadPool();
    private int serverPort;
    private int receiveTimeOut;
    private SocketServer socketServer;
    private int receiveEndOut;
    boolean ifHeartBeat = false;
    private AtomicInteger funcid = new AtomicInteger();

    private ReceiveDataThread oldReceiveDataThread;

    public Simulator(int serverPort, int receiveTimeOut, int receiveEndWait) {
	this.serverPort = serverPort;
	this.receiveTimeOut = receiveTimeOut;
	this.receiveEndOut = receiveEndWait;
	socketServer = new SocketServer(this.serverPort);
    }

    public Simulator(int serverPort, int receiveTimeOut, int receiveEndWait, boolean ifHeartBeat) {
	this.serverPort = serverPort;
	this.receiveTimeOut = receiveTimeOut;
	this.receiveEndOut = receiveEndWait;
	this.ifHeartBeat = ifHeartBeat;
	socketServer = new SocketServer(this.serverPort);
    }

    @Override
    public void run() {
	while (true) {
	    SocketExtend socket = socketServer.acceptSocket();
	    oldReceiveDataThread = new ReceiveDataThread(socketServer,socket,receiveTimeOut,receiveEndOut);
	    oldReceiveDataThread.start();
	    Log.info("[{}]收到客户端[{}][{}]连接请求", new Object[] { serverPort, socket.getInetAddress(), socket.getPort() });
	}
    }

    public static void main(String[] args) {
	//ByteArrayTool.setPrintStreamToFile(ByteArrayTool.FILE_STATION);
	String instrPortStr = PropertyUtils.getProperty("start_instruction_port");
	String numberStr = PropertyUtils.getProperty("number_of_simulator");
	RequestParameter parameter = RequestParameter.getInstanceByConfFile();
	for (int i = 0; i < Integer.valueOf(numberStr); i++) {
	    new Thread(new Simulator(Integer.valueOf(instrPortStr) + i, parameter.getResponseTimeOut(),
		    parameter.getResponseEndWait())).start();
	}

    }
}
