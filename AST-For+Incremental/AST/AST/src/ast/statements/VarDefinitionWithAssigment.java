package ast.statements;

import ast.AbstractLocatable;
import ast.definitions.VarDefinition;
import ast.expressions.Expression;
import visitor.Visitor;

// Clase concreta VarDefinitionWithAssigment: int i=0
public class VarDefinitionWithAssigment extends AbstractLocatable implements Statement {

    VarDefinition var;
    Expression exp;

    public VarDefinitionWithAssigment(VarDefinition var, Expression exp, int line, int column) {
        super(line, column);
        this.var = var;
        this.exp = exp;
    }

    public VarDefinition getVar() {
        return var;
    }

    public Expression getExp() {
        return exp;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
