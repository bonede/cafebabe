package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.SrcFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateShareReq {
    private Integer hoursToLive;
    @NotNull
    @NotEmpty
    private List<SrcFile> srcFiles;
    @NotNull
    private CompilerOps ops;
}
