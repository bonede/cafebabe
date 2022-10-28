package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StackMapFrame {
    private int frameType;
    private int byteCodeOffset;
    private List<StackMapTypeInfo> typesOfLocals;
    private List<StackMapTypeInfo> typesOfStackItems;

}
