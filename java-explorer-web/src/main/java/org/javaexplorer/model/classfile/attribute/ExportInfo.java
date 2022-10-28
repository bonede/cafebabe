package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.flag.ExpFlag;

import java.util.List;

@Data
@AllArgsConstructor
public class ExportInfo {
    private int exportsIndex;
    private String exports;
    private List<ExpFlag> exportFlags;
    private int[] exportsToIndices;
}
