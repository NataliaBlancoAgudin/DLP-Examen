package visitor;

import ast.Program;
import ast.definitions.FunctionDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.*;
import ast.statements.*;
import ast.types.*;

public interface Visitor<RT,PT> {
    // Definitions
    RT visit(FunctionDefinition f, PT param);
    RT visit(VarDefinition v, PT param);

    // Expressions
    RT visit(ArithmeticOperation a, PT param);
    RT visit(ArrayAccess a, PT param);
    RT visit(Cast c, PT param);
    RT visit(CharLiteral c, PT param);
    RT visit(ComparationOperation c, PT param);
    RT visit(FieldAccess f, PT param);
    RT visit(IntLiteral i, PT param);
    RT visit(Invocation invocation, PT param);
    RT visit(LogicOperation l, PT param);
    RT visit(RealLiteral r, PT param);
    RT visit(UnaryMinus u, PT param);
    RT visit(UnaryNot u, PT param);
    RT visit(Variable v, PT param);

    // Statements
    RT visit(Assigment a, PT param);
    RT visit(If_else i, PT param);
    RT visit(Input i, PT param);
    RT visit(Log l, PT param);
    RT visit(Return r, PT param);
    RT visit(While w, PT param);

    // Types
    RT visit(ArrayType a, PT param);
    RT visit(CharType c, PT param);
    RT visit(ErrorType e, PT param);
    RT visit(FuncType f, PT param);
    RT visit(IntType i, PT param);
    RT visit(NumberType n, PT param);
    RT visit(RecordField r, PT param);
    RT visit(RecordType r, PT param);
    RT visit(VoidType v, PT param);

    // Program
    RT visit(Program p, PT param);
}
