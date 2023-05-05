package app.cafebabe.bytecode.classimage.attribute;

import lombok.Data;

@Data
public class stack_map_frame {
    public int frame_type;

    public String getFrameTypeName() {
        if (frame_type >= 0 && frame_type <= 63) {
            return "SAME";
        } else if (frame_type >= 64 && frame_type <= 127) {
            return "SAME_LOCALS_1_STACK_ITEM";
        } else if (frame_type == 247) {
            return "SAME_LOCALS_1_STACK_ITEM_EXTENDED";
        } else if (frame_type >= 248 && frame_type <= 250) {
            return "CHOP";
        } else if (frame_type == 251) {
            return "SAME_FRAME_EXTENDED";
        } else if (frame_type >= 252 && frame_type <= 254) {
            return "APPEND";
        } else if (frame_type == 255) {
            return "FULL_FRAME";
        } else {
            return "RESERVED";
        }
    }

    public Integer offset_delta;
    public Integer number_of_locals;
    public Integer number_of_stack_items;
    public verification_type_info[] stack;
    public verification_type_info[] locals;
}
