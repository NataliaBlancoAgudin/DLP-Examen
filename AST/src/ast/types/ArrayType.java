package ast.types;

import ast.Locatable;
import visitor.Visitor;

public class ArrayType extends AbstractType {
    int size;
    Type of;

    public ArrayType(int size, Type elementType){
        this.size = size;
        this.of = elementType;
    }

    public int getSize() {
        return size;
    }

    public Type getOf() {
        return of;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }

    // METODOS DEL TYPE -------------------------------------------------------------------------------
    @Override
    public int numberOfBytes() {
        return size*of.numberOfBytes();
    }

    @Override
    public Type squareBrackets(Type other, Locatable l){
        if(other == IntType.getInstance()){
            return of;
        }
        return super.squareBrackets(other, l);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ArrayType[");
        if(size > 0){
            str.append("of:" + of.toString() + ",size:"+size+"]");
            return str.toString();
        } else{
            return "ArrayType";
        }
    }
}
