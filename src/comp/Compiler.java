/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import ast.*;
import lexer.Lexer;
import lexer.Token;

public class Compiler {

	public Compiler() { }

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectAnnotation> metaobjectCallList = new ArrayList<>();
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		boolean thereWasAnError = false;
		while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT ) {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				classDec();
			}
			catch( CompilerError e) {
				// if there was an exception, there is a compilation error
				thereWasAnError = true;
				while ( lexer.token != Token.CLASS && lexer.token != Token.EOF ) {
					try {
						next();
					}
					catch ( RuntimeException ee ) {
						e.printStackTrace();
						return program;
					}
				}
			}
			catch ( RuntimeException e ) {
				e.printStackTrace();
				thereWasAnError = true;
			}

		}
		if ( !thereWasAnError && lexer.token != Token.EOF ) {
			try {
				error("End of file expected");
			}
			catch( CompilerError e) {
			}
		}
		return program;
	}

	/**  parses a metaobject annotation as <code>{@literal @}cep(...)</code> in <br>
     * <code>
     * {@literal @}cep(5, "'class' expected") <br>
     * class Program <br>
     *     func run { } <br>
     * end <br>
     * </code>
     *

	 */
	@SuppressWarnings("incomplete-switch")
	private void metaobjectAnnotation(ArrayList<MetaobjectAnnotation> metaobjectAnnotationList) {
		
		System.out.println("metaobjectAnnotation" + lexer.token + " " + lexer.getMetaobjectName());
		
		String name = lexer.getMetaobjectName();
		int lineNumber = lexer.getLineNumber();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		boolean getNextToken = false;
		if ( lexer.token == Token.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Token.LITERALINT || lexer.token == Token.LITERALSTRING ||
					lexer.token == Token.ID ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case ID:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Token.COMMA )
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
			else {
				getNextToken = true;
			}
		}
		switch ( name ) {
		case "nce":
			if ( metaobjectParamList.size() != 0 )
				error("Annotation 'nce' does not take parameters");
			break;
		case "cep":
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				error("Annotation 'cep' takes three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				error("The second and third parameters of annotation 'cep' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )
				error("The fourth parameter of annotation 'cep' should be a literal string");
			break;
		case "annot":
			if ( metaobjectParamList.size() < 2  ) {
				error("Annotation 'annot' takes at least two parameters");
			}
			for ( Object p : metaobjectParamList ) {
				if ( !(p instanceof String) ) {
					error("Annotation 'annot' takes only String parameters");
				}
			}
			if ( ! ((String ) metaobjectParamList.get(0)).equalsIgnoreCase("check") )  {
				error("Annotation 'annot' should have \"check\" as its first parameter");
			}
			break;
		default:
			error("Annotation '" + name + "' is illegal");
		}
		metaobjectAnnotationList.add(new MetaobjectAnnotation(name, metaobjectParamList));
		if ( getNextToken ) lexer.nextToken();
	}

	private void classDec() {
		
		System.out.println("classDec " + lexer.token + " " + lexer.getStringValue());
		
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			// open class
		}
		if ( lexer.token != Token.CLASS ) error("'class' expected");
		lexer.nextToken();
		
		System.out.println("classDec ID " + lexer.token + " " + lexer.getStringValue());
		
		if ( lexer.token != Token.ID )
			error("Identifier expected");
		String className = lexer.getStringValue();
		lexer.nextToken();
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID )
				error("Identifier expected");
			String superclassName = lexer.getStringValue();

			lexer.nextToken();
		}

		memberList();
		if ( lexer.token != Token.END)
			error("'end' expected");
		lexer.nextToken();

	}

	private void memberList() {
		
		System.out.println("memberList " + lexer.token);
		
		while ( true ) {
			qualifier();
			
			System.out.println("memberList Qualifier " + lexer.token);
			
			if ( lexer.token == Token.VAR ) {
				fieldDec();
			}
			else if ( lexer.token == Token.FUNC ) {
				methodDec();
			}
			else {
				break;
			}
		}
	}

	private void error(String msg) {
		this.signalError.showError(msg);
	}


	private void next() {
		lexer.nextToken();
	}

	private void check(Token shouldBe, String msg) {
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
	}

	private void methodDec() {
		
		System.out.println("methodDec " + lexer.token);
		
		lexer.nextToken();
		if ( lexer.token == Token.ID ) {
			// unary method
			
			System.out.println("methodDec ID " + lexer.token + " " + lexer.getStringValue());
			
			lexer.nextToken();

		}
		else if ( lexer.token == Token.IDCOLON ) {
			// keyword method. It has parameters
			//lexer.nextToken();
		}
		else {
			error("An identifier or identifer: was expected after 'func'");
		}
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			type();
		}
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		next();
		statementList();
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'{' expected");
		}
		next();

	}

	private void statementList() {
		
		System.out.println("statementList " + lexer.token);
		
		// only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
	}

	private void statement() {
		
		System.out.println("statement " + lexer.token + " " + lexer.getStringValue());
				
		boolean checkSemiColon = true;
		switch ( lexer.token ) {
		case IF:
			ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			returnStat();
			break;
		case BREAK:
			breakStat();
			break;
		case SEMICOLON:
			next();
			break;
		case REPEAT:
			repeatStat();
			break;
		case VAR:
			localDec();
			break;
		case ASSERT:
			assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				writeStat();
				
				if (lexer.token == Token.SEMICOLON) {
					checkSemiColon = false;
				}
				
				next();
			}
			else {
				expr();
			}

		}
		
		if ( checkSemiColon ) {
			check(Token.SEMICOLON, "';' expected");
		}
	}

	private void localDec() {
		next();
		type();
		check(Token.ID, "A variable name was expected");
		while ( lexer.token == Token.ID ) {
			next();
			if ( lexer.token == Token.COMMA ) {
				next();
			}
			else {
				break;
			}
		}
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			expr();
		}

	}

	private void repeatStat() {
		next();
		while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
		check(Token.UNTIL, "missing keyword 'until'");
	}

	private void breakStat() {
		next();

	}

	private void returnStat() {
		next();
		expr();
	}

	private void whileStat() {
		next();
		expr();
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
	}

	private void ifStat() {
		next();
		expr();
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				statement();
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
		}
	}

	/**

	 */
	private void writeStat() {
		System.out.println("writeStat");
		next();
		System.out.println("writeStat " + lexer.token);
		check(Token.DOT, "a '.' was expected after 'Out'");		
		next();
		System.out.println("writeStat " + lexer.token + " " + lexer.getStringValue());
		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		String printName = lexer.getStringValue();
		//expr();
		ArrayList<Expr> exprList = exprList();
		
		if (lexer.token == Token.SEMICOLON) {
			System.out.println("writeStat semicolon " + lexer.token + " " + lexer.getStringValue());
			//System.exit(1);
		}
	}
	
	private ArrayList<Expr> exprList() {
		ArrayList<Expr> exprList = new ArrayList<>();
		
		Expr e = null;
		next();
		e = expr();
		
		if (e == null) {
			this.error("Expression expected");
		}
		
		exprList.add(e);
		
		while (lexer.token == Token.COMMA) {
			System.out.println("exprList comma " + lexer.token);
			next();
			e = expr();
			
			if (e == null) {
				this.error("Expression expected");
			}
			
			exprList.add(e);
			System.out.println("exprList before add " + lexer.token);
		}
		
		
		return exprList;
	}

	private Expr expr() {
		System.out.println("expr " + lexer.token);
		Expr e = null;
		e = simpleExpr();
		//relation();
		//simpleExpr();
		
		return e;
	}
	
	private Expr simpleExpr() {
		System.out.println("simpleExpr " + lexer.token);
		Expr e = null;
		e = sumSubExpr();
		//++
		//sumSubExpr();
		
		return e;
	}
	
	private Expr sumSubExpr() {
		System.out.println("sumSubExpr " + lexer.token);
		Expr e = null;
		e = term();
		//lowOperator();
		//term();
		
		return e;
	}
	
	private Expr term() {
		System.out.println("term " + lexer.token);
		Expr e = null;
		e = signalFactor();
		//highOperator();
		
		return e;
	}
	
	private SignalFactor signalFactor() {
		System.out.println("signalFactor " + lexer.token);
		return factor();
		//signal(); +/-
		//Token op = null;
		//op = lexer.token if +/-
		//Factor factor = factor();
	}
	
	private Factor factor() {
		
		System.out.println("factor " + lexer.token);
		
		return basicValue();		
		
		//BasicValue |
		//“(” Expression “)” |
		//“!” Factor |
		//“nil” |
		//ObjectCreation |
		//PrimaryExpr
	}
	
	private BasicValue basicValue() {
		System.out.println("basicValue " + lexer.token);
		//next();
		
		if (lexer.token == Token.LITERALINT) {
			Integer value = lexer.getNumberValue();
			System.out.println("LITERALINT " + lexer.token + " " + value);
			next();
			return new BasicValue(value);
		} else if (lexer.token == Token.LITERALSTRING) {
			String value = lexer.getLiteralStringValue();
			System.out.println("LITERALSTRING " + lexer.token + " " + value);
			next();
			return new BasicValue(value);
		} else if (lexer.token == Token.TRUE) {
			boolean value = true;
			next();
			return new BasicValue(value);
		} else if (lexer.token == Token.FALSE) {
			boolean value = false;
			next();
			return new BasicValue(value);
		} else {
			this.error("Basic value expected");
			return null;
		}	
	}

	private void fieldDec() {
		lexer.nextToken();
		type();
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		}
		else {
			while ( lexer.token == Token.ID  ) {
				lexer.nextToken();
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
				}
				else {
					break;
				}
			}
		}

	}

	private void type() {
		if ( lexer.token == Token.INT || lexer.token == Token.BOOLEAN || lexer.token == Token.STRING ) {
			next();
		}
		else if ( lexer.token == Token.ID ) {
			next();
		}
		else {
			this.error("A type was expected");
		}

	}


	private void qualifier() {
		if ( lexer.token == Token.PRIVATE ) {
			next();
		}
		else if ( lexer.token == Token.PUBLIC ) {
			next();
		}
		else if ( lexer.token == Token.OVERRIDE ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
		}
		else if ( lexer.token == Token.FINAL ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
			else if ( lexer.token == Token.OVERRIDE ) {
				next();
				if ( lexer.token == Token.PUBLIC ) {
					next();
				}
			}
		}
	}
	/**
	 * change this method to 'private'.
	 * uncomment it
	 * implement the methods it calls
	 */
	public Statement assertStat() {

		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		expr();
		if ( lexer.token != Token.COMMA ) {
			this.error("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Token.LITERALSTRING ) {
			this.error("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		if ( lexer.token == Token.SEMICOLON )
			lexer.nextToken();

		return null;
	}




	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	private static boolean startExpr(Token token) {

		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NULL
				|| token == Token.ID || token == Token.LITERALSTRING;

	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	
	private void teste() {
		System.out.println("somente teste para commit");
	}

}
