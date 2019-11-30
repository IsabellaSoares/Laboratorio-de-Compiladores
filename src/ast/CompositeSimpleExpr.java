/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.Token;

public class CompositeSimpleExpr extends SimpleExpr {
	private Token operator;
	private Expr left, right;
	
	public CompositeSimpleExpr (Expr left, Token operator, Expr right, Type type) {
		super(type);
		this.operator = operator;
		this.left = left;
		this.right = right;
	}
	
	/*
	 * public Type getType() { return Type.undefinedType; }
	 */

	@Override
	public void genJava(PW pw, boolean putParenthesis) {	
		
		left.genJava(pw, true);
		
		if (right != null)
			pw.print(" + ");
		
		right.genJava(pw, true);	
	}
}