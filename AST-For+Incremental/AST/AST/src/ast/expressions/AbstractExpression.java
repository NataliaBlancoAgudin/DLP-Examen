package ast.expressions;

import ast.AbstractLocatable;
import ast.types.Type;

public abstract class AbstractExpression extends AbstractLocatable implements Expression {

    private boolean lvalue;
    private Type type;

    public AbstractExpression(int line, int column) {
        super(line, column);
    }

    public void setLvalue(boolean lvalue){
        this.lvalue = lvalue;
    }

    public boolean getLvalue(){
        return this.lvalue;
    }

    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }
}
