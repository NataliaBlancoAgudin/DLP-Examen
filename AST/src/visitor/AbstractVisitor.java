package visitor;

import ast.Program;
import ast.definitions.Definition;
import ast.definitions.FunctionDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.*;
import ast.statements.*;
import ast.types.*;

public abstract class AbstractVisitor<RT,PT> implements Visitor<RT,PT> {
    // Definitions ---------------------------------------------------------------------------------------------
    @Override
    public RT visit(FunctionDefinition f, PT param) {
        f.getType().accept(this, param);
        for(Statement st : f.getStatements()){
            st.accept(this, param);
        }
        return null;
    }

    @Override
    public RT visit(VarDefinition v, PT param) {
        v.getType().accept(this, param);
        return null;
    }

    // Expressions ----------------------------------------------------------------------------------------------
    @Override
    public RT visit(ArithmeticOperation a, PT param) {
        a.getLeft().accept(this, param);
        a.getRight().accept(this, param);
        return null;
    }

    @Override
    public RT visit(ArrayAccess a, PT param) {
        a.getLeft().accept(this, param);
        a.getRight().accept(this, param);
        return null;
    }

    @Override
    public RT visit(Cast c, PT param) {
        c.getExpression().accept(this, param);
        return null;
    }

    @Override
    public RT visit(CharLiteral c, PT param) {
        return null;
    }

    @Override
    public RT visit(ComparationOperation c, PT param) {
        c.getLeft().accept(this, param);
        c.getRight().accept(this, param);
        return null;
    }

    @Override
    public RT visit(FieldAccess f, PT param) {
        f.getExpression().accept(this, param);
        return null;
    }

    @Override
    public RT visit(IntLiteral i, PT param) {
        return null;
    }

    @Override
    public RT visit(Invocation invocation, PT param) {
        invocation.getVariable().accept(this, param);
        for(Expression exp:invocation.getArgs()){
            exp.accept(this, param);
        }
        return null;
    }

    @Override
    public RT visit(LogicOperation l, PT param) {
        l.getLeft().accept(this, param);
        l.getRight().accept(this, param);
        return null;
    }

    @Override
    public RT visit(RealLiteral r, PT param) {
        return null;
    }

    @Override
    public RT visit(UnaryMinus u, PT param) {
        u.getOperand().accept(this, param);
        return null;
    }

    @Override
    public RT visit(UnaryNot u, PT param) {
        u.getOperand().accept(this, param);
        return null;
    }

    @Override
    public RT visit(Variable v, PT param) {
        return null;
    }

    // Statements -------------------------------------------------------------------------------------------------
    @Override
    public RT visit(Assigment a, PT param) {
        a.getLeft().accept(this, param);
        a.getRight().accept(this, param);
        return null;
    }

    @Override
    public RT visit(If_else i, PT param) {
        for(Statement ifBody: i.getIf_body()){
            ifBody.accept(this, param);
        }
        for(Statement elseBody: i.getElse_body()){
            elseBody.accept(this, param);
        }
        i.getCondition().accept(this, param);
        return null;
    }

    @Override
    public RT visit(Input i, PT param) {
        i.getTarget().accept(this, param);
        return null;
    }

    @Override
    public RT visit(Log l, PT param) {
        l.getExpression().accept(this, param);
        return null;
    }

    @Override
    public RT visit(Return r, PT param) {
        r.getValue().accept(this, param);
        return null;
    }

    @Override
    public RT visit(While w, PT param) {
        for(Statement st: w.getBody()){
            st.accept(this, param);
        }
        w.getCondition().accept(this, param);
        return null;
    }

    @Override
    public RT visit(ArrayType a, PT param) {
        a.getOf().accept(this, param);
        return null;
    }

    @Override
    public RT visit(CharType c, PT param) {
        return null;
    }

    @Override
    public RT visit(ErrorType e, PT param) {
        e.getLocatable().accept(this, param);
        return null;
    }

    @Override
    public RT visit(FuncType f, PT param) {
        f.getReturnType().accept(this, param);
        for(VarDefinition v: f.getParams()){
            v.accept(this, param);
            v.getType().accept(this, param);
        }
        return null;
    }

    @Override
    public RT visit(IntType i, PT param) {
        return null;
    }

    @Override
    public RT visit(NumberType n, PT param) {
        return null;
    }

    @Override
    public RT visit(RecordField r, PT param) {
        r.getTargetype().accept(this, param);
        return null;
    }

    @Override
    public RT visit(RecordType r, PT param) {
        for(RecordField rf: r.getFieldList()){
            rf.accept(this, param);
        }
        return null;
    }

    @Override
    public RT visit(VoidType v, PT param) {
        return null;
    }

    // Program  -------------------------------------------------------------------------------------------------
    @Override
    public RT visit(Program p, PT param) {
        for(Definition definition:p.getDefinitionList()){
            definition.accept(this, param);
        }
        return null;
    }
}
