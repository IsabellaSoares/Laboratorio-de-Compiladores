/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class WhileStat extends Statement {
	private ArrayList<Expr> expr = new ArrayList<>();
	private ArrayList<Statement> statList = new ArrayList<>();
	
	public WhileStat (ArrayList<Expr> expr, ArrayList<Statement> statList) {
		this.expr = expr;
		this.statList = statList;
	}
	
	public void genJava( PW pw ) {};
}