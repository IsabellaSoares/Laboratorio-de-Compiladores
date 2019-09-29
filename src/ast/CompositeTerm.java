/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.Token;

public class CompositeTerm extends Term {
	private Token operator;
	private Expr left, right;
	
	public CompositeTerm (Expr left, Token operator, Expr right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
	}
	
	public Type getType() {
		return Type.undefinedType;
	}

	@Override
	public void genJava(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
	}
}