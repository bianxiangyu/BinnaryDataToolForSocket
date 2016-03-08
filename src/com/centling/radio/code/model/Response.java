/**
 * 
 */
package com.centling.radio.code.model;

import java.util.ArrayList;

import com.centling.radio.code.xml.JsonSimpleComplexTranslate;
import com.centling.radio.code.xml.MsgMap;

/**
 * @author lenovo
 *
 */
public class Response {
    private boolean isCheck = false;
    private ArrayList<MsgMap<String, Object>> objects;

    public boolean isCheck() {
	return isCheck;
    }

    public void setCheck(boolean isCheck) {
	this.isCheck = isCheck;
    }

    public ArrayList<MsgMap<String,Object>> getSimpleObjects() {
	return objects;
    }
    public ArrayList<MsgMap<String,Object>> getComplexObjects(){
	ArrayList<MsgMap<String,Object>> list = new ArrayList<MsgMap<String,Object>>();
	for(int i=0;i<objects.size();i++){
	   MsgMap<String, Object> msgMap = JsonSimpleComplexTranslate.getComplexJSON(objects.get(i));
	   list.add(msgMap);
	}
	return list;
    }

    public void setObjects(ArrayList<MsgMap<String, Object>> objects) {
	this.objects = objects;
    }

}
