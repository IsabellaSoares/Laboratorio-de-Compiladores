/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.ArrayList;

public class PrimarySuperExpr extends PrimaryExpr {
	private String id = null;
	private ArrayList<Expr> exprList = new ArrayList<>();
	
	public PrimarySuperExpr (String id, Type type) {
		super(type);
		this.id = id;
	}
	
	public PrimarySuperExpr (String id, ArrayList<Expr> exprList, Type type) {
		super(type);
		this.id = id;
		this.exprList = exprList;
	}
	
	/*
	 * public Type getType() { return null; };
	 */
	
	public void genJava( PW pw, boolean putParenthesis ) {
		if (id.substring(id.length() - 1).equals(":")) {
			id = id.substring(0, id.length() - 1);
	    }
		
		pw.printIdent("super." + id + "(");
		
		int indent = pw.get();
		pw.set(0);
		
		for (int i = 0; i < exprList.size(); i++) {
			if (i > 0) {
				pw.print(", ");
			}
			
			exprList.get(i).genJava(pw);
		}
		
		pw.set(indent);
		
		pw.print(")");
	};
}