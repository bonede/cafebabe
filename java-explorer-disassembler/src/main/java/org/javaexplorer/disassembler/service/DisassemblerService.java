package org.javaexplorer.disassembler.service;

import org.javaexplorer.model.ClassFile;
import org.javaexplorer.model.DisassembleResult;
import org.javaexplorer.model.classfile.DisassembledClassFile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisassemblerService {
    public DisassembledClassFile disassemble(byte[] classFileBinary){
        return null;
    }
    public DisassembleResult disassemble(List<ClassFile> classFiles){
        return null;
    }
}
