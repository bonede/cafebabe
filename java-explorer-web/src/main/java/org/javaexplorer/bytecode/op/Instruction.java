package org.javaexplorer.bytecode.op;


import org.javaexplorer.bytecode.vm.ClassImage;
import org.javaexplorer.bytecode.vm.Vm;

public interface Instruction {
    default String getOpMnemonic(){
        return toString();
    }
    void execute(Vm vm);

    /**
     *
     * @param code_attribute
     * @return operand size
     */
    void parse(ClassImage.Code_attribute code_attribute);
    int getOpCode();
    int getSize();
}
