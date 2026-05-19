package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

// Clase concreta FOR: for(int i=0; i<10; i=i+1) stmt*
public class For extends AbstractLocatable implements Statement {

    VarDefinitionWithAssigment initial;
    Expression condition;
    Statement increment;
    List<Statement> body;

    public For(VarDefinitionWithAssigment initial, Expression condition, Statement increment, List<Statement> body, int line, int column) {
        super(line, column);
        this.initial = initial;
        this.condition = condition;
        this.increment = increment;
        if(body != null)
            this.body = body;
        else
            this.body = new ArrayList<>();
    }

    public VarDefinitionWithAssigment getInitial() {
        return initial;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getIncrement() {
        return increment;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
