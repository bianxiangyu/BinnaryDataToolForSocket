/**
 * 
 */
package com.centling.radio.socket.model;

import com.centling.radio.utils.PropertyUtils;

/**
 * @author lenovo
 *
 */
public class RequestParameter {
    private final static String CONNECT_TIMEOUT = "request_param_connect_timeout";
    private final static String CONNECT_TIMES = "request_param_connect_times";
    private final static String RESPONSE_TIMEOUT = "request_param_response_timeout";
    private final static String RESPONSE_WAITEND = "request_param_response_waitEnd";
    String stationAddress;
    Integer stationPort = 0;
    Integer connectTimeOut = 0;
    Integer connectionTimes = 1;
    Integer responseTimeOut = 0;
    Integer responseEndWait = 0;

    public static RequestParameter getInstanceByConfFile() {
	return RequestParameter.getInstanceByConfFile(null);
    }
    public static RequestParameter getInstanceByConfFile(String id) {
	if (id==null) {
	    id="";
	}
	else {
	    id="_"+id;
	}
	RequestParameter requestParameter = new RequestParameter();
	String connectTimeout = PropertyUtils.getProperty(CONNECT_TIMEOUT + id);
	String connectTimes = PropertyUtils.getProperty(CONNECT_TIMES + id);
	String responseTimeout = PropertyUtils.getProperty(RESPONSE_TIMEOUT + id);
	String responseWaitEnd = PropertyUtils.getProperty(RESPONSE_WAITEND + id);
	requestParameter.setConnectTimeOut(Integer.valueOf(connectTimeout));
	requestParameter.setConnectionTimes(Integer.valueOf(connectTimes));
	requestParameter.setResponseTimeOut(Integer.valueOf(responseTimeout));
	requestParameter.setResponseEndWait(Integer.valueOf(responseWaitEnd));
	return requestParameter;
    }

    public String getStationAddress() {
	return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
	this.stationAddress = stationAddress;
    }

    public Integer getStationPort() {
	return stationPort;
    }

    public void setStationPort(Integer stationPort) {
	this.stationPort = stationPort;
    }

    public Integer getConnectTimeOut() {
	return connectTimeOut;
    }

    public void setConnectTimeOut(Integer connectTimeOut) {
	this.connectTimeOut = connectTimeOut < 0 ? 0 : connectTimeOut;
    }

    public Integer getConnectionTimes() {
	return connectionTimes;
    }

    public void setConnectionTimes(Integer connectionTimes) {
	this.connectionTimes = connectionTimes < 1 ? 1 : connectionTimes;
    }

    public Integer getResponseTimeOut() {
	return responseTimeOut;
    }

    public void setResponseTimeOut(Integer responseTimeOut) {
	this.responseTimeOut = responseTimeOut < 0 ? 0 : responseTimeOut;
    }

    public Integer getResponseEndWait() {
	return responseEndWait;
    }

    public void setResponseEndWait(Integer responseEndWait) {
	this.responseEndWait = responseEndWait;
    }

    public static void main(String[] args) {

    }

}
