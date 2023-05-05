package app.cafebabe.bytecode.classimage;

import app.cafebabe.bytecode.classimage.attribute.element_value_pair;
import lombok.Data;

@Data
public class annotation {
    public int type_index;
    public String typeName;
    public int num_element_value_pairs;
    public element_value_pair[] element_value_pairs;
}
