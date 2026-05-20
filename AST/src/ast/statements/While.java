package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class While extends AbstractLocatable implements Statement{

    List<Statement> body;
    Expression condition;

    public While(List<Statement> body,Expression condition, int line, int column) {
        super(line, column);
        this.body = new ArrayList<>(body);
        this.condition = condition;
    }

    public List<Statement> getBody() {
        return body;
    }

    public Expression getCondition() {
        return condition;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }
}
