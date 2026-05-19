package semantic;

import ast.expressions.*;
import ast.statements.*;
import ast.types.*;
import visitor.AbstractVisitor;

public class LValueVisitor extends AbstractVisitor<Void, Void> {
    // Expressions ----------------------------------------------------------------------------------------------

    /**
     * // ArithmeticOperation:    expr1 -> expr2 (+|-|*|/|%) expr3
     *     (1) expr1.lvalue = false
     */
    @Override
    public Void visit(ArithmeticOperation a, Void param) {
        super.visit(a, param);
        a.setLvalue(false);
        return null;
    }

    /**
     * // ArrayAccess:            expr1 -> expr2 expr3
     *     (2) expr1.lvalue = true
     */
    @Override
    public Void visit(ArrayAccess a, Void param) {
        super.visit(a, param);
        a.setLvalue(true);
        return null;
    }

    /**
     * // Cast:                   expr1 -> type expr2
     *     (3) expr1.lvalue = false
     */
    @Override
    public Void visit(Cast c, Void param) {
        super.visit(c, param);
        c.setLvalue(false);
        return null;
    }

    /**
     *  // CharLiteral:            expr  -> CHAR_CONSTANT
     *     (4) expr.lvalue = false
     */
    @Override
    public Void visit(CharLiteral c, Void param) {
        super.visit(c, param);
        c.setLvalue(false);
        return null;
    }

    /**
     * // ComparationOp:          expr1  -> expr2 (>= | <= | > | < | == | !=) expr3
     *     (5) expr1.lvalue = false
     */
    @Override
    public Void visit(ComparationOperation c, Void param) {
        super.visit(c, param);
        c.setLvalue(false);
        return null;
    }

    /**
     * // FieldAccess:            expr1       -> expr2 ID
     *     (6) expr1.lvalue = true
     */
    @Override
    public Void visit(FieldAccess f, Void param) {
        super.visit(f, param);
        f.setLvalue(true);
        return null;
    }

    /**
     * // IntLiteral:             expr        -> INT_CONSTANT
     *     (7) expr.lvalue = false
     */
    @Override
    public Void visit(IntLiteral i, Void param) {
        super.visit(i, param);
        i.setLvalue(false);
        return null;
    }

    /**
     * // Invocation:             expr1       -> expr2 expr3*
     *     (8) expr.lvalue = false
     */
    @Override
    public Void visit(Invocation i, Void param) {
        super.visit(i, param);
        i.setLvalue(false);
        return null;
    }

    /**
     * // LogicOperation:         expr1       -> expr2 (&& | ||) expr3
     *     (9) expr1.lvalue = false
     */
    @Override
    public Void visit(LogicOperation l, Void param) {
        super.visit(l, param);
        l.setLvalue(false);
        return null;
    }

    /**
     * // RealLiteral:            expr        -> REAL_CONSTANT
     *     (10) expr1.lvalue = false
     */
    @Override
    public Void visit(RealLiteral r, Void param) {
        super.visit(r, param);
        r.setLvalue(false);
        return null;
    }

    /**
     * // UnaryMinus:             expr1       -> expr2
     *     (11) expr1.lvalue = false
     */
    @Override
    public Void visit(UnaryMinus u, Void param) {
        super.visit(u, param);
        u.setLvalue(false);
        return null;
    }

    /**
     * // UnaryNot:               expr1       -> expr2
     *     (12) expr1.lvalue = false
     */
    @Override
    public Void visit(UnaryNot u, Void param) {
        super.visit(u, param);
        u.setLvalue(false);
        return null;
    }

    /**
     * // Variable:               expr        -> ID
     *     (13) expr.lvalue = true
     */
    @Override
    public Void visit(Variable v, Void param) {
        super.visit(v, param);
        v.setLvalue(true);
        return null;
    }

    // Statements -------------------------------------------------------------------------------------------------

    /**
     * // Assigment:              stmt        -> expr1 expr2
     *     (14) if(!expr1.lvalue)
     *         ErrorType("El lado izquierdo de la asignación no es direccionable (lvalue))
     */
    @Override
    public Void visit(Assigment a, Void param) {
        super.visit(a, param);
        if(!a.getLeft().getLvalue()){
            new ErrorType("El lado izquierdo de la asignación no es direccionable (lvalue)", a.getLeft());
        }
        return null;
    }

    /**
     * // Input:                  stmt        -> expr
     *     (15) if(!expr.lvalue)
     *         ErrorType("La expresión del input debe ser direccionable (lvalue))
     */
    @Override
    public Void visit(Input i, Void param) {
        super.visit(i, param);
        if(!i.getTarget().getLvalue()){
            new ErrorType("La expresión del input debe ser direccionable (lvalue)", i.getTarget());
        }
        return null;
    }

    /**
     * // Post:                  stmt    -> expr (++|--)
     *     (16) if(!expr.lvalue)
     *             ErrorType("La expresión del post debe ser direccionable (lvalue))
     */
    @Override
    public Void visit(Post p, Void param) {
        super.visit(p, param);
        if(!p.getExpression().getLvalue()){
            new ErrorType("La expresión del post debe ser direccionable (lvalue)", p.getExpression());
        }
        return null;
    }
}
