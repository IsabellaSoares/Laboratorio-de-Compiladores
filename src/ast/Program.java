/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<TypeCianetoClass> classList, ArrayList<MetaobjectAnnotation> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}


	public void genJava(PW pw) {
		pw.set(0);
		pw.println("/*");
		pw.println("Isabella Soares de Lima             726541");
		pw.println("Marcelo Augusto Rodrigues da Silva  726565");
		pw.println("*/");
		pw.println();		
		
		pw.println("public class " + this.mainJavaClassName + " {");
		pw.add();
		pw.printlnIdent("public static void main(String []args) {");
		pw.add();
		pw.printlnIdent("new Program().run();");
		pw.sub();
		pw.printlnIdent("}");
		pw.println();
		
		if (this.classList != null) {
			for (int i = 0; i < classList.size(); i++) {
				pw.printlnIdent("private static class " + classList.get(i).getName() + " {");
				
				if (classList.get(i).getMemberList() != null) {
					pw.add();
					pw.printIdent(classList.get(i).getMemberList().getQualifier().toString() + " ");
					classList.get(i).getMemberList().getMember().genJava(pw);
					pw.println("() {");					
					pw.printlnIdent("}");										
					pw.sub();
				}
				
				pw.printlnIdent("}");
				pw.println();			
			}
		}
		
		pw.println("}");	
	}

	public void genC(PW pw) {
	}
	
	public ArrayList<TypeCianetoClass> getClassList() {
		return classList;
	}


	public ArrayList<MetaobjectAnnotation> getMetaobjectCallList() {
		return metaobjectCallList;
	}
	

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}
	
	public void setMainJavaClassName(String mainJavaClassName) {
		this.mainJavaClassName = mainJavaClassName;
	}

	/**
	the name of the main Java class when the
	code is generated to Java. This name is equal
	to the file name (without extension)
	*/
	private String mainJavaClassName;
	
	private ArrayList<TypeCianetoClass> classList;
	private ArrayList<MetaobjectAnnotation> metaobjectCallList;
	
	ArrayList<CompilationError> compilationErrorList;

	
}