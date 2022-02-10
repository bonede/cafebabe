package org.javaexplorer.model;

import lombok.Data;
import org.javaexplorer.model.classfile.DisassembledClassFile;

import java.util.List;

@Data
public class DisassembleResult {
    private boolean success;
    private String msg;
    private List<DisassembledClassFile> disassembledClassFiles;
}
