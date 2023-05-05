package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;
import app.cafebabe.bytecode.op.Instruction;
import app.cafebabe.bytecode.op.Instructions;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Code_attribute extends attribute_info {
    Instruction[] instructions;
    attribute_info[] attributes;
    private int max_stack;
    private int max_locals;
    private int code_length;
    private ByteBuffer codeBuffer;

    public int getCodeLength() {
        return code_length;
    }

    private int exception_table_length;
    private exception_table_item[] exception_table;
    private int attributes_count;

    public int getAttributesCount() {
        return attributes_count;
    }

    public Code_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    @JsonIgnore
    public int getCodeBufferPosition() {
        return codeBuffer.position();
    }

    public int getMaxStack() {
        return max_stack;
    }

    public int getMaxLocals() {
        return max_locals;
    }

    public attribute_info[] getAttributes() {
        return attributes;
    }

    public int getExceptionTableLength() {
        return exception_table_length;
    }

    @Override
    public void read() {
        max_stack = classImage.readShort();
        max_locals = classImage.readShort();
        code_length = classImage.readInt();
        codeBuffer = ByteBuffer.wrap(classImage.readBytes(code_length));
        exception_table_length = classImage.readu2();
        parseExceptionTable(classImage);
        attributes_count = classImage.readu2();
        attributes = classImage.parseAttributeInfo(attributes_count);
    }


    private void parseExceptionTable(ClassImage classImage) {
        List<exception_table_item> exception_table = new ArrayList<>();
        for (int i = 0; i < exception_table_length; i++) {
            exception_table.add(new exception_table_item(
                    classImage.readu2(),
                    classImage.readu2(),
                    classImage.readu2(),
                    classImage.readu2()
            ));
        }
        this.exception_table = exception_table.toArray(new exception_table_item[]{});
    }

    private int readu1(ByteBuffer byteBuffer, int offset) {
        return Byte.toUnsignedInt(byteBuffer.get(offset));
    }

    public Instruction getInstructionAt(int offset) {
        int opCode = readu1(codeBuffer, offset);
        Instruction instruction = Instructions.instructionOf(opCode);
        codeBuffer.position(offset + 1);
        instruction.parse(this);
        codeBuffer.clear();
        return instruction;
    }

    public int readu1() {
        return Byte.toUnsignedInt(codeBuffer.get());
    }

    public void skip(int bytes) {
        codeBuffer.position(codeBuffer.position() + bytes);
    }

    public byte readByte() {
        return codeBuffer.get();
    }

    public List<Instruction> getInstructions() {
        codeBuffer.clear();
        List<Instruction> instructions = new ArrayList<>();
        while (codeBuffer.hasRemaining()) {
            int opCode = Byte.toUnsignedInt(codeBuffer.get());
            Instruction instruction = Instructions.instructionOf(opCode);
            instruction.parse(this);
            instructions.add(instruction);
        }
        return instructions;
    }

    @Override
    public String toString() {
        return String.format(
                "    Code_attribute:\n" +
                        "    max_stack: %d\n" +
                        "    max_locals: %d\n" +
                        "    code_length: %d\n" +
                        "    exception_table_length: %d\n" +
                        "    instructions:\n%s\n" +
                        "    %s\n" +
                        "    exception_table: %s" +
                        "\n",
                max_stack,
                max_locals,
                code_length,
                exception_table_length,
                Arrays.stream(instructions).map(e -> "      " + e.toString()).collect(Collectors.joining("\n")),
                Arrays.stream(attributes).map(e -> e.toString()).collect(Collectors.joining("\n")),
                Arrays.stream(exception_table).map(e -> e.toString()).collect(Collectors.joining("\n"))
        );
    }

    public int readu2() {
        return Short.toUnsignedInt(codeBuffer.getShort());
    }

    public int readu4() {
        return codeBuffer.getInt();
    }

    public short readShort() {
        return codeBuffer.getShort();
    }
}
