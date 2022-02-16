package org.javaexplorer.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExplorerResult {
    private List<Map<String, Object>> disassembledClassFiles;
    private boolean success;
    private String msg;
    private String shareUrl;
}
