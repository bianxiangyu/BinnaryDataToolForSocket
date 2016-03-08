/**
 * 
 */
package com.centling.radio.code.xml.keys;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.centling.radio.code.xml.MethodForKeyWord;
import com.centling.radio.code.xml.MsgDOM;
import com.centling.radio.code.xml.MsgMap;
import com.centling.radio.code.xml.MsgPiece;
import com.centling.radio.code.xml.MsgProperty;

/**
 * @author lenovo
 *
 */
public class Repeat implements MethodForKeyWord {

    @Override
    public  void WraperChild(Element element, MsgProperty prop, MsgMap<String, MsgPiece> map, MsgDOM dom) {
	//Log.info("MsgDom为报文[{}],生成基本数据结构---repeat---->[{}]",prop.getId(),prop.getRepeatSize());
	for (int i = 0; i < prop.getRepeatSize(); i++) {
	    NodeList rNodes = element.getChildNodes();
	    for (int k = 0; k < rNodes.getLength(); k++) {
		Node rNode = rNodes.item(k);
		if (rNode.getNodeType() == Node.ELEMENT_NODE) {
		    Element rElement = (Element) rNode;
		    dom.wraperElement(rElement, prop, map, i);
		}
	    }
	}

    }

}
