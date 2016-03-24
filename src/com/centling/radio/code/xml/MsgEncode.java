package com.centling.radio.code.xml;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgEncode {
    private final static Logger Log = LoggerFactory.getLogger(MsgEncode.class);
    private MsgMap<String, MsgPiece> msgMap;
    private byte[] msgData;
    private int pos = 0;
    private int length = 0;
    private String id;

    public MsgEncode(MsgMap<String, MsgPiece> msgMap) {
	this.msgMap = msgMap;
	String val = msgMap.get("length").getValue();
	id = msgMap.get("funcid").getValue();
	length = Integer.valueOf(val);
	msgData = new byte[length];
    }

    public MsgMap<String, Object> getObjectsStr() {
	MsgMap<String, Object> map = new MsgMap<String, Object>();
	Iterator<String> iterator = msgMap.keyList.iterator();
	while (iterator.hasNext()) {
	    String key = iterator.next();
	    map.put(key, msgMap.get(key).getValue());
	}
	return map;
    }

    public MsgMap<String, Object> getObjects() {
	MsgMap<String, Object> map = new MsgMap<String, Object>();
	Iterator<String> iterator = msgMap.keyList.iterator();
	while (iterator.hasNext()) {
	    String key = iterator.next();
	    map.put(key, msgMap.get(key).getObjectFromVaule());
	}
	return map;
    }

    private void getBytes(MsgMap<String, MsgPiece> msgMap) {
	/*Log.info("准备合成指令[{}]，长度[{}]", id, length);*/
	Iterator<String> iterator = msgMap.keyList().iterator();
	while (iterator.hasNext()) {
	    String key = iterator.next();
	    MsgPiece piece = msgMap.get(key);
	    int size = piece.getSizeInt();
	    byte[] pieceBytes;
	    if (pos == length - 1) {
		pieceBytes = this.getCheck();
	    } else if (key.equals(MsgDOM.START_TAG)) {
		pieceBytes = piece.getBytes();
	    } else {
		pieceBytes = piece.getReverseBytes();
	    }
	    if (pieceBytes.length == size) {
		System.arraycopy(pieceBytes, 0, msgData, pos, size);
		pos = pos + size;
	    } else {
		Log.error("[{}]--[{}]:填入的value值length:[{}]bytes,与预订数据byte length:[{}]不符合",
			new Object[] { id, piece.getName(), pieceBytes.length, size });
		return;
	    }
	}
	return;
    }

    public byte[] getBytes() {
	this.getBytes(msgMap);
	if (pos != length) {
	    Log.info("指令[{}]合成失败，正确长度[{}]，实际长度[{}]", new Object[] { id, length, pos });
	    return null;
	}
	/*Log.info("指令合成[{}]成功，正确长度[{}]，实际长度[{}]", new Object[] { id, length, pos });*/
	pos = 0;
	return msgData;
    }

    private byte[] getCheck() {
	int b = msgData[0];
	for (int i = 1; i < msgData.length - 1; i++) {
	    b = b ^ msgData[i];
	}
	//Log.info("[{}]指令---->check[{}]", id, b);
	return new byte[] { (byte) ~b };
    }

    /**
     * <repeat length="1" type="string" baseSize = "5" repeatSize="2">
     * <startFrequency length="4" type="int"></startFrequency>
     * <endFrequency length="4" type="int"></endFrequency>
     * <scanStep length="4" type="int"></scanStep>
     * <bandWidth length="4" type ="int"></bandWidth>
     * <attenuation length="2" type="short"></attenuation> </repeat>
     * 
     * @param args
     * @throws XMLException 
     */
    public static void main(String[] args)  {
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
	ByteArrayTool.printlnByteArray(msgEncode.getBytes());
    }
}
