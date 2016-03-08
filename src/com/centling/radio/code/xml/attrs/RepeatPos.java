/**
 * 
 */
package com.centling.radio.code.xml.attrs;

import org.w3c.dom.Element;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.code.xml.MethodForAttribute;
import com.centling.radio.code.xml.MsgProperty;
import com.centling.radio.code.xml.MsgPropertyDOM;

public class RepeatPos implements MethodForAttribute {
    @Override
    public  void  setAttribute(MsgProperty msgProperty, Element element, byte[] data){
	String attrStr = element.getAttribute(MsgPropertyDOM.REPEATPOS);
	if (attrStr == null || attrStr.length() <= 0) {
	    msgProperty.setRepeatSize(0);
	    return;
	}
	Integer repeatpPos = Integer.valueOf(attrStr);
	String lengthStr = element.getAttribute(MsgPropertyDOM.REPEATLENGTH);
	Integer repeatPosLength = Integer.valueOf(lengthStr);
	byte[] r = ByteArrayTool.byteArrayCut(data, repeatpPos, repeatPosLength);
	r = ByteArrayTool.reverseByteArray(r);
	int repeatSize = 0;
	if (r.length == 4) {
	    repeatSize = ByteArrayTool.byteToInt(r);
	}
	if (r.length == 1) {
	    repeatSize = r[0];
	}
	msgProperty.setRepeatSize(repeatSize);
	//Log.info("正在初始化报文[{}]，msgProperty.repeatSize--->[{}]",msgProperty.getId(),repeatSize);
    }

}
