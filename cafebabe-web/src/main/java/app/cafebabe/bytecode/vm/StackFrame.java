package app.cafebabe.bytecode.vm;

public class StackFrame {
    private static final int BUFFER_SIZE = 1024;
    // Method index
    private int mi;
    // Class image index
    private int ci;
    private int pc;

    public StackBuffer getLocals() {
        return locals;
    }

    public StackBuffer getOperands() {
        return operands;
    }

    public int getMi() {
        return mi;
    }

    public int getCi() {
        return ci;
    }


    private StackBuffer locals;
    private StackBuffer operands;

    public int getPc() {
        return pc;
    }
    public void increasePc(int offset){
        pc += offset;
    }
    public void setPc(int pc){
        this.pc = pc;
    }
    public StackFrame(int ci, int mi){
        this.ci = ci;
        this.mi = mi;
        locals = new StackBuffer(BUFFER_SIZE);
        operands = new StackBuffer(BUFFER_SIZE);
    }
}
