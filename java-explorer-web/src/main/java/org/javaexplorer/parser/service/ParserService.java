package org.javaexplorer.parser.service;

import org.apache.commons.codec.binary.Base64;
import org.javaexplorer.bytecode.vm.ClassImage;
import org.javaexplorer.model.ClassFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParserService {
    public List<ClassImage> parse(List<ClassFile> classFiles){
        return classFiles.stream()
                .map(c -> Base64.decodeBase64(c.getContent()))
                .map(b -> new ClassImage(b))
                .collect(Collectors.toList());
    }
}
