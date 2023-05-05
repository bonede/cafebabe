package app.cafebabe.model.vo;

import app.cafebabe.bytecode.op.InstructionDoc;
import app.cafebabe.bytecode.op.JdkVersion;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AppInfo {
    private List<CompilerInfo> compilers;
    private Map<String, InstructionDoc> instructionDocs;
    private Map<String, JdkVersion> versions;
    private String specUrl;
}
