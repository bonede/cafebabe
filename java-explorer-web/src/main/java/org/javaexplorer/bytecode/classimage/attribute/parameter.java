package org.javaexplorer.bytecode.classimage.attribute;

import lombok.Data;
import org.javaexplorer.bytecode.classimage.parameter_access_flag;

import java.util.List;

@Data
public class parameter {
    public String name;
    public int name_index;
    public short access_flags;

    public List<parameter_access_flag> getAccess_flags() {
        return parameter_access_flag.fromBitField(access_flags);
    }
}
