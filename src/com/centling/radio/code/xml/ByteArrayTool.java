package com.centling.radio.code.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class ByteArrayTool {
    public final static String FILE_CLIENT = "D:\\receiveClient.txt";
    public final static String FILE_STATION = "D:\\receiveStation.txt";

    // 从一个byte数组中截取出一个子byte数组
    public static byte[] byteArrayCut(byte[] fromObj, int pos, int length) {
	if (fromObj == null || pos + length > fromObj.length || pos < 0 || length <= 0) {
	    return null;
	}
	byte[] toObj = new byte[length];
	for (int i = 0; i < length; i++) {
	    toObj[i] = fromObj[pos + i];
	}
	return toObj;
    }

    // 判定两个byte数组是否完全相等
    public static boolean byteArrayEqual(byte[] arrayA, byte[] arrayB) {
	if (arrayA == null || arrayB == null) {
	    if (arrayA == arrayB) {
		return true;
	    }
	    return false;
	}
	if (arrayA.length != arrayB.length) {
	    return false;
	}
	for (int i = 0; i < arrayA.length; i++) {
	    if (arrayA[i] != arrayB[i]) {
		return false;
	    }
	}
	return true;
    }

    // 将子数组复制到目的byte数组中
    public static boolean byteArrayCopy(byte[] dest, int pos, byte[] from) {
	if (dest == null || from == null || pos < 0 || pos + from.length > dest.length) {
	    System.out.println("byte数组复制出错，请检查数据输入。。。");
	    return false;
	}
	for (int i = 0; i < from.length; i++) {
	    dest[pos + i] = from[i];
	}
	return true;

    }

    public static void setPrintStreamToFile(String fileName) {
	File file = new File(fileName);
	OutputStream outputStream = null;
	try {
	    outputStream = new FileOutputStream(file, true);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	PrintStream printStream = new PrintStream(outputStream);
	System.setOut(printStream);
    }

    public static int byteToInt(byte[] data) {

	int ia = (data[0] & 0xff) << 24;
	int ib = (data[1] & 0xff) << 16;
	int ic = (data[2] & 0xff) << 8;
	int id = (data[3] & 0xff);
	return ia | ib | ic | id;
    }

    public static byte[] intToByte(int number) {
	byte[] data = new byte[4];
	data[0] = (byte) (number >>> 24);
	data[1] = (byte) (number >>> 16);
	data[2] = (byte) (number >>> 8);
	data[3] = (byte) number;
	return data;
    }

    public static byte[] floatToByte(float number) {
	int n = Float.floatToIntBits(number);
	return intToByte(n);

    }

    public static String byteArrayToString(byte[] data) {
	if (data == null) {
	    return "";
	}
	String str = "";
	for (byte b : data) {
	    str += (b);
	}
	return str;
    }

    public static void printlnByteArray(byte[] data) {
	System.out.println(byteArrayToString(data));
    }

    public static float byteTofloat(byte[] data) {
	return Float.intBitsToFloat(byteToInt(data));
    }

    public static short byteToShort(byte[] data) {
	int ic = (data[0] & 0xff) << 8;
	int id = (data[1] & 0xff);
	return (short) (ic | id);
    }

    public static byte[] shortToByte(short number) {
	byte[] data = new byte[2];
	data[0] = (byte) (number >>> 8);
	data[1] = (byte) number;
	return data;
    }

    public static byte[] longToUnsignedIntByte(long number) {
	byte[] data = new byte[4];
	data[0] = (byte) (number >>> 24);
	data[1] = (byte) (number >>> 16);
	data[2] = (byte) (number >>> 8);
	data[3] = (byte) (number);
	return data;
    }

    public static long unSignedIntByteToLong(byte[] data) {
	long ia = ((long) (data[0] & 0xff)) << 24;
	long ib = ((long) (data[1] & 0xff)) << 16;
	long ic = ((long) (data[2] & 0xff)) << 8;
	long id = ((long) (data[3] & 0xff));
	return ia | ib | ic | id;
    }

    public static byte[] intToCCharByte(int number) {
	byte[] data = new byte[1];
	data[0] = (byte) number;
	return data;
    }

    public static int cCharByteToInt(byte[] data) {
	return data[0];
    }

    public static int searchByteArray(byte[] res, byte[] dest, int index) {
	int resultCount = 0;
	for (int i = 0; i < res.length; i++) {
	    int equalCount = 0;
	    for (int j = 0; j < dest.length; j++, equalCount++) {
		if (res[i + j] != dest[j]) {
		    break;
		}
	    }
	    if (equalCount == dest.length) {
		if (resultCount == index) {
		    return i;
		}
		resultCount++;
	    }
	}
	return -1;
    }

    public static byte[] reverseByteArray(byte[] data) {
	byte[] result = null;
	if (data != null) {
	    result = new byte[data.length];
	    for (int i = 0; i < result.length; i++) {
		result[i] = data[result.length - 1 - i];
	    }
	}
	return result;
    }

    public static void main(String[] args) {
	System.out.println("===================SearchByteArray 测试================");
	byte[] res = { 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6 };
	byte[] dest = { 1, 2, 3, 4 };
	int begin = ByteArrayTool.searchByteArray(res, dest, 0);
	int end = ByteArrayTool.searchByteArray(res, dest, 1);
	if (end == -1) {
	    end = res.length;
	}
	byte[] result = ByteArrayTool.byteArrayCut(res, begin, end);
	System.out.println(ByteArrayTool.byteArrayToString(result));
	System.out.println("=================byte to Long Test==================================");

    }
}
