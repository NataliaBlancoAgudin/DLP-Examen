package ast.types;

import ast.Locatable;
import visitor.Visitor;

public class NumberType extends AbstractType {

    private static final NumberType NUMBER_TYPE = new NumberType();

    private NumberType() {}

    public static NumberType getInstance(){
        return NUMBER_TYPE;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }

    // METODOS DEL TYPE -------------------------------------------------------------------------------
    @Override
    public char suffix(){
        return 'f';
    }

    @Override
    public int numberOfBytes() {
        return 4;
    }

    @Override
    public Type arithmetic(Type other, Locatable l){
        // number + number || number + int || number + char => number
        if(other == this || other == IntType.getInstance() || other == CharType.getInstance()){
            return this;
        }
        else{
            return super.arithmetic(other, l);
        }
    }

    @Override
    public Type arithmetic(Locatable loc) {
        return this;
    }

    @Override
    public Type comparison(Type other, Locatable loc) {
        if(other == this || other == IntType.getInstance() || other == CharType.getInstance()){
            return IntType.getInstance();
        }
        return super.comparison(other, loc);
    }

    // Solo promociona implicitamente a su mismo tipo
    @Override
    public void mustPromotesTo(Type t, Locatable l) {
        if(this == t){
            return;
        }
        super.mustPromotesTo(t, l);
    }

    @Override
    public Type canBeCastTo(Type other, Locatable c){
        if(other == this || other == IntType.getInstance()) {
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
        return "NumberType";
    }

    @Override
    public Type commonType(Type other, Locatable l) {
        if(other == this || other == IntType.getInstance() || other == CharType.getInstance()){
            return this;
        }
        return super.commonType(other, l);
    }
}
