package ast.types;

import ast.Locatable;
import ast.expressions.RealLiteral;
import errorhandler.ErrorHandler;
import visitor.Visitor;

import java.util.List;

public class ErrorType extends AbstractType {
    private final String cause;
    private final Locatable locatable;

    public ErrorType(String cause, Locatable locatable){
        this.cause = cause;
        this.locatable = locatable;
        ErrorHandler.getInstance().addError(this);
    }

    public String getCause(){
        return this.cause;
    }

    public Locatable getLocatable(){
        return this.locatable;
    }

    @Override
    public String toString() {
        return "ErrorType {" +
                "casuse='" + this.getCause() + '\'' +
                ", fila=" + locatable.getLine()+
                ", columna= " + locatable.getColumn()+
                '}';
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }

    // METODOS DEL TYPE -------------------------------------------------------------------------------

    @Override
    public void mustBeLogical(Locatable locatable) {
        // No se haria nada
    }
    @Override
    public void mustPromotesTo(Type t,Locatable locatable) {
        // No se haria nada
    }

    @Override
    public void mustBeBuiltIn(Locatable locatable) {
        // No se haria nada
    }

    @Override
    public Type arithmetic(Type other, Locatable locatable) {
        return this;
    }
    @Override
    public Type arithmetic(Locatable loc) {
        return this;
    }

    @Override
    public Type logic(Type other, Locatable locatable) {
        return this;
    }

    @Override
    public Type logic(Locatable loc) {
        return this;
    }

    @Override
    public Type comparison(Type other, Locatable loc) {
        return this;
    }

    @Override
    public Type canBeCastTo(Type other, Locatable c){
        return this;
    }

    @Override
    public Type dot(String name, Locatable l){
        return this;
    }

    @Override
    public Type parenthesis(List<Type> e, Locatable l) {
        return this;
    }

    @Override
    public Type squareBrackets(Type other, Locatable l) {
        return this;
    }
}
