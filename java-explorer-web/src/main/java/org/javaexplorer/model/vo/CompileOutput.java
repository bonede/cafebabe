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
    private CompilerOps ops;
    public static CompileOutput fail(
            int returnCode,
            String stdout,
            String stderr,
            CompilerOps ops

    ){
        CompileOutput compileOutput = new CompileOutput();
        compileOutput.setStdout(stdout);
        compileOutput.setStderr(stderr);
        compileOutput.setReturnCode(returnCode);
        compileOutput.setOps(ops);
        return compileOutput;
    }

    public static CompileOutput success(
            List<ClassFile> classFiles,
            String stdout,
            String stderr,
            CompilerOps ops
    ){
        CompileOutput compileOutput = new CompileOutput();
        compileOutput.setClassFiles(classFiles);
        compileOutput.setStdout(stdout);
        compileOutput.setStderr(stderr);
        compileOutput.setOps(ops);
        compileOutput.setReturnCode(0);
        return compileOutput;
    }
}
