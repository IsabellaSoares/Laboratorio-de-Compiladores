/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class MethodDec extends Member {
	private Variable method;
	private ArrayList<Statement> statList = new ArrayList<>();
	private ArrayList<Variable> paramList = new ArrayList<>();
	
	public MethodDec (Variable method, ArrayList<Statement> statList, ArrayList<Variable> paramList) {
		this.method = method;
		this.statList = statList;
		this.paramList = paramList;
	}
	
	public String getMethodName() {
		return method.getName();
	}
	
	public Type getType() {
		return method.getType();
	}
	
	public ArrayList<Variable> getParamList() {
		return paramList;
	}
	
	public void genJava( PW pw ) {
		
		String methodName = this.method.getName();
		
		if (methodName.substring(methodName.length() - 1).equals(":")) {
			methodName = methodName.substring(0, methodName.length() - 1);
	    }
		
		if (this.method.getType().getCname().equals("NULL")) {
			pw.print("void " + methodName);
		} else {			
			pw.print(this.method.getType().getName() + " " + methodName);
		}
		
		if (paramList != null) {
			pw.print(" (");
			
			for (int i = 0; i < paramList.size(); i++) {
				if (i > 0) {
					pw.print(", ");
				}
				
				paramList.get(i).genJava(pw);
			}
			
			pw.println(") {");
		} else {
			pw.println(" () {");
		}		
		
		if (statList != null) {
			pw.add();
			for (int i = 0; i < statList.size(); i++) {
				statList.get(i).genJava(pw);
			}
			pw.sub();
		}
		
		pw.printlnIdent("}");
	};
}