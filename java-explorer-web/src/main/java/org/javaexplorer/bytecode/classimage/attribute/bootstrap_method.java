package org.javaexplorer.bytecode.classimage.attribute;

import lombok.Data;

@Data
public class bootstrap_method {
    public int bootstrap_method_ref;
    public int num_bootstrap_arguments;
    public int[] bootstrap_arguments;
}
