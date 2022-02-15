package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProvideInfo {
    private int providesIndex;
    private String provides;
    private int[] providesWithIndices;
}
