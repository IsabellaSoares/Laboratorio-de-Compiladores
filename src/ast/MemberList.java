/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class MemberList {
	private Member member;
	private Qualifier qualifier;
	
	public MemberList (Qualifier qualifier, Member member) {
		this.qualifier = qualifier;
		this.member = member;
	}
}