package ast.types;

import visitor.Visitor;

public class VoidType extends AbstractType {

    private static final VoidType VOID_TYPE = new VoidType();

    private VoidType() {}

    public static VoidType getInstance(){
        return VOID_TYPE;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }

    @Override
    public String toString() {
        return "VoidType";
    }
}
