package ast.expressions;

public abstract class BinaryOperation extends AbstractExpression {

    Expression left, right;
    String operator;

    public BinaryOperation(Expression left, Expression right, String operator, int line, int column) {
        super(line, column);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public String getOperator() { return operator; }
}
