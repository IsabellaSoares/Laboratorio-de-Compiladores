/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class AssignExpr extends Statement {
	private Expr left, right;
	
	public AssignExpr (Expr left, Expr right) {
		this.left = left;
		this.right = right;
	}
	
	public void genC( PW pw ) {};
}