/**
 * 
 */
package com.centling.radio.code;

import com.centling.radio.code.model.Response;

/**
 * @author lenovo
 *
 */
public interface IResponseDecodeService {
    public  Response getResponse(byte[] data) throws Exception;
}
