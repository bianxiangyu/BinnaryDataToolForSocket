package com.centling.radio.code.xml.attrs;

import org.w3c.dom.Element;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.code.xml.MethodForAttribute;
import com.centling.radio.code.xml.MsgProperty;
import com.centling.radio.code.xml.MsgPropertyDOM;

public class ChosePos implements MethodForAttribute {

    @Override
    public  void setAttribute(MsgProperty msgProperty,Element element, byte[] data){
	String attrStr = element.getAttribute(MsgPropertyDOM.CHOSEPOS);
	if (attrStr==null||attrStr.length()<=0) {
	    return;
	}
	Integer chosepos = Integer.valueOf(attrStr);
	String lengthStr = element.getAttribute(MsgPropertyDOM.CHOSE_LENGTH);
	Integer length = Integer.valueOf(lengthStr);
	byte[] which = ByteArrayTool.byteArrayCut(data, chosepos, length);
	String whichbody = Integer.toString(which[0]);
	msgProperty.setWhichbody(whichbody);
	//Log.info("正在初始化报文[{}]，msgProperty.whichbody--->[{}]",msgProperty.getId(),whichbody);
    }

}
