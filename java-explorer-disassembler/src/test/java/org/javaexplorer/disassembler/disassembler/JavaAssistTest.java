package org.javaexplorer.disassembler.disassembler;

import javassist.ClassPool;
import javassist.bytecode.ClassFile;
import lombok.extern.slf4j.Slf4j;
import org.javaexplorer.utils.ResourcesUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
@Slf4j
public class JavaAssistTest {
    @Test
    public void testJavaAssist() throws IOException {
        ClassPool classPool = new ClassPool();
        ClassFile classFile = classPool.makeClass(ResourcesUtils.readResource(this.getClass(), "source/main/Main.class"))
                .getClassFile();
        classFile.getAttributes();
    }
}
