package app.cafebabe.bytecode.op;

import java.util.ArrayList;
import java.util.List;

public class DescriptorParser {
    public static Descriptor parse(String source){
        return new Parser(source).parse();
    }
    public enum TokenType{
        LP,
        RP,
        /**
         * BYTE
         */
        B,
        C,
        D,
        F,
        I,
        J,
        L,
        SEMICOLON,
        S,
        Z,
        /** [ **/
        A,
        V,
        NAME,
        NAME_SP,
        EOF;

        @Override
        public String toString() {
            switch (this){
                case LP:
                    return "(";
                case RP:
                    return ")";
                case B:
                    return "B";
                case C:
                    return "C";
                case D:
                    return "D";
                case F:
                    return "F";
                case I:
                    return "I";
                case J:
                    return "J";
                case L:
                    return "L";
                case SEMICOLON:
                    return ";";
                case S:
                    return "S";
                case Z:
                    return "Z";
                case A:
                    return "[";
                case V:
                    return "V";
                case NAME:
                    return "NAME";
                case NAME_SP:
                    return "/";
                case EOF:
                    return "EOF";
            }
            throw new RuntimeException("SHOULD NOT REACH");
        }
    }
    public static class Token {
        private TokenType type;
        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public TokenType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        private static Token LP = Token.of(TokenType.LP);
        private static Token RP = Token.of(TokenType.RP);
        private static Token B = Token.of(TokenType.B);
        private static Token C = Token.of(TokenType.C);
        private static Token D = Token.of(TokenType.D);
        private static Token F = Token.of(TokenType.F);
        private static Token I = Token.of(TokenType.I);
        private static Token J = Token.of(TokenType.J);
        private static Token L = Token.of(TokenType.L);
        private static Token SEMICOLON = Token.of(TokenType.SEMICOLON);
        private static Token S = Token.of(TokenType.S);
        private static Token Z = Token.of(TokenType.Z);
        private static Token A = Token.of(TokenType.A);
        private static Token V = Token.of(TokenType.V);
        private static Token NAME_SP = Token.of(TokenType.NAME_SP);
        private static Token EOF = Token.of(TokenType.EOF);
        private static Token NAME(String value){
            return new Token(TokenType.NAME, value);
        }

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            if(value != null){
                return type + ": " + value;
            }else {
                return type.toString();
            }
        }

        public static Token of(TokenType tokenType){
            return new Token(tokenType, null);
        }
    }
    public interface Descriptor {}

    public enum FieldTag {
        BYTE,
        CHAR,
        DOUBLE,
        FLOAT,
        INT,
        LONG,
        SHORT,
        CLASS,
        BOOL,
        ARRAY,
        V
    }


    public enum ReturnTag {
        BYTE,
        CHAR,
        DOUBLE,
        INT,
        LONG,
        SHORT,
        CLASS,
        BOOL,
        ARRAY,
        VOID
    }

    public static class FieldType implements Descriptor {
        private FieldTag tag;
        private FieldTag arrayType;
        private String className;

        public FieldTag getTag() {
            return tag;
        }

        public FieldTag getArrayType() {
            return arrayType;
        }

        public String getClassName() {
            return className;
        }

        public int getDimension() {
            return dimension;
        }

        private int dimension;

        private FieldType(FieldTag arrayType, int dimension, String className) {
            this.tag = FieldTag.ARRAY;
            this.arrayType = arrayType;
            this.dimension = dimension;
            this.className = className;
        }

        private FieldType(FieldTag tag) {
            this.tag = tag;
        }

        public static FieldType of(FieldTag tag){
            switch (tag){
                case BYTE: return FieldType.BYTE;
                case CHAR: return FieldType.CHAR;
                case DOUBLE: return FieldType.DOUBLE;
                case INT: return FieldType.INT;
                case LONG: return FieldType.LONG;
                case SHORT: return FieldType.SHORT;
                case BOOL: return FieldType.BOOL;
                case FLOAT: return FieldType.FLOAT;
                case V: return FieldType.VOID;
            }
            throw new RuntimeException("Should not reach");
        }

        private FieldType(String className) {
            this.tag = FieldTag.CLASS;
            this.className = className;
        }

        public static FieldType BYTE = new FieldType(FieldTag.BYTE);
        public static FieldType CHAR = new FieldType(FieldTag.CHAR);
        public static FieldType DOUBLE = new FieldType(FieldTag.DOUBLE);
        public static FieldType INT = new FieldType(FieldTag.INT);
        public static FieldType LONG = new FieldType(FieldTag.LONG);
        public static FieldType SHORT = new FieldType(FieldTag.SHORT);
        public static FieldType BOOL = new FieldType(FieldTag.BOOL);
        public static FieldType FLOAT = new FieldType(FieldTag.FLOAT);
        public static FieldType VOID = new FieldType(FieldTag.V);
        public static FieldType CLASS(String className){
            return new FieldType(className);
        }
        public static FieldType ARRAY(FieldTag arrayType, int dimension, String className){
            return new FieldType(arrayType, dimension, className);
        }

        public void setTag(FieldTag tag) {
            this.tag = tag;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public void setDimension(int dimension) {
            this.dimension = dimension;
        }
    }


    public static class MethodType implements Descriptor {
        private List<FieldType> params;
        private FieldType returnType;

        public MethodType(List<FieldType> params, FieldType returnType) {
            this.params = params;
            this.returnType = returnType;
        }

        public List<FieldType> getParams() {
            return params;
        }

        public FieldType getReturnType() {
            return returnType;
        }
    }


    public static class Lexer {
        private String source;
        private int index;
        private char chr;
        private StringBuffer buffer = new StringBuffer();
        List<Token> tokens = new ArrayList<>();
        Token nameToken;
        private char EOF = 0;
        public Lexer(String source) {
            this.source = source;
        }
        private void nextChar(){
            if(index < source.length()){
                chr = source.charAt(index++);
            }else {
                chr = EOF;
            }
        }

        private void nameToken(){
            nextChar();
            while (chr != EOF && chr != ';' && chr != '/'){
                buffer.append(chr);
                nextChar();
            }
            tokens.add(Token.NAME(buffer.toString()));
            buffer.setLength(0);
            index--;
        }



        public List<Token> tokenize(){
            nextChar();
            while (chr != EOF){
                if(chr == '('){
                    tokens.add(Token.LP);
                }else if(chr == ')'){
                    tokens.add(Token.RP);
                }else if(chr == 'B'){
                    tokens.add(Token.B);
                }else if(chr == 'C'){
                    tokens.add(Token.C);
                }else if(chr == 'D'){
                    tokens.add(Token.D);
                }else if(chr == 'F'){
                    tokens.add(Token.F);
                }else if(chr == 'I'){
                    tokens.add(Token.I);
                }else if(chr == 'L'){
                    tokens.add(Token.L);
                    nameToken();
                }else if(chr == ';'){
                    tokens.add(Token.SEMICOLON);
                }else if(chr == 'S'){
                    tokens.add(Token.S);
                }else if(chr == 'Z'){
                    tokens.add(Token.Z);
                }else if(chr == '['){
                    tokens.add(Token.A);
                }else if(chr == 'V'){
                    tokens.add(Token.V);
                }else if(chr == '/'){
                    tokens.add(Token.NAME_SP);
                    nameToken();
                } else {
                    throw new RuntimeException("Invalid char: '" + chr + "' at: " + index);
                }
                nextChar();
            }
            tokens.add(Token.EOF);
            return tokens;
        }
    }

    public static class Parser{
        private List<Token> tokens;
        int index = 0;
        public Parser(String source) {
            tokens = new Lexer(source).tokenize();
        }

        private Token token;
        private Descriptor descriptor;
        private FieldTag fieldTag;
        private int dimension;
        private StringBuffer className = new StringBuffer();
        public Descriptor parse(){
            nextToken();
            if(accept(TokenType.LP)){
                method();
            }else{
                type();
            }
            expect(TokenType.EOF);
            return descriptor;
        }
        public boolean hasClassName(){
            return className.length() > 0;
        }
        public String consumeClassName(){
            String result = className.toString();
            className.setLength(0);
            return result;
        }
        public void array(){
            dimension = 0;
            while (accept(TokenType.A)){
                dimension++;
            }
            type();
            if(hasClassName()){
                descriptor = FieldType.ARRAY(FieldTag.CLASS, dimension, consumeClassName());
            }else {
                descriptor = FieldType.ARRAY(fieldTag, dimension, null);
            }
            fieldTag = null;
            dimension = 0;
        }

        public void className(){
            nextToken();
            className.append(token.getValue());
            expect(TokenType.NAME);

            while (accept(TokenType.NAME_SP)){
                className.append("/");
                className.append(token.getValue());
                expect(TokenType.NAME);
            }
            expect(TokenType.SEMICOLON);
        }

        public void method(){
            List<FieldType> params = new ArrayList<>();
            while (token.type != TokenType.RP){
                type();
                params.add((FieldType) descriptor);
            }
            expect(TokenType.RP);
            returnType();
            descriptor = new MethodType(params, (FieldType) descriptor);
        }
        public void returnType(){
            if(accept(TokenType.V)){
                descriptor = FieldType.VOID;
            }else{
                type();
            }

        }
        public void type(){
            if(token.type == TokenType.L){
                fieldTag = FieldTag.CLASS;
                className();
            }else if(token.type == TokenType.A){
                array();
            }else if(token.type == TokenType.B) {
                fieldTag = FieldTag.BYTE;
                nextToken();
            }else if(token.type == TokenType.C) {
                fieldTag = FieldTag.CHAR;
                nextToken();
            }else if(token.type == TokenType.D) {
                fieldTag = FieldTag.DOUBLE;
                nextToken();
            }else if(token.type == TokenType.F) {
                fieldTag = FieldTag.FLOAT;
                nextToken();
            }else if(token.type == TokenType.I) {
                fieldTag = FieldTag.INT;
                nextToken();
            }else if(token.type == TokenType.J) {
                fieldTag = FieldTag.LONG;
                nextToken();
            }else if(token.type == TokenType.S) {
                fieldTag = FieldTag.SHORT;
                nextToken();
            }else if(token.type == TokenType.Z) {
                fieldTag = FieldTag.BOOL;
                nextToken();
            }else {
                error();
            }
            if(fieldTag != null && fieldTag != FieldTag.ARRAY){
                if(hasClassName()){
                    descriptor = FieldType.CLASS(consumeClassName());
                }else {
                    descriptor = FieldType.of(fieldTag);
                }

            }

        }

        void error(){
            throw new RuntimeException("Syntax error, unexpected token: " + token);
        }


        private boolean expect(TokenType tokenType){
            if (accept(tokenType)) {
                return true;
            }
            throw new RuntimeException("expected: " + tokenType + ", but got: " + this.token);
        }

        public void nextToken(){
            if(index < tokens.size()){
                this.token = tokens.get(index++);
            }else {
                this.token = null;
            }

        }

        private boolean accept(TokenType tokenType){
            if (this.token.getType() == tokenType) {
                nextToken();
                return true;
            }
            return false;
        }
    }
}
