package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.SrcFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExplorerReq {
    @NotNull
    private String compilerName;
    private String compilerOptions;
    @NotEmpty
    private List<SrcFile> srcFiles;
    @NotNull
    private boolean save;
}
