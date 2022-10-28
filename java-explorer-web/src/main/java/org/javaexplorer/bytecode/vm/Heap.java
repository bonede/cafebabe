package org.javaexplorer.bytecode.vm;

import java.util.HashMap;
import java.util.Map;

public class Heap {
    private final Map<Integer, HeapObject> storage = new HashMap<>();
    public HeapObject get(int ref){
        return storage.get(ref);
    }

    public boolean contains(int ref){
        return storage.containsKey(ref);
    }

    public int createObject(){
        HeapObject heapObject = new HeapObject();
        storage.put(heapObject.hashCode(), heapObject);
        return heapObject.hashCode();
    }

    public int createObject(String className){
        HeapObject heapObject = new HeapObject(className);
        storage.put(heapObject.hashCode(), heapObject);
        return heapObject.hashCode();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
