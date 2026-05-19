package ast;

import visitor.Visitor;

// Interfaz `ASTNode`
public interface ASTNode {
    <RT, PT> RT accept(Visitor<RT,PT> v, PT param);
}
