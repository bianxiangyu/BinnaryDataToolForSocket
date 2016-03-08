/**
 * 
 */
package com.centling.radio.code.xml;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.centling.radio.code.xml.attrs.BaseLength;
import com.centling.radio.code.xml.attrs.Chose;
import com.centling.radio.code.xml.attrs.ChosePos;
import com.centling.radio.code.xml.attrs.ExtendBaseLength;
import com.centling.radio.code.xml.attrs.RepeatPos;
import com.centling.radio.code.xml.attrs.UnNormalRepeatPos;

public class MsgPropertyDOM {
    private final static Logger Log = LoggerFactory.getLogger(MsgPropertyDOM.class);
    public final static String MsgPropertiesPath = "msgProperties.xml";
    private final static HashMap<String, MethodForAttribute> methodAttrMap = new HashMap<String, MethodForAttribute>();
    private static NodeList rootNodes;
    public final static String ID = "id";
    public final static String CHOSE = "chose";
    public final static String CHOSEPOS = "chosePos";
    public final static String REPEATPOS = "repeatPos";
    public final static String REPEATLENGTH = "repeatLength";
    public final static String UNNORMALREPEATPOS = "unNormalRepeatPos";
    public final static String UNNORMALREPEATLENGTH = "uRepeatlength";
    public final static String BASE_LENGTH = "baseLength";
    public final static String EXTEND_BASE_LENGTH = "extendBaseLength";
    public static final String CHOSE_LENGTH = "choseLength";
    private byte[] data;
    private static String[] attribute = { CHOSE, CHOSEPOS, BASE_LENGTH, EXTEND_BASE_LENGTH, REPEATPOS,
	    UNNORMALREPEATPOS };
    private static MethodForAttribute[] methods = { new Chose(), new ChosePos(), new BaseLength(),
	    new ExtendBaseLength(), new RepeatPos(), new UnNormalRepeatPos() };

    static {
	for (int i = 0; i < attribute.length; i++) {
	    methodAttrMap.put(attribute[i], methods[i]);
	}
    }

    public MsgPropertyDOM(String filePath) {
	InputStream is = MsgPropertyDOM.class.getClassLoader().getResourceAsStream(filePath);
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	try {
	    Document document = dbf.newDocumentBuilder().parse(is);
	    Element root = document.getDocumentElement();
	    rootNodes = root.getChildNodes();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }
    public MsgProperty getMsgProperty(String id){
	MsgProperty msgProperty = new MsgProperty();
	this.getBodyAttribute(id, msgProperty);
	return msgProperty;
    }
    public MsgProperty getMsgProperty(byte[] data){
	MsgProperty msgProperty = new MsgProperty();
	this.data = data;
	byte[] idData = ByteArrayTool.byteArrayCut(data, MsgProperty.START_TAG_LENGTH + MsgProperty.SIZE_LENGTH,
		MsgProperty.FUNC_ID_LENGTH);
	idData = ByteArrayTool.reverseByteArray(idData);
	String id = new Integer(ByteArrayTool.byteToInt(idData)).toString();
	Log.info("准备解析响应报文===========[{}]=================",id);
	/*Log.info("准备生成报文[{}]的,msgProperty实例",id);*/
	msgProperty.setId(id);
	this.getBodyAttribute(id, msgProperty);
	/*Log.info("解析生成msgProperty:[{}]即将用于响应报文解析/合成，请核查该属性是否异常，否则可能导致报文/合成解析失败",msgProperty.toString());*/
	return msgProperty;
    }

    private void getBodyAttribute(String id, MsgProperty msgProperty){
	Element element = getIfChose(id, msgProperty);
	if (msgProperty.isIfChose()) {
	    methodAttrMap.get(CHOSEPOS).setAttribute(msgProperty, element, data);
	    NodeList bodys = element.getChildNodes();
	    for (int i = 0; i < bodys.getLength(); i++) {
		Node body = bodys.item(i);
		if (body.getNodeType() == Node.ELEMENT_NODE) {
		    Element bodyElement = (Element) body;
		    if (bodyElement.getAttribute(ID).equals(msgProperty.getWhichbody())) {
			element = bodyElement;
		    }
		}
	    }
	}
	this.initOtherAttribute(msgProperty, element);
    }
    private void initOtherAttribute(MsgProperty msgProperty,Element element){
	String[] attrs={REPEATPOS,UNNORMALREPEATPOS,BASE_LENGTH,EXTEND_BASE_LENGTH};
	for(int i=0;i<attrs.length;i++){
	    methodAttrMap.get(attrs[i]).setAttribute(msgProperty, element, data);
	}
    }
    private Element getIfChose(String id, MsgProperty msgProperty){
	Element element = null;
	for (int i = 0; i < rootNodes.getLength(); i++) {
	    Node node = rootNodes.item(i);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		element = (Element) node;
		if (element.getAttribute(ID).equals(id)) {
		    methodAttrMap.get(CHOSE).setAttribute(msgProperty,  element, data);
		    return element;
		}
	    }
	}
	return element;
    }

    public static void main(String[] args) {

    }

}
