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
	
	public BasicValue (Integer value, int type) {
		super(type);
		this.intValue = value;
		this.type = Type.intType;
	}
	
	public BasicValue (String value, int type) {
		super(type);
		this.stringValue = value;
		this.type = Type.stringType;
	}
	
	public BasicValue (boolean value, int type) {
		super(type);
		this.boolValue = value;
		this.type = Type.booleanType;
	}
	
	public void genJava(PW pw, boolean putParenthesis) {};
	
	public Type getType() {
		return this.type;
	};
}