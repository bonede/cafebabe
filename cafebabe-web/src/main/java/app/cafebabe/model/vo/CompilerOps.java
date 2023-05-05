package app.cafebabe.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CompilerOps {
    @NotNull
    private String compilerName;
    @NotNull
    private Boolean debug;
    @NotNull
    private Boolean optimize;
}
