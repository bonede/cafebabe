package app.cafebabe.bytecode.classimage.attribute;

public class line_number_table_item {
    public final int start_pc;
    public final int line_number;

    public line_number_table_item(int start_pc, int line_number) {
        this.start_pc = start_pc;
        this.line_number = line_number;
    }

    public int getStartPc() {
        return start_pc;
    }

    public int getLineNumber() {
        return line_number;
    }

    @Override
    public String toString() {
        return String.format("Line: %d -> %d", line_number, start_pc);
    }
}
