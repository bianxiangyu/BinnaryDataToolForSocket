/**
 * 
 */
package com.centling.radio.code.xml.attrs;

import org.w3c.dom.Element;

import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.code.xml.MethodForAttribute;
import com.centling.radio.code.xml.MsgProperty;
import com.centling.radio.code.xml.MsgPropertyDOM;

/**
 * @author lenovo
 *
 */
public class UnNormalRepeatPos implements MethodForAttribute{
    @Override
    public  void setAttribute(MsgProperty msgProperty,  Element element, byte[] data){
	String attrStr = element.getAttribute(MsgPropertyDOM.UNNORMALREPEATPOS);
	if (attrStr==null||attrStr.length()<=0) {
	    msgProperty.setUnNormalRepeatSize(0);
	    return;
	}
	    Integer unNormalRepeatpos = Integer.valueOf(attrStr);
	    String uRepeatPosLengthS = element.getAttribute(MsgPropertyDOM.UNNORMALREPEATLENGTH);
	    Integer uRepeatPosLength = Integer.valueOf(uRepeatPosLengthS);
	    byte[] r = ByteArrayTool.byteArrayCut(data, unNormalRepeatpos, uRepeatPosLength);
	    r = ByteArrayTool.reverseByteArray(r);
	    int unNormalRepeatSize = 0;
	    if (r.length == 4) {
		unNormalRepeatSize = ByteArrayTool.byteToInt(r);
	    }
	    if (r.length == 1) {
		unNormalRepeatSize = r[0];
	    }
	    msgProperty.setUnNormalRepeatSize(unNormalRepeatSize);
	    //Log.info("正在初始化报文[{}]，msgProperty.unNormalRepeatSize--->[{}]",msgProperty.getId(),unNormalRepeatSize);
    }

}
