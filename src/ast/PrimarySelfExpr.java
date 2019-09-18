/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.ArrayList;

public class PrimarySelfExpr extends PrimaryExpr {
	private String id1 = "self", id2 = null;
	private ArrayList<Expr> exprList = new ArrayList<>();
	
	public PrimarySelfExpr (String id) {
		this.id1 = id;
	}
	
	public PrimarySelfExpr (String id, ArrayList<Expr> exprList) {
		this.id1 = id;
		this.exprList = exprList;
	}
	
	public PrimarySelfExpr (String id1, String id2) {
		this.id1 = id1;
		this.id2 = id2;
	}
	
	public PrimarySelfExpr (String id1, String id2, ArrayList<Expr> exprList) {
		this.id1 = id1;
		this.id2 = id2;
		this.exprList = exprList;
	}
	
	public Type getType() {
		return null;
	};
	
	public void genC( PW pw, boolean putParenthesis ) {};
	
}