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
	
	public void genJava( PW pw, boolean putParenthesis ) {};
}