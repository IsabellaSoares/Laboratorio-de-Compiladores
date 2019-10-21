/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.Token;

public class CompositeSignalFactor extends SignalFactor {
	private Type type;
	private Token operator;
	private Factor factor;
	
	public CompositeSignalFactor (Token operator, Factor factor, int type) {
		super(type);
		this.operator = operator;
		this.factor = factor;
	}
	
	public Type getType() {
		return this.type;
	}

	@Override
	public void genJava(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
	};
}