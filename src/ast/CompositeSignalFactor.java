/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import lexer.Token;

public class CompositeSignalFactor extends SignalFactor {
	private Token operator;
	private Factor factor;
	
	public CompositeSignalFactor (Token operator, Factor factor, Type type) {
		super(type);
		this.operator = operator;
		this.factor = factor;
	}
	
	/*
	 * public Type getType() { return this.type; }
	 */

	@Override
	public void genJava(PW pw, boolean putParenthesis) {
		if (operator != null) 
			pw.print(operator.toString());	
		
		factor.genJava(pw, putParenthesis);
	};
}