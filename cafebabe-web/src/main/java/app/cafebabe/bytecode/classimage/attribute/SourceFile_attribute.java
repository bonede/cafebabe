package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;

public class SourceFile_attribute extends attribute_info {
    private short sourceFileNameIndex;

    public SourceFile_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    @Override
    public void read() {
        sourceFileNameIndex = classImage.readShort();
    }

    public String getSourceFileName() {
        return classImage.getUtf8At(sourceFileNameIndex);
    }

    public short getSourceFileNameIndex() {
        return sourceFileNameIndex;
    }

}
