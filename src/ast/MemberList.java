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
		
		if (qualifier != null) {
			if (qualifier.getToken1().toString().equals("override")) {
				pw.printIdent("@Override ");
			} else {			
				pw.printIdent(qualifier.getToken1() + " ");			
			}
		}	
		
		member.genJava(pw);
	}
}