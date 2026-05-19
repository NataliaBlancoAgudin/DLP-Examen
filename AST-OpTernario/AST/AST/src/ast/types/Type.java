package ast.types;

import ast.ASTNode;
import ast.Locatable;

import java.util.List;

// Interfaz `Type`: hereda de ASTNode porque es un nodo más
public interface Type extends ASTNode {
    // Sufijo para saber el tipo que tenemos que hacer en las instrucciones de codigo maquina
    char suffix();

    // Numero de bytes que tienen los tipos
    int numberOfBytes();

    // Para las operaciones binarias (+|-|*|/|%)
    Type arithmetic(Type other, Locatable l);

    // Para el UnaryMinus. Solo pueden los NumberType y los IntType
    Type arithmetic(Locatable l);

    // Comprueba que puede ser usado en condiciones (if/while). Solo pueden los InType y CharType
    void mustBeLogical(Locatable l);

    // Para las operaciones logicas binarias (&& | ||). Solo pueden ser los IntType y CharType
    Type logic(Type other, Locatable l);

    // Para ele UnaryNot. Solo pueden los IntType y CharType
    Type logic(Locatable l);

    // Para las comparaciones >, >=, <, <=, ==, !=. Solo pueden los IntType, CharType y NumberType
    Type comparison(Type other, Locatable c);

    // Para la invocacion a funciones. Solo pueden los FuncType
    Type parenthesis(List<Type> argumentsTypes, Locatable i);

    // Para ver si los tipos son promocionables. La promoción implícita es Int -> Char; Char -> Int, Int -> Number y Char -> Number (este último PREGUNTAR)
    void mustPromotesTo(Type other, Locatable l);

    // Para el acceso a array. Solo pueden los ArrayType
    Type squareBrackets(Type other, Locatable a);

    // Para el cast. Solo pueden los IntType, NumberType y CharType
    Type canBeCastTo(Type other, Locatable c);

    // Para el acceso a campos. Solo pueden los RecordType
    Type dot(String name, Locatable f);

    // Comprueba si es un tipo primitivo (IntType, CharType o NumberType)
    void mustBeBuiltIn(Locatable i);

    // EJERCICIO: Devuelve el tipo mas generico
    // int : char -> int; int : number -> number
    // char : int -> int; char : number -> number
    // number : int -> number; char : number -> number
    Type commonType(Type other, Locatable l);
}
