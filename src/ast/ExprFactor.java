/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class ExprFactor extends Factor {
	private Expr expr;
	
	public ExprFactor (Expr expr) {
		this.expr = expr;
	}
	
	public Type getType() {
		return expr.getType();
	};
	
	public void genC( PW pw, boolean putParenthesis ) {};
}