package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.28">ref</a>
 */
public class NestHost_attribute extends attribute_info {
    private int host_class_index;
    private String hostClass;

    public NestHost_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public int getHost_class_index() {
        return host_class_index;
    }

    public String getHostClass(){
        return hostClass;
    }

    @Override
    public void read() {
        host_class_index = classImage.readu2();
        hostClass = classImage.getClassInfoAt(host_class_index).getName();
    }
}
