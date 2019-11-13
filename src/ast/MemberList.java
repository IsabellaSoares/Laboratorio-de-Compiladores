/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import lexer.Token;

public class MemberList {
	private Member member;
	private Qualifier qualifier;
	
	public MemberList (Qualifier qualifier, Member member) {
		this.qualifier = qualifier;
		this.member = member;
	}
	
	public Token getQualifier () {
		return this.qualifier.getToken1();
	}
	
	public Member getMember () {
		return this.member;
	}
	
	public void genJava(PW pw) {	
		
		if (qualifier.getToken1() != null) {
			if (qualifier.getToken1().toString().equals("override")) {
				pw.printIdent("@Override ");				
			} else {			
				pw.printIdent(qualifier.getToken1() + " ");			
			}
		}
		
		if (qualifier.getToken2() != null)
			pw.print(qualifier.getToken2() + " ");
		
		if (qualifier.getToken3() != null)
			pw.print(qualifier.getToken3() + " ");
		
		if (qualifier.getToken4() != null)
			pw.print(qualifier.getToken4() + " ");
		
		member.genJava(pw);
	}
}