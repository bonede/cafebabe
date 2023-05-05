package org.javaexplorer.bytecode.classimage.constant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.bytecode.op.DescriptorParser;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2>ref</a>
 */
public class CONSTANT_Fieldref_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_Fieldref_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private short class_index;
    private short name_and_type_index;


    @JsonIgnore
    public DescriptorParser.FieldType getFieldType() {
        return (DescriptorParser.FieldType) DescriptorParser.parse(getFieldDescriptor());
    }

    public String getFieldDescriptor() {
        return classImage.getNameAndTypeInfo(name_and_type_index).getDescriptor();
    }

    public String getClassName() {
        return classImage.getClassInfoAt(class_index).getName();
    }

    public String getFieldName() {
        return classImage.getNameAndTypeInfo(name_and_type_index).getName();
    }

    public short getClassIndex() {
        return class_index;
    }

    public short getNameAndTypeIndex() {
        return name_and_type_index;
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_Fieldref;
    }

    @Override
    public void read() {
        this.class_index = classImage.readShort();
        this.name_and_type_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_Fieldref: class@%d nameType@%d",
                class_index,
                name_and_type_index
        );
    }
}
