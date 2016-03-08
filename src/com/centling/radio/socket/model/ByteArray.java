/**
 * 
 */
package com.centling.radio.socket.model;

/**
 * @author lenovo
 *
 */
public class ByteArray {

    private byte[] data;

    public static ByteArray valueOf(byte[] data) {
	return new ByteArray(data);
    }

    private ByteArray(byte[] data) {
	this.data = data;
    }

    public byte[] value() {
	return this.data;
    }

}
