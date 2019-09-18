/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.ArrayList;

public class ReadExpr extends PrimaryExpr {
	public Type getType() {
		return null;
	};
	
	public void genC( PW pw, boolean putParenthesis ) {};
}