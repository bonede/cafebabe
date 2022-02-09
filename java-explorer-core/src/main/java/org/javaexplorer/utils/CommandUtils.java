package org.javaexplorer.utils;

import lombok.Data;
import org.apache.commons.io.IOUtils;

public abstract class CommandUtils {

    @Data
    public static class CommandResult{
        private int code;
        private String output;
        public boolean success(){
            return code == 0;
        }
    }


    public static CommandResult run(String... args){
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            String output = IOUtils.toString(process.getInputStream(), "utf-8");
            int code = process.waitFor();
            CommandResult commandResult = new CommandResult();
            commandResult.setCode(code);
            commandResult.setOutput(output);
            return commandResult;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
