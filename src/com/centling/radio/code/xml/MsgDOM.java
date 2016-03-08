package com.centling.radio.code.xml;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.centling.radio.code.xml.keys.BodyType;
import com.centling.radio.code.xml.keys.Include;
import com.centling.radio.code.xml.keys.Piece;
import com.centling.radio.code.xml.keys.Repeat;
import com.centling.radio.code.xml.keys.RepeatU;

public class MsgDOM {
    private HashMap<MsgProperty, MsgMap<String, MsgPiece>> msgStructCache = new HashMap<MsgProperty, MsgMap<String, MsgPiece>>();
    private final static Logger Log = LoggerFactory.getLogger(MsgDOM.class);
    private final static HashMap<String, MethodForKeyWord> keyWords = new HashMap<String, MethodForKeyWord>();
    private final static HashSet<String> keyExcludeSet = new HashSet<String>();
    public final static String ResponsePath = "response.xml";
    public final static String InstructionPath = "instruction.xml";
    public final static String id = "id";
    public final static String include = "include";
    public final static String piece = "piece";
    public final static String repeat = "repeat";
    public final static String repeatU = "repeatU";
    public final static String bodyType = "bodyType";
    public final static String btype = "btype";
    public final static String type = "type";
    public final static String length = "length";
    public final static String funcid = "funcid";
    public final static String dataType = "dataType";
    public final static String check = "check";
    private final static String[] keys = { repeat, repeatU, bodyType, include, piece };
    private final static String[] keysExclude = { include, piece };
    private final static MethodForKeyWord[] methods = { new Repeat(), new RepeatU(), new BodyType(), new Include(),
	    new Piece() };
    public final static int NOINDEX = -1;
    public static final String START_TAG = "startTag";
    private NodeList rootNodes;

    static {
	for (int i = 0; i < keys.length; i++) {
	    keyWords.put(keys[i], methods[i]);
	}
	for (int i = 0; i < keysExclude.length; i++) {
	    keyExcludeSet.add(keysExclude[i]);
	}
    }

    public MsgDOM(String filePath) {
	InputStream inputStream = MsgDOM.class.getClassLoader().getResourceAsStream(filePath);
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	try {
	    Document document = dbf.newDocumentBuilder().parse(inputStream);
	    Element rootElement = document.getDocumentElement();
	    rootNodes = rootElement.getChildNodes();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public MsgMap<String, MsgPiece> getMsgStructure(MsgProperty pro) {
	MsgMap<String, MsgPiece> map = msgStructCache.get(pro);
	if (map != null) {
	   /* Log.info("为tcp报文[{}],从缓存中查取SimpleMsgMap[{}]", pro.getId(), map.toString());*/
	    return map;
	}
	map = new MsgMap<String, MsgPiece>();
	for (int i = 0; i < rootNodes.getLength(); i++) {
	    Node instrNode = rootNodes.item(i);
	    if (instrNode.getNodeType() == Node.ELEMENT_NODE) {
		Element element = (Element) instrNode;
		String idGet = element.getAttribute(id);
		if (idGet.equals(pro.getId())) {
		    NodeList paramNodes = element.getChildNodes();
		    for (int j = 0; j < paramNodes.getLength(); j++) {
			Node pNode = paramNodes.item(j);
			if (pNode.getNodeType() == Node.ELEMENT_NODE) {
			    Element pElement = (Element) pNode;
			    this.wraperElement(pElement, pro, map, NOINDEX);
			}
		    }
		}
	    }
	}
	//Log.info("为tcp报文[{}],生成SimpleMsgMap[{}]", pro.getId(), map.toString());
	this.initBaseData(pro, map);
	msgStructCache.put(pro, map);
	return map;
    }

    private void initBaseData(MsgProperty msgProperty, MsgMap<String, MsgPiece> msgMap) {
	/*Log.info("为msgMap[{}]添加一些基本的数据[{}]:[{}]/[{}]:[{}](响应解析时该属性不需要)",
		new Object[] { msgProperty.getId(), funcid, msgProperty.getId(), length, msgProperty.getLength() });*/
	msgMap.get(funcid).setValue(msgProperty.getId());
	msgMap.get(length).setValue(msgProperty.getLength().toString());
    }

    public void wraperElement(Element element, MsgProperty prop, MsgMap<String, MsgPiece> map, int index) {
	String bType = element.getAttribute(type);
	String bSize = element.getAttribute(length);
	String value = element.getTextContent();
	if (value != null) {
	    value = value.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", "");
	}
	String name = element.getTagName();
	if (!keyExcludeSet.contains(name)) {
	    MsgPiece msgPiece = new MsgPiece(name, bSize, bType, value);
	    if (index >= 0) {
		name = name + "_" + index;
	    }
	    map.put(name, msgPiece);
	}
	MethodForKeyWord wraperChild = keyWords.get(name);
	if (wraperChild != null) {
	    wraperChild.WraperChild(element, prop, map, this);
	}
    }

    public NodeList getRootNodes() {
	return this.rootNodes;
    }

    public static void main(String[] args) {
	MsgProperty properties = new MsgProperty();
	properties.setId("120");
	properties.setIfChose(true);
	properties.setWhichbody("1");
	properties.setUnNormalRepeatSize(3);
	String res = new MsgDOM(ResponsePath).getMsgStructure(properties).toString();
	System.out.println(res);
    }

}
