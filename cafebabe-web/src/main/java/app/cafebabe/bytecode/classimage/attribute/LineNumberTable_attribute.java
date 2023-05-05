package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LineNumberTable_attribute extends attribute_info {
    public int getLineNumberTableLength() {
        return line_number_table_length;
    }

    private int line_number_table_length;
    private line_number_table_item[] line_number_table;

    public LineNumberTable_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public line_number_table_item[] getLineNumberTable() {
        return line_number_table;
    }

    @Override
    public void read() {
        line_number_table_length = classImage.readu2();
        line_number_table = new line_number_table_item[line_number_table_length];
        for (int i = 0; i < line_number_table_length; i++) {
            int start_pc = classImage.readu2();
            int line_number = classImage.readu2();
            line_number_table[i] = new line_number_table_item(start_pc, line_number);
        }
    }

    @Override
    public String toString() {
        return "LineNumberTable_attribute: \n" +
                Arrays.stream(line_number_table).map(e -> "     " + e.toString()).collect(Collectors.joining("\n")
                );
    }
}
