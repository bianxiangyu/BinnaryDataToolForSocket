package com.centling.radio.socket.code;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.socket.model.BaseTcpMsg;

public class BaseTcpMesgDecode {
    protected byte[] msgData = null;
    public final static String MSG_TAG_STR = "SIEW";
    public final static Integer MSG_DETERMINED_LENGTH = 33;
    public final static Integer MSG_HEAD_LENGTH = 32;
    public final static Integer MSG_START_TAG_LENGTH = 4;
    public final static Integer MSG_SIZE_LENGTH = 4;
    public final static Integer MSG_FUNCTION_LENGTH = 4;
    public final static Integer MSG_ADDRESS_FROM_LENGTH = 10;
    public final static Integer MSG_ADRESS_TO_LENGTH = 10;
    public final static Integer MSG_CHECK_LENGTH = 1;
    private byte[] remain = null;

    public BaseTcpMesgDecode(final byte[] data) {
	this.msgData = data;
	remain = data;
    }

    public static byte[] getStartTag(byte[] msgData) {
	return ByteArrayTool.byteArrayCut(msgData, 0, MSG_START_TAG_LENGTH);
    }

    public static String getStartStr(byte[] msgData) {
	byte[] data = getStartTag(msgData);
	if (data != null) {
	    String str = new String(data);
	    return str;
	}
	return null;
    }

    public static byte[] getSize(byte[] msgData) {
	byte[] data = ByteArrayTool.byteArrayCut(msgData, MSG_START_TAG_LENGTH, MSG_SIZE_LENGTH);
	return ByteArrayTool.reverseByteArray(data);
    }

    public static int getSizeInt(byte[] msdData) {
	byte[] data = getSize(msdData);
	if (data != null) {
	    int size = ByteArrayTool.byteToInt(data);
	    return size;
	}
	return -1;
    }

    public byte[] getOnePiece() {
	if (remain == null) {
	    return remain;
	}
	int begin = ByteArrayTool.searchByteArray(remain, MSG_TAG_STR.getBytes(), 0);
	int end = ByteArrayTool.searchByteArray(remain, MSG_TAG_STR.getBytes(), 1);
	if (begin == -1) {
	    return null;
	}
	byte[] needPiece = ByteArrayTool.byteArrayCut(remain, begin, end == -1 ? remain.length : end);
	if (end == -1) {
	    if (BaseTcpMesgDecode.getStartStr(needPiece).equals(MSG_TAG_STR) && BaseTcpMesgDecode.isCheck(needPiece)) {
		remain = ByteArrayTool.byteArrayCut(remain, end, remain.length - end);
		return needPiece;
	    }else {
		return null;
	    }
	}
	remain = ByteArrayTool.byteArrayCut(remain, end, remain.length - end);
	return needPiece;
    }

    public byte[] getOnePieceFromTwo() {
	if (remain == null) {
	    return remain;
	}
	int begin = ByteArrayTool.searchByteArray(remain, MSG_TAG_STR.getBytes(), 0);
	int end = ByteArrayTool.searchByteArray(remain, MSG_TAG_STR.getBytes(), 1);
	if (begin == -1 || end == -1) {
	    return null;
	} else {
	    byte[] needPiece = ByteArrayTool.byteArrayCut(remain, begin, end);
	    remain = ByteArrayTool.byteArrayCut(remain, end, remain.length - end);
	    return needPiece;
	}
    }

    public byte[] getUsePieces() {
	byte[] need = getOnePiece();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	while (need != null) {
	    try {
		outputStream.write(need);
		need = getOnePieceFromTwo();
	    } catch (IOException e) {
		e.printStackTrace();
		return null;
	    }
	}
	return outputStream.toByteArray();
    }

    public byte[] getRemain() {
	return this.remain;
    }

    public static byte[] getFunction(byte[] msgData) {
	int pos = MSG_START_TAG_LENGTH + MSG_SIZE_LENGTH;
	byte[] data = ByteArrayTool.byteArrayCut(msgData, pos, MSG_FUNCTION_LENGTH);
	return ByteArrayTool.reverseByteArray(data);
    }

    public static Integer getFunctionInt(byte[] msgData) {
	return ByteArrayTool.byteToInt(getFunction(msgData));
    }

    public static byte[] getAdrFrom(byte[] msgData) {
	int pos = MSG_START_TAG_LENGTH + MSG_SIZE_LENGTH + MSG_FUNCTION_LENGTH;
	return ByteArrayTool.byteArrayCut(msgData, pos, MSG_ADDRESS_FROM_LENGTH);
    }

    public static String getAdrFromStr(byte[] msgData) {
	return new String(getAdrFrom(msgData));
    }

    public static byte[] getAdrTo(byte[] msgData) {
	int pos = MSG_START_TAG_LENGTH + MSG_SIZE_LENGTH + MSG_FUNCTION_LENGTH + MSG_ADDRESS_FROM_LENGTH;
	return ByteArrayTool.byteArrayCut(msgData, pos, MSG_ADRESS_TO_LENGTH);
    }

    public static String getAdrToStr(byte[] msgData) {
	return new String(getAdrTo(msgData));
    }

    public static boolean isCheck(byte[] msgData) {
	int b = msgData[0];
	for (int i = 1; i < msgData.length - 1; i++) {
	    b = b ^ msgData[i];
	}
	return ~b == msgData[msgData.length - 1];
    }
}
