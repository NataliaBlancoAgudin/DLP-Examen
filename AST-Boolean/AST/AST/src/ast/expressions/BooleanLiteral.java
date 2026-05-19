package ast.expressions;

import visitor.Visitor;

public class BooleanLiteral extends AbstractExpression {

    boolean value;

    public BooleanLiteral(boolean value, int line, int column) {
        super(line, column);
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
