package org.javaexplorer.bytecode.vm;


import org.javaexplorer.bytecode.op.Instruction;

import java.io.IOException;
import java.util.Stack;

/*
x86 call exmaple:
0000000000000000 <caller>:
   0:   55                      push   rbp
   1:   48 89 e5                mov    rbp,rsp
   4:   48 83 ec 10             sub    rsp,0x10 ; point to stack top
   8:   c7 45 f8 03 00 00 00    mov    DWORD PTR [rbp-0x8],0x3
   f:   c7 45 fc 05 00 00 00    mov    DWORD PTR [rbp-0x4],0x5
  16:   8b 55 fc                mov    edx,DWORD PTR [rbp-0x4]
  19:   8b 45 f8                mov    eax,DWORD PTR [rbp-0x8]
  1c:   89 d6                   mov    esi,edx
  1e:   89 c7                   mov    edi,eax
  20:   e8 00 00 00 00          call   25 <caller+0x25>
  25:   c9                      leave
  26:   c3                      ret

0000000000000027 <callee>:
  27:   55                      push   rbp
  28:   48 89 e5                mov    rbp,rsp ; skip setup stack, since not calling any function
  2b:   89 7d ec                mov    DWORD PTR [rbp-0x14],edi
  2e:   89 75 e8                mov    DWORD PTR [rbp-0x18],esi
  31:   8b 45 ec                mov    eax,DWORD PTR [rbp-0x14]
  34:   0f af 45 e8             imul   eax,DWORD PTR [rbp-0x18]
  38:   89 45 fc                mov    DWORD PTR [rbp-0x4],eax
  3b:   8b 45 fc                mov    eax,DWORD PTR [rbp-0x4]
  3e:   5d                      pop    rbp
  3f:   c3                      ret



The CALL instruction performs two operations:
1. It pushes the return address (address immediately after the CALL instruction)
    on the stack.
2. It changes EIP to the call destination. This effectively transfers control to
    the call target and begins execution there.


The RTN instruction:
1. pop EIP value from stack to EIP register.
2. JMP to instruction EIP points to.
 */



public class Vm {
    /*
        Stack format:
            caller return pc <- another frame
            ... <- stack top(sp)
            ...
            ... <- start of locals
            ...
            ...
            ...
            caller mi <- start of operands(bp)
            caller ci
            caller osp
            caller bp
            caller return pc
     */


    private ClassImageHeap classImageHeap = new ClassImageHeap();
    Stack<StackFrame> stack = new Stack<>();
    Stack<StackFrame> staticStack = new Stack<>();

    public Heap getHeap() {
        return heap;
    }

    private Heap heap = new Heap();

    public HeapObject getStaticHeap() {
        return staticHeapObject;
    }

    private HeapObject staticHeapObject = new HeapObject();


    private String userClassPath = System.getProperty( "java.class.path" );
    private String javaHome = System.getProperty( "java.home" );
    private ClassImageLoader classImageLoader = new ClassImageLoader(userClassPath, javaHome);

    public ClassImage findClassImageByClassName(String className){
        ClassImage classImage = classImageHeap.getByClassName(className);
        if (classImage == null) {
            try {
                classImage = classImageLoader.loadClassImageByName(className);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (classImage == null) {
                throw new RuntimeException("TODO: Load class image by class name: " + className);
            }
            classImageHeap.put(classImage);
            initImage(classImage);
        }
        return classImage;
    }

    public void pushFrame(int mi, int ci, int offset){
        getCurrentFrame().increasePc(offset);
        stack.push(new StackFrame(ci, mi));
    }

    public void pushStaticFrame(int mi, int ci, int offset){
        getCurrentStaticFrame().increasePc(offset);
        staticStack.push(new StackFrame(ci, mi));
    }

    public void popStaticFrame(){
        staticStack.pop();
    }
    public void popFrame(){
        stack.pop();
    }


    public void initImage(ClassImage classImage){
        for(ClassImage.cp_info c : classImage.getConstant_pool()){
            if(c instanceof ClassImage.CONSTANT_String_info){
                String value = ((ClassImage.CONSTANT_String_info) c).getValue(classImage);
                // TODO put string to heap
            }
        }

        for(ClassImage.method_info method : classImage.getMethods()){
            if(method.isStaticInitializer(classImage)){
                pushFrame(-1, 0, 0);
                pushFrame(method.getIndex(), classImage.hashCode(), 0);
                run();
                return;
            }

        }

    }
    public void loadMainClass(ClassImage classImage){
        stack.push(new StackFrame(-1, -1));
        classImageHeap.put(classImage);
        initImage(classImage);
        ClassImage.method_info mainMethod = classImage.getMainMethod();
        if(mainMethod == null){
            throw new RuntimeException("No main method in " + classImage.getClassName());
        }
        pushFrame(mainMethod.getIndex(), classImage.hashCode(), 1);
        run();
    }
    public StackFrame getCurrentFrame(){
        return stack.peek();
    }

    public StackFrame getCurrentStaticFrame(){
        return staticStack.peek();
    }
    public ClassImage.method_info getCurrentStaticMethod(){
        if(staticStack.isEmpty()){
            return null;
        }
        ClassImage classImage = getCurrentStaticClassImage();
        if(classImage == null){
            return null;
        }

        return classImage.getMethodByIndex(getCurrentStaticFrame().getMi());
    }
    public ClassImage.method_info getCurrentMethod(){
        if(stack.isEmpty()){
            return null;
        }
        ClassImage classImage = getCurrentClassImage();
        if(classImage == null){
            return null;
        }
        return classImage.getMethodByIndex(getCurrentFrame().getMi());
    }

    public ClassImage getCurrentClassImage(){
        return classImageHeap.getByRef(getCurrentFrame().getCi());
    }

    public ClassImage getClassImageByName(String className){
        return classImageHeap.getByClassName(className);
    }

    public ClassImage getCurrentStaticClassImage(){
        return classImageHeap.getByRef(getCurrentStaticFrame().getCi());
    }

    public void run(){
        Instruction instruction;
        try {
            while (getCurrentMethod() != null && (instruction = fetchInstruction(getCurrentMethod())) != null){
                System.out.println(instruction);
                instruction.execute(this);
                if(getCurrentFrame().getMi() < 0){
                    popFrame();
                    return;
                }
            }
        }catch (Exception e){
            printDebugInfo();
            throw e;
        }

    }

    public Instruction fetchInstruction(ClassImage.method_info method_info){
        Instruction instruction = method_info.fetchInstruction(this);
        if(instruction == null){
            return null;
        }
        return instruction;
    }

    public ClassImage.cp_info getConstant(int index){
        return getCurrentClassImage().getConstant(index);
    }


    public void printDebugInfo(){
//        printClassInfo();
//        printRegisters();
//        printStack();
        printStaticHeap();
        printHeap();
        printStacktrace();
        System.out.println("\n");
    }

    private void printClassInfo() {
        System.out.println("Current class: " + getCurrentClassImage().getClassName());
    }

    private void printStaticHeap() {
        System.out.println("Static Heap:");
        System.out.println(staticHeapObject);
    }

    private void printHeap() {
        System.out.println("Heap:");
        System.out.println(heap);
    }

    private void printRegisters() {
        System.out.println(
            String.format("Registers: pc:%d",
                0
        ));
    }
    private void printStacktrace(){
        System.out.println("Stacktrace:");
        for(int i = stack.size() - 1; i > 0; i--){
            StackFrame stackFrame = stack.get(i);
            if(stackFrame.getMi() < 0){
                continue;
            }
            ClassImage classImage = classImageHeap.getByRef(stackFrame.getCi());
            ClassImage.method_info method = classImage.getMethodByIndex(stackFrame.getMi());
            System.out.println(
                    classImage.getClassName() + "." +
                    method.getName(classImage) +
                    method.getDescriptor(classImage)
            );
        }
    }

    public void putStatic(String className, String fieldName, Object value) {
        staticHeapObject.put(className, fieldName, value);
    }

    public void setLocalRef(int index, int value){
        getCurrentFrame().getLocals().setRef(index, value);
    }

    public void setLocalInt(int index, int value){
        getCurrentFrame().getLocals().setInt(index, value);
    }

    public void setLocalShort(int index, short value){
        getCurrentFrame().getLocals().setShort(index, value);
    }

    public void setLocalByte(int index, byte value){
        getCurrentFrame().getLocals().setShort(index, value);
    }

    public void setLocalFloat(int index, float value){
        getCurrentFrame().getLocals().setFloat(index, value);
    }

    public void setLocalLong(int index, long value){
        getCurrentFrame().getLocals().setLong(index, value);
    }

    public void setLocalBool(int index, boolean value){
        getCurrentFrame().getLocals().setBool(index, value);
    }

    public void setLocalChar(int index, char value){
        getCurrentFrame().getLocals().setChar(index, value);
    }

    public void setLocalDouble(int index, double value){
        getCurrentFrame().getLocals().setDouble(index, value);
    }

    public int popOpInt(){
        return getCurrentFrame().getOperands().popInt();
    }

    public short popOpShort(){
        return getCurrentFrame().getOperands().popShort();
    }

    public byte popOpByte(){
        return getCurrentFrame().getOperands().popByte();
    }
    public long popOpLong(){
        return getCurrentFrame().getOperands().popLong();
    }
    public float popOpFloat(){
        return getCurrentFrame().getOperands().popFloat();
    }

    public boolean popOpBool(){
        return getCurrentFrame().getOperands().popBool();
    }

    public double popOpDouble(){
        return getCurrentFrame().getOperands().popDouble();
    }

    public char popOpChar(){
        return getCurrentFrame().getOperands().popChar();
    }

    public int popOpRef(){
        return getCurrentFrame().getOperands().popRef();
    }



    public void pushOpFloat(float value){
        getCurrentFrame().getOperands().pushFloat(value);
    }

    public void pushOpInt(int value){
        getCurrentFrame().getOperands().pushInt(value);
    }

    public void pushOpByte(byte value){
        getCurrentFrame().getOperands().pushByte(value);
    }

    public void pushOpNull(){
        getCurrentFrame().getOperands().pushInt(0);
    }

    public void pushOpDouble(double value){
        getCurrentFrame().getOperands().pushDouble(value);
    }

    public void pushOpShort(short value){
        getCurrentFrame().getOperands().pushShort(value);
    }

    public void pushOpLong(long value){
        getCurrentFrame().getOperands().pushLong(value);
    }

    public void pushOpBool(boolean value){
        getCurrentFrame().getOperands().pushBool(value);
    }

    public void pushOpChar(char value){
        getCurrentFrame().getOperands().pushChar(value);
    }

    public void pushOpRef(int value){
        getCurrentFrame().getOperands().pushRef(value);
    }

    public int getLocalInt(int index){
        return getCurrentFrame().getLocals().getInt(index);
    }

    public int getPc() {
        return getCurrentFrame().getPc();
    }

    public void increasePc(int offset){
        getCurrentFrame().increasePc(offset);
    }
}
