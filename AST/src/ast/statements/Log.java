package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

public class Log extends AbstractLocatable implements Statement {

    Expression expression;

    public Log(Expression expression, int line, int column) {
        super(line, column);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }
}
