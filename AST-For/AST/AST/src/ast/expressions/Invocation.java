package ast.expressions;

import ast.statements.Statement;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Invocation extends AbstractExpression implements Statement, Expression {

    List<Expression> args;
    Variable variable;

    public Invocation(List<Expression> args, Variable variable, int line, int column) {
        super(line, column);
        this.args = new ArrayList<>(args);
        this.variable = variable;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
