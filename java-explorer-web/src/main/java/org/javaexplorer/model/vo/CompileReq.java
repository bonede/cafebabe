package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.SrcFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompileReq {
    @NotNull
    private CompilerOps ops;
    @NotEmpty
    private List<SrcFile> srcFiles;
}
