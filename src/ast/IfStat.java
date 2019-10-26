/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class IfStat extends Statement {
	private Expr expr;
	private ArrayList<Statement> ifState;
	private ArrayList<Statement> elseState;
	
	public IfStat (Expr expr, ArrayList<Statement> ifState, ArrayList<Statement> elseState) {
		this.expr = expr;
		this.ifState = ifState;
		this.elseState = elseState;
	}
	
	public void genJava( PW pw ) {
		if (ifState != null) {
			pw.println();
			pw.printIdent("if ");
			
			if (expr != null)
				expr.genJava(pw, true);
			
			pw.println(" {");
			
			pw.add();
			
			for (var i = 0; i < ifState.size(); i++) {
				ifState.get(i).genJava(pw);
			}
			
			pw.sub();
			
			pw.printlnIdent("}");
		}
		
		if (elseState != null) {
			
		}
	};
}