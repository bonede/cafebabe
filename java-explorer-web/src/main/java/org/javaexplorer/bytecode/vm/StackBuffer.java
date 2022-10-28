package org.javaexplorer.bytecode.vm;

import java.nio.ByteBuffer;

public class StackBuffer {
    private ByteBuffer convertor = ByteBuffer.wrap(new byte[8]);
    private int[] stack;
    private int sp;

    public StackBuffer(int size){
        stack = new int[size];
    }

    public void pushInt(int value){
        stack[sp++] = value;
    }


    public void pushByte(byte value){
        stack[sp++] = value;
    }
    public int popInt(){
        return stack[--sp];
    }

    public void pushLong(long value){
        convertor.clear();
        convertor.putLong(value);
        convertor.flip();
        pushInt(convertor.getInt());
        pushInt(convertor.getInt());
    }

    public long popLong() {
        convertor.clear();
        convertor.putInt(popInt());
        convertor.putInt(popInt());
        convertor.flip();
        return convertor.getLong();
    }

    public void pushFloat(float value) {
        convertor.clear();
        convertor.putFloat(value);
        convertor.flip();
        stack[sp++] = convertor.getInt();
    }

    public float popFloat() {
        convertor.clear();
        convertor.putInt(popInt());
        convertor.flip();
        return convertor.getFloat();
    }

    public void pushBool(boolean value) {
        stack[sp++] = value ? 1 : 0;
    }
    public boolean popBool(){
        int result = popInt();
        return (result & 1) == 1;
    }
    public void pushChar(char value) {
        stack[sp++] = value;
    }

    public char popChar(){
        return (char) popInt();
    }

    public byte popByte(){
        return (byte) popInt();
    }

    public void pushShort(short value) {
        stack[sp++] = value;
    }

    public short popShort(){
        return (short) popInt();
    }

    public void pushDouble(double value){
        convertor.clear();
        convertor.putDouble(value);
        convertor.flip();
        pushInt(convertor.getInt());
        pushInt(convertor.getInt());
    }

    public double popDouble() {
        convertor.clear();
        convertor.putInt(popInt());
        convertor.putInt(popInt());
        convertor.flip();
        return convertor.getDouble();
    }

    public void pushRef(int ref){
        pushInt(ref);
    }

    public int popRef(){
        return popInt();
    }

    public int getInt(int index){
        return stack[index];
    }

    public void setInt(int index, int value){
        stack[index] = value;
    }

    public long getLong(int index) {
        convertor.clear();
        convertor.putInt(getInt(index));
        convertor.putInt(getInt(index + 1));
        convertor.flip();
        return convertor.getLong();
    }
    public void setLong(int index, long value) {
        convertor.clear();
        convertor.putLong(value);
        convertor.flip();
        setInt(index, convertor.getInt());
        setInt(index + 1, convertor.getInt());
    }

    public float getFloat(int index) {
        convertor.clear();
        convertor.putInt(getInt(index));
        convertor.flip();
        return convertor.getFloat();
    }

    public void setFloat(int index, float value) {
        convertor.clear();
        convertor.putFloat(value);
        convertor.flip();
        stack[index] = convertor.getInt();
    }

    public boolean getBool(int index){
        return stack[index] == 1 ? true : false;
    }

    public void setBool(int index, boolean value){
        stack[index] = value ? 1 : 0;
    }

    public char getChar(int index){
        return (char) stack[index];
    }

    public void setChar(int index, char value){
        stack[index] = value;
    }

    public byte getByte(int index){
        return (byte) stack[index];
    }

    public void setByte(int index, byte value){
        stack[index] = value;
    }

    public short getShort(int index){
        return (short) stack[index];
    }

    public void setShort(int index, short value){
        stack[index] = value;
    }


    public void setDouble(int index, double value){
        convertor.clear();
        convertor.putDouble(value);
        convertor.flip();
        setInt(index, convertor.getInt());
        setInt(index + 1, convertor.getInt());
    }

    public double getDouble(int index) {
        convertor.clear();
        convertor.putInt(getInt(index));
        convertor.putInt(getInt(index + 1));
        convertor.flip();
        return convertor.getDouble();
    }

    public int getRef(int index){
        return getInt(index);
    }

    public void setRef(int index, int value){
        setInt(index, value);
    }

}
