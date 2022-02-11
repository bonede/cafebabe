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
    void disassemble() throws IOException {
        Disassembler disassembler = new Disassembler();
        byte[] classBinary = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.class");
        DisassembledClassFile result = disassembler.disassemble(classBinary);
        log.info("result\n{}", JSONObject.toJSONString(result, SerializerFeature.PrettyFormat));
    }
}