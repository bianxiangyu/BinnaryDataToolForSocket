/**
 * 
 */
package com.centling.radio.socket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.centling.radio.code.impl.ResponseDecodeServiceImpl;
import com.centling.radio.code.model.Response;
import com.centling.radio.code.xml.ByteArrayTool;
import com.centling.radio.code.xml.MsgDOM;
import com.centling.radio.code.xml.MsgEncode;
import com.centling.radio.code.xml.MsgMap;
import com.centling.radio.code.xml.MsgPiece;
import com.centling.radio.code.xml.MsgProperty;

/**
 * @author lenovo
 *
 */
public class RequesResponsetMap {

    /**
     * void
     * 
     * @param args
     */
    private HashMap<String, MsgMap<String, MsgPiece>> structureMap = getMsgStructure();

    /*
     * <centerFrequency type="uint" length="4">60 </centerFrequency> <bandWidth
     * type="uint" length="4">12</bandWidth> <fieldStrength type="short"
     * length="2">21</fieldStrength> <outSideNoiseRate type="short"
     * length="2">12 </outSideNoiseRate> <XRate type="short"
     * length="2">21</XRate> <machineBaseNoise type="short" length="2">21
     * </machineBaseNoise> <outSideNoiseLevel type="short" length="2">21
     * </outSideNoiseLevel> <noiseCorrectionValue type="short" length="2">21
     * </noiseCorrectionValue> <XRateCorrectionValue type="short" length="2">21
     * </XRateCorrectionValue> <remainNoiseSamplesMiddleValue type="short"
     * length="2">21</remainNoiseSamplesMiddleValue>
     * <remainNoiseSamplesAverageValue type="short"
     * length="2">21</remainNoiseSamplesAverageValue> <antennaFactor
     * type="short" length="2">21</antennaFactor> <cableLoss type="short"
     * length="2">21</cableLoss> <machineNoiseRate type="short" length="2">21
     * </machineNoiseRate>
     */
    /*
    <timeYear type="short" length="2">2016</timeYear>
	<timeMonth type="byte" length="1">12</timeMonth>
	<timeDay type="byte" length="1">31</timeDay>
	<timeHour type="byte" length="1">12</timeHour>
	<timeMinute type="byte" length="1">35</timeMinute>
	<timeSecond type="byte" length="1">34</timeSecond>
	<timeMilliSecond type="short" length="2">120
	</timeMilliSecond>
	<timeMicroSecond type="short" length="2">21
	</timeMicroSecond>
	<timeNanoSecond type="short" length="2">121
	</timeNanoSecond>*/
    private void setTimeStamp(MsgMap<String, MsgPiece> msgMap){
	Calendar calendar = Calendar.getInstance();
	String year = String.valueOf(calendar.get(Calendar.YEAR));
	String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
	String day = String.valueOf(calendar.get(Calendar.DATE));
	String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
	String minute = String.valueOf(calendar.get(Calendar.MINUTE));
	String second = String.valueOf(calendar.get(Calendar.SECOND));
	msgMap.get("timeYear").setValue(year);
	msgMap.get("timeMonth").setValue(month);
	msgMap.get("timeDay").setValue(day);
	msgMap.get("timeHour").setValue(hour);
	msgMap.get("timeMinute").setValue(minute);
	msgMap.get("timeSecond").setValue(second);
    }
    public byte[] responseForRequest(String instrId) {
	MsgMap<String, MsgPiece> msgMap = structureMap.get(instrId);
	Random random = new Random(System.currentTimeMillis());
	if (instrId.equals("30")) {
	    MsgPiece frePiece = msgMap.get("centerFrequency");
	    frePiece.setValue(Integer.valueOf(random.nextInt(100)).toString());
	    MsgPiece fieldPiece = msgMap.get("fieldStrength");
	    fieldPiece.setValue(Integer.valueOf(random.nextInt(100)).toString());
	    MsgPiece levelPiece = msgMap.get("outSideNoiseLevel");
	    levelPiece.setValue(Integer.valueOf(random.nextInt(100)).toString());
	    MsgPiece outSideNoiseRate = msgMap.get("outSideNoiseRate");
	    outSideNoiseRate.setValue(Integer.valueOf(random.nextInt(100)).toString());
	    MsgPiece machineNoiseRate = msgMap.get("machineNoiseRate");
	    machineNoiseRate.setValue(Integer.valueOf(random.nextInt(100)).toString());
	    setTimeStamp(msgMap);
	}
	System.out.println(msgMap.toString());
	MsgEncode msgEncode = new MsgEncode(msgMap);
	return msgEncode.getBytes();
    }

    private HashMap<String, MsgMap<String, MsgPiece>> getMsgStructure() {
	HashMap<String, MsgMap<String, MsgPiece>> arrayList = new HashMap<String, MsgMap<String, MsgPiece>>();
	MsgDOM xmldom = new MsgDOM(MsgDOM.ResponsePath);
	String[] requestIds = { "1", "2", "3", "30", "40" };
	String[] responseIds = { "100", "102", "103", "130", "140" };
	// String[] length = { "34", "38", "34", "107", "79", "79" };
	String[] length = { "34", "38", "34", "79", "103" };
	for (int i = 0; i < responseIds.length; i++) {
	    MsgProperty msgProperties = new MsgProperty();
	    msgProperties.setId(responseIds[i]);
	    msgProperties.setUnNormalRepeatSize(3);

	    if (i == 4) {
		msgProperties.setWhichbody("2");
	    } else {
		msgProperties.setWhichbody("1");
	    }
	    MsgMap<String, MsgPiece> msgMapEncode = xmldom.getMsgStructure(msgProperties);
	    MsgPiece msgPiece = msgMapEncode.get("bodyType");
	    if (msgPiece != null) {
		if (i == 4) {
		    msgPiece.setValue("2");
		} else {
		    msgPiece.setValue("1");
		}
	    }

	    msgPiece = msgMapEncode.get("length");
	    if (msgPiece != null) {
		msgPiece.setValue(length[i]);
	    }
	    msgPiece = msgMapEncode.get("repeatU");
	    if (msgPiece != null) {
		msgPiece.setValue("3");
	    }
	    msgPiece = msgMapEncode.get("funcid");
	    msgPiece.setValue(responseIds[i]);
	    arrayList.put(requestIds[i], msgMapEncode);
	}
	return arrayList;
    }

    public static void main(String[] args) throws Exception {
	RequesResponsetMap requesResponsetMap = new RequesResponsetMap();
	for (int i = 0; i < 10; i++) {
	    System.out.println("=========================================["+i+"]============================================");
	    Thread.sleep(1000);
	    byte[] responseData = requesResponsetMap.responseForRequest("30");
	    String str = ByteArrayTool.byteArrayToString(responseData);
	    System.out.println(str);
	    ResponseDecodeServiceImpl decode = new ResponseDecodeServiceImpl();
	    Response response = decode.getResponse(responseData);
	    ArrayList<MsgMap<String, Object>> simple = response.getSimpleObjects();
	    System.out.println(simple.toString());
	    ArrayList<MsgMap<String, Object>> complex = response.getComplexObjects();
	    System.out.println(complex.toString());
	}
    }

}
