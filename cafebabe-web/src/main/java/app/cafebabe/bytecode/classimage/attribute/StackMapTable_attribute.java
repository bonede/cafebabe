package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.4">ref</a>
 */
public class StackMapTable_attribute extends attribute_info {
    private int number_of_entries;
    private stack_map_frame[] entries;

    public StackMapTable_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public int getNumberOfEntries() {
        return number_of_entries;
    }

    public stack_map_frame[] getEntries() {
        return entries;
    }


    @Override
    public void read() {
        number_of_entries = classImage.readu2();
        entries = new stack_map_frame[number_of_entries];
        for (int i = 0; i < number_of_entries; i++) {
            entries[i] = ClassImage.readStackMapFrame(classImage);
        }
    }
}
