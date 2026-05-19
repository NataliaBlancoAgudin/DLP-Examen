package codegen;

import ast.types.CharType;
import ast.types.IntType;
import ast.types.NumberType;
import ast.types.Type;

import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {

    private PrintWriter out;
    private int contador = 0;

    public CodeGenerator(String outputFilename, String sourceFilename){
        try{
            this.out = new PrintWriter(outputFilename);
        }
        catch (IOException e){
            System.err.println("Error opening the file " + outputFilename + ".");
            System.exit(-1);
        }
        this.source(sourceFilename);        // source "input.txt"
    }

    // Push instructions ---------------------------------------------------------------------------------------------

    public void pushb(char charConstant){
        out.println("\tpushb\t" + (int) charConstant);
        out.flush();
    }

    public void pushi(int intConstant){
        out.println("\tpushi\t" + intConstant);
        out.flush();
    }

    public void pushf(double realConstant){
        out.println("\tpushf\t" + realConstant);
        out.flush();
    }

    public void pusha(int intConstant) {
        out.println("\tpusha\t" + intConstant);
        out.flush();
    }

    public void pushBP() {
        out.println("\tpush\tbp");
        out.flush();
    }

    // Load and store ------------------------------------------------------------------------------------------------
    public void load(Type type) {
        out.println("\tload" + type.suffix());
        out.flush();
    }
    public void store(Type type) {
        out.println("\tstore" + type.suffix());
        out.flush();
    }

    // Popping and duplicating values on the stack -------------------------------------------------------------------
    public void pop(Type type) {
        out.println("\tpop" + type.suffix());
        out.flush();
    }

    public void dup(Type type) {
        out.println("\tdup" + type.suffix());
        out.flush();
    }

    // Arithmetic operations. They pop two operands, perform the operation and push the result. ----------------------
    public void arithmetic(String operator, Type type) {
        switch(operator){
            case "+": add(type); break;
            case "*": mul(type); break;
            case "-": sub(type); break;
            case "/": div(type); break;
            case "%": mod(type); break;
        }
        out.flush();
    }

    public void add(Type type) {
        out.println("\tadd" + type.suffix());
        out.flush();
    }

    public void mul(Type type) {
        out.println("\tmul" + type.suffix());
        out.flush();
    }

    public void sub(Type type) {
        out.println("\tsub" + type.suffix());
        out.flush();
    }

    public void div(Type type) {
        out.println("\tdiv" + type.suffix());
        out.flush();
    }

    public void mod(Type type) {
        out.println("\tmod" + type.suffix());
        out.flush();
    }

    // Comparison operations. They pop two operands, perform the operation and push the result. ----------------------
    public void comparation(String operator, Type type) {
        switch(operator){
            case ">": gt(type); break;
            case "<": lt(type); break;
            case ">=": ge(type); break;
            case "<=": le(type); break;
            case "==": eq(type); break;
            case "!=": ne(type); break;
        }
        out.flush();
    }

    public void gt(Type type) {
        out.println("\tgt" + type.suffix());
        out.flush();
    }

    public void lt(Type type) {
        out.println("\tlt" + type.suffix());
        out.flush();
    }

    public void ge(Type type) {
        out.println("\tge" + type.suffix());
        out.flush();
    }

    public void le(Type type) {
        out.println("\tle" + type.suffix());
        out.flush();
    }

    public void eq(Type type) {
        out.println("\teq" + type.suffix());
        out.flush();
    }

    public void ne(Type type) {
        out.println("\tne" + type.suffix());
        out.flush();
    }

    // Logical operations. Pop one or two operands, perform the operation and push the result. ----------------------
    public void logic(String operator, Type type) {
        switch(operator){
            case "&&": and(); break;
            case "||": or(); break;
        }
        out.flush();
    }

    public void and(){
        out.println("\tand");
        out.flush();
    }

    public void or(){
        out.println("\tor");
        out.flush();
    }

    public void not(){
        out.println("\tnot");
        out.flush();
    }

    // Input / Output ------------------------------------------------------------------------------------------------
    public void output(Type type){
        out.println("\tout" + type.suffix());
        out.flush();
    }

    public void in(Type type) {
        out.println("\tin" + type.suffix());
        out.flush();
    }

    // Conversions ---------------------------------------------------------------------------------------------------
    public void convertTo(Type from, Type to) {
        // Es el mismo tipo
        if(from.suffix() == to.suffix()){
            return;
        }
        // Es un entero y...
        if(from.equals(IntType.getInstance())){
            // Lo quiero convertir a char (conversion directa)
            if(to.equals(CharType.getInstance())){
                out.println("\ti2b");
            }
            // Lo quiero convertir a number (conversion directa)
            else if(to.equals(NumberType.getInstance())){
                out.println("\ti2f");
            }
        }
        // Es un caracter y...
        else if(from.equals(CharType.getInstance())){
            // Lo quiero convertir a entero (conversion directa)
            if(to.equals(IntType.getInstance())){
                out.println("\tb2i");
            }
            // Lo quiero convertir a number (conversion indirecta: Char -> Int; Int -> Number)
            else if(to.equals(NumberType.getInstance())){
                out.println("\tb2i");
                out.println("\ti2f");
            }
        }
        // Es un Number y...
        else if(from.equals(NumberType.getInstance())){
            // Lo quiero convertir a entero (conversion directa)
            if(to.equals(IntType.getInstance())){
                out.println("\tf2i");
            }
            // Lo quiero convertir a char (conversion indirecta: Number -> Int; Int -> Char)
            else if(to.equals(CharType.getInstance())){
                out.println("\tf2i");
                out.println("\ti2b");
            }
        }
        out.flush();
    }

    // Jumps ---------------------------------------------------------------------------------------------------
    public String getLabel() {
        return "label" + this.contador++;
    }

    public void insertLabel(String label) {
        out.println("\n" + label +":\n");
        out.flush();
    }

    public void jmp(String cond) {
        out.println("\tjmp\t" + cond);
        out.flush();
    }

    public void jz(String label) {
        out.println("\tjz\t" + label);
        out.flush();
    }

    public void jnz(String label) {
        out.println("\tjnz\t" + label);
        out.flush();
    }

    // Functions  ---------------------------------------------------------------------------------------------------
    public void call(String name) {
        out.println("\tcall\t" + name);
        out.flush();
    }

    public void enter(int bytesLocalSum) {
        out.println("\tenter\t" + bytesLocalSum);
        out.flush();
    }

    public void ret(int returnBytes, int bytesLocalSum, int paramBytes) {
        out.println("\tret\t" + returnBytes + ", " + bytesLocalSum + ", " + paramBytes);
        out.flush();
    }

    public void halt() {
        out.println("halt");
        out.flush();
    }

    // Debugging info -----------------------------------------------------------------------------------------------
    public void source(String source) {
        out.println("\n#source\t" + "\"" + source + "\"\n");
        out.flush();
    }

    public void commentLine(int line) {
        out.println("\n#line\t" + line);
        out.flush();
    }

    // Comments -----------------------------------------------------------------------------------------------------
    public void printFunction(String name) {
        out.println("\n " + name + ":");
        out.flush();
    }

    public void comment(String name) {
        out.println("\t ' * " + name);
        out.flush();
    }

    public void mainInvocation() {
        out.println("\n' Invocation to the main function");
        out.println("call main");
        out.flush();
    }
}
