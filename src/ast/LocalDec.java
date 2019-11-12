/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.ArrayList;

public class LocalDec extends Statement {
	private Type type;
	private ArrayList<Variable> idList = new ArrayList<>();
	private Expr expr;
	
	public LocalDec (Type type, ArrayList<Variable> idList, Expr expr) {
		this.type = type;
		this.idList  = idList;
		this.expr = expr;
	}
	
	public Type getType() {
		return type;
	}
	
	public void genJava(PW pw) {
		System.out.println(type);
		pw.printIdent(this.type.getName());
		
		if (idList != null) {
			for (int i = 0; i < idList.size(); i++) {
				if (i > 0)
					pw.print(",");
				
				pw.print(" " + idList.get(i).getName());
			}
			
			pw.println(";");
		}
	};
}