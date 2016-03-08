package com.centling.radio.code.xml;

import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgDecode {
    private final static Logger LOG = LoggerFactory.getLogger(MsgDecode.class);
    private byte[] data;
    private MsgMap<String, MsgPiece> msgMap;
    private int pos = 0;

    public MsgDecode(byte[] data, MsgMap<String, MsgPiece> msgMap) {
	this.data = data;
	this.msgMap = msgMap;
    }

    private boolean isCheck() {
	int b = data[0];
	for (int i = 1; i < data.length - 1; i++) {
	    b = b ^ data[i];
	}
	return ~b == data[data.length - 1];
    }

    public boolean getCheck() {
	return isCheck();
    }

    public MsgMap<String, Object> getResult() {
	MsgMap<String, Object> resultMap = new MsgMap<String, Object>();
	ArrayList<String> list = msgMap.keyList();
	Iterator<String> iterator = list.iterator();
	while (iterator.hasNext()) {
	    String key = iterator.next();
	    /*LOG.info("解析字段[{}]", key);*/
	    MsgPiece msgPiece = msgMap.get(key);
	    int size = msgPiece.getSizeInt();
	    byte[] pData = ByteArrayTool.byteArrayCut(data, pos, size);
	    if (pos == data.length - 1 || key.equals(MsgDOM.check)) {
		if (key.equals(MsgDOM.check) && pos == data.length - 1) {
		    resultMap.put(key, isCheck());
		    //LOG.info("getResult()解析出data对应的结果:SimpleMsgMap:[{}]", resultMap.toString());
		} else {
		    LOG.error("解析失败:getResult()解析出data对应的结果:SimpleMsgMap:[{}]", resultMap.toString());
		    resultMap = null;
		}
		pos = 0;
		return resultMap;
	    } else {
		if (!key.equals(MsgDOM.START_TAG)) {
		    pData = ByteArrayTool.reverseByteArray(pData);
		}
		msgPiece.setData(pData);
		resultMap.put(key, msgPiece.getObjectFromByte());
	    }
	    pos = pos + size;
	}
	pos = 0;
	return null;
    }

    public static void main(String[] args) {

	MsgProperty msgp = new MsgProperty();
	msgp.setRepeatSize(2);
	msgp.setId("30");
	MsgMap<String, MsgPiece> map = new MsgDOM(MsgDOM.InstructionPath).getMsgStructure(msgp);
	MsgPiece piece = map.get("repeat");
	piece.setValue("2");
	map.get("stopTime").setValue("9");
	map.get("AGCControl").setValue("1");
	map.get("methodType").setValue("1");
	for (int i = 0; i < msgp.getRepeatSize(); i++) {
	    map.get("startFrequency" + i).setValue("4");
	    map.get("endFrequency" + i).setValue("5");
	    map.get("scanStep" + i).setValue("6");
	    map.get("bandWidth" + i).setValue("7");
	    map.get("attenuation" + i).setValue("8");
	}
	System.out.println(map.toString());
	MsgEncode msgEncode = new MsgEncode(map);
	byte[] data = msgEncode.getBytes();
	ByteArrayTool.printlnByteArray(data);
	MsgPropertyDOM msgPropertyDOM = new MsgPropertyDOM(MsgPropertyDOM.MsgPropertiesPath);
	MsgProperty msgp2 = msgPropertyDOM.getMsgProperty(data);
	MsgMap<String, MsgPiece> map2 = new MsgDOM(MsgDOM.InstructionPath).getMsgStructure(msgp2);
	MsgDecode msgDecode = new MsgDecode(data, map2);
	System.out.println(msgDecode.getResult().toString());
    }

}
