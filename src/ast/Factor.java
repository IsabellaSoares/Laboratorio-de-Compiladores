/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

abstract public class Factor extends SignalFactor {
	
	public Factor(int type) {
		super(type);
	}
	
	abstract public Type getType();
}