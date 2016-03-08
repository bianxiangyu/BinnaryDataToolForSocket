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
public class RepeatU implements MethodForKeyWord {
    @Override
    public  void WraperChild(Element element, MsgProperty prop, MsgMap<String, MsgPiece> map, MsgDOM dom) {
	//Log.info("MsgDom为报文[{}],生成基本数据结构---repeatU---->[{}]",prop.getId(),prop.getUnNormalRepeatSize());
	NodeList rUNodes = element.getChildNodes();
	for (int k = 0; k < rUNodes.getLength(); k++) {
		for (int i = 0; i < prop.getUnNormalRepeatSize(); i++) {
			Node rUNode = rUNodes.item(k);
			if (rUNode.getNodeType() == Node.ELEMENT_NODE) {
				Element rUElement = (Element) rUNode;
				dom.wraperElement(rUElement, prop, map, i);
			}
		}

	}

    }

}
