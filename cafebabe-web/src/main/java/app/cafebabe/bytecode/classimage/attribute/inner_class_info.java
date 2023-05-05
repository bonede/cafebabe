package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.class_access_flag;
import lombok.Data;

import java.util.List;

@Data
public class inner_class_info {
    public int inner_class_info_index;
    public int outer_class_info_index;
    public int inner_name_index;
    public String innerName;
    public String innerClass;
    public String outerClass;

    public List<class_access_flag> inner_class_access_flags;
}
