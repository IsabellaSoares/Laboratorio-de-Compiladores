/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.*;

public class CompositeExpr extends Expr {
	private Expr left, right;
	private Token op;
	
	public CompositeExpr (Expr left, Token op, Expr right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	public Type getType() {
		return null;
	};
	
	public void genC( PW pw, boolean putParenthesis ) {};
}