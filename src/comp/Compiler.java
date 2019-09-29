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
				/*while ( lexer.token != Token.CLASS && lexer.token != Token.EOF ) {
					try {
						next();
					}
					catch ( RuntimeException ee ) {
						e.printStackTrace();
						return program;
					}
				}*/
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
		
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			// open class
		}
		
		if ( lexer.token != Token.CLASS ) error("'class' expected");
		
		lexer.nextToken();
		
		if ( lexer.token != Token.ID ) error("Identifier expected");
		
		String className = lexer.getStringValue();
		lexer.nextToken();
		
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			
			if ( lexer.token != Token.ID ) error("Identifier expected");
			
			String superclassName = lexer.getStringValue();
			lexer.nextToken();
		}

		MemberList memberList = memberList();
		
		if ( lexer.token != Token.END) error("'end' expected");
		
		lexer.nextToken();
	}

	private MemberList memberList() {
		
		Qualifier qualifier = null;
		MethodDec methodDec = null;
		
		while ( true ) {
			if (lexer.token != Token.END) {
				qualifier = qualifier();
			}
			
			if ( lexer.token == Token.VAR ) {
				fieldDec();
			} else if ( lexer.token == Token.FUNC ) {
				methodDec = methodDec();
			} else {
				break;
			}
		}
		
		return new MemberList(qualifier, methodDec);
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

	private MethodDec methodDec() {
		
		lexer.nextToken();
		
		String id = null;
		Type type = Type.nullType;
		
		if ( lexer.token == Token.ID ) {
			// unary method
			
			id = lexer.getStringValue();
			lexer.nextToken();
		} else if ( lexer.token == Token.IDCOLON ) {
			// keyword method. It has parameters
			//lexer.nextToken();
		} else {
			error("An identifier or identifer: was expected after 'func'");
		}
		
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			type = type();
		}
		
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		
		next();
		
		ArrayList<Statement> statList = new ArrayList<>();		
		//ArrayList<Variable> paramList = new ArrayList<>();
		
		statList = statementList();
		
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}
		
		next();
		
		return new MethodDec(id, type, statList);
	}

	private ArrayList<Statement> statementList() {
		
		ArrayList<Statement> statList = new ArrayList<>();
		
		// only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			Statement e = statement();
			statList.add(e);
		}
		
		return statList;
	}

	private Statement statement() {
		
		if (lexer.token == Token.SEMICOLON) {
			next();
		}
				
		boolean checkSemiColon = true;
		
		Statement e = null;
		
		switch ( lexer.token ) {
		case IF:
			e = ifStat();
			checkSemiColon = false;
			next();
			break;
		case WHILE:
			e = whileStat();
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
			e = localDec();	
			
			if (lexer.token == Token.SEMICOLON) {
				checkSemiColon = false;
			}
			
			next();
			
			break;
		case ASSERT:
			assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				e = writeStat();
				
				if (lexer.token == Token.SEMICOLON) {
					checkSemiColon = false;
				}
				
				next();				
			} else {
				e = assignExpr();
				
				if (lexer.token == Token.SEMICOLON) {
					checkSemiColon = false;
				}
				
				next();
			}
		}
		
		if ( checkSemiColon ) {
			check(Token.SEMICOLON, "';' expected");
		}
		
		return e;
	}

	private LocalDec localDec() {
		next();
		
		Type type = Type.nullType;
		type = type();
		
		check(Token.ID, "A variable name was expected");
		
		ArrayList<Variable> idList = new ArrayList<>();
		Expr expr = null;
		
		while ( lexer.token == Token.ID ) {
			Variable var = new Variable(type, lexer.getStringValue());
			idList.add(var);
			
			next();
			
			if ( lexer.token == Token.COMMA ) {
				next();
			} else {
				break;
			}
		}
		
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			expr = expr();
		}
		
		return new LocalDec(type, idList, expr);
	}

	private void repeatStat() {
		next();
		while ( lexer.token != Token.UNTIL &&
				lexer.token != Token.RIGHTCURBRACKET &&
				lexer.token != Token.END ) {
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

	private WhileStat whileStat() {
		next();
		
		Expr e = expr();
		
		next();
		
		//ArrayList<Statement> statList = statList();
		
		ArrayList<Statement> statList = new ArrayList<>();		
		statList = statementList();
		
		/*while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			Statement stat = statement();
			statList.add(stat);
		}
		
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}*/		
		
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		next();
		
		return new WhileStat(e, statList);
	}

	private IfStat ifStat() {
		next();
		
		Expr expr = expr();
		
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		
		next();
		
		ArrayList<Statement> ifState = new ArrayList<>();
		ArrayList<Statement> elseState = new ArrayList<>();
		
		while ( lexer.token != Token.RIGHTCURBRACKET &&
				lexer.token != Token.END && lexer.token != Token.ELSE ) {
			Statement e = statement();
			ifState.add(e);
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();		
			
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				Statement e = statement();
				elseState.add(e);
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
		}
		
		return new IfStat(expr, ifState, elseState);
	}

	/**

	 */
	private WriteStat writeStat() {
		next();
		check(Token.DOT, "a '.' was expected after 'Out'");		
		next();
		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		
		String printName = lexer.getStringValue();
		ArrayList<Expr> exprList = exprList();
		
		if (lexer.token != Token.SEMICOLON) {
			this.error("';' expected");
		}
		
		return new WriteStat(exprList, printName);
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
			next();
			e = expr();
			
			if (e == null) {
				this.error("Expression expected");
			}
			
			exprList.add(e);
		}
		
		
		return exprList;
	}

	private Expr expr() {
		Expr left = null;
		left = simpleExpr();
		
		while (relation(lexer.token)) {	
			Token op = lexer.token;
			next();
			
			Expr right = null;
			right = simpleExpr();
			
			left = new CompositeExpr(left, op, right);
		}
		
		return left;
	}
	
	private Expr simpleExpr() {
		Expr e = null;
		e = sumSubExpr();
		//++
		//sumSubExpr();
		
		return e;
	}
	
	private Expr sumSubExpr() {
		Expr left = null;
		left = term();
		
		if (lexer.token == Token.PLUS || 
			lexer.token == Token.MINUS || 
			lexer.token == Token.OR) {
			Token operator = lexer.token;
			Expr right = null;
			right = term();
			
			left = new CompositeSumSubExpr(left, operator, right);
		}
		
		return left;
	}
	
	private Expr term() {
		Expr left = null;
		left = signalFactor();
		
		if (lexer.token == Token.MULT || 
			lexer.token == Token.DIV || 
			lexer.token == Token.AND) {
			Token operator = lexer.token;
			next();
			Expr right = null;
			right = signalFactor();
			
			left = new CompositeTerm(left, operator, right);
		}		
		
		return left;
	}
	
	private SignalFactor signalFactor() {
		Token operator = null;
		
		if (lexer.token == Token.PLUS || 
			lexer.token == Token.MINUS || 
			lexer.token == Token.OR) {
			operator = lexer.token;
			next();
		}
		
		Factor factor = factor();
		
		return new CompositeSignalFactor(operator, factor);
	}
	
	private Factor factor() {
		if (lexer.token == Token.LEFTPAR) {
			next();
			
			Expr e = null;
			e = expr();
			
			if (lexer.token != Token.RIGHTPAR) {
				this.error("')' expected");
			} else {
				
			}
			
			if (e == null) {
				return null;
			}
			
			next();
			return new ExprFactor(e);
		} else if (lexer.token == Token.ID || 
					lexer.token == Token.SUPER || 
					lexer.token == Token.SELF || 
					lexer.token == Token.READ) {
			return primaryExpr();
		} else if (lexer.token == Token.NOT) {
			next();
			Factor factor = factor();
			return new BooleanExpr(Token.NOT, factor);
		} else {
			return basicValue();
		}
	}
	
	private BasicValue basicValue() {
		if (lexer.token == Token.LITERALINT) {
			Integer value = lexer.getNumberValue();
			next();
			return new BasicValue(value);
		} else if (lexer.token == Token.LITERALSTRING) {
			String value = lexer.getLiteralStringValue();
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
			System.out.println(lexer.token + " " + lexer.getStringValue());
			this.error("Basic value expected");
			return null;
		}	
	}
	
	private AssignExpr assignExpr () {
		Expr left = null, right = null;
		left = expr();
		
		if (lexer.token == Token.ASSIGN) {
			next();			
			right = expr();
		}
		
		return new AssignExpr(left, right);
	}
	
	private Factor primaryExpr() {		
		if (lexer.token == Token.ID) {			
			String id = lexer.getStringValue();
			next();
			
			if (lexer.token == Token.DOT ) {
				next();			
				
				if (lexer.token == Token.ID) {
					String id2 = lexer.getStringValue();
					next();
					return new PrimarySimpleExpr(id, id2);
				} else if (lexer.token == Token.IDCOLON) {
					String id2 = lexer.getStringValue();
					next();
				} else if (lexer.token == Token.NEW) {
					next();
					return new ObjectCreation(id);
				} else {
					this.error("Id or IdColon expected");
				}
			} else {
				return new PrimarySimpleExpr(id);
			}
			
		} else if (lexer.token == Token.SUPER) {
			
		} else if (lexer.token == Token.SELF) {
			
		} else {
			// return readExpr();
		}
		
		return null;
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

	private Type type() {
		Type type = Type.nullType;
		
		if ( lexer.token == Token.INT ) {
			type = Type.intType;
			next();
		}
		else if ( lexer.token == Token.BOOLEAN ) {
			type = Type.booleanType;
			next();
		}
		else if ( lexer.token == Token.STRING ) {
			type = Type.stringType;
			next();
		}
		else if ( lexer.token == Token.ID ) {
			//type = Type.nullType;
			next();
		}
		else {
			this.error("A type was expected");
		}
		
		return type;

	}


	private Qualifier qualifier() {		
		Token q1 = lexer.token, q2 = null, q3 = null, q4 = null;
		
		if (q1 == Token.FUNC) {			
			q1 = Token.PUBLIC;
			return new Qualifier(q1, q2, q3, q4);
		}
		
		next();
		
		if ( lexer.token == Token.PRIVATE ) {
			q2 = lexer.token;
			next();
		} else if ( lexer.token == Token.PUBLIC ) {
			q2 = lexer.token;
			next();
		} else if ( lexer.token == Token.OVERRIDE ) {
			q2 = lexer.token;
			next();
			
			if ( lexer.token == Token.PUBLIC ) {
				q3 = lexer.token;
				next();
			}
		} else if ( lexer.token == Token.FINAL ) {
			q2 = lexer.token;
			next();
			
			if ( lexer.token == Token.PUBLIC ) {
				q3 = lexer.token;
				next();
			} else if ( lexer.token == Token.OVERRIDE ) {
				q3 = lexer.token;
				next();
				
				if ( lexer.token == Token.PUBLIC ) {
					q4 = lexer.token;
					next();
				}
			}
		}
		
		return new Qualifier(q1, q2, q3, q4);
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
	
	private static boolean relation (Token token) {
		return token == Token.EQ || token == Token.LT || token == Token.GT
				|| token == Token.LE || token == Token.GE || token == Token.NEQ;
	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	
	private void teste() {
		System.out.println("somente teste para commit");
	}

}
