package org.javaexplorer.model.classfile.annotation;

public enum ElementValueType {
    ELEMENT_VALUE_BYTE('B'),
    ELEMENT_VALUE_CHAR('C'),
    ELEMENT_VALUE_DOUBLE('D'),
    ELEMENT_VALUE_FLOAT('F'),
    ELEMENT_VALUE_INT('I'),
    ELEMENT_VALUE_LONG('J'),
    ELEMENT_VALUE_SHORT('S'),
    ELEMENT_VALUE_BOOL('Z'),
    ELEMENT_VALUE_STRING('s'),
    ELEMENT_VALUE_ENUM('e'),
    ELEMENT_VALUE_CLASS('c'),
    ELEMENT_VALUE_ANNOTATION('@'),
    ELEMENT_VALUE_ARRAY('[');
    private char tag;
    ElementValueType(char tag) {
        this.tag = tag;
    }

    public static ElementValueType valueOf(char tag){
        switch (tag){
            case 'B' : return ELEMENT_VALUE_BYTE;
            case 'C' : return ELEMENT_VALUE_CHAR;
            case 'D' : return ELEMENT_VALUE_DOUBLE;
            case 'F' : return ELEMENT_VALUE_FLOAT;
            case 'I' : return ELEMENT_VALUE_INT;
            case 'J' : return ELEMENT_VALUE_LONG;
            case 'S' : return ELEMENT_VALUE_SHORT;
            case 'Z' : return ELEMENT_VALUE_BOOL;
            case 's' : return ELEMENT_VALUE_STRING;
            case 'e' : return ELEMENT_VALUE_ENUM;
            case 'c' : return ELEMENT_VALUE_CLASS;
            case '@' : return ELEMENT_VALUE_ANNOTATION;
            case '[' : return ELEMENT_VALUE_ARRAY;
        }
        throw new RuntimeException("Invalid element value tag " + tag);
    }



}
