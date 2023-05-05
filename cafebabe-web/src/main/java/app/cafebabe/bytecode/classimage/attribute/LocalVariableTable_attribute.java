package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;

public class LocalVariableTable_attribute extends attribute_info {
    private int local_variable_table_length;
    private local_variable_info[] local_variable_table;

    public LocalVariableTable_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public int getLocal_variable_table_length() {
        return local_variable_table_length;
    }

    public local_variable_info[] getLocal_variable_table() {
        return local_variable_table;
    }

    @Override
    public void read() {
        local_variable_table_length = classImage.readu2();
        local_variable_table = new local_variable_info[local_variable_table_length];
        for (int i = 0; i < local_variable_table_length; i++) {
            local_variable_table[i] = new local_variable_info();
            local_variable_table[i].start_pc = classImage.readu2();
            local_variable_table[i].length = classImage.readu2();
            local_variable_table[i].name_index = classImage.readu2();
            local_variable_table[i].name = classImage.getUtf8At(local_variable_table[i].name_index);
            local_variable_table[i].descriptor_index = classImage.readu2();
            local_variable_table[i].descriptor = classImage.getUtf8At(local_variable_table[i].descriptor_index);
            local_variable_table[i].index = classImage.readu2();
        }
    }
}
