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
public class BodyType implements MethodForKeyWord {
    @Override
    public  void WraperChild(Element element, MsgProperty prop, MsgMap<String, MsgPiece> map, MsgDOM dom) {
	//Log.info("MsgDom为报文[{}],生成基本数据结构---bodyType---->[{}]",prop.getId(),prop.getWhichbody());
	NodeList bNodes = element.getChildNodes();
	// 遍历bodyType字节点btype
	for (int k = 0; k < bNodes.getLength(); k++) {
		Node bNode = bNodes.item(k);
		if (bNode.getNodeType() == Node.ELEMENT_NODE) {
			Element bElement = (Element) bNode;
			// 遍历btype字节点，并添加到map，生成一个map
			if (bElement.getTagName().equals(MsgDOM.btype)
					&& bElement.getAttribute(MsgDOM.dataType).equals(prop.getWhichbody())) {
				NodeList btNodes = bElement.getChildNodes();
				for (int i = 0; i < btNodes.getLength(); i++) {
					Node btNode = btNodes.item(i);
					if (btNode.getNodeType() == Node.ELEMENT_NODE) {
						Element btElement = (Element) btNode;
						dom.wraperElement(btElement, prop, map, MsgDOM.NOINDEX);
					}
				}
			}

		}
	}

    }

}
