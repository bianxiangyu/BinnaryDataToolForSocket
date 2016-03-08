package com.centling.radio.socket;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.socket.model.ByteArray;
import com.centling.radio.socket.model.RequestParameter;

public class RequestForResponse {
    private SocketClient socketClient = new SocketClient();
    RequestParameter parameter = null;
    private final static Logger Log = LoggerFactory.getLogger(RequestForResponse.class);

    public RequestForResponse(RequestParameter parameter) {
	this.parameter = parameter;
    }

    public byte[] sendRequestForResponse(byte[] data) throws IOException{
	byte[] repData = null;
	if(sendRequest(data)){
	  repData = receiveData();
	}
	destoryConnect();
	return repData;
    }
    public boolean receiveDataToBuff() throws IOException{
	return socketClient.receiveDataFromServerToBuff(parameter.getResponseTimeOut());
    }
    public ConcurrentLinkedQueue<ByteArray> getRecBuff(){
	return socketClient.getRecBuff();
    }
    public byte[] receiveData() throws IOException {
	byte[] responseByte = socketClient.receiveDataFromServer(parameter.getResponseTimeOut(),
		parameter.getResponseEndWait());
	if (responseByte != null) {
	    Log.info("收到响应====================[{}]======================数据", responseByte.length);
	    
	} else {
	    Log.info("未收到响应--可能超时timeout:[{}]ms/客户端未发送任何响应数据", parameter.getResponseTimeOut());
	}
	return responseByte;
    }

    public void destoryConnect() {
	if (socketClient.isConnecting()) {
	    socketClient.destoryConnect();
	}
    }

    public boolean sendRequest(byte[] data){
	if (data == null) {
	    Log.warn("发送数据不能为空");
	    return false;
	}
	Log.info("发送请求，数据长度[{}]bytes",data.length);
	try {
	    if (socketClient.isConnecting()&&socketClient.sendDataToServer(data)) {
	        return true;
	    }else {
	        //服务端连接被重置
	        socketClient = new SocketClient();
	    }
	    if (socketClient.connect(parameter.getStationAddress(), parameter.getStationPort(),
			parameter.getConnectTimeOut(), parameter.getConnectionTimes())) {
		    return socketClient.sendDataToServer(data);

		}
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
	return false;

    }

    /**
     * ConcurrentLinkedQueue<ByteArray>
     * @return
     */
    public ConcurrentLinkedQueue<ByteArray> getImpBuff() {
	return socketClient.getImpBuff();
    }
}
