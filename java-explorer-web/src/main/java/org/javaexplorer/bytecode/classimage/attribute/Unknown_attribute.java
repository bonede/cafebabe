package org.javaexplorer.bytecode.classimage.attribute;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.javaexplorer.bytecode.classimage.ClassImage;

@Slf4j
public class Unknown_attribute extends attribute_info {
    private byte[] value;

    public Unknown_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
        log.info("Unknown attributes: " + getAttributeName());
    }

    @Override
    public void read() {
        value = classImage.readBytes(attribute_length);
    }

    public String getValue() {
        return Hex.encodeHexString(value);
    }

}
