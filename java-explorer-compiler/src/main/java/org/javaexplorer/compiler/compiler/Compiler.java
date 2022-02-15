package org.javaexplorer.compiler.compiler;

import lombok.Data;
import org.javaexplorer.model.JavaFile;
import org.javaexplorer.model.vo.CompileResult;

import java.util.List;

@Data
public class Compiler {
    private String workingDir;
    CompileResult compile(List<JavaFile> javaFiles, String compilerOptions){
        return null;
    }
}
