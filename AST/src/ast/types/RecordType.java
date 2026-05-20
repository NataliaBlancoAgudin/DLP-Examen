package ast.types;

import ast.Locatable;
import visitor.Visitor;

import java.util.List;

public class RecordType extends AbstractType {
    List<RecordField> fieldList;

    public RecordType(List<RecordField> fieldList) {
        this.fieldList = fieldList;
    }

    public List<RecordField> getFieldList() {
        return fieldList;
    }

    public RecordField getField(String id) {
        for(RecordField field : fieldList) {
            if(field.getName().equals(id)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param){
        return v.visit(this, param);
    }

    // METODOS DEL TYPE -------------------------------------------------------------------------------

    @Override
    public int numberOfBytes() {
        int number = 0;
        for(RecordField field : fieldList){
            number+=field.getTargetype().numberOfBytes();
        }
        return number;
    }

    @Override
    public Type dot(String name, Locatable l){
        for(RecordField field : fieldList){
            if(field.getName().equals(name)){
                return field.getTargetype();
            }
        }
        return new ErrorType("El Record no tiene ningún campo con el nombre: " + name, l);
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        if(!fieldList.isEmpty()){
            st.append("RecordType" + "[fields:");
            for (RecordField recordField : fieldList) {
                st.append("[Field[name:" + recordField.getName() + ",type:" + recordField.getTargetype() + ",offset:" + recordField.getOffset() + "]]");
            }
            st.append("]");
            return st.toString();
        } else{
            return "RecordType";
        }

    }
}
