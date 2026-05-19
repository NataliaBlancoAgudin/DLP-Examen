package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

// Clase concreta FOR: for(i=0; i<10; i=i+1){ statements*}
public class For extends AbstractLocatable implements Statement {

    Statement initial;
    Expression condicion;
    Statement increment;
    List<Statement> body;

    public For(Statement initial, Expression condicion, Statement increment, List<Statement> body, int line, int column) {
        super(line, column);
        this.initial = initial;
        this.condicion = condicion;
        this.increment = increment;

        if(body != null)
            this.body = body;
        else
            this.body = new ArrayList<Statement>();
    }

    public Statement getInitial() {
        return initial;
    }

    public Expression getCondicion() {
        return condicion;
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
