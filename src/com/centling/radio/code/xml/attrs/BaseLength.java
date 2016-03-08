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
public class BaseLength implements MethodForAttribute {
    @Override
    public  void  setAttribute(MsgProperty msgProperty, Element element, byte[] data) {
	String attrStr = element.getAttribute(MsgPropertyDOM.BASE_LENGTH);
	if (attrStr==null||attrStr.length()<=0) {
	    msgProperty.setBaseLength(0);
	    return;
	}
	Integer lengthI = Integer.valueOf(attrStr);
	msgProperty.setBaseLength(lengthI);
	 //Log.info("正在初始化报文[{}]，msgProperty.baseLength--->[{}]",msgProperty.getId(),lengthI);
    }

}
