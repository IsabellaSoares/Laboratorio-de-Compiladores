/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class ReadExpr extends PrimaryExpr {
	private Type type;
	
	public ReadExpr (Type type) {
		super(type);
		this.type = type;
	}
	
	/*
	 * public Type getType() { return this.type; };
	 */
	
	public void genJava( PW pw, boolean putParenthesis ) {
		if (type.getCname().equals("int"))
			pw.print("input.nextInt()");
		else
			pw.print("input.nextLine()");
	};
}