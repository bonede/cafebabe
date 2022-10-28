package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.ClassFile;

import java.util.List;

@Data
public class CompileResult {
    private List<ClassFile> classFiles;
    private int code;
    private String output;
    private String compiler;
    private String compilerOptions;
    public static CompileResult fail(
            int code,
            String output,
            String compiler,
            String compilerOptions

    ){
        CompileResult compileResult = new CompileResult();
        compileResult.setOutput(output);
        compileResult.setCode(code);
        compileResult.setCompiler(compiler);
        compileResult.setCompilerOptions(compilerOptions);
        return compileResult;
    }

    public static CompileResult success(
            List<ClassFile> classFiles,
            String output,
            String compiler,
            String compilerOptions
    ){
        CompileResult compileResult = new CompileResult();
        compileResult.setClassFiles(classFiles);
        compileResult.setOutput(output);
        compileResult.setCompiler(compiler);
        compileResult.setCode(0);
        compileResult.setCompilerOptions(compilerOptions);
        return compileResult;
    }
}
