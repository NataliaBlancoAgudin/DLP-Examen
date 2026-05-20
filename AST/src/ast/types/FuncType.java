package ast.types;

import ast.Locatable;
import ast.definitions.VarDefinition;
import visitor.Visitor;

import java.util.List;

public class FuncType extends AbstractType {
    // Atributo de la clase `FuncType`: tiene que guardar
    // un `Type` de retorno y una lista de `VarDefintion`
    Type returnType;
    List<VarDefinition> params;

    public FuncType(Type returnType, List<VarDefinition> params) {
        this.returnType = returnType;
        this.params = params;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<VarDefinition> getParams() {
        return params;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }

    // METODOS DEL TYPE -------------------------------------------------------------------------------
    @Override
    public Type parenthesis(List<Type> e, Locatable l){
        if(e.size() != params.size()){
            return new ErrorType("Número incorrecto de parámetros (se esperaban " + params.size() + " y fueron " + e.size(), l);
        }
        for(int i = 0; i < params.size(); i++){
            e.get(i).mustPromotesTo(params.get(i).getType(), l);
        }
        return returnType;
    }

    @Override
    public String toString() {
        return "FunctionType";
    }
}
