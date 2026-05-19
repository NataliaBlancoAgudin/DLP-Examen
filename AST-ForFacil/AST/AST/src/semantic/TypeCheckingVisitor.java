package semantic;

import ast.definitions.FunctionDefinition;
import ast.expressions.*;
import ast.statements.*;
import ast.types.*;
import visitor.AbstractVisitor;

import java.util.ArrayList;
import java.util.List;

public class TypeCheckingVisitor extends AbstractVisitor<Void, Type> {

    /**
     * //IntLiteral: expr -> INT_CONSTANT
     * (1) expr.type = IntType.getInstance();
     */
    @Override
    public Void visit(IntLiteral i, Type param){
        i.setType(IntType.getInstance());
        return null;
    }

    /**
     * //CharLiteral: expr -> CHAR_CONSTANT
     * (2) expr.type = CharType.getInstance();
     */
    @Override
    public Void visit(CharLiteral c, Type param){
        c.setType(CharType.getInstance());
        return null;
    }

    /**
     * //RealLiteral: expr -> REAL_CONSTANT
     * (3) expr.type = NumberType.getInstance();
     */
    @Override
    public Void visit(RealLiteral r, Type param){
        r.setType(NumberType.getInstance());
        return null;
    }

    /**
     * // Variable: expr -> ID
     * (4) expr.type = expr.definition.type;
     */
    @Override
    public Void visit(Variable v, Type param){
        super.visit(v, param);
        v.setType(v.getDefinition().getType());
        return null;
    }

    /**
     * // ArithmeticOperation: expr1 -> expr2 (+|-|*|/|%) expr3
     * (5) expr1.type = expr2.type.arithmetic(expr3.type, expr1);
     */
    @Override
    public Void visit(ArithmeticOperation a, Type param){
        super.visit(a, param);
        a.setType(a.getLeft().getType().arithmetic(a.getRight().getType(), a));
        return null;
    }

    /**
     * // While: stmt1 -> expr stmt2*
     * (6) expr.type.mustBeLogical(stmt1);
     */
    @Override
    public Void visit(While w, Type param){
        super.visit(w, param);
        w.getCondition().getType().mustBeLogical(w);
        return null;
    }

    /**
     * // UnaryMinus: expr1 -> expr2
     * (7) expr1.type = expr2.type.arithmetic(expr1)
     */
    @Override
    public Void visit(UnaryMinus u, Type param){
        super.visit(u, param);
        u.setType(u.getOperand().getType().arithmetic(u));
        return null;
    }

    /**
     * // UnaryNot: expr1 -> expr2
     * (8) expr1.type = expr2.type.logic(expr1)
     */
    @Override
    public Void visit(UnaryNot u, Type param){
        super.visit(u, param);
        u.setType(u.getOperand().getType().logic(u));
        return null;
    }

    /**
     * // LogicOperation: expr1 -> expr2 (&& | ||) expr3
     * (9) expr1.type = expr2.type.logic(expr3.type, expr1)
     */
    @Override
    public Void visit(LogicOperation l, Type param){
        super.visit(l, param);
        l.setType(l.getLeft().getType().logic(l.getRight().getType(), l));
        return null;
    }

    /**
     * // ComparationOp: expr1 -> expr2 (>= | <= | > | < | == | !=) expr3
     * (10) expr1.type = expr2.type.comparison(expr3.type, expr1)
     */
    @Override
    public Void visit(ComparationOperation c, Type param){
        super.visit(c, param);
        c.setType(c.getLeft().getType().comparison(c.getRight().getType(),c));
        return null;
    }

    /**
     * // Invocation: expr1 -> expr2 expr3*
     * (11) List<Type> argumentsTypes = new ArrayList<Type>();
     *     	for(Expression e : expr3*){
     *     	   argumentsTypes.add(e.type);
     *     	 }
     *     	expr1.type = expr2.type.parenthesis(argumentsTypes, expr1)
     */
    @Override
    public Void visit(Invocation i, Type param){
        super.visit(i, param);
        List<Type> argumentsTypes = new ArrayList<Type>();
        for(Expression e : i.getArgs()){
            argumentsTypes.add(e.getType());
        }
        i.setType(i.getVariable().getType().parenthesis(argumentsTypes, i));

        return null;
    }

    /**
     * // ArrayAccess: expr1 -> expr2 expr3
     * (12) expr1.type = expr2.type.squareBrackets(expr3.type, expr1)
     */
    @Override
    public Void visit(ArrayAccess a, Type param){
        super.visit(a, param);
        a.setType(a.getLeft().getType().squareBrackets(a.getRight().getType(), a));
        return null;
    }

    /**
     * // Cast: expr1 -> type expr2
     * (13) expr1.type = expr2.type.canBeCast(type,expr1)
     */
    @Override
    public Void visit(Cast c, Type param){
        super.visit(c, param);
        c.setType(c.getExpression().getType().canBeCastTo(c.getTargetType(), c));
        return null;
    }

    /**
     * // FieldAccess: expr1 -> name expr2
     * (14) expr1.type = expr2.type.dot(name, expr1)
     */
    @Override
    public Void visit(FieldAccess f, Type param){
        super.visit(f, param);
        f.setType(f.getExpression().getType().dot(f.getName(), f));
        return null;
    }

    /**
     * // If_Else: stmt1 -> stmt2* stmt3* expr
     * (15) expr.type.mustBeLogical(stmt1)
     */
    @Override
    public Void visit(If_else i, Type param){
        super.visit(i, param);
        i.getCondition().getType().mustBeLogical(i);
        return null;
    }

    /**
     * // Input: stmt -> expr
     * (16) expr.type.mustBeBuiltIn(stmt1)
     */
    @Override
    public Void visit(Input i, Type param){
        super.visit(i, param);
        i.getTarget().getType().mustBeBuiltIn(i);
        return null;
    }

    /**
     * // Log: stmt -> expr
     * (17) expr.type.mustBeBuiltIn(stmt1)
     */
    @Override
    public Void visit(Log l, Type param){
        super.visit(l, param);
        l.getExpression().getType().mustBeBuiltIn(l);
        return null;
    }

    /**
     * // Return: stmt -> expr
     * (18) expr.type.mustPromoteTo(returnType, stmt1)
     */
    @Override
    public Void visit(Return r, Type param){
        super.visit(r, param);
        r.getValue().getType().mustPromotesTo(param, r);
        return null;
    }

    /**
     * // Assigment: stmt -> expr1 expr2
     * (19) expr2.type.mustPromoteTo(expr1.type)
     */
    @Override
    public Void visit(Assigment a, Type param){
        super.visit(a, param);
        a.getRight().getType().mustPromotesTo(a.getLeft().getType(), a);
        return null;
    }


//    @Override
//    public Void visit(FuncType f, Type param){
//        super.visit(f, param);
//        for(VarDefinition v: f.getParams()){
//            v.getType().mustBeBuiltIn(v);
//        }
//        return null;
//    }

    /**
     * // FuncDefinition: def -> ID type stmt*
     * (20) Type returnType = ((FuncType) type).getReturnType
     *      for(Statement st: stmt*)
     *        // st.accept(this, returnType)
     */
    @Override
    public Void visit(FunctionDefinition f, Type param){
        f.getType().accept(this, param);

        // obtengo el tipo de retorno y se lo paso al visitor
        Type returnType = ((FuncType) f.getType()).getReturnType();
        for(Statement st: f.getStatements()){
            st.accept(this, returnType);
        }
        return null;
    }

    /**
     * // EJERCICIO
     *     // For:                   stmt1   -> stmt2 exp stmt3 stmt4*
     *     (21) exp.type.mustBeLogical()
     */
    @Override
    public Void visit(For f, Type param) {
        super.visit(f, param);
        f.getCondicion().getType().mustBeLogical(f);
        return null;
    }
}
