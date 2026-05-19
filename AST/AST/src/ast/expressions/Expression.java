package ast.expressions;

import ast.Locatable;
import ast.types.Type;

public interface Expression extends Locatable {

    void setLvalue(boolean bool);

    boolean getLvalue();

    void setType(Type type);

    Type getType();
}
