/**
 * 
 */
package com.centling.radio.code;

import java.util.HashMap;

import com.centling.radio.code.model.Instruction;

/**
 * @author lenovo
 *
 */
public interface IInstructionEncodeService {
    Instruction getInstruction(String id, HashMap<String, Object> param) throws Exception;
    Instruction getInstruction(String id) throws Exception;
}
