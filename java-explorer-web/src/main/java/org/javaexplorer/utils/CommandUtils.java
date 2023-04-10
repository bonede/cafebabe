package org.javaexplorer.utils;

import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class CommandUtils {

    /**
     * 运行命令结果
     */
    @Data
    public static class CommandResult{
        private int code;
        private String stdout;
        private String stderr;
        public boolean success(){
            return code == 0;
        }
    }


    public static CommandResult run(File workingDir, String... args){
        String[] sanitizedArgs = Arrays.stream(args).filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()).toArray(new String[]{});
        ProcessBuilder pb = new ProcessBuilder(sanitizedArgs);
        pb.directory(workingDir);
        try {
            Process process = pb.start();
            String stdout = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            String stderr = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);
            int code = process.waitFor();
            CommandResult commandResult = new CommandResult();
            commandResult.setCode(code);
            commandResult.setStdout(stdout);
            commandResult.setStdout(stderr);
            return commandResult;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
