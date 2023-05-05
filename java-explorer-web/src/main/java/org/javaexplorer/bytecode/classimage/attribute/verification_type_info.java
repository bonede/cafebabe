package org.javaexplorer.bytecode.classimage.attribute;

import lombok.Data;

@Data
public class verification_type_info {
    public int tag;
    public Integer cpool_index;
    public Integer offset;

    public String getTagName() {
        switch (tag) {
            case 0:
                return "ITEM_Top";
            case 1:
                return "ITEM_Integer";
            case 2:
                return "ITEM_Float";
            case 3:
                return "ITEM_Double";
            case 4:
                return "ITEM_Long";
            case 6:
                return "ITEM_UninitializedThis";
            case 7:
                return "ITEM_Object";
            case 8:
                return "ITEM_Uninitialized";
            default:
                return "ITEM_Unknown";
        }
    }
}
