/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

abstract public class Expr extends Statement {
    abstract public void genJava( PW pw, boolean putParenthesis );
	@Override
	public void genJava(PW pw) {
		this.genJava(pw, false);
	}

      // new method: the type of the expression
    abstract public Type getType();
}