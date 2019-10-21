/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public abstract class SumSubExpr extends SimpleExpr {
	
	public SumSubExpr(int type) {
		super(type);
	}
	
	public abstract Type getType();
}