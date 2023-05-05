package org.javaexplorer.bytecode.classimage.attribute;

import lombok.Data;

@Data
public class array_value {
    public int num_values;
    public element_value values[];
}
