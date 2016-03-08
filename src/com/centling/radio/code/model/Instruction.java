/**
 * 
 */
package com.centling.radio.code.model;

import com.centling.radio.code.xml.JsonSimpleComplexTranslate;
import com.centling.radio.code.xml.MsgMap;

/**
 * @author lenovo
 *
 */
public class Instruction {
    public final static String STOP_WATCH_ID = "1";
    public final static String MACHINE_CHECK_ID = "2";
    public final static String LINK_CHECK_ID = "3";
    public final static String FREQUENCY_BANDS_SCAN_ID = "30";
    public final static String FREQUENCY_SCAN_ID = "40";
    private byte[] data;
    private MsgMap<String, Object> objects;
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public MsgMap<String, Object> getObjects() {
        return objects;
    }
    public void setObjects(MsgMap<String, Object> objects) {
        this.objects = objects;
    }
    public  MsgMap<String, Object> getComplexObjects(){
	return JsonSimpleComplexTranslate.getComplexJSON(objects);
    }
    
}
