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
	
	public void genJava( PW pw ) {
		if (left != null)
			left.genJava(pw);		
		
		if (right != null) {
			pw.print(" = ");
			
			int indent = pw.get();
			pw.set(0);
			
			right.genJava(pw);
			
			pw.set(indent);
		}			
		
		pw.println(";");
	};
}