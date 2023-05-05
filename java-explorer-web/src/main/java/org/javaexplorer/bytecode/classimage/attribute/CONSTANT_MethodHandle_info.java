package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.bytecode.classimage.constant.Kind;
import org.javaexplorer.bytecode.classimage.constant.cp_info;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.8">ref</a>
 */
public class CONSTANT_MethodHandle_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_MethodHandle_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private Kind kind;
    private short reference_index;

    public Kind getKind() {
        return kind;
    }

    public short getReferenceIndex() {
        return reference_index;
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_MethodHandle;
    }

    @Override
    public void read() {
        this.kind = Kind.ofValue(classImage.readByte());
        this.reference_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_MethodHandle: kind=%d reference@%d",
                kind.value,
                reference_index
        );
    }
}
