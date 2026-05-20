package ast.expressions;

import visitor.Visitor;

public class FieldAccess extends AbstractExpression{

    Expression expression;
    String name;

    public FieldAccess(Expression expression, String name, int line, int column) {
        super(line, column);
        this.expression = expression;
        this.name = name;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getName() {
        return name;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
