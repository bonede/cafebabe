package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.flag.AccFlag;

import java.util.List;

@Data
@AllArgsConstructor
public class InnerClassInfo {
    private int innerClassIndex;
    private String innerClass;
    private int outerClassIndex;
    private String outClass;
    private int innerNameIndex;
    private String innerName;
    private List<AccFlag> accFlags;
}
