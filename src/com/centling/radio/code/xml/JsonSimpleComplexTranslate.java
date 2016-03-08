/**
 * 
 */
package com.centling.radio.code.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lenovo
 *
 */
public class JsonSimpleComplexTranslate {
    private final static Logger LOG = LoggerFactory.getLogger(JsonSimpleComplexTranslate.class); 
    public static MsgMap<String, String> getSimpleJSON(HashMap<String, Object> complexMap) {
	MsgMap<String, String> simpleMap = new MsgMap<String, String>();
	Set<String> keySet= complexMap.keySet();
	ArrayList<String> keyList = new ArrayList<String>(keySet);
	for (int i = 0; i < keyList.size(); i++) {
	    String key = keyList.get(i);
	    System.out.println(key);
	    if (key.equals(MsgDOM.repeat)) {
		ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) complexMap.get(key);
		simpleMap.put(MsgDOM.repeat, new Integer(list.size()).toString());
		for (int j = 0; j < list.size(); j++) {
		    HashMap<String, Object> msgMap = list.get(j);
		    ArrayList<String> subKeyList = new ArrayList<String>(msgMap.keySet());
		    for (int k = 0; k < subKeyList.size(); k++) {
			String subKey = subKeyList.get(k);
			simpleMap.put(subKey + "_" + j, msgMap.get(subKey).toString());
		    }
		}
	    } else {

		Object object = complexMap.get(key);
		if (object != null) {
		    simpleMap.put(key, object.toString());
		}

	    }
	}
	//LOG.info("complex:[{}]------>simple:[{}]",complexMap.toString(),simpleMap.toString());
	return simpleMap;
    }

    public static MsgMap<String, Object> getComplexJSON(MsgMap<String, Object> simpleMap) {
	MsgMap<String, Object> complexMap = new MsgMap<String, Object>();
	ArrayList<String> keyList = simpleMap.keyList();
	for (int i = 0; i < keyList.size(); i++) {
	    String key = keyList.get(i);
	    Object object = simpleMap.get(key);
	    if (key.equals(MsgDOM.repeatU)) {
		Integer repeatU;
		try {
		    repeatU = (Integer) object;
		} catch (ClassCastException e) {
		    repeatU = Integer.valueOf(object.toString());
		}

		i = putListToMapU(simpleMap, i + 1, complexMap, repeatU);
		i--;
	    } else if (key.equals(MsgDOM.repeat)) {
		Integer repeat;
		try {
		    repeat = (Integer) object;
		} catch (ClassCastException e) {
		    repeat = Integer.valueOf(object.toString());
		}
		i = putListToMap(simpleMap, i+1, complexMap, repeat);
		i--;
	    } else {
		complexMap.put(key, object);
	    }
	}
	/*LOG.info("simple:[{}]------>complex:[{}]",simpleMap.toString(),complexMap.toString());*/
	return complexMap;
    }

    private static Integer putListToMap(MsgMap<String, Object> simpleMap, int index, MsgMap<String, Object> complexMap,
	    Integer repeat) {
	ArrayList<MsgMap<String, Object>> list = new ArrayList<MsgMap<String, Object>>();
	ArrayList<String> keyList = simpleMap.keyList();
	String subKey = keyList.get(index);
	String newSubKey = subKey.replace("_" + 0, "");
	String oldSubKey = newSubKey;
	for (int i = 0; i < repeat; i++, index++) {
	    MsgMap<String, Object> item = new MsgMap<String, Object>();
	    int n = 0;
	    while (index<keyList.size()) {
		subKey = keyList.get(index);
		int endIndex = subKey.lastIndexOf("_");
		if (endIndex>0) {
		    newSubKey = subKey.substring(0, endIndex);
		}else {
		    break;
		}
		if (newSubKey.equals(oldSubKey)&&n!=0) {
		    index--;
		    break;
		}
		item.put(newSubKey, simpleMap.get(subKey));
		index++;
		n++;
	    }
	    list.add(item);
	}
	index--;
	complexMap.put(MsgDOM.repeat, list);
	return index;
    }

    private static Integer putListToMapU(MsgMap<String, Object> simpleMap, int index, MsgMap<String, Object> complexMap,
	    Integer repeatU) {
	ArrayList<MsgMap<String, Object>> list = new ArrayList<MsgMap<String, Object>>();
	ArrayList<String> keyList = simpleMap.keyList();
	String subKey = null;
	for (int i = 0; i < repeatU; i++, index++) {
	    MsgMap<String, Object> msgMap = new MsgMap<String, Object>();
	    subKey = keyList.get(index);
	    String newSubKey = subKey.replace("_" + i, "");
	    msgMap.put(newSubKey, simpleMap.get(subKey));
	    list.add(msgMap);
	}
	for (; index < keyList.size(); index++) {
	    subKey = keyList.get(index);
	    if (subKey.contains("_")) {
		for (int j = 0; j < repeatU; j++, index++) {
		    subKey = keyList.get(index);
		    MsgMap<String, Object> msgMap = list.get(j);
		    String newSubKey = subKey.replace("_" + j, "");
		    msgMap.put(newSubKey, simpleMap.get(subKey));
		}
		index--;
	    } else {
		break;
	    }
	}
	complexMap.put(MsgDOM.repeat, list);
	return index;
    }

    public static void main(String[] args) {
/*	MsgMap<String, Object> simpleMapU = new MsgMap<String, Object>();
	simpleMapU.put("1", 1);
	simpleMapU.put("2", 3);
	simpleMapU.put("4", true);
	simpleMapU.put("5", "str");
	simpleMapU.put("6", null);
	simpleMapU.put("repeatU", 3);
	simpleMapU.put("a_0", 1);
	simpleMapU.put("a_1", 2);
	simpleMapU.put("a_2", 3);
	simpleMapU.put("b_0", 4);
	simpleMapU.put("b_1", 5);
	simpleMapU.put("b_2", 6);
	simpleMapU.put("c_0", 7);
	simpleMapU.put("c_1", 8);
	simpleMapU.put("c_2", 9);
	simpleMapU.put("d", 0);
	MsgMap<String, Object> complexU = JsonSimpleComplexTranslate.getComplexJSON(simpleMapU);
	String msgMapStr = simpleMapU.toString();
	System.out.println(msgMapStr);
	System.out.println(complexU.toString());
	MsgMap<String, String> paramUse = JsonSimpleComplexTranslate.getSimpleJSON(complexU.getHashMap());
	System.out.println(paramUse.toString());*/
	MsgMap<String, Object> simpleMap = new MsgMap<String, Object>();
	simpleMap.put("1", 1);
	simpleMap.put("2", 3);
	simpleMap.put("4", true);
	simpleMap.put("5", "str");
	simpleMap.put("6", null);
	simpleMap.put("repeat", 3);
	simpleMap.put("a_0", 1);
	simpleMap.put("b_0", 4);
	simpleMap.put("c_0", 7);
	simpleMap.put("a_1", 2);
	simpleMap.put("b_1", 5);
	simpleMap.put("c_1", 8);
	simpleMap.put("a_2", 3);
	simpleMap.put("b_2", 6);
	simpleMap.put("c_2", 9);
	simpleMap.put("d", 0);
	MsgMap<String, Object> complex = JsonSimpleComplexTranslate.getComplexJSON(simpleMap);
	System.out.println(complex.toString());
	MsgMap<String, Object> simpleMap2 = new MsgMap<String, Object>();
	simpleMap2.put("1", 1);
	simpleMap2.put("2", 3);
	simpleMap2.put("4", true);
	simpleMap2.put("5", "str");
	simpleMap2.put("6", null);
	simpleMap2.put("repeatU", 1);
	simpleMap2.put("a_0", 1);
	simpleMap2.put("b_0", 5);
	simpleMap2.put("c_0", 8);
	simpleMap2.put("d", 0);
	MsgMap<String, Object> complex2 = JsonSimpleComplexTranslate.getComplexJSON(simpleMap2);
	System.out.println(complex2.toString());
    }
}
