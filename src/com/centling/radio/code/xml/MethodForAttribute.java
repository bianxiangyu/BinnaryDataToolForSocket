package com.centling.radio.code.xml;

import org.w3c.dom.Element;

public interface MethodForAttribute {
    void setAttribute(MsgProperty msgProperty, Element element, byte[] data);
}
