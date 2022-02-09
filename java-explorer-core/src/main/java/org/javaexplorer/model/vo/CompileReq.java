package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.JavaFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompileReq {
    @NotNull
    private String compilerNickname;
    private String compilerOptions;
    @NotEmpty
    private List<JavaFile> javaFiles;
}
