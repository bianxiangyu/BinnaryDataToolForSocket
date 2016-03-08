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
public class Chose implements MethodForAttribute {
    @Override
    public  void setAttribute(MsgProperty msgProperty, Element element, byte[] data) {
         String attrStr = element.getAttribute(MsgPropertyDOM.CHOSE);
	 boolean ifChose = Boolean.valueOf(attrStr);
	 msgProperty.setIfChose(ifChose);
	// Log.info("正在初始化报文[{}]，msgProperty.ifChose--->[{}]",msgProperty.getId(),ifChose);
    }

}
