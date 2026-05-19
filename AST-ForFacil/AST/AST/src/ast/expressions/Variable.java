package ast.expressions;

import ast.definitions.Definition;
import visitor.Visitor;

public class Variable extends AbstractExpression{

    String name;
    Definition definition;

    public Variable(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
