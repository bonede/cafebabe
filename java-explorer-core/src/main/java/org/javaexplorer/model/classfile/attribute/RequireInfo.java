package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.flag.ReqFlag;

import java.util.List;

@Data
@AllArgsConstructor
public class RequireInfo {
    private int requiresIndex;
    private String requires;
    private int requiresVersionIndex;
    private String requiresVersion;
    private List<ReqFlag> reqFlags;
}
