/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.ArrayList;

public class PrimarySimpleExpr extends PrimaryExpr {
	private String id1 = null, id2 = null;
	private ArrayList<Expr> exprList = new ArrayList<>();
	
	public PrimarySimpleExpr (String id1, Type type) {
		super(type);
		this.id1 = id1;
	}
	
	public PrimarySimpleExpr (String id1, String id2, Type type) {
		super(type);
		this.id1 = id1;
		this.id2 = id2;
	}
	
	public PrimarySimpleExpr (String id1, String id2, ArrayList<Expr> exprList, Type type) {
		super(type);
		this.id1 = id1;
		this.id2 = id2;
		this.exprList = exprList;
	}
	
	/*
	 * public Type getType() { return null; };
	 */
	
	public void genJava( PW pw, boolean putParenthesis ) {
		if (id1 != null) {
			if (putParenthesis) {
				pw.print(id1);
			} else {
				pw.printIdent(id1);
			}
		}			
		
		if (id2 != null) {
			if (id2.substring(id2.length() - 1).equals(":")) {
				id2 = id2.substring(0, id2.length() - 1);
		    }
			
			pw.print("." + id2 + "(");
			
			int indent = pw.get();
			pw.set(0);
			
			if (exprList != null) {
				for (int i = 0; i < exprList.size(); i++) {
					if (i > 0) {
						pw.print(", ");
					}
					
					exprList.get(i).genJava(pw);
				}
			}
			
			pw.set(indent);
			
			pw.print(")");
		}
			
	};
}