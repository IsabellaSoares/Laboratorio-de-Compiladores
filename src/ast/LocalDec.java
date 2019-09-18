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
	
	public void genC(PW pw) {};
}