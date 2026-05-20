package ast.types;

import ast.Locatable;
import visitor.Visitor;

public class IntType extends AbstractType {

    private static final IntType INT_TYPE = new IntType();

    private IntType(){}

    public static IntType getInstance(){
        return INT_TYPE;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }

    // METODOS DEL TYPE -------------------------------------------------------------------------------
    @Override
    public char suffix(){
        return 'i';
    }

    @Override
    public int numberOfBytes() {
        return 2;
    }

    @Override
    public Type arithmetic(Type other, Locatable loc) {
        // int + int || int + char => int
        if(other == this || other == CharType.getInstance()) {
            return this;
        }
        // int + number => number
        else if(other == NumberType.getInstance()) {
            return NumberType.getInstance();
        }
        return super.arithmetic(other, loc);
    }

    @Override
    public Type arithmetic(Locatable loc) {
        return this;
    }

    @Override
    public void mustBeLogical(Locatable l){
        // No se hace nada, porque IntType si que se puede usar como condicion
        // En AbstractType se lanza un ErrorType, pero en este caso no se haría
    }

    @Override
    public Type logic(Type other, Locatable loc) {
        if(other == this || other == CharType.getInstance()) {
            return this;
        }
        return super.logic(other, loc);
    }

    @Override
    public Type logic(Locatable loc) {
        return this;
    }

    @Override
    public Type comparison(Type other, Locatable loc) {
        if(other == this || other == CharType.getInstance() || other == NumberType.getInstance()) {
            return this;
        }
        return super.comparison(other, loc);
    }

    // Solo promociona implicitamente a su mismo tipo, a Number(93 -> 93.0) y Char (93->'a')
    @Override
    public void mustPromotesTo(Type t, Locatable l) {
        if(this == t || t == NumberType.getInstance() || t == CharType.getInstance()){
            return;
        }
        super.mustPromotesTo(t, l);
    }

    @Override
    public Type canBeCastTo(Type other, Locatable c){
        if(other == this || other == NumberType.getInstance() || other == CharType.getInstance()) {
            return other;
        }
        return super.canBeCastTo(other, c);
    }

    @Override
    public void mustBeBuiltIn(Locatable locatable) {
        // No se haria nada
    }

    @Override
    public String toString(){
        return "IntType";
    }
}
