grammar TSmm;
// imports para el TSmmParser
@header{
import ast.definitions.*;
import ast.expressions.*;
import ast.statements.*;
import ast.types.*;
import ast.*;
}

// reglas sintácticas ----------------------------------------

// PROGRAM
// primera forma
program returns [Program ast] locals [List<Definition> defs = new ArrayList<Definition>()]:
        (definition {$defs.addAll($definition.ast);})*
        m1=mainDefinition {$defs.add($m1.ast);}
        EOF
        {$ast = new Program($defs);}
       ;

// Segunda forma: sin locals
//program returns [Program ast = new Program()] :
//        (definition {$defs.add($definition.ast);})* mainDefinition EOF
//       ;

// Expression
expression returns [Expression ast] locals [List<Expression> exps = new ArrayList<Expression>()]:
            // Parentesis
            '(' e1=expression ')'
                {$ast = $e1.ast;}
            // Invocation como expresion
           | ID'(' (e1=expression {$exps.add($e1.ast);}(',' e2=expression {$exps.add($e2.ast);})*)? ')'
                {   Variable name = new Variable($ID.text, $ID.getLine(), $ID.getCharPositionInLine()+1);
                    $ast = new Invocation(
                        $exps,
                        name,
                        $ID.getLine(),
                        $ID.getCharPositionInLine()+1);
                }
           // ArrayAccess
           | e1=expression '[' e2=expression ']'
                {$ast = new ArrayAccess(
                        $e1.ast,
                        $e2.ast,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // FieldAccess
           | e1=expression '.' ID
                {$ast = new FieldAccess(
                        $e1.ast,
                        $ID.text,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // Cast
           | '(' e1=expression 'as' t1=simple_type ')'
                {$ast = new Cast(
                        $e1.ast,
                        $t1.ast,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // UnaryMinus
           | '-' e1=expression
                {$ast = new UnaryMinus(
                        $e1.ast,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // UnaryNot
           | '!' e1=expression
                {$ast = new UnaryNot(
                        $e1.ast,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // Arithmetic
           | e1=expression OP=('*'|'/' | '%') e2=expression
                {$ast = new ArithmeticOperation(
                        $e1.ast,
                        $e2.ast,
                        $OP.text,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // Arithmetic
           | e1=expression OP=('+'|'-') e2=expression
                {$ast = new ArithmeticOperation(
                        $e1.ast,
                        $e2.ast,
                        $OP.text,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // Comparation
           | e1=expression OP=('>'|'>='|'<'|'<='|'!='|'==') e2=expression
                {$ast = new ComparationOperation(
                        $e1.ast,
                        $e2.ast,
                        $OP.text,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // Logic
           | e1=expression OP=('&&'|'||') e2=expression
                {$ast = new LogicOperation(
                        $e1.ast,
                        $e2.ast,
                        $OP.text,
                        $e1.ast.getLine(),
                        $e1.ast.getColumn());
                }
           // Variable
           | ID
                {$ast = new Variable(
                        $ID.text,
                        $ID.getLine(),
                        $ID.getCharPositionInLine()+1);
                }
           // IntLiteral
           | INT_CONSTANT
                {$ast = new IntLiteral(
                        LexerHelper.lexemeToInt($INT_CONSTANT.text) ,
                        $INT_CONSTANT.getLine(),
                        $INT_CONSTANT.getCharPositionInLine()+1);
                }
           // RealLiteral
           | REAL_CONSTANT
                {$ast = new RealLiteral(
                        LexerHelper.lexemeToReal($REAL_CONSTANT.text),
                        $REAL_CONSTANT.getLine(),
                        $REAL_CONSTANT.getCharPositionInLine()+1);
                }
           // CharLiteral
           | CHAR_CONSTANT
                {$ast = new CharLiteral(
                        LexerHelper.lexemeToChar($CHAR_CONSTANT.text),
                        $CHAR_CONSTANT.getLine(),
                        $CHAR_CONSTANT.getCharPositionInLine()+1);
                }

           | BOOLEAN_CONSTANT
                {$ast = new BooleanLiteral(
                        LexerHelper.lexemeToBoolean($BOOLEAN_CONSTANT.text),
                        $BOOLEAN_CONSTANT.getLine(),
                        $BOOLEAN_CONSTANT.getCharPositionInLine()+1);
                }
           ;

// Statements
statement returns [List<Statement> ast = new ArrayList<Statement>()] locals[List<Expression>args = new ArrayList<Expression>(),
                                                                            List<Statement> elseBody = new ArrayList<Statement>()]:
            // Log
            'log' e1=expression {$ast.add(new Log($e1.ast,$e1.ast.getLine(),$e1.ast.getColumn()));}
            (',' e2=expression{$ast.add(new Log($e2.ast,$e2.ast.getLine(),$e2.ast.getColumn()));})*
            ';'
            // Input
          | 'input' e1=expression {$ast.add(new Input($e1.ast, $e1.ast.getLine(), $e1.ast.getColumn()));}
          (',' e2=expression{$ast.add(new Input($e2.ast, $e2.ast.getLine(), $e2.ast.getColumn()));})*
          ';'
          // Assigment
          | e1=expression '=' e2=expression ';'
                {$ast.add(new Assigment($e1.ast, $e2.ast, $e1.ast.getLine(), $e1.ast.getColumn()));}
          // While
          | 'while' '(' e1=expression ')' b1=body
                {$ast.add(new While($b1.ast, $e1.ast, $e1.ast.getLine(), $e1.ast.getColumn()));}
          // If-else
          | 'if' '(' e1=expression ')' b1=body ('else' b2=body {$elseBody = $b2.ast;} )?
                {$ast.add(new If_else($b1.ast, $elseBody, $e1.ast, $e1.ast.getLine(), $e1.ast.getColumn()));}
          // Invocation como statement
          | ID '(' (e1=expression{$args.add($e1.ast);} (',' e2=expression {$args.add($e2.ast);})*)? ')' ';'
                {Variable name = new Variable($ID.text, $ID.getLine(), $ID.getCharPositionInLine()+1);
                $ast.add(new Invocation($args, name, $ID.getLine(), $ID.getCharPositionInLine()+1));
                }
          // Return
          | 'return' e1=expression ';'
                {$ast.add(new Return($e1.ast, $e1.ast.getLine(), $e1.ast.getColumn()));}
          ;

// Regla auxiliar para no repetir codigo con el body de if-else y while
body returns [List<Statement> ast=new ArrayList<Statement>()]:
      s1=statement {$ast.addAll($s1.ast);}
     | '{' (s2=statement {$ast.addAll($s2.ast);})* '}'
     ;

// Tipos puede ser tipo simple o tipos compuestos (arrays y records)
type returns [Type ast] locals [List<RecordField> recordF = new ArrayList<>()]:
    s1=simple_type {$ast=$s1.ast;}
     | '[' INT_CONSTANT ']' t1=type
        {$ast = new ArrayType(
                LexerHelper.lexemeToInt($INT_CONSTANT.text),
                $t1.ast);
        }
     | '[' (r1 = recordField
            {
                for(RecordField rf:$r1.ast) {
                    boolean isDuplicate = false;

                    for(RecordField r:$recordF) {
                        if(r.getName().equals(rf.getName())){
                            isDuplicate = true;
                            break;
                        }
                    }

                    if(isDuplicate){
                        ErrorType errorType = new ErrorType("Error en el campo: " + rf.getName() + " del RecordField porque ya está declarado.", rf);
                    }
                    else {
                        $recordF.add(rf);
                    }
                }
            })+
       ']' {$ast = new RecordType($recordF);}
     ;

// Ponemos en distintas reglas el simple y el complex para
// que no podamos hacer cosas como n as [19]int
simple_type returns [Type ast]:
            'int' {$ast = IntType.getInstance();}
            | 'number' {$ast = NumberType.getInstance();}
            | 'char' {$ast = CharType.getInstance();}
            | 'boolean' {$ast = BooleanType.getInstance();}
            ;

// Tipo RecordFiled
recordField returns [List<RecordField> ast = new ArrayList<RecordField>()]:
            'let' i1=ids ':' t1=type ';'
            {
                for (Variable v:$i1.ast) {
                    RecordField record = new RecordField($t1.ast,v.getName(), v.getLine(), v.getColumn());
                    $ast.add(record);
                }
            }
            ;
// Definition
definition returns [List<Definition> ast = new ArrayList<Definition>()]:
            v1=varDefinition {$ast.addAll($v1.ast);}
           | fd=functionDefinition {$ast.add($fd.ast);}
           ;

// FunctionDefinition
functionDefinition returns [FunctionDefinition ast]
    locals [List<VarDefinition> prms = new ArrayList<VarDefinition>(),
            Type returnType]:
            'function'
            id1=ID
            '(' (p1=params {$prms=$p1.ast;})? ')' ':'
            ('void' {$returnType = VoidType.getInstance();}
                | st=simple_type {$returnType = $st.ast;})
            '{' b1=bodyFunction '}'
            {
                FuncType ftype = new FuncType($returnType, $prms);
                $ast = new FunctionDefinition(
                            $id1.text,
                            ftype,
                            $b1.ast,
                            $id1.getLine(),
                            $id1.getCharPositionInLine()+1);
            }
            ;

// Regla auxiliar para la definicion de funciones
bodyFunction returns [List<Statement> ast = new ArrayList<Statement>()]
    locals [List<VarDefinition> varDef = new ArrayList<VarDefinition>(),
            List<Statement>statements = new ArrayList<Statement>()]:
            (v1=varDefinition {$varDef.addAll($v1.ast);})*
            (s1=statement {$statements.addAll($s1.ast);})*
            {
                $ast.addAll($varDef);
                $ast.addAll($statements);
            }
            ;

// Regla auxiliar para la definicion de funciones
params returns [List<VarDefinition> ast = new ArrayList<VarDefinition>()]:
        id1=ID ':' t1=type
            {$ast.add(new VarDefinition(
                            $id1.text,
                            $t1.ast,
                            $id1.getLine(),
                            $id1.getCharPositionInLine()+1));
            }
        (',' id2=ID ':' t2=type
            {$ast.add(new VarDefinition(
                            $id2.text,
                            $t2.ast,
                            $id2.getLine(),
                            $id2.getCharPositionInLine()+1));
            }
        )*
        ;

// VarDefinition
varDefinition returns [List<VarDefinition> ast = new ArrayList<VarDefinition>()]:
             'let' i1=ids ':' t1=type ';'
             {
                for (Variable v:$i1.ast) {
                    VarDefinition vardef = new VarDefinition(v.getName(), $t1.ast, v.getLine(), v.getColumn());
                    $ast.add(vardef);
                }
             }
             ;

// Regla auxiliar para la definicion de variables
ids returns [List<Variable> ast = new ArrayList<Variable>()]:
        id1=ID
            {$ast.add(new Variable(
                    $id1.text,
                    $id1.getLine(),
                    $id1.getCharPositionInLine()+1));}
        (',' id2=ID
            {$ast.add(new Variable(
                    $id2.text,
                    $id2.getLine(),
                    $id2.getCharPositionInLine()+1));}
        )*
       ;

// mainDefinition
mainDefinition returns [FunctionDefinition ast]:
                'function' m='main' '(' ')' ':' 'void' '{' b1=bodyFunction '}'
                {
                    List<VarDefinition> varDef = new ArrayList<VarDefinition>();
                    FuncType type = new FuncType(VoidType.getInstance(), varDef);
                    $ast = new FunctionDefinition(
                                $m.text,
                                type,
                                $b1.ast,
                                $m.getLine(),
                                $m.getCharPositionInLine()+1
                                );
                }
               ;

// REGLAS LEXICAS ---------------------------------------

// Patrón espacios en blanco
WHITES: [ \n\t\r]+ -> skip
      ;

// Patrón numeros enteros
INT_CONSTANT: [1-9]DIGIT*
            | '0'
            ;

// Patrón para los comentario de una sola linea y dos
COMMENTS: ('/*' .*? '*/'
         | '//' ~[\r\n]*)
         -> skip
         ;

// Patrón para los números reales
REAL_CONSTANT: BASE_DECIMAL EXP?
             | INT_CONSTANT EXP
            ;

// Patrón para los identificadores
ID: [a-zA-Z_][a-zA-Z0-9_]*
    ;

// Patrón para los characteres
CHAR_CONSTANT: '\'' . '\''
              | '\'\\' ('n'|'r'|'t'|'b') '\''
              | '\'\\' INT_CONSTANT '\''
            ;

// Framgentos
// Fragmento para los digitos
fragment
DIGIT: [0-9]
        ;
// Fragmento para la base decimal
fragment
BASE_DECIMAL: INT_CONSTANT '.' DIGIT*
    | '.' DIGIT+
    ;

// Fragmento para el exponente
fragment
EXP:[Ee] [+-]? INT_CONSTANT
    ;

BOOLEAN_CONSTANT: 'true'
                | 'false'
                ;