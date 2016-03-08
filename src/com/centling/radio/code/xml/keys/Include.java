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
public class Include implements MethodForKeyWord {
    @Override
    public  void WraperChild(Element element, MsgProperty prop, MsgMap<String, MsgPiece> map, MsgDOM dom) {
	NodeList rootNodes = dom.getRootNodes();
	for(int i=0;i<rootNodes.getLength();i++){
	    Node node = rootNodes.item(i);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		Element cElement = (Element) node;
		if (cElement.getTagName().equals(MsgDOM.piece)&&cElement.getAttribute("id").equals(element.getAttribute("id"))) {
		    dom.wraperElement(cElement, prop, map, MsgDOM.NOINDEX);
		}
	    }
	}

    
    }

}
