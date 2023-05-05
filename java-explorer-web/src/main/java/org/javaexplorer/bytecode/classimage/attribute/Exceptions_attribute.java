package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.5">Ref</a>
 */
public class Exceptions_attribute extends attribute_info {
    private int number_of_exceptions;
    private int[] exception_index_table;

    public Exceptions_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    @Override
    public void read() {
        number_of_exceptions = classImage.readu2();
        exception_index_table = new int[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++) {
            exception_index_table[i] = classImage.readu2();
        }
    }

    public int getNumberOfExceptions() {
        return number_of_exceptions;
    }

    public int[] getExceptionIndexTable() {
        return exception_index_table;
    }
}
