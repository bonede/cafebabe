package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.parameter_access_flag;
import lombok.Data;

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
