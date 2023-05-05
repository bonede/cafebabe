package app.cafebabe.bytecode.classimage.attribute;

import lombok.Data;

@Data
public class local_variable_info {
    public int start_pc;
    public int length;
    public String name;
    public String descriptor;
    public int name_index;
    public int descriptor_index;
    public int index;
}
