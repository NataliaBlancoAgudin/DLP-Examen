package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

public class Post extends AbstractLocatable implements Statement {

    Expression expression;
    String operator;            // sera el que guarde '++' o '--'

    public Post(Expression expression, String operator, int line, int column) {
        super(line, column);
        this.expression = expression;
        this.operator = operator;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
