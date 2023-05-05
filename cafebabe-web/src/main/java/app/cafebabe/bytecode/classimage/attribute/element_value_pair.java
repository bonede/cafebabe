package app.cafebabe.bytecode.classimage.attribute;

import lombok.Data;

@Data
public class element_value_pair {
    public int element_name_index;
    public String elementName;
    public element_value value;
}
