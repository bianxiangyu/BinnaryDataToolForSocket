package com.centling.radio.code.xml;

import java.util.logging.Logger;

import org.w3c.dom.Element;

public interface MethodForAttribute {
    void setAttribute(MsgProperty msgProperty, Element element, byte[] data);
}
