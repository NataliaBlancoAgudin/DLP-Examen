package ast.definitions;

import ast.AbstractLocatable;
import ast.types.Type;

public abstract class AbstractDefinition extends AbstractLocatable implements Definition {
    String name;
    Type type;
    int scope;

    public AbstractDefinition(String name, Type type, int line, int column) {
        super(line, column);
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public int getScope() {
        return scope;
    }

    @Override
    public void setScope(int scope) {
        this.scope = scope;
    }
}
