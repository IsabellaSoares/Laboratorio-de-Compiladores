/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class NullExpr extends Factor {
	
	public NullExpr(Type type) {
		super(type);
	}
	
   public void genJava( PW pw, boolean putParenthesis ) {
      pw.print("null");
   }
   
	/*
	 * public Type getType() { //# corrija return null; }
	 */
}