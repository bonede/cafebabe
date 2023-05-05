package org.javaexplorer.bytecode.classimage;

import lombok.Data;
import org.javaexplorer.bytecode.classimage.attribute.element_value_pair;

@Data
public class annotation {
    public int type_index;
    public String typeName;
    public int num_element_value_pairs;
    public element_value_pair[] element_value_pairs;
}
