package org.javaexplorer.bytecode.classimage.attribute;

import lombok.Data;
import org.javaexplorer.bytecode.classimage.annotation;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.16.1>ref</a>
 */
@Data
public class element_value {
    public char tag;
    public int const_value_index;
    public org.javaexplorer.bytecode.classimage.attribute.enum_const_value enum_const_value;
    public int class_info_index;
    public annotation annotation_value;
    public int num_values;
    public element_value[] values;
}
