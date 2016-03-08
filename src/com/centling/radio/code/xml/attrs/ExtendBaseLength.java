/**
 * 
 */
package com.centling.radio.code.xml.attrs;

import org.w3c.dom.Element;

import com.centling.radio.code.xml.MethodForAttribute;
import com.centling.radio.code.xml.MsgProperty;
import com.centling.radio.code.xml.MsgPropertyDOM;

/**
 * @author lenovo
 *
 */
public class ExtendBaseLength implements MethodForAttribute{
    @Override
    public  void setAttribute(MsgProperty msgProperty,  Element element, byte[] data) {
	String attrStr = element.getAttribute(MsgPropertyDOM.EXTEND_BASE_LENGTH);
	if (attrStr==null||attrStr.length()<=0) {
	    msgProperty.setExtendBaseLength(0);
	    return;
	}
	    Integer extendBaseLengthI = Integer.valueOf(attrStr);
	    msgProperty.setExtendBaseLength(extendBaseLengthI);
	    //Log.info("正在初始化报文[{}]，msgProperty.extendBaseLengthI--->[{}]",msgProperty.getId(),extendBaseLengthI);
    }

}
