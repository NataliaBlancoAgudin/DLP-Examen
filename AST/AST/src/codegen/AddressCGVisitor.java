package codegen;

import ast.definitions.VarDefinition;
import ast.expressions.ArrayAccess;
import ast.expressions.FieldAccess;
import ast.expressions.Variable;
import ast.types.IntType;
import ast.types.RecordType;

// Address tiene plantillas solo para los lvalue (Variable, ArrayAccess y FieldAccess)
public class AddressCGVisitor extends AbstractCGVisitor<Void, Void> {

    private ValueCGVisitor valueCGVisitor;

    public AddressCGVisitor(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    public void setValueCGVisitor(ValueCGVisitor valueCGVisitor) {
        this.valueCGVisitor = valueCGVisitor;
    }

    /**
     * address[[Variable: expr -> ID]]()=
     *      if(expr.definition.scope == 0){
     *          <pusha> expr.definition.offset
     *      }
     *      else{
     *          <push BP>
     *          <pushi> expr.definition.offset
     *          <addi>
     *      }
     */
    @Override
    public Void visit(Variable v, Void param) {
        if(v.getDefinition().getScope() == 0){
            getCodeGenerator().pusha(((VarDefinition) v.getDefinition()).getOffset());
        }
        else{
            getCodeGenerator().pushBP();
            getCodeGenerator().pushi(((VarDefinition) v.getDefinition()).getOffset());
            getCodeGenerator().add(IntType.getInstance());
        }
        return null;
    }

    /**
     * address[[ArrayAccess: expr1 -> exp2 expr3]]()=
     *      address[[expr2]]()
     *      value[[expr3]]()
     *      cg.converTo(expr3.type, IntType.instance) -> esto es para que funcione v['a']
     *      <pushi> expr1.type.numberOfBytes
     *      <muli>
     *      <addi>
     */
    @Override
    public Void visit(ArrayAccess a, Void param) {
        a.getLeft().accept(this, null);
        a.getRight().accept(valueCGVisitor, null);
        getCodeGenerator().convertTo(a.getRight().getType(), IntType.getInstance());
        getCodeGenerator().pushi(a.getType().numberOfBytes());
        getCodeGenerator().mul(IntType.getInstance());
        getCodeGenerator().add(IntType.getInstance());
        return null;
    }

    /**
     * address[[FieldAccess: expr1 -> expr2 ID]]()=
     *      address[[expr2]]()
     *      <pushi> expr2.type.fields(ID).offset
     *      <addi>
     */
    @Override
    public Void visit(FieldAccess f, Void param) {
        f.getExpression().accept(this, null);
        getCodeGenerator().pushi(((RecordType) f.getExpression().getType()).getField(f.getName()).getOffset());
        getCodeGenerator().add(IntType.getInstance());
        return null;
    }
}
