package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.ClassFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class DisassembleReq {
    @NotEmpty
    private List<ClassFile> classFiles;
}
