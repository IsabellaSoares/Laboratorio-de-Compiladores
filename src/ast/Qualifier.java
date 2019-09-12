/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.Token;

public class Qualifier {
	private Token q1, q2, q3, q4;
	
	public Qualifier (Token q1, Token q2, Token q3, Token q4) {
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
	}
	
	public Token getToken1 () {
		return this.q1;
	}
	
	public Token getToken2 () {
		return this.q2;
	}
	
	public Token getToken3 () {
		return this.q3;
	}
	
	public Token getToken4 () {
		return this.q4;
	}
}