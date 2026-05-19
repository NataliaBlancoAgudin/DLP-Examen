package codegen;

import ast.Program;
import ast.definitions.FunctionDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.*;
import ast.statements.*;
import ast.types.*;
import visitor.Visitor;

public abstract class AbstractCGVisitor <RT,PT> implements Visitor<RT,PT> {
    private CodeGenerator codeGenerator;

    public AbstractCGVisitor(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    @Override
    public RT visit(FunctionDefinition f, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + f.getClass().getSimpleName());
    }

    @Override
    public RT visit(VarDefinition v, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + v.getClass().getSimpleName());
    }

    @Override
    public RT visit(ArithmeticOperation a, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + a.getClass().getSimpleName());
    }

    @Override
    public RT visit(ArrayAccess a, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + a.getClass().getSimpleName());
    }

    @Override
    public RT visit(Cast c, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + c.getClass().getSimpleName());
    }

    @Override
    public RT visit(CharLiteral c, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + c.getClass().getSimpleName());
    }

    @Override
    public RT visit(ComparationOperation c, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + c.getClass().getSimpleName());
    }

    @Override
    public RT visit(FieldAccess f, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + f.getClass().getSimpleName());
    }

    @Override
    public RT visit(IntLiteral i, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + i.getClass().getSimpleName());
    }

    @Override
    public RT visit(Invocation i, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + i.getClass().getSimpleName());
    }

    @Override
    public RT visit(LogicOperation l, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + l.getClass().getSimpleName());
    }

    @Override
    public RT visit(RealLiteral r, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + r.getClass().getSimpleName());
    }

    @Override
    public RT visit(UnaryMinus u, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + u.getClass().getSimpleName());
    }

    @Override
    public RT visit(UnaryNot u, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + u.getClass().getSimpleName());
    }

    @Override
    public RT visit(Variable v, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + v.getClass().getSimpleName());
    }

    @Override
    public RT visit(Assigment a, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + a.getClass().getSimpleName());
    }

    @Override
    public RT visit(If_else i, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + i.getClass().getSimpleName());
    }

    @Override
    public RT visit(Input i, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + i.getClass().getSimpleName());
    }

    @Override
    public RT visit(Log l, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + l.getClass().getSimpleName());
    }

    @Override
    public RT visit(Return r, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + r.getClass().getSimpleName());
    }

    @Override
    public RT visit(While w, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + w.getClass().getSimpleName());
    }

    @Override
    public RT visit(ArrayType a, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + a.getClass().getSimpleName());
    }

    @Override
    public RT visit(CharType c, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + c.getClass().getSimpleName());
    }

    @Override
    public RT visit(ErrorType e, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + e.getClass().getSimpleName());
    }

    @Override
    public RT visit(FuncType f, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + f.getClass().getSimpleName());
    }

    @Override
    public RT visit(IntType i, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + i.getClass().getSimpleName());
    }

    @Override
    public RT visit(NumberType n, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + n.getClass().getSimpleName());
    }

    @Override
    public RT visit(RecordField r, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + r.getClass().getSimpleName());
    }

    @Override
    public RT visit(RecordType r, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + r.getClass().getSimpleName());
    }

    @Override
    public RT visit(VoidType v, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + v.getClass().getSimpleName());
    }

    @Override
    public RT visit(Program p, PT param) {
        throw new UnsupportedOperationException("Code generation no implementa para el nodo: " + p.getClass().getSimpleName());
    }
}
