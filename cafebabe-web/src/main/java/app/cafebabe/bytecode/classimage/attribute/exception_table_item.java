package app.cafebabe.bytecode.classimage.attribute;

public class exception_table_item {
    public final int start_pc;
    public final int end_pc;
    public final int handler_pc;
    public final int catch_type;

    public int getStartPc() {
        return start_pc;
    }

    public int getEndPc() {
        return end_pc;
    }

    public int getHandlerPc() {
        return handler_pc;
    }

    public int getCatchType() {
        return catch_type;
    }

    public exception_table_item(int start_pc, int end_pc, int handler_pc, int catch_type) {
        this.start_pc = start_pc;
        this.end_pc = end_pc;
        this.handler_pc = handler_pc;
        this.catch_type = catch_type;
    }

    @Override
    public String toString() {
        return String.format("exception_table_item: start_pc=%d end_pc=end_pc handler_pc=%d catch_type@%d",
                start_pc, end_pc, handler_pc, catch_type
        );
    }
}
