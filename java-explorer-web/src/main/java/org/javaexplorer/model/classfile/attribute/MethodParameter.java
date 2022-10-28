package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.flag.AccFlag;

import java.util.List;

@Data
@AllArgsConstructor
public class MethodParameter {
    private int nameIndex;
    private String name;
    List<AccFlag> accFlags;
}
