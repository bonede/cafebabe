package org.javaexplorer.disassembler.disassembler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.javaexplorer.model.classfile.DisassembledClassFile;
import org.javaexplorer.utils.ResourcesUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
@Slf4j
class DisassemblerTest {

    @Test
    void disassembleClass() throws IOException {
        Disassembler disassembler = new Disassembler();
        byte[] classBinary = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.class");
        DisassembledClassFile result = disassembler.disassemble(classBinary);
        log.info("result\n{}", JSONObject.toJSONString(result, SerializerFeature.PrettyFormat));
    }

    @Test
    void disassembleAnnotation() throws IOException {
        Disassembler disassembler = new Disassembler();
        byte[] classBinary = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Foo.class");
        DisassembledClassFile result = disassembler.disassemble(classBinary);
        log.info("result\n{}", JSONObject.toJSONString(result, SerializerFeature.PrettyFormat));
    }

    @Test
    void disassembleSubClass() throws IOException {
        Disassembler disassembler = new Disassembler();
        byte[] classBinary = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main$Sub.class");
        DisassembledClassFile result = disassembler.disassemble(classBinary);
        log.info("result\n{}", JSONObject.toJSONString(result, SerializerFeature.PrettyFormat));
    }

    @Test
    void disassembleAnonymousClass() throws IOException {
        Disassembler disassembler = new Disassembler();
        byte[] classBinary = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main$1.class");
        DisassembledClassFile result = disassembler.disassemble(classBinary);
        log.info("result\n{}", JSONObject.toJSONString(result, SerializerFeature.PrettyFormat));
    }

    @Test
    void disassembleInnerClass() throws IOException {
        Disassembler disassembler = new Disassembler();
        byte[] classBinary = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main$Inner.class");
        DisassembledClassFile result = disassembler.disassemble(classBinary);
        log.info("result\n{}", JSONObject.toJSONString(result, SerializerFeature.PrettyFormat));
    }
}