package org.javaexplorer.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class AppInfo {
    private List<CompilerInfo> compilers;
}
