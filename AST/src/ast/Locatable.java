package ast;

// Interfaz `Locatable`: extiende de ASTNode
public interface Locatable extends ASTNode{
    // Metodos para sacar la linea y la columna en el analzador lexico
    int getLine();
    int getColumn();
}
