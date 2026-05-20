package symboltable;

import java.util.*;
import ast.definitions.Definition;

public class SymbolTable {
	
	private int scope=0;
	// string sería la clave, y lo que queremos que devuelva es una definition
	// cada mapa es un ambito
	private List<Map<String,Definition>> table;
	public SymbolTable()  {
		table=new ArrayList<>();
		table.add(new HashMap<>());
	}

	// cuando en el identificationVisitor entra en un functionDefinition llamara a este set (acutalizar el scope)
	public void set() {
		scope++;
		table.add(new HashMap<>());
	}

	// cuando en el identificationVisitor sale en un functionDefinition llamara a este reset (acutalizar el scope)
	public void reset() {
		table.removeLast();
		scope--;
	}

	// Puede haber un error si intentamos insertar una variable que ya existe en el mismo ambito.
	// Devolveremos un false -> los errores no tiene que ser aqui, es en el IdentificationVisitor
	public boolean insert(Definition definition) {
		if(findInCurrentScope(definition.getName())){
			return false;
		}
		table.getLast().put(definition.getName(),definition);
		definition.setScope(scope);
		return true;
	}

	// Puede haber un error si no existe ninguna variable en ninguno de los ambitos
	// Devolveremos un null  -> los errores no tiene que ser aqui, es en el IdentificationVisitor
	public Definition find(String id) {
		for(int i=table.size()-1; i>=0; i--){
			Map<String,Definition> map = table.get(i);
			if(map.containsKey(id)){
				return map.get(id);
			}
		}
		return null;
	}

	//package-protected for testing pourposes
	// es solo para que lo use el insert (y para test)
	boolean findInCurrentScope(String id) {
		Map<String, Definition> map = table.get(scope);
        return map.containsKey(id);
    }
}
