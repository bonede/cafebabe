package app.cafebabe.bytecode.classimage;

import app.cafebabe.bytecode.classimage.attribute.attribute_info;

import java.util.List;

public class field_info {
    private ClassImage classImage;

    public short getNameIndex() {
        return name_index;
    }

    public String getName() {
        return classImage.getUtf8At(name_index);
    }

    public short getDescriptorIndex() {
        return descriptor_index;
    }

    public String getDescriptor() {
        return classImage.getUtf8At(descriptor_index);
    }

    public attribute_info[] getAttributes() {
        return attributes;
    }

    public List<field_access_flag> getAccessFlags() {
        return access_flags;
    }

    private attribute_info[] attributes;
    private List<field_access_flag> access_flags;
    private short name_index;
    private short descriptor_index;
    private short attributes_count;

    public short getAttributesCount() {
        return attributes_count;
    }

    public field_info(ClassImage classImage, List<field_access_flag> access_flags,
                      short name_index,
                      short descriptor_index,
                      short attributes_count,
                      attribute_info[] attributes
    ) {
        this.classImage = classImage;
        this.access_flags = access_flags;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.attributes_count = attributes_count;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return String.format(
                "name@%d descriptor@%",
                name_index,
                descriptor_index
        );
    }
}
