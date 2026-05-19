package codegen;

import ast.definitions.FunctionDefinition;
import ast.definitions.VarDefinition;
import ast.statements.Statement;
import ast.types.FuncType;
import ast.types.RecordField;
import ast.types.RecordType;
import visitor.AbstractVisitor;

public class OffsetVisitor extends AbstractVisitor<Void, Boolean> {

    private int bytesGlobalSum = 0;
    private int bytesLocalSum = 0;  // Es para las locales

    /**
     * // VarDefinition:  vardefiniton    ->   type ID
     *     (2) if(vardefinition.scope == 0){
     *             vardefintion.offset = bytesGlobalSum;
     *             bytesGlobalSum += vardefinition.type.numberOfBytes;
     *         }
     *         else if(isLocal){
     *             bytesLocalSum += vardefinition.type.numberOfBytes;
     *             vardefinition.offset = - bytesLocalSum;
     *         }
     */
    @Override
    public Void visit(VarDefinition v, Boolean isLocal){
        super.visit(v, isLocal);  // Hay que visitar a los hijos porque si tenemos un VarDefinition de RecordType
        // GLOBALES
        if(v.getScope() == 0){
            v.setOffset(bytesGlobalSum);
            bytesGlobalSum += v.getType().numberOfBytes();
        }
        // LOCALES
        else if(isLocal){
            bytesLocalSum += v.getType().numberOfBytes();
            v.setOffset(-bytesLocalSum);
        }
        return null;
    }

    /**
     * // FuncDefintion:  funcdefinition  ->   type ID varDefinition* statement*
     *     (1) int bytesLocalSum = 0;
     *         for(Statement st: statement*){
     *             bytesLocalSum += st.type.numberOfBytes;
     *             st.offset = bytesLocalSum;
     *         }
     *         funcdefinition.bytesLocalSum = bytesLocalSum;
     */
    @Override
    public Void visit(FunctionDefinition f, Boolean isLocal){
        bytesLocalSum = 0;  // estamos dentro de una nueva funcion (reesetamos)

        // tipo (false)
        f.getType().accept(this, false);
        // Sentencias locales (true)
        for(Statement st: f.getStatements()){
            st.accept(this, true);
        }

        // Asignamos el bytesLocalSum de la definicion de funcion
        f.setBytesLocalSum(bytesLocalSum);
        return null;
    }

    /**
     * // FuncType:       type_1          ->   type_2 vardefinition*
     *     (3) int bytesParam = 4;
     *         for(int i=statement*.size()-1; i>0; i--){
     *             VarDefinition v = statement*.get(i);
     *             v.offset = bytesParam;
     *             bytesParam += v.type.numberOfBytes;
     *         }
     */
    @Override
    public Void visit(FuncType f, Boolean isLocal){
        int bytesParam = 4;

        for(int i = f.getParams().size()-1; i >= 0; i--){
            VarDefinition var = f.getParams().get(i);
            var.setOffset(bytesParam);
            bytesParam += var.getType().numberOfBytes();

            var.accept(this, false);
        }

        f.getReturnType().accept(this, isLocal);
        return null;
    }

    /**
     * // RecordType:     type_2          ->   recordField*
     *     (4) int contador = 0;
     *         for(RecordField record:recordField*){
     *             record.offset = contador;
     *             contador += record.targetType.numberOfBytes;
     *         }
     */
    @Override
    public Void visit(RecordType r, Boolean isLocal){
        int contador = 0;
        for(RecordField recordField: r.getFieldList()){
            recordField.setOffset(contador);
            contador+= recordField.getTargetype().numberOfBytes();
            recordField.getTargetype().accept(this, isLocal); // Para cuando tengamos records dentro de records
        }
        return null;
    }
}