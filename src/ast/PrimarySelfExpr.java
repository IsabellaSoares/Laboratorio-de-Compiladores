/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.ArrayList;

public class PrimarySelfExpr extends PrimaryExpr {
	private String id1 = "self", id2 = null;
	private ArrayList<Expr> exprList = new ArrayList<>();
	
	public PrimarySelfExpr (String id, Type type) {
		super(type);
		this.id1 = id;
		this.exprList = null;
	}
	
	public PrimarySelfExpr (String id, ArrayList<Expr> exprList, Type type) {
		super(type);
		this.id1 = id;
		this.exprList = exprList;
	}
	
	public PrimarySelfExpr (String id1, String id2, Type type) {
		super(type);
		this.id1 = id1;
		this.id2 = id2;
		this.exprList = null;
	}
	
	public PrimarySelfExpr (String id1, String id2, ArrayList<Expr> exprList, Type type) {
		super(type);
		this.id1 = id1;
		this.id2 = id2;
		this.exprList = exprList;
	}
	
	/*
	 * public Type getType() { return null; };
	 */
	
	public void genJava( PW pw, boolean putParenthesis ) {		
		
		putParenthesis = false;
		
		if (exprList != null) {
			if (id1.substring(id1.length() - 1).equals(":")) {
				id1 = id1.substring(0, id1.length() - 1);
		    }
			
			if (id1.equals("self")) {
				pw.printIdent("this(");
			} else {
				pw.printIdent("this." + id1 + "(");
			}
			
			
			putParenthesis = true;
		} else {
			if (id1.equals("self")) {
				pw.printIdent("this");
			} else {
				pw.printIdent("this" + id1);
			}			
		}		
		
		if (exprList != null) {
			for (int i = 0; i < exprList.size(); i++) {
				exprList.get(i).genJava(pw);
			}
		}
		
		if (putParenthesis) {
			pw.print(")");
		}
	}
	
}