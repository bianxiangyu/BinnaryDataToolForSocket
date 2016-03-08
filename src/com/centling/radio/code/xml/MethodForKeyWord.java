/**
 * 
 */
package com.centling.radio.code.xml;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author lenovo
 *
 */

public interface MethodForKeyWord {
    final static Logger Log = LoggerFactory.getLogger(MethodForKeyWord.class);
    void WraperChild(Element element, MsgProperty prop, MsgMap<String, MsgPiece> map, MsgDOM dom);
}
