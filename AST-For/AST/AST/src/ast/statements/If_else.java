package ast.statements;

import ast.AbstractLocatable;
import ast.expressions.Expression;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class If_else extends AbstractLocatable implements Statement{

    List<Statement> if_body;
    List<Statement> else_body;
    Expression condition;

    public If_else(List<Statement> if_body, List<Statement> else_body,Expression condition, int line, int column) {
        super(line, column);
        this.if_body = new ArrayList<>(if_body);

        // Esto es por si no me llega un else (es opcional)
        if(else_body != null)
            this.else_body = new ArrayList<>(else_body);
        else
            this.else_body = new ArrayList<>();

        this.condition = condition;
    }

    public List<Statement> getIf_body() {
        return if_body;
    }

    public List<Statement> getElse_body() {
        return else_body;
    }

    public Expression getCondition() {
        return condition;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
