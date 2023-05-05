package app.cafebabe.bytecode.vm;

import java.util.HashMap;
import java.util.Map;

/**
 * Store statics
 */
public class HeapObject {
    private final Map<String, Object> storage = new HashMap<>();
    private String className;
    public HeapObject(){

    }

    public HeapObject(String className) {
        this.className = className;
    }

    private String getKey(String className, String filedName){
        return className + "." + filedName;
    }

    public int getInt(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (int) storage.get(key);
    }

    public double getDouble(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (double) storage.get(key);
    }

    public boolean contains(String className, String fieldName){
        String key = getKey(className, fieldName);
        return storage.containsKey(key);
    }

    public boolean getBool(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (boolean) storage.get(key);
    }


    public long getLong(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (long) storage.get(key);
    }

    public short getShort(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (short) storage.get(key);
    }

    public char getChar(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (char) storage.get(key);
    }

    public byte getByte(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (byte) storage.get(key);
    }

    public float getFloat(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (float) storage.get(key);
    }

    public int getRef(String className, String fieldName){
        String key = getKey(className, fieldName);
        return (int) storage.get(key);
    }


    public void put(String className, String fieldName, Object value){
        String key = getKey(className, fieldName);
        storage.put(key, value);
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
