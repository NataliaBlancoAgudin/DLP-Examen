package ast.types;

import ast.Locatable;
import visitor.Visitor;

public class CharType extends AbstractType {

    private static final CharType CHAR_TYPE = new CharType();

    private CharType() {}

    public static CharType getInstance(){
        return CHAR_TYPE;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }

    // METODOS DEL TYPE -------------------------------------------------------------------------------
    @Override
    public char suffix(){
        return 'b';
    }

    @Override
    public int numberOfBytes() {
        return 1;
    }

    @Override
    public Type arithmetic(Type other, Locatable l){
        if(other == this || other == IntType.getInstance()){
            return IntType.getInstance();
        }
        else if(other == NumberType.getInstance()){
            return NumberType.getInstance();
        }
        else {
            return super.arithmetic(other, l);
        }
    }

    @Override
    public Type arithmetic(Locatable l){
        return IntType.getInstance();
    }

    @Override
    public void mustBeLogical(Locatable l){
        // No se hace nada, porque CharType si que se puede usar como condicion
        // En AbstractType se lanza un ErrorType, pero en este caso no se haría
    }

    @Override
    public Type logic(Type other, Locatable l) {
        if(other == this || other == IntType.getInstance()){
            return IntType.getInstance();
        }
        return super.logic(other, l);
    }

    @Override
    public Type logic(Locatable loc) {
        return IntType.getInstance();
    }

    @Override
    public Type comparison(Type other, Locatable loc) {
        if(other == this || other == IntType.getInstance() || other == NumberType.getInstance()){
            return IntType.getInstance();
        }
        return super.comparison(other, loc);
    }

    // Solo promociona implicitamente a su mismo tipo y a Int ('a'-> 93)
    @Override
    public void mustPromotesTo(Type t, Locatable l){
        if(this == t || t == IntType.getInstance() || t == NumberType.getInstance()){
            return;
        }
        super.mustPromotesTo(t, l);
    }

    @Override
    public Type canBeCastTo(Type other, Locatable c){
        if(other == this || other == IntType.getInstance() || other == NumberType.getInstance()){
            return other;
        }
        return super.canBeCastTo(other, c);
    }

    @Override
    public void mustBeBuiltIn(Locatable loc) {
        // Vacío
    }

    @Override
    public String toString(){
        return "CharType";
    }

    // EJERCICIO
    @Override
    public Type commonType(Type other, Locatable l) {
        if(other == this){
            return this;
        }
        else if(other == IntType.getInstance()){
            return IntType.getInstance();
        }
        else if(other == NumberType.getInstance()){
            return NumberType.getInstance();
        }
        return super.commonType(other, l);
    }
}
