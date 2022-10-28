package org.javaexplorer.model.classfile.bytecode;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Instruction {
    private String mnemonic;
    private int opcode;
    private int operand;
    private String doc;
    private String url;
}
