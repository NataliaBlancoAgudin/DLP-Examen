package codegen;

import ast.expressions.*;
import ast.types.FuncType;
import ast.types.IntType;

public class ValueCGVisitor extends AbstractCGVisitor<Void,Void> {

    private AddressCGVisitor addressCGVisitor;

    public ValueCGVisitor(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    public void setAddressCGVisitor(AddressCGVisitor addressCGVisitor) {
        this.addressCGVisitor = addressCGVisitor;
    }

    /**
     * value[[Variable: expr -> ID]]()=
     *      address[[expr]]
     *      <load> expr.type.suffix
     */
    @Override
    public Void visit(Variable v, Void param){
        v.accept(addressCGVisitor, null);
        getCodeGenerator().load(v.getType());
        return null;
    }

    /**
     * value[[ArrayAccess: expr1 -> expr2 expr3]]()=
     *      address[[expr1]]
     *      <load> expr1.type.suffix
     */
    @Override
    public Void visit(ArrayAccess a, Void param){
        a.accept(addressCGVisitor, null);
        getCodeGenerator().load(a.getType());
        return null;
    }

    /**
     * value[[FieldAccess: expr1 -> expr2 ID]]()=
     *      address[[expr1]]
     *      <load> expr1.type.suffix
     */
    @Override
    public Void visit(FieldAccess f, Void param){
        f.accept(addressCGVisitor, null);
        getCodeGenerator().load(f.getType());
        return null;
    }

    /**
     * value[[ArithmeticOperation: expr1 -> expr2 expr3]]()
     *      value[[expr2]]()
     *      cg.convertTo(expr2.type, expr1.type)
     *      value[[expr3]]()
     *      cg.convertTo(expr3.type, expr2.type)
     *      cg.arithmetic(expr1.operator, expr1.type)
     */
    @Override
    public Void visit(ArithmeticOperation a, Void param){
        a.getLeft().accept(this, param);
        getCodeGenerator().convertTo(a.getLeft().getType(), a.getType());
        a.getRight().accept(this, param);
        getCodeGenerator().convertTo(a.getRight().getType(), a.getType());
        getCodeGenerator().arithmetic(a.getOperator(), a.getType());
        return null;
    }

    /**
     * value[[Cast: expr1 -> type expr2]]() =
     *      value[[expr2]]()
     *      cg.converTo(expr2.type, type)
     */
    @Override
    public Void visit(Cast c, Void param){
        c.getExpression().accept(this, param);
        getCodeGenerator().convertTo(c.getExpression().getType(), c.getType());
        return null;
    }

    /**
     * value[[CharLiteral: expr -> CHAR_CONSTANT]]() =
     *         <pushb> expr.value
     */
    @Override
    public Void visit(CharLiteral c, Void param){
        getCodeGenerator().pushb(c.getValue());
        return null;
    }

    /**
     *value[[ComparationOperation: expr1 -> expr2 expr3]]()=
     *         value[[expr2]]()
     *         cg.convertTo(expr2.type, expr1.type)
     *         value[[expr3]]()
     *         cg.convertTo(expr3.type, expr1.type)
     *         cg.comparation(expr1.operator, expr1.type)
     */
    @Override
    public Void visit(ComparationOperation c, Void param){
        c.getLeft().accept(this, null);
        getCodeGenerator().convertTo(c.getLeft().getType(), c.getType());
        c.getRight().accept(this, null);
        getCodeGenerator().convertTo(c.getRight().getType(), c.getType());
        getCodeGenerator().comparation(c.getOperator(), c.getType());
        return null;
    }

    /**
     * value[[IntLiteral: expr -> INT_CONSTANT]]()=
     *         <pushi> expr.value
     */
    @Override
    public Void visit(IntLiteral i, Void param){
        getCodeGenerator().pushi(i.getValue());
        return null;
    }

    /**
     * value[[Invocation: expr1 -> expr2 expr3*]]()=
     *      for(Expression exp: expr3*)
     *          value[[expr]]
     *      <call> expr2.name
     */
//    @Override
//    public Void visit(Invocation i, Void param){
//        for(Expression expr: i.getArgs()){
//            expr.accept(this, null);
//        }
//        getCodeGenerator().call(i.getVariable().getName());
//        return null;
//    }

    /**
     * value[[LogicOperation: expr1 -> expr2 expr3]]() =
     *         value[[expr2]]()
     *         cg.convertTo(expr2.type, expr1.type)
     *         value[[expr3]]()
     *         cg.convertTo(expr3.type, expr1.type)
     *         cg.logic(expr1.operator, expr1.type)
     */
    @Override
    public Void visit(LogicOperation l, Void param){
        l.getLeft().accept(this, null);
        getCodeGenerator().convertTo(l.getLeft().getType(), l.getType());
        l.getRight().accept(this, null);
        getCodeGenerator().convertTo(l.getRight().getType(), l.getType());
        getCodeGenerator().logic(l.getOperator(), l.getType());
        return null;
    }

    /**
     * value[[RealLiteral: expr -> REAL_CONSTANT]]()=
     *         <pusf> expr.value
     */
    @Override
    public Void visit(RealLiteral r, Void param){
        getCodeGenerator().pushf(r.getValue());
        return null;
    }

    /**
     * value[[UnaryMinus: expr1 -> expr2]]()=
     *      value[[expr2]]()
     *      cg.convertTo(expr2.type, expr1.type)
     *      <pushi> -1
     *      cg.convertTo(IntType, expr1.type)
     *      <muli>
     */
    @Override
    public Void visit(UnaryMinus u, Void param){
        u.getOperand().accept(this, null);
        getCodeGenerator().convertTo(u.getOperand().getType(), u.getType());
        getCodeGenerator().pushi(-1);
        getCodeGenerator().convertTo(IntType.getInstance(), u.getType());
        getCodeGenerator().mul(u.getType());
        return null;
    }

    /**
     * value[[UnaryNot: expr1 -> expr2]]()=
     *         value[[expr2]]()
     *         cg.convertTo(expr2.type, IntType)
     *         <not>
     */
    @Override
    public Void visit(UnaryNot u, Void param){
        u.getOperand().accept(this, null);
        getCodeGenerator().convertTo(u.getOperand().getType(), u.getType());
        getCodeGenerator().not();
        return null;
    }

    /**
     * value[[Invocation: expr1 -> expr2 expr3*]]()=
     *         for(int i=0; i<expr3*.size(); i++){
     *             value[[expr3*.get(i)]]
     *             cg.convertTo(expr3*.get(i).type, expr2.type.parameters.get(i).type
     *         }
     *         <call> expr2.name
     */
    @Override
    public Void visit(Invocation invocation, Void param){
        for(int i=0; i<invocation.getArgs().size(); i++){
            invocation.getArgs().get(i).accept(this, null);
            getCodeGenerator().convertTo(invocation.getArgs().get(i).getType(),
                    ((FuncType)invocation.getVariable().getType()).getParams().get(i).getType());
        }
        getCodeGenerator().call(invocation.getVariable().getName());
        return null;
    }

}
