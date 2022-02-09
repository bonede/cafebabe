package org.javaexplorer.model;

import lombok.Data;

import java.util.List;

@Data
public class CompileResult {
    private List<ClassFile> classFiles;
    private boolean success;
    private String msg;
    private String compiler;
    private String compilerOptions;
    private String compilerVersion;
    private String compilerNickname;
    public static CompileResult fail(
            String msg,
            String compiler,
            String compilerOptions,
            String compilerNickname,
            String compilerVersion
    ){
        CompileResult compileResult = new CompileResult();
        compileResult.setMsg(msg);
        compileResult.setSuccess(false);
        compileResult.setCompiler(compiler);
        compileResult.setCompilerOptions(compilerOptions);
        compileResult.setCompilerVersion(compilerVersion);
        compileResult.setCompilerNickname(compilerNickname);
        return compileResult;
    }

    public static CompileResult success(
            List<ClassFile> classFiles,
            String compiler,
            String compilerOptions,
            String compilerNickname,
            String compilerVersion
    ){
        CompileResult compileResult = new CompileResult();
        compileResult.setClassFiles(classFiles);
        compileResult.setCompiler(compiler);
        compileResult.setSuccess(true);
        compileResult.setCompilerOptions(compilerOptions);
        compileResult.setCompilerVersion(compilerVersion);
        compileResult.setCompilerNickname(compilerNickname);
        return compileResult;
    }
}
