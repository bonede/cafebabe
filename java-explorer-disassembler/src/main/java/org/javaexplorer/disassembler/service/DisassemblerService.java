package org.javaexplorer.disassembler.service;

import org.apache.commons.codec.binary.Base64;
import org.javaexplorer.disassembler.disassembler.Disassembler;
import org.javaexplorer.model.ClassFile;
import org.javaexplorer.model.classfile.DisassembledClassFile;
import org.javaexplorer.model.vo.DisassembleResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisassemblerService {

    public DisassembledClassFile disassemble(byte[] classFileBinary)  {
        Disassembler disassembler = new Disassembler();
        return disassembler.disassemble(classFileBinary);
    }
    public DisassembleResult disassemble(List<ClassFile> classFiles){
        List<DisassembledClassFile> disassembledJavaClasses = classFiles.stream()
                .map(c -> Base64.decodeBase64(c.getContent()))
                .map(b -> disassemble(b))
                .collect(Collectors.toList());
        DisassembleResult disassembleResult = new DisassembleResult();
        disassembleResult.setDisassembledClassFiles(disassembledJavaClasses);
        disassembleResult.setSuccess(true);
        return disassembleResult;
    }
}
