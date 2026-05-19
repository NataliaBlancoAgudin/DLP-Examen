package codegen;

import ast.Program;
import ast.definitions.Definition;
import ast.definitions.FunctionDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.Invocation;
import ast.statements.*;
import ast.types.FuncType;
import ast.types.IntType;
import ast.types.VoidType;


public class ExecuteCGVisitor extends AbstractCGVisitor<Void, FunctionDefinition> {

    private AddressCGVisitor addressCGVisitor;
    private ValueCGVisitor valueCGVisitor;

    public ExecuteCGVisitor(CodeGenerator codeGenerator) {
        super(codeGenerator);

        this.addressCGVisitor = new AddressCGVisitor(codeGenerator);
        this.valueCGVisitor = new ValueCGVisitor(codeGenerator);
        this.addressCGVisitor.setValueCGVisitor(valueCGVisitor);
        this.valueCGVisitor.setAddressCGVisitor(addressCGVisitor);
    }

    /**
     *execute[[Assigment: stmt -> expr1 expr2]]()=
     *         address[[expr1]]()
     *         value[[expr2]]()
     *         cg.convertTo(expr2.type, expr1.type)
     *         <store> expr1.type.suffix()
     */
    @Override
    public Void visit(Assigment a, FunctionDefinition param){
        getCodeGenerator().commentLine(a.getLine());
        getCodeGenerator().comment("Assignment");

        a.getLeft().accept(addressCGVisitor, null);
        a.getRight().accept(valueCGVisitor, null);
        getCodeGenerator().convertTo(a.getRight().getType(), a.getLeft().getType());
        getCodeGenerator().store(a.getLeft().getType());
        return null;
    }

    /**
     * execute[[Input: stmt1 -> expr]]()=
     *         <#line> expr.line
     *         < ' * Read>
     *         address[[expr]]()
     *         <in> expr.type.suffix
     *         <store> expr.type.suffix
     */
    @Override
    public Void visit(Input i, FunctionDefinition param){
        getCodeGenerator().commentLine(i.getLine());
        getCodeGenerator().comment("Read");
        i.getTarget().accept(addressCGVisitor, null);
        getCodeGenerator().in(i.getTarget().getType());
        getCodeGenerator().store(i.getTarget().getType());
        return null;
    }

    /**
     * execute[[Log: stmt -> expr]]()=
     *         <#line> expr.line
     *         < ' * Write>
     *         value[[expr]]
     *         <out> expr.type.suffix
     */
    @Override
    public Void visit(Log l, FunctionDefinition param){
        getCodeGenerator().commentLine(l.getLine());
        getCodeGenerator().comment("Write");
        l.getExpression().accept(valueCGVisitor, null);
        getCodeGenerator().output(l.getExpression().getType());
        return null;
    }

    /**
     *execute[[FunctionDefinition: definition -> ID type stmt*]]()=
     *         ID <:>
     *         < ' * Parameters: >
     *         for(VarDefinition v: type.params){
     *             exectue[[v]]();
     *         }
     *
     *         < ' * Local Variables: >
     *         for(Statements st: stmt*){
     *             if(st instanceof VarDefinition){
     *                 execute[[st]]();
     *             }
     *         }
     *         ------------------------LO HACEMOS EN EL OFFSET ---------------------------------------
     *         if(definitions*.isEmpty()){
     *             defintion.bytesLocalSum = 0;
     *         }
     *         else {
     *             VarDefinition ultimaVardefintion = definition*.get(definition*.size()-1)
     *             definition.bytesLocalSum = -1 * ultimaVardefinition.offset
     *         }
     *         -------------------------------------------------------------------------------------
     *         <enter> definitions.bytesLocalSum
     *         for(Statements st:stmt*){
     *             if(!(st instaceof VarDefinition)){
     *                 execute[[st]](definition)
     *             }
     *         }
     *         if(type.returnType == VoidType.instance){
     *             <ret> 0, definitions.bytesLocalSum, type.parameters.map(p->p.type.numberOfBytes).sum()
     *         }
     */
    @Override
    public Void visit(FunctionDefinition f, FunctionDefinition param){
        getCodeGenerator().commentLine(f.getLine());
        getCodeGenerator().printFunction(f.getName());

        getCodeGenerator().comment("Parameters:");
        for(VarDefinition def: ((FuncType) f.getType()).getParams()){
            def.accept(this, null);
        }

        getCodeGenerator().comment("Local Variables:");
        for(Statement st: f.getStatements()){
            if(st instanceof VarDefinition){
                st.accept(this, null);
            }
        }

        getCodeGenerator().enter(f.getBytesLocalSum());
        for(Statement st: f.getStatements()){
            if(!(st instanceof VarDefinition)){
                st.accept(this, f);
            }
        }

        // Para las funciones tipo Void
        //      Esto es porque la plantilla del Return ya nos hace el ret en caso de que no sea Void la funcion
        if(((FuncType) f.getType()).getReturnType() == VoidType.getInstance()){
            getCodeGenerator().ret(0, f.getBytesLocalSum(),
                    ((FuncType)f.getType()).getParams().stream().mapToInt(p -> p.getType().numberOfBytes()).sum());
        }

        return null;
    }

    /**
     * VarDefinition
     *     execute[[VarDefinition: definition -> type ID]](){
     *         < ' * > type ID <(offset > definition.offset <)>
     *     }
     */
    @Override
    public Void visit(VarDefinition v, FunctionDefinition param){
        getCodeGenerator().comment(v.getType() + " " + v.getName() + "(offset " + v.getOffset() + ")");
        return null;
    }

    /**
     *
     * execute[[While: stmt1 -> expr stmt2*]]()=
     *         <#line> expr.line
     *         <' * While>
     *         String cond = cg.getLabel()
     *         String end = cg.getLabel()
     *         cond <:>
     *         value[[expr]]()
     *         cg.convertTo(expr.type, IntType.instance)
     *         <jz> end
     *         <' * While body>
     *         stmt2*.forEach(s -> execute[[s]]())
     *         <jmp> cond
     *         end <:>
     */
    @Override
    public Void visit(While w, FunctionDefinition param){
        getCodeGenerator().commentLine(w.getLine());
        getCodeGenerator().comment("While");

        String cond = getCodeGenerator().getLabel();
        String end = getCodeGenerator().getLabel();
        getCodeGenerator().insertLabel(cond);
        w.getCondition().accept(valueCGVisitor, null);
        getCodeGenerator().convertTo(w.getCondition().getType(), IntType.getInstance());
        getCodeGenerator().jz(end);
        getCodeGenerator().comment("While body");
        for(Statement st: w.getBody()){
            st.accept(this, null);
        }
        getCodeGenerator().jmp(cond);
        getCodeGenerator().insertLabel(end);
        return null;
    }

    /**
     * execute[[If_else: stmt1 -> expr stmt2* stmt3*]]()=
     *         <#line> expr.line
     *         <' * If>
     *         String else = cg.getLabel()
     *         String end = cg.getLabel()
     *         value[[expr]]()
     *         cg.convertTo(expr.type, IntType.instance)
     *         <jz> else
     *         <' * if body>
     *         stmt2*.forEach(s -> execute[[s]]())
     *         <jmp> end
     *         else <:>
     *         <' * else body>
     *         stmt3*.forEach(s -> execute[[s]]())
     *         end <:>
     */
    @Override
    public Void visit(If_else i, FunctionDefinition param){
        getCodeGenerator().commentLine(i.getLine());
        getCodeGenerator().comment("If");

        String elseC = getCodeGenerator().getLabel();
        String end = getCodeGenerator().getLabel();
        i.getCondition().accept(valueCGVisitor, null);
        getCodeGenerator().convertTo(i.getCondition().getType(), IntType.getInstance());
        getCodeGenerator().jz(elseC);
        getCodeGenerator().comment("if body");
        for(Statement st : i.getIf_body()){
            st.accept(this, null);
        }
        getCodeGenerator().jmp(end);
        getCodeGenerator().insertLabel(elseC);
        getCodeGenerator().comment("else body");
        for(Statement st : i.getElse_body()){
            st.accept(this, null);
        }

        getCodeGenerator().insertLabel(end);
        return null;
    }

    /**
     * execute[[Program: program -> def*]]()=
     *         for(Defintion d:def*){
     *             if(d instanceof VarDefinition){
     *                 execute[[d]]()
     *             }
     *         }
     *         <' Invocation to the main function>
     *         <call main>
     *         <halt>
     *         for(Definition d:def*){
     *             if(d instanceof FuncDefinition){
     *                 execute[[d]]()
     *             }
     *         }
     */
    @Override
    public Void visit(Program p, FunctionDefinition param){
        for(Definition def: p.getDefinitionList()){
            if(def instanceof VarDefinition){
                def.accept(this, null);
            }
        }

        getCodeGenerator().mainInvocation();
        getCodeGenerator().halt();

        for(Definition def: p.getDefinitionList()){
            if(def instanceof FunctionDefinition){
                def.accept(this, null);
            }
        }
        return null;
    }

    /**
     * exectue[[Invocation: stmt -> expr1 expr2*]]()=
     *         value[[(Expression) stmt]]()
     *         if(expr1.type.returnType != VoidType.instance){
     *             <pop> expr1.type.returnType.suffix()
     *         }
     */
    @Override
    public Void visit(Invocation invocation, FunctionDefinition param){
        invocation.accept(valueCGVisitor, null);
        if(((FuncType)invocation.getVariable().getType()).getReturnType() != VoidType.getInstance()){
            getCodeGenerator().pop(((FuncType)invocation.getVariable().getType()).getReturnType());
        }
        return null;
    }

    /**
     * execute[[Return: stmt -> expr]](FunctionDefintion fd)=
     *         value[[expr]]()
     *         cg.convertTo(expr.type, fd.type.returnType)
     *         <ret> fd.type.returnType.numberOfBytes
     *               fd.bytesLocalSum,
     *               fd.types.parameters.map(p->p.type.numberOfBytes).sum()
     */
    @Override
    public Void visit(Return a, FunctionDefinition fd){
        a.getValue().accept(valueCGVisitor, null);
        getCodeGenerator().convertTo(a.getValue().getType(), ((FuncType) fd.getType()).getReturnType());
        getCodeGenerator().ret(((FuncType) fd.getType()).getReturnType().numberOfBytes(),
                                fd.getBytesLocalSum(),
                                ((FuncType)fd.getType()).getParams().stream().mapToInt(p-> p.getType().numberOfBytes()).sum());
        return null;
    }

    // EJERCICIO
    /**
     * execute[[VarDefWithAssigment:   stmt    -> def expr]]()=
     *         < ' * > def.type def.name <(offset > def.type.offset <)>
     *
     *         <pusha bp>
     *         <push> def.offset
     *         <addi>
     *
     *         value[[expr]]()
     *         cg.convertTo(expr.type, def.type)
     *         <store> def.type.suffix()
     */
    @Override
    public Void visit(VarDefinitionWithAssigment v, FunctionDefinition param) {
        getCodeGenerator().comment(v.getVar().getType() + " " + v.getVar().getName() + "(offset " + v.getVar().getOffset() + ")");
        getCodeGenerator().pushBP();
        getCodeGenerator().pushi(v.getVar().getOffset());
        getCodeGenerator().add(IntType.getInstance());

        v.getExp().accept(valueCGVisitor, null);
        getCodeGenerator().convertTo(v.getExp().getType(), v.getVar().getType());
        getCodeGenerator().store(v.getVar().getType());

        return null;
    }

    /**
     * execute[[For:                   stmt1   -> varDefAss exp stmt2 stmt3*]]()=
     *         <#line> expr.line
     *         <' * For>
     *         String cond = cg.getLabel()
     *         String end = cg.getLabel()
     *
     *         execute[[varDefAss]]()
     *         cond <:>
     *         value[[expr]]()
     *         cg.convertTo(expr.type, IntType)
     *         <jz> end
     *         stmt3*.forEach(s -> execute[[s]]())
     *         execute[[stmt2]]
     *         <jmp> cond
     *         end <:>
     */
    @Override
    public Void visit(For f, FunctionDefinition param) {
        getCodeGenerator().commentLine(f.getLine());
        getCodeGenerator().comment("For");

        String cond = getCodeGenerator().getLabel();
        String end = getCodeGenerator().getLabel();

        f.getInitial().accept(this, param);
        getCodeGenerator().insertLabel(cond);
        f.getCondition().accept(valueCGVisitor, null);
        getCodeGenerator().convertTo(f.getCondition().getType(), IntType.getInstance());
        getCodeGenerator().jz(end);
        for(Statement st: f.getBody()){
            st.accept(this, param);
        }
        f.getIncrement().accept(this, param);
        getCodeGenerator().jmp(cond);
        getCodeGenerator().insertLabel(end);

        return null;
    }
}
