/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class ObjectCreation extends Factor {
	String id;
	
	public ObjectCreation (String id, Type type) {
		super(type);
		this.id = id;
	}

	/*
	 * @Override public Type getType() { // TODO Auto-generated method stub return
	 * null; }
	 */

	@Override
	public void genJava(PW pw, boolean putParenthesis) {
		pw.print("new " + id + "()");		
	}
	
}