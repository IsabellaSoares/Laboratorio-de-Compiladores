/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.*;

public class CompositeExpr extends Expr {
	private Expr left, right;
	private Token op;
	
	public CompositeExpr (Expr left, Token op, Expr right, int type) {
		super(type);
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	public Type getType() {
		return null;
	};
	
	public void genJava( PW pw, boolean putParenthesis ) {
		if (putParenthesis) 
			pw.print("(");
		
		if (left != null) 
			left.genJava(pw, putParenthesis);
		
		if (op != null) 
			pw.print(" " + op.toString() + " ");
		
		if (right != null) 
			right.genJava(pw, putParenthesis);
		
		if (putParenthesis) 
			pw.print(")");
	};
}