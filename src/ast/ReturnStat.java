/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class ReturnStat extends Statement {
	private Expr expr = null;
	
	public ReturnStat (Expr expr) {
		this.expr = expr;
	}
	
	public void genJava( PW pw ) {};
}