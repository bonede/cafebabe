package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.bytecode.op.InstructionDoc;

import java.util.List;
import java.util.Map;

@Data
public class AppInfo {
    private List<CompilerInfo> compilers;
    private Map<String, InstructionDoc> instructionDocs;
}
