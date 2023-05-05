package app.cafebabe.model.vo;

import app.cafebabe.model.SrcFile;
import lombok.Data;

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
