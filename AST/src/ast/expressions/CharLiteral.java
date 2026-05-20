package ast.expressions;

import visitor.Visitor;

public class CharLiteral extends AbstractExpression {
    char value;

    public CharLiteral(char value, int line, int column) {
        super(line, column);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
