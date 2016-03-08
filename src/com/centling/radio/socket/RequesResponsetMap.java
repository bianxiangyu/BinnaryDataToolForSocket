/**
 * 
 */
package com.centling.radio.socket;

import java.util.ArrayList;
import java.util.HashMap;

import com.centling.radio.code.impl.ResponseDecodeServiceImpl;
import com.centling.radio.code.model.Response;
import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.code.xml.MsgDOM;
import com.centling.radio.code.xml.MsgEncode;
import com.centling.radio.code.xml.MsgMap;
import com.centling.radio.code.xml.MsgPiece;
import com.centling.radio.code.xml.MsgProperty;

/**
 * @author lenovo
 *
 */
public class RequesResponsetMap {

    /**
     * void
     * 
     * @param args
     */
    private HashMap<String, MsgEncode> map = this.getResponseMap();
    public byte[] responseForRequest(String instrId) {
	return map.get(instrId).getBytes();
    }

    private HashMap<String, MsgEncode> getResponseMap() {
	MsgDOM xmldom = new MsgDOM(MsgDOM.ResponsePath);
	HashMap<String, MsgEncode> hashMap = new HashMap<String, MsgEncode>();
	String[] requestIds = { "1", "2", "3", "30", "40" };
	String[] responseIds = { "100", "102", "103", "130", "140" };
	//String[] length = { "34", "38", "34", "107", "79", "79" };
	String[] length = { "34", "38", "34", "79", "105" };
	for (int i = 0; i < responseIds.length; i++) {
	    MsgProperty msgProperties = new MsgProperty();
	    msgProperties.setId(responseIds[i]);
	    msgProperties.setUnNormalRepeatSize(3);
	    if (i==4) {
		msgProperties.setWhichbody("2");
	    }else {
		msgProperties.setWhichbody("1");
	    }
	    MsgMap<String, MsgPiece> msgMapEncode = xmldom.getMsgStructure(msgProperties);
	    MsgPiece msgPiece = msgMapEncode.get("bodyType");
	    if (msgPiece != null) {
		if (i==4) {
		    msgPiece.setValue("2");
		}else {
		    msgPiece.setValue("1");
		}
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
	    msgPiece.setValue(responseIds[i]);
	    MsgEncode msgEncode = new MsgEncode(msgMapEncode);
	  /* System.out.println("=======================响应合成======================");
	    System.out.println(msgMapEncode.toString());*/
	    byte[] data = msgEncode.getBytes();
	    hashMap.put(requestIds[i], msgEncode);
	   /* String str = ByteArrayTool.byteArrayToString(data);
	    System.out.println(str);*/
	}
	return hashMap;
    }

    public static void main(String[] args) throws Exception {
	byte[] responseData = new RequesResponsetMap().responseForRequest("40");
	String str = ByteArrayTool.byteArrayToString(responseData);
	System.out.println(str);
	ResponseDecodeServiceImpl decode = new ResponseDecodeServiceImpl();
	Response response = decode.getResponse(responseData);
	ArrayList<MsgMap<String, Object>> simple = response.getSimpleObjects();
	System.out.println(simple.toString());
	ArrayList<MsgMap<String, Object>> complex = response.getComplexObjects();
	System.out.println(complex.toString());
    }

}
