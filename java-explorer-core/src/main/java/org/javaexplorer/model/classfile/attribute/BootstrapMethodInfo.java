package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BootstrapMethodInfo {
    private int bootstrapMethodRef;
    private int bootstrapArguments[];
}
