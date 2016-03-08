package com.centling.radio.socket.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.socket.model.BaseTcpMsg;

public class BaseTcpMesgEncode {
	private final static Logger Log = LoggerFactory.getLogger(BaseTcpMesgEncode.class);
	protected byte[] msgData = null;
	public final static String MSG_TAG_STR = "SIEW";

	public final static Integer MSG_DETERMINED_LENGTH = 33;
	public final static Integer MSG_HEAD_LENGTH = 32;
	public final static Integer MSG_START_TAG_LENGTH = 4;
	protected boolean isStartTagSet = false;
	public final static Integer MSG_SIZE_LENGTH = 4;
	protected boolean isSizeSet = false;
	public final static Integer MSG_FUNCTION_LENGTH = 4;
	protected boolean isFunctionSet = false;
	public final static Integer MSG_ADDRESS_FROM_LENGTH = 10;
	protected boolean isAdrFromSet = false;
	public final static Integer MSG_ADRESS_TO_LENGTH = 10;
	protected boolean isAdrToSet = false;
	public final static Integer MSG_CHECK_LENGTH = 1;
	protected boolean isCheckSet = false;
	private static Integer MSG_BODY_LENGTH = 0;
	protected boolean isBodySet = false;

	public BaseTcpMesgEncode(byte[] msgData) {
		this.msgData = msgData;
	}

	public BaseTcpMesgEncode(final int msgBodyLen) throws Exception {
		this.msgData = new byte[msgBodyLen + MSG_DETERMINED_LENGTH];
		MSG_BODY_LENGTH = msgBodyLen;
		if (msgBodyLen <= 0) {
			isBodySet = true;
		}
		this.setSize(msgBodyLen + MSG_DETERMINED_LENGTH);
		this.setStartTag();
	}

	public BaseTcpMesgEncode(BaseTcpMsg msg) throws Exception {
		int msgBodyLen = msg.getBodyLength();
		this.msgData = new byte[msgBodyLen + MSG_DETERMINED_LENGTH];
		MSG_BODY_LENGTH = msgBodyLen;
		if (msgBodyLen <= 0) {
			isBodySet = true;
		}
		this.setSize(msgBodyLen + MSG_DETERMINED_LENGTH);
		this.setStartTag();
		this.setAdrFromStr(msg.getFromAddress());
		this.setAdrToStr(msg.getToAddress());
		this.setFunctionInt(msg.getFunction());
		// this.setSize(msg.getSize());
		// this.setCheck();
	}

	public int getBodyLength() {
		return MSG_BODY_LENGTH;
	}

	public boolean setBody(byte[] data) throws Exception {
		if (data == null || data.length != MSG_BODY_LENGTH) {
			return false;
		}
		isBodySet = ByteArrayTool.byteArrayCopy(msgData, MSG_DETERMINED_LENGTH - MSG_CHECK_LENGTH, data);
		return isBodySet;
	}

	public boolean setStartTag() throws Exception {
		isStartTagSet = ByteArrayTool.byteArrayCopy(msgData, 0, MSG_TAG_STR.getBytes());
		return isStartTagSet;
	}

	public boolean setSize(int size) throws Exception {
		if (size < 0) {
			return false;
		}
		isSizeSet = ByteArrayTool.byteArrayCopy(msgData, MSG_START_TAG_LENGTH, ByteArrayTool.intToByte(size));
		return isSizeSet;
	}

	public boolean setFunction(byte[] data) throws Exception {
		if (data == null || data.length != MSG_FUNCTION_LENGTH) {
			return false;
		}
		int pos = MSG_START_TAG_LENGTH + MSG_SIZE_LENGTH;
		isFunctionSet = ByteArrayTool.byteArrayCopy(msgData, pos, data);
		return isFunctionSet;
	}

	public boolean setFunctionInt(int func) throws Exception {
		return setFunction(ByteArrayTool.intToByte(func));

	}

	public boolean setAdrFrom(byte[] data) throws Exception {
		if (data == null || data.length != MSG_ADDRESS_FROM_LENGTH) {
			return false;
		}
		int pos = MSG_START_TAG_LENGTH + MSG_SIZE_LENGTH + MSG_FUNCTION_LENGTH;
		isAdrFromSet = ByteArrayTool.byteArrayCopy(msgData, pos, data);
		return isAdrFromSet;
	}

	public boolean setAdrFromStr(String adr) throws Exception {
		if (adr != null) {
			return setAdrFrom(adr.getBytes());
		}
		return false;
	}

	public boolean setAdrTo(byte[] data) throws Exception {
		if (data == null || data.length != MSG_ADRESS_TO_LENGTH) {
			return false;
		}
		int pos = MSG_START_TAG_LENGTH + MSG_SIZE_LENGTH + MSG_FUNCTION_LENGTH + MSG_ADDRESS_FROM_LENGTH;
		isAdrToSet = ByteArrayTool.byteArrayCopy(msgData, pos, data);
		return isAdrToSet;
	}

	public boolean setAdrToStr(String adr) throws Exception {
		if (adr != null) {
			return setAdrTo(adr.getBytes());
		}
		return false;
	}

	public boolean setCheck() {
		if (this.isDataExcptCheckFin()) {
			int b = msgData[0];
			for (int i = 0; i < msgData.length - 1; i++) {
				b = b ^ msgData[i];
			}
			msgData[msgData.length - 1] = (byte) ~b;
			isCheckSet = true;
			return isCheckSet;
		}

		return false;
	}

	protected boolean isDataExcptCheckFin() {
		return isDataExcptCheckBodyFin() && isBodySet;
	}

	private boolean isDataExcptCheckBodyFin() {
		return isAdrFromSet && isAdrToSet && isFunctionSet && isSizeSet && isStartTagSet;
	}

	public boolean isFinished() {
		return this.isDataExcptCheckFin() && isCheckSet;
	}

	public byte[] getMSGByte() {
		if (this.isFinished()) {
			return msgData;
		}
		Log.error("数据设置不完整，没有生成合格的报文,请检查是否所有字段均被正确设置");
		return null;
	}
}
