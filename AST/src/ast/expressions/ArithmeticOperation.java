package ast.expressions;

import visitor.Visitor;

public class ArithmeticOperation extends BinaryOperation {

    public ArithmeticOperation(Expression left, Expression right, String operator, int line, int column) {
        super(left, right, operator, line, column);
    }


    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
