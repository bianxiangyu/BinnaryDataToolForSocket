package com.centling.radio.code.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MsgMap<T, V> {
    ArrayList<T> keyList;
    HashMap<T, V> map;

    public MsgMap(ArrayList<T> keyList, HashMap<T, V> map) {
	super();
	this.keyList = keyList;
	this.map = map;
    }

    public MsgMap() {
	this.keyList = new ArrayList<T>();
	this.map = new HashMap<T, V>();
    }

    public V put(T key, V value) {
	V val = map.put(key, value);
	if (!keyList.contains(key)) {
	    keyList.add(key);
	}
	return val;
    }

    public V get(T key) {
	return map.get(key);
    }

    public ArrayList<T> keyList() {
	return keyList;
    }
    public HashMap<T, V> getHashMap(){
	return map;
    }
    public MsgMap<T, V> copy() {
	ArrayList<T> keys = new ArrayList<T>();
	Iterator<T> listI = keyList.iterator();
	while (listI.hasNext()) {
	    keys.add(listI.next());
	}
	HashMap<T, V> mapC = new HashMap<T, V>();
	Set<T> set = map.keySet();
	Iterator<T> mapI = set.iterator();
	while (mapI.hasNext()) {
	    T key = mapI.next();
	    mapC.put(key, map.get(key));
	}
	return new MsgMap<T, V>(keys, mapC);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	Iterator<T> iterator = keyList.iterator();
	builder.append("{");
	while (iterator.hasNext()) {
	    T t = iterator.next();
	    if (map.get(t) == null) {
		continue;
	    }
	    builder.append('"');
	    builder.append(t);
	    builder.append('"');
	    builder.append(":");
	    V value = map.get(t);
	    if (value != null && value.getClass() == String.class) {
		builder.append('"');
		builder.append(map.get(t));
		builder.append('"');
	    } else {
		builder.append(map.get(t));
	    }
	    if (iterator.hasNext()) {
		builder.append(",");
	    }
	}
	if (builder.lastIndexOf(",")==builder.length()-1) {
	    builder.deleteCharAt(builder.length()-1);
	}
	builder.append("}");
	return builder.toString();
    }
}
