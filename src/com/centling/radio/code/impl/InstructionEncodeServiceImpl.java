/**
 * 
 */
package com.centling.radio.code.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.centling.radio.code.IInstructionEncodeService;
import com.centling.radio.code.model.Instruction;
import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.code.xml.JsonSimpleComplexTranslate;
import com.centling.radio.code.xml.MsgDOM;
import com.centling.radio.code.xml.MsgEncode;
import com.centling.radio.code.xml.MsgMap;
import com.centling.radio.code.xml.MsgPiece;
import com.centling.radio.code.xml.MsgProperty;
import com.centling.radio.code.xml.MsgPropertyDOM;

/**
 * @author lenovo
 *
 */

public class InstructionEncodeServiceImpl implements IInstructionEncodeService{
    private static MsgDOM msgDOM = new MsgDOM(MsgDOM.InstructionPath);
    private static MsgPropertyDOM msgPropertyDOM = new MsgPropertyDOM(MsgPropertyDOM.MsgPropertiesPath);
    @Override
    public synchronized Instruction getInstruction(String id) throws Exception {
	HashMap<String, Object> hashMap = new HashMap<String, Object>();
	return this.getInstruction(id, hashMap);
    }
    @Override
    public synchronized Instruction getInstruction(String id, HashMap<String, Object> param) throws Exception{
	Instruction instruction = new Instruction();
	MsgMap<String, String> paramSimple = JsonSimpleComplexTranslate.getSimpleJSON(param);
	MsgEncode msgEncode = this.getMsgEncode(id, paramSimple);
	if (msgEncode!=null) {
	    instruction.setData(msgEncode.getBytes());
	    instruction.setObjects(msgEncode.getObjects());
	}
	return instruction;
	
    }
    private MsgEncode getMsgEncode(String id, MsgMap<String, String> param) throws Exception{
	MsgProperty pro = msgPropertyDOM.getMsgProperty(id);
	pro.setId(id);
	String repeatSizeStr = param.get(MsgDOM.repeat);
	if (repeatSizeStr != null) {
	    pro.setRepeatSize(Integer.valueOf(repeatSizeStr));
	}
	MsgMap<String, MsgPiece> msgMap = msgDOM.getMsgStructure(pro);
	ArrayList<String> keys = msgMap.keyList();
	Iterator<String> iterator = keys.iterator();
	while (iterator.hasNext()) {
	    String key = iterator.next();
	    String value = param.get(key);
	    if (value != null) {
		msgMap.get(key).setValue(value);
	    }
	}
	return new MsgEncode(msgMap);
    }
    public static void main(String[] args) throws Exception {
	InstructionEncodeServiceImpl encode = new InstructionEncodeServiceImpl();
	String[] ids = { "1", "2", "3", "20", "30", "40" };
	for (int i = 0; i < ids.length; i++) {
	    HashMap<String, Object> param = new HashMap<String, Object>();
	    byte[] data = null;
	    if (i >= 3) {	
		param.put(MsgDOM.repeat, "2");
	    }
	    Instruction instruction  = encode.getInstruction(ids[i], param);
	    data = instruction.getData();
	    MsgMap<String, Object> map = instruction.getObjects();
	    String byteStr = ByteArrayTool.byteArrayToString(data);
	    String objectStr  = map.toString();
	    System.out.println(objectStr);
	    System.out.println(byteStr);
	}
    }
}
