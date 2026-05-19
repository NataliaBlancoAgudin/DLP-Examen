package ast.types;

import ast.Locatable;
import visitor.Visitor;

public class BooleanType extends AbstractType {

    private static final BooleanType BOOLEAN_TYPE = new BooleanType();

    private BooleanType(){}

    public static BooleanType getInstance(){
        return BOOLEAN_TYPE;
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
    public void mustBeLogical(Locatable l){
        // No se hace nada, porque IntType si que se puede usar como condicion
        // En AbstractType se lanza un ErrorType, pero en este caso no se haría
    }

    @Override
    public Type logic(Type other, Locatable loc) {
        if(other == this) {
            return this;
        }
        return super.logic(other, loc);
    }

    @Override
    public Type logic(Locatable loc) {
        return this;
    }

    @Override
    public void mustBeBuiltIn(Locatable locatable) {
        // No se haria nada
    }

    @Override
    public String toString(){
        return "BooleanType";
    }
}
