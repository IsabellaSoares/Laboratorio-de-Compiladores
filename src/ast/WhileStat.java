/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class WhileStat extends Statement {
	private Expr expr = null;
	private ArrayList<Statement> statList = new ArrayList<>();
	
	public WhileStat (Expr expr, ArrayList<Statement> statList) {
		this.expr = expr;
		this.statList = statList;
	}
	
	public void genJava( PW pw ) {};
}