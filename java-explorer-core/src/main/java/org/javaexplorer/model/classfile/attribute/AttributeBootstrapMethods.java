package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

import java.util.List;

@Data
public class AttributeBootstrapMethods extends AttributeInfo {
    private List<BootstrapMethodInfo> bootstrapMethods;
    public AttributeBootstrapMethods(
            int nameIndex,
            String name,
            int length,
            List<BootstrapMethodInfo> bootstrapMethods
    ) {
        super(nameIndex, name, length);
        this.bootstrapMethods = bootstrapMethods;
    }
}
