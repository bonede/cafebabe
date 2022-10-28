package org.javaexplorer.bytecode.vm;

import java.util.HashMap;
import java.util.Map;

public class ClassImageHeap {
    private Map<Integer, ClassImage> classImages = new HashMap<>();
    private Map<String, Integer> classNames = new HashMap<>();

    public ClassImage getByRef(int ref){
        return classImages.get(ref);
    }

    public void put(ClassImage classImage){
        classImages.put(classImage.hashCode(), classImage);
        classNames.put(classImage.getClassName(), classImage.hashCode());
    }

    public ClassImage getByClassName(String className){
        Integer ref = classNames.get(className);
        if(ref != null){
            return getByRef(ref);
        }
        return null;
    }
}
