/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class AssertStat extends Statement {
	private Expr expr = null;
	private String message;
	
	public AssertStat (Expr expr, String message) {
		this.expr = expr;
		this.message = message;
	}
	
	public void genJava( PW pw ) {
		pw.printIdent("assert ");
		
		int indent = pw.get();
		pw.set(0);
		
		expr.genJava(pw);
		
		pw.set(indent);
        pw.println(" : " + "\"" + message + "\";");
	};
}