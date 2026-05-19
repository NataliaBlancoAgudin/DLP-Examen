package semantic;

import ast.definitions.Definition;
import ast.definitions.FunctionDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.Variable;
import ast.statements.For;
import ast.statements.Statement;
import ast.types.ErrorType;
import symboltable.SymbolTable;
import visitor.AbstractVisitor;

public class IdentificationVisitor extends AbstractVisitor<Void, Void> {
    private final SymbolTable symbolTable = new SymbolTable();
    // Definitions ---------------------------------------------------------------------------------------------

    /**
     * // FunctionDefinition:     def  -> ID type stmt*
     *     (1) if(!sg.insert(ID)
     *             ErrorType(Ya existe una función con el nombre " + ID + " en el mismo ambito")
     *         sg.set()
     *         sg.reset()
     */
    @Override
    public Void visit(FunctionDefinition f, Void param) {
        if(!symbolTable.insert(f)){
            new ErrorType("Ya existe una función con el nombre " + f.getName() + " en el mismo ambito", f);
        }
        symbolTable.set();
        super.visit(f, param);
        symbolTable.reset();
        return null;
    }

    /**
     * // VarDefinition:          def  -> ID type
     *     (2) if(!sg.insert(ID)
     *             ErrorType("Ya existe una variable con el nombre " + ID + " en el mismo ambito"
     */
    @Override
    public Void visit(VarDefinition v, Void param) {
        if(!symbolTable.insert(v)){
            new ErrorType("Ya existe una variable con el nombre " + v.getName() + " en el mismo ambito", v);
        }
        super.visit(v, param);
        return null;
    }

    // Expressions ----------------------------------------------------------------------------------------------

    /**
     * // Variable:               expr -> ID
     *     (3) Definition def = sg.find(ID)
     *         if(def != null)
     *             expr.definition = def
     *         else
     *             ErrorType error = ("No existe una variable con el nombre '" + ID + "'.")
     *             expr.definition(new VarDefinition("",error, 0,0)
     */
    @Override
    public Void visit(Variable v, Void param) {
        Definition def = symbolTable.find(v.getName());
        if(def != null){
            v.setDefinition(def);
        }
        else{
            ErrorType type = new ErrorType("No existe una variable con el nombre '" + v.getName()+"'.", v);
            v.setDefinition(new VarDefinition("", type, 0,0));
        }
        super.visit(v, param);
        return null;
    }

    /**
     * // EJERCICIO
     *     For:                    stmt1   -> stmt2 exp stmt3 stmt4*
     *     (4) sg.set()
     *         // super.visit()
     *         sg.reset
     */
    @Override
    public Void visit(For f, Void param) {
        symbolTable.set();
        super.visit(f, param);
        symbolTable.reset();
        return null;
    }
}
