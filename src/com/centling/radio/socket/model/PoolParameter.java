/**
 * 
 */
package com.centling.radio.socket.model;

import com.centling.radio.utils.PropertyUtils;

/**
 * @author lenovo
 *
 */
public class PoolParameter {
    public final static String CORE_SIZE = "pool_coreSize";
    public final static String MAX_SIZE = "pool_maxSize";
    public final static String MAX_CATCH = "pool_maxCache";
    public final static String TIME_OUT = "pool_timeOut";
    public final static String CORE_SIZE_SOCKET = "socket_pool_coreSize";
    public final static String MAX_SIZE_SOCKET = "socket_pool_maxSize";
    public final static String MAX_CATCH_SOCKET = "socket_pool_maxCache";
    public final static String TIME_OUT_SOCKET = "socket_pool_timeOut";
    private Integer coreSize = 0;
    private Integer maxConnect = 0;
    private Integer maxCatch = 0;
    private Integer timeOut = 0;
    public static PoolParameter getForThreadPoolByCfgFile(){
	PoolParameter socketPoolParameter = new PoolParameter();
	String core = PropertyUtils.getProperty(CORE_SIZE);
	String max = PropertyUtils.getProperty(MAX_SIZE);
	String cache = PropertyUtils.getProperty(MAX_CATCH);
	String timeout = PropertyUtils.getProperty(TIME_OUT);
	socketPoolParameter.setCoreSize(Integer.valueOf(core));
	socketPoolParameter.setMaxCatch(Integer.valueOf(cache));
	socketPoolParameter.setMaxConnect(Integer.valueOf(max));
	socketPoolParameter.setTimeOut(Integer.valueOf(timeout));
	return socketPoolParameter;
    
    }
    public static PoolParameter getForSocketPoolByCfgFile() {
	PoolParameter socketPoolParameter = new PoolParameter();
	String core = PropertyUtils.getProperty(CORE_SIZE_SOCKET);
	String max = PropertyUtils.getProperty(MAX_SIZE_SOCKET);
	String cache = PropertyUtils.getProperty(MAX_CATCH_SOCKET);
	String timeout = PropertyUtils.getProperty(TIME_OUT_SOCKET);
	socketPoolParameter.setCoreSize(Integer.valueOf(core));
	socketPoolParameter.setMaxCatch(Integer.valueOf(cache));
	socketPoolParameter.setMaxConnect(Integer.valueOf(max));
	socketPoolParameter.setTimeOut(Integer.valueOf(timeout));
	return socketPoolParameter;
    }

    public Integer getCoreSize() {
	return coreSize;
    }

    public void setCoreSize(Integer coreSize) {
	this.coreSize = coreSize == null ? 0 : coreSize;
	this.coreSize = this.coreSize < 0 ? 0 : this.coreSize;
    }

    public Integer getMaxConnect() {
	return maxConnect;
    }

    public void setMaxConnect(Integer maxConnect) {
	this.maxConnect = maxConnect == null ? 0 : maxConnect;
	this.maxConnect = this.maxConnect < 0 ? 0 : this.maxConnect;
    }

    public Integer getMaxCatch() {
	return maxCatch;
    }

    public void setMaxCatch(Integer maxCatch) {
	this.maxCatch = maxCatch == null ? 0 : maxCatch;
	this.maxCatch = this.maxCatch < 0 ? 0 : this.maxCatch;
    }

    public Integer getTimeOut() {
	return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
	this.timeOut = timeOut == null ? 0 : timeOut;
	this.timeOut = this.timeOut < 0 ? 0 : this.timeOut;
    }

    public static void main(String[] args) {
	PoolParameter socketPoolParameter = PoolParameter.getForSocketPoolByCfgFile();
	System.out.println(socketPoolParameter.getCoreSize());
    }

}
