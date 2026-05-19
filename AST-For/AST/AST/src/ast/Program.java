package ast;

import ast.definitions.Definition;
import visitor.Visitor;

import java.util.List;

public class Program implements ASTNode{

    List<Definition> definitionList;

    public Program(List<Definition> definitionList) {
        this.definitionList = definitionList;
    }

    public List<Definition> getDefinitionList() {
        return definitionList;
    }

    @Override
    public <RT, PT> RT accept(Visitor<RT, PT> v, PT param) {
        return v.visit(this, param);
    }

    // se puede añadir un constructor sin parametros y con un metodo que sea addDefintion
}
