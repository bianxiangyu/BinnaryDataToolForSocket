package com.centling.radio.socket.model;

public class BaseTcpMsg {
	public final static Integer BASE_LENGTH = 33;
	protected Integer bodyLength = 0;
	protected String startTag = "SIEW";
	protected Integer function;
	protected Integer size;
	protected String fromAddress;
	protected String toAddress;
	protected boolean isCheck;
	public String getStartTag() {
		return startTag;
	}
	public Integer getSize() {
		return size;
	}

	public Integer getFunction() {
		return function;
	}

	public void setFunction(Integer function) {
		this.function = function;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean check) {
		this.isCheck = check;
	}

	public Integer getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(Integer bodyLength) {
		size = bodyLength+BASE_LENGTH;
		this.bodyLength = bodyLength;
	}

}
