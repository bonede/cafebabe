package app.cafebabe.bytecode.op;


import app.cafebabe.bytecode.classimage.attribute.Code_attribute;
import app.cafebabe.bytecode.vm.Vm;

public interface Instruction {
    String getOpMnemonic();
    void execute(Vm vm);

    /**
     *
     * @param code_attribute
     * @return operand size
     */
    default void parse(Code_attribute code_attribute){}
    int getOpCode();
    int getSize();
}
