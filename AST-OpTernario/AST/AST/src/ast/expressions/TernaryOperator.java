package ast.expressions;

import visitor.Visitor;

// EJERCICIO: Clase concreta del operador ternario a>0? 2 : 3
public class TernaryOperator extends AbstractExpression {
    // Tiene 3 expresiones
    Expression left;
    Expression rightTrue;
    Expression rightFalse;

    public TernaryOperator(Expression left, Expression rightTrue, Expression rightFalse, int line, int column) {
        super(line, column);
        this.left = left;
        this.rightTrue = rightTrue;
        this.rightFalse = rightFalse;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRightTrue() {
        return rightTrue;
    }

    public Expression getRightFalse() {
        return rightFalse;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return null;
    }
}
