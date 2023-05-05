package org.javaexplorer.bytecode.classimage;

import org.javaexplorer.bytecode.classimage.attribute.Code_attribute;
import org.javaexplorer.bytecode.classimage.attribute.attribute_info;
import org.javaexplorer.bytecode.op.Instruction;
import org.javaexplorer.bytecode.vm.Vm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class method_info {
    private ClassImage classImage;

    private int index;
    public List<method_access_flag> access_flags;
    public int name_index;
    public int descriptor_index;
    public int attributes_count;
    public attribute_info[] attributes;
    public Code_attribute code_attribute;

    public List<method_access_flag> getAccessFlags() {
        return access_flags;
    }

    public int getNameIndex() {
        return name_index;
    }

    public method_info(
            ClassImage classImage,
            int index,
            List<method_access_flag> access_flags,
            int name_index,
            int descriptor_index,
            int attributes_count,
            attribute_info[] attributes
    ) {
        this.classImage = classImage;
        this.index = index;
        this.access_flags = access_flags;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.attributes_count = attributes_count;
        this.attributes = attributes;
        for (attribute_info attribute_info : attributes) {
            if (attribute_info instanceof Code_attribute) {
                code_attribute = (Code_attribute) attribute_info;
            }
        }
    }

    public int getIndex() {
        return index;
    }

    public boolean isConstructor(ClassImage classImage) {
        return "<init>".equals(classImage.getUtf8At(name_index));
    }

    public boolean isStaticInitializer(ClassImage classImage) {
        return "<clinit>".equals(classImage.getUtf8At(name_index));
    }

    public int getMaxLocals() {
        return code_attribute == null ? 0 : code_attribute.getMaxLocals();
    }

    public int getMaxStack() {
        return code_attribute == null ? 0 : code_attribute.getMaxStack();
    }

    public List<method_access_flag> access_flags() {
        return access_flags;
    }

    public int name_index() {
        return name_index;
    }

    public String getName() {
        return classImage.getUtf8At(name_index);
    }

    public String getDescriptor(ClassImage classImage) {
        return classImage.getUtf8At(descriptor_index);
    }

    public int getDescriptorIndex() {
        return descriptor_index;
    }

    public String getDescriptor() {
        return classImage.getUtf8At(descriptor_index);
    }

    public attribute_info[] getAttributes() {
        return attributes;
    }

    public int getAttributesCount() {
        return attributes_count;
    }

    @Override
    public String toString() {
        return String.format(
                "  Method: name_index@%d descriptor@%d %s\n%s",
                name_index,
                descriptor_index,
                ClassImage.debugMethodAccessFlagString(access_flags),
                String.join("\n", Arrays.stream(attributes).map(e -> e.toString()).collect(Collectors.joining()))
        );
    }

    public Instruction fetchInstruction(Vm vm) {
        if (vm.getPc() < 0) {
            return null;
        }
        Instruction instruction = code_attribute.getInstructionAt(vm.getPc());
        return instruction;
    }
}
