/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class WriteStat extends Statement {
	private ArrayList<Expr> expr = new ArrayList<>();
	String printName;
	
	public WriteStat (ArrayList<Expr> expr, String printName) {
		this.expr = expr;
		this.printName = printName;
	}
	
	public void genJava( PW pw ) {
		if (printName.equals("println:")) {
			pw.printIdent("System.out.println(");
			
			int indent = pw.get();
			pw.set(0);
			
			if (expr != null) {
				for (int i = 0; i < expr.size(); i++) {
					if (i > 0) pw.print(" + ");
					expr.get(i).genJava(pw, false);
				}
			}
			
			pw.set(indent);
			
			pw.println(");");
		} else {
			pw.printIdent("System.out.print(");
			
			int indent = pw.get();
			pw.set(0);
			
			if (expr != null) {
				for (int i = 0; i < expr.size(); i++) {
					if (i > 0) pw.print(" + ");
					expr.get(i).genJava(pw, false);					
				}
			}
			
			pw.set(indent);
			
			pw.println(");");
		}		
	};
}