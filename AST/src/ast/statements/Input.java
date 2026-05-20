package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

public class Input extends AbstractLocatable implements Statement {

    Expression target;

    public Input(Expression target, int line, int column) {
        super(line, column);
        this.target = target;
    }

    public Expression getTarget() {
        return target;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }
}
