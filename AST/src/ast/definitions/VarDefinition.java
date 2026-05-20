package ast.definitions;

import ast.statements.Statement;
import ast.types.Type;
import visitor.Visitor;

public class VarDefinition extends AbstractDefinition implements Statement {

    private int offset;

    public VarDefinition(String name, Type type, int line, int column) {
        super(name, type, line, column);
    }

    public VarDefinition(int line, int column, String name, Type type) {
        super(name, type, line, column);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }


}
