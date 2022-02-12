package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ParameterAnnotationInfo {
    private List<AnnotationInfo> annotations;
}
