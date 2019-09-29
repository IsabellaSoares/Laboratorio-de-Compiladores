/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.Token;

public class BooleanExpr extends Factor {
	
	private Type type;
	private Token operator;
	private Factor factor;
	
	public BooleanExpr (Token operator, Factor factor) {
		this.operator = operator;
		this.factor = factor;
	}
	
	public void genJava(PW pw, boolean putParenthesis) {};
	
	public Type getType() {
		return this.type;
	};
}