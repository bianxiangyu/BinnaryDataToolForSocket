package com.centling.radio.code.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgPiece {
    private final static Logger LOG = LoggerFactory.getLogger(MsgPiece.class);
    public final static String STRING = "string";
    public final static String INT = "int";
    public final static String UINT = "uint";
    public final static String FLOAT = "float";
    public final static String SHORT = "short";
    public final static String Byte = "byte";
    private byte[] data;
    private String length;
    private Integer sizeInt;
    private String type;
    private String value;
    private String name;

    public MsgPiece(String name, String length, String type, String value){
	this.length = length;
	this.type = type;
	this.value = value;
	this.name = name;
	this.sizeInt = Integer.valueOf(length);
    }

    public void setData(byte[] data) {
	this.data = data;
    }

    public String getLength() {
	return length;
    }

    public void setLength(String length) {
	this.length = length;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public Integer getSizeInt() {
	return sizeInt;
    }

    public Object getObjectFromVaule() {
	try {
	    if (type.equals(STRING)) {
		return value;
	    }
	    if (type.equals(INT)) {
		return Integer.valueOf(value);
	    }
	    if (type.equals(SHORT)) {
		return Short.valueOf(value);
	    }
	    if (type.equals(Byte)) {
		return Integer.valueOf(value);
	    }
	    if (type.equals(UINT)) {
		return Long.valueOf(value);
	    }
	} catch (Exception e) {
	    if (name.equals("check")) {
		return null;
	    }
	    LOG.info("字段：[{}]-value[{}]无法得到实际数据类型", name, value);
	    e.printStackTrace();
	}
	return null;

    }

    public Object getObjectFromByte() {
	try {
	    if (type.equals(STRING)) {
		return new String(data);
	    }
	    if (type.equals(INT)) {
		return ByteArrayTool.byteToInt(data);
	    }
	    if (type.equals(SHORT)) {
		return ByteArrayTool.byteToShort(data);
	    }
	    if (type.equals(Byte)) {
		return ByteArrayTool.cCharByteToInt(data);
	    }
	    if (type.equals(UINT)) {
		return ByteArrayTool.unSignedIntByteToLong(data);
	    }
	} catch (Exception e) {
	    if (name.equals("check")) {
		return null;
	    }
	    LOG.warn("字段[{}],解码失败,目标数据类型[{}]", name, type);
	    e.printStackTrace();
	}
	return null;
    }

    public byte[] getReverseBytes() {
	return ByteArrayTool.reverseByteArray(getBytes());
    }

    public byte[] getBytes() {

	try {
	    if (type.equals(STRING)) {
		return value.getBytes();
	    }
	    if (type.equals(INT)) {
		return ByteArrayTool.intToByte(Integer.parseInt(value));
	    }
	    if (type.equals(SHORT)) {
		return ByteArrayTool.shortToByte(Short.parseShort(value));
	    }
	    if (type.equals(Byte)) {
		int data = Integer.parseInt(value);
		return ByteArrayTool.intToCCharByte(data);
	    }
	    if (type.equals(UINT)) {
		long data = Long.valueOf(value);
		return ByteArrayTool.longToUnsignedIntByte(data);
	    }
	} catch (Exception e) {
	    if (name.equals("check")) {
		return null;
	    }
	    LOG.error("msgPiece:字段:[{}]-type:[{}]-value[{}]编码失败失败", new Object[] { name, type, value });
	    e.printStackTrace();
	}
	return null;

    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(" ");
	builder.append("length = ");
	builder.append(length);
	builder.append(",type = ");
	builder.append(type);
	builder.append(",value = ");
	builder.append(value.replaceAll("\t", "").replaceAll("\n", "")).append("\n").append("\t");
	return builder.toString();
    }

}
