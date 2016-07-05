/**
 * 
 */
package com.centling.radio.code.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.centling.radio.code.IResponseDecodeService;
import com.centling.radio.code.model.Response;
import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.code.xml.MsgDOM;
import com.centling.radio.code.xml.MsgDecode;
import com.centling.radio.code.xml.MsgEncode;
import com.centling.radio.code.xml.MsgMap;
import com.centling.radio.code.xml.MsgPiece;
import com.centling.radio.code.xml.MsgProperty;
import com.centling.radio.code.xml.MsgPropertyDOM;
import com.centling.radio.socket.code.BaseTcpMesgDecode;

/**
 * @author lenovo
 *
 */
public class ResponseDecodeServiceImpl implements IResponseDecodeService {
    private final static Logger LOG = LoggerFactory.getLogger(ResponseDecodeServiceImpl.class);
    private MsgDOM xmldom = new MsgDOM(MsgDOM.ResponsePath);
    private MsgPropertyDOM msgPropertyDOM = new MsgPropertyDOM(MsgPropertyDOM.MsgPropertiesPath);

    @Override
    public synchronized Response getResponse(byte[] data) throws Exception {
	if (data==null) {
	    LOG.error("响应解析失败");
	    throw new Exception();
	}
	Response response = new Response();
	BaseTcpMesgDecode baseDecode = new BaseTcpMesgDecode(data);
	ArrayList<MsgMap<String, Object>> list = new ArrayList<MsgMap<String,Object>>();
	byte[] dataPiece = baseDecode.getOnePiece();
	if (dataPiece==null||!checkData(dataPiece)) {
	    LOG.error("数据报文解析未通过初步检验");
	    throw new Exception();
	}
	while (dataPiece != null) {
	    MsgDecode msgDecode = this.getMsgDecode(dataPiece);
	    response.setCheck(msgDecode.getCheck());
	    list.add(msgDecode.getResult());
	    dataPiece = baseDecode.getOnePiece();
	}
	response.setObjects(list);
	return response;
    }
    private boolean checkData(byte[] data){
	Integer length = BaseTcpMesgDecode.getSizeInt(data);
	if (length.equals(data.length)&&BaseTcpMesgDecode.isCheck(data)) {
	    return true;
	}
	return false;
    }
    private MsgDecode getMsgDecode(byte[] data) {
	MsgProperty pro = msgPropertyDOM.getMsgProperty(data);
	MsgMap<String, MsgPiece> msgMap = xmldom.getMsgStructure(pro);
	MsgDecode decode = new MsgDecode(data, msgMap);
	return decode;
    }

    public static void main(String[] args) {
	MsgDOM xmldom = new MsgDOM(MsgDOM.ResponsePath);
	String[] ids = { "100", "102", "103", "120", "130", "140" };
	String[] length = { "34", "38", "34", "79", "79", "79" };
	for (int i = 0; i < ids.length; i++) {
	    System.out.println("================准备合成响应[" + ids[i] + "]====================");
	    MsgProperty msgProperties = new MsgProperty();
	    msgProperties.setId(ids[i]);
	    msgProperties.setUnNormalRepeatSize(3);
	    msgProperties.setWhichbody("1");
	    MsgMap<String, MsgPiece> msgMapEncode = xmldom.getMsgStructure(msgProperties);
	    MsgPiece msgPiece = msgMapEncode.get("bodyType");
	    if (msgPiece != null) {
		msgPiece.setValue("1");
	    }
	    msgPiece = msgMapEncode.get("length");
	    if (msgPiece != null) {
		msgPiece.setValue(length[i]);
	    }
	    msgPiece = msgMapEncode.get("repeatU");
	    if (msgPiece != null) {
		msgPiece.setValue("3");
	    }
	    msgPiece = msgMapEncode.get("funcid");
	    msgPiece.setValue(ids[i]);
	    MsgEncode msgEncode = new MsgEncode(msgMapEncode);
	    System.out.println("================指令合成[" + ids[i] + "]====================");
	    // System.out.println(msgMapEncode.toString());
	    byte[] data = msgEncode.getBytes();
	    // System.out.println(msgEncode.getObjects().toString());
	    String str = ByteArrayTool.byteArrayToString(data);
	    System.out.println(str);
	    System.out.println("================指令解析[" + ids[i] + "]====================");
	    ResponseDecodeServiceImpl responseDecode = new ResponseDecodeServiceImpl();
	    Response response;
	    try {
		response = responseDecode.getResponse(data);
		System.out.println(response.getSimpleObjects().toString());
		System.out.println(response.getComplexObjects().toString());
		System.out.println(response.isCheck());
	    } catch (Exception e) {
		System.out.println("指令合成失败");
		e.printStackTrace();
	    }

	}

    }
}
