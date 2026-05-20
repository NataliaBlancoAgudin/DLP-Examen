package ast.expressions;

import visitor.Visitor;

public class RealLiteral extends AbstractExpression {

    double value;

    public RealLiteral(double value, int line, int column) {
        super(line, column);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
