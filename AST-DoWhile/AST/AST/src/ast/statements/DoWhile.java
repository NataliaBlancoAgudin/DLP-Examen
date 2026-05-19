package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class DoWhile extends AbstractLocatable implements Statement {

    Expression condition;
    List<Statement> body;

    public DoWhile(Expression condition, List<Statement> body, int line, int column) {
        super(line, column);
        this.condition = condition;
        if(body != null)
            this.body = body;
        else
            this.body = new ArrayList<Statement>();
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
