package org.javaexplorer.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class WebAppInfo {
    private List<CompilerInfo> compilers;
}
