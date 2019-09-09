/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class BasicValue extends Factor {
	
	private Type type;
	private Integer intValue;
	private String stringValue;
	private boolean boolValue;
	
	public BasicValue (Integer value) {
		this.intValue = value;
		this.type = Type.intType;
	}
	
	public BasicValue (String value) {
		this.stringValue = value;
		this.type = Type.stringType;
	}
	
	public BasicValue (boolean value) {
		this.boolValue = value;
		this.type = Type.booleanType;
	}
	
	public void genC(PW pw, boolean putParenthesis) {};
	
	public Type getType() {
		return this.type;
	};
}