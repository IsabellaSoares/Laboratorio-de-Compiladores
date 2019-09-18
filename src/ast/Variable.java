/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class Variable extends Member {
	private Type type;
	private String name;
	
	public Variable (Type type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public Type getType () {
		return this.type;
	}
}