package app.cafebabe.bytecode.op;

import lombok.Data;

@Data
public class InstructionDoc {
    private String mnemonic;
    private int opcode;
    private String category;
    private String shortdescr;
    private String specref;
}
