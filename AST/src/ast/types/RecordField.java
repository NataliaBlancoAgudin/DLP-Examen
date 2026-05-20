package ast.types;

import ast.AbstractLocatable;
import visitor.Visitor;

public class RecordField extends AbstractLocatable {
    Type targetype;
    String name;
    int offset;

    public RecordField(Type targetype, String name, int line, int column) {
        super(line, column);
        this.targetype = targetype;
        this.name = name;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public Type getTargetype() {
        return targetype;
    }

    public String getName() {
        return name;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }
}
