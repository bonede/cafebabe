package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.flag.OpenFlag;

import java.util.List;

@Data
@AllArgsConstructor
public class OpenInfo {
    private int opensIndex;
    private String opens;
    private List<OpenFlag> openFlags;
    private int[] opensToIndices;
}
