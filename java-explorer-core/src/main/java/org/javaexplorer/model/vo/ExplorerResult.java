package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.classfile.DisassembledClassFile;

import java.util.List;

@Data
public class ExplorerResult {
    private List<DisassembledClassFile> disassembledClassFiles;
    private boolean success;
    private String msg;
    private String shareUrl;
}
