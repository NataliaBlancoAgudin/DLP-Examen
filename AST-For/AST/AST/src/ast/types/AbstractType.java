package ast.types;

import ast.Locatable;
import java.util.List;

public abstract class AbstractType implements Type {

    @Override
    public char suffix() {
        throw new UnsupportedOperationException("El tipo '" + this + "' no puede tener sufijo.");
    }

    // Las VarDefinition no pueden ser de tipo VoidType, ErrorType o FuncType
    @Override
    public int numberOfBytes() {
        throw new UnsupportedOperationException("La definción de una variable no puede ser de tipo '" + this + "'.");
    }

    // Expresiones logicas son Int y Char
    @Override
    public void mustBeLogical(Locatable l){
        new ErrorType("No se puede usar el tipo '" + this + "' como expresión lógica.", l);
    }

    // Las operaciones aritmeticas binarias son soportadas para Int, Char y Real
    @Override
    public Type arithmetic(Type other, Locatable l){
        if(other instanceof ErrorType){
            return other;
        }
        return new ErrorType("No se puede realizar una Operación Aritmética entre el tipo '" + this + "' y '" + other.toString() + "'.", l);
    }

    // Las operaciones aritméticas unarias (-) son soportadas para Int, Char y Real
    @Override
    public Type arithmetic(Locatable l){
        return new ErrorType("No se puede utilizar un Operador Aritmético Unario para el tipo '" + this + "'.", l);
    }

    @Override
    public Type logic(Type other, Locatable l){
        if(other instanceof ErrorType){
            return other;
        }
        return new ErrorType("No se puede realizar una Operación Lógica entre el tipo '" + this + "' y '" + other.toString() + "'.", l);
    }

    @Override
    public Type logic(Locatable l){
        return new ErrorType("No se puede utilizar un Operador Lógico Unario para el tipo '" + this + "'.", l);
    }

    @Override
    public Type comparison(Type t, Locatable l){
        if(t instanceof ErrorType){
            return t;
        }
        return new ErrorType("No se puede realizar una Operación de Comparación entre el tipo '" + this + "' y '" + t.toString() +"'.", l);
    }

    @Override
    public Type parenthesis(List<Type> e, Locatable l){
        for(Type t: e){
            if(t instanceof ErrorType){
                return t;
            }
        }
        return new ErrorType("No se puede invocar una expresión de tipo '" + this + "'.", l);
    }

    @Override
    public void mustPromotesTo(Type type, Locatable l){
        if(type instanceof ErrorType){
            return;
        }
        new ErrorType("El tipo '" + this + "' no es promocionable al tipo '" + type.toString() + "'.", l);
    }

    @Override
    public Type squareBrackets(Type other, Locatable l){
        if(other instanceof ErrorType){
            return other;
        }
        return new ErrorType("No se puede indexar el tipo '" + this + "'.", l);
    }

    @Override
    public Type canBeCastTo(Type other, Locatable c){
        if(other instanceof ErrorType){
            return other;
        }
        return new ErrorType("No se puede castear el tipo '" + this + "' al tipo '" + other.toString() + "'.", c);
    }

    @Override
    public Type dot(String name, Locatable l) {
        return new ErrorType("El tipo '" + this + "' no contiene un campo con el nombre '" + name + "'.", l);
    }

    @Override
    public void mustBeBuiltIn(Locatable l) {
        new ErrorType("Se esperaba un tipo primitivo (Int, Char, Real) o Void, pero es '" + this + "'.", l);
    }
}
