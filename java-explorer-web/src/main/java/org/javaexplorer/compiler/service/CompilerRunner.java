package org.javaexplorer.compiler.service;

import org.javaexplorer.model.SrcFile;
import org.javaexplorer.model.vo.CompileResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class CompilerRunner {
    abstract List<String> getSupportCompilerNicknames();
    /**
     * Get compiler detail.<code>javac 1.8.0_221</code> e.g.
     * @return
     */
    abstract String getCompilerVersion();

    /**
     * Get compiler command.<code>javac</code> e.g.
     * @return
     */
    abstract String getCompiler();

    /**
     * Sanitize compiler options.
     * @param compilerOptions
     * @return sanitized options
     */
    abstract String sanitizeCompilerOptions(String compilerOptions);

    /**
     * Compile without sanitized options.
     * @param srcFiles
     * @param compilerOptions
     * @return
     * @throws IOException
     */
    abstract CompileResult compileWithRawCompilerOptions(List<SrcFile> srcFiles, String compilerOptions) throws IOException;

    /**
     * Compile with sanitized options.
     * @param srcFiles
     * @param compilerOptions
     * @return
     * @throws IOException
     */
    public CompileResult compile(List<SrcFile> srcFiles, String compilerOptions) throws IOException {
        return compileWithRawCompilerOptions(srcFiles, sanitizeCompilerOptions(compilerOptions));
    }

    /**
     * Create a new temp directory for compilation.
     * @return
     */
    protected Path createTempleDir() throws IOException {
        return Files.createTempDirectory(getCompiler());
    }


}
