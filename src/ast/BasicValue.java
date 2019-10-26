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
	
	public BasicValue (Integer value, Type type) {
		super(type);
		this.intValue = value;
		this.type = Type.intType;
	}
	
	public BasicValue (String value, Type type) {
		super(type);
		this.stringValue = value;
		this.type = Type.stringType;
	}
	
	public BasicValue (boolean value, Type type) {
		super(type);
		this.boolValue = value;
		this.type = Type.booleanType;
	}
	
	public void genJava(PW pw, boolean putParenthesis) {
		if (type == Type.booleanType) {
			pw.print(Boolean.toString(boolValue));
		} else if (type == Type.intType) {
			pw.print(Integer.toString(intValue));
		} else {
			pw.print("\"" + stringValue + "\"");
		}
	};
	
	/*
	 * public Type getType() { return this.type; };
	 */
}