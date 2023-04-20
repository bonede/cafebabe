package org.javaexplorer.model.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DeleteShareFileReq {
    @NotEmpty
    @NotNull
    private String url;
    @NotEmpty
    @NotNull
    private String deletingToken;
}
