/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.Token;

public class BooleanExpr extends Factor {
	
	private Token operator;
	private Factor factor;
	
	public BooleanExpr (Token operator, Factor factor, Type type) {
		super(type);
		this.operator = operator;
		this.factor = factor;
	}
	
	public void genJava(PW pw, boolean putParenthesis) {
		if (putParenthesis)
			pw.print("(");
		
		pw.print(operator.toString());
		
		int indent = pw.get();
		pw.set(0);
		
		factor.genJava(pw, putParenthesis);
		
		pw.set(indent);
		
		if (putParenthesis)
			pw.print(")");
	};
	
	/*
	 * public Type getType() { return this.type; };
	 */
}