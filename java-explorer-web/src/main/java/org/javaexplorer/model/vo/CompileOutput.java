package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.ClassFile;

import java.util.List;

/**
 * 编译输出、包含class文件、进程的return code、stdout、stderr
 */
@Data
public class CompileOutput {
    private List<ClassFile> classFiles;
    private int returnCode;
    private String stdout;
    private String stderr;
    private String compiler;
    private String compilerOptions;
    public static CompileOutput fail(
            int returnCode,
            String stdout,
            String stderr,
            String compiler,
            String compilerOptions

    ){
        CompileOutput compileOutput = new CompileOutput();
        compileOutput.setStdout(stdout);
        compileOutput.setReturnCode(returnCode);
        compileOutput.setCompiler(compiler);
        compileOutput.setCompilerOptions(compilerOptions);
        return compileOutput;
    }

    public static CompileOutput success(
            List<ClassFile> classFiles,
            String stdout,
            String stderr,
            String compiler,
            String compilerOptions
    ){
        CompileOutput compileOutput = new CompileOutput();
        compileOutput.setClassFiles(classFiles);
        compileOutput.setStdout(stdout);
        compileOutput.setCompiler(compiler);
        compileOutput.setReturnCode(0);
        compileOutput.setCompilerOptions(compilerOptions);
        return compileOutput;
    }
}
