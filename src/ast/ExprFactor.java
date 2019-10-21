/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class ExprFactor extends Factor {
	private Expr expr;
	
	public ExprFactor (Expr expr, int type) {
		super(type);
		this.expr = expr;
	}
	
	public Type getType() {
		return expr.getType();
	};
	
	public void genJava( PW pw, boolean putParenthesis ) {};
}