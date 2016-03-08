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
public class Piece implements MethodForKeyWord {

    /* (non-Javadoc)
     * @see com.centling.radio.codexml.WraperChildElement#WraperChild(org.w3c.dom.Element, com.centling.radio.codexml.MsgProperties, com.centling.radio.codexml.MsgMap, com.centling.radio.codexml.XMLDOM)
     */
    @Override
    public  void WraperChild(Element element, MsgProperty prop, MsgMap<String, MsgPiece> map, MsgDOM dom) {
	NodeList nodes = element.getChildNodes();
	for(int i=0;i<nodes.getLength();i++){
	    Node node = nodes.item(i);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		Element cElement = (Element) node;
		dom.wraperElement(cElement, prop, map, MsgDOM.NOINDEX);
	    }
	}

    }

}
