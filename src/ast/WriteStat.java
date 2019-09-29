/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class WriteStat extends Statement {
	private ArrayList<Expr> expr = new ArrayList<>();
	String printName;
	
	public WriteStat (ArrayList<Expr> expr, String printName) {
		this.expr = expr;
		this.printName = printName;
	}
	
	public void genJava( PW pw ) {};
}