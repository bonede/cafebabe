package org.javaexplorer.model.classfile.field;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.attribute.AttributeInfo;
import org.javaexplorer.model.classfile.flag.AccFlag;

import java.util.List;

@Data
@AllArgsConstructor
public class FieldInfo {
    private List<AccFlag> accFlags;
    private int nameIndex;
    private String name;
    private int descriptorIndex;
    private String descriptor;
    private List<AttributeInfo> attributes;
}
