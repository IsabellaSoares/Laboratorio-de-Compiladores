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
		this.type = new TypeInt();
	}
	
	public BasicValue (String value, Type type) {
		super(type);
		this.stringValue = value;
		this.type = new TypeString();
	}
	
	public BasicValue (boolean value, Type type) {
		super(type);
		this.boolValue = value;
		this.type = new TypeBoolean();
	}
	
	public void genJava(PW pw, boolean putParenthesis) {
		if (type == new TypeBoolean()) {
			pw.print(Boolean.toString(boolValue));
		} else if (type == new TypeInt()) {
			pw.print(Integer.toString(intValue));
		} else {
			pw.print("\"" + stringValue + "\"");
		}
	};
	
	/*
	 * public Type getType() { return this.type; };
	 */
}