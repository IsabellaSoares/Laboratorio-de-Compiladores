/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package lexer;

import java.util.Hashtable;
import comp.ErrorSignaller;

public class Lexer {

    public Lexer( char []input, ErrorSignaller error ) {
        this.input = input;
          // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
          // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        lastTokenPos = 0;
        beforeLastTokenPos = 0;
        this.error = error;
        metaobjectName = "";
      }


    private static final int MaxValueInteger = 32767;
      // contains the keywords
    static private Hashtable<String, Token> keywordsTable;

     // this code will be executed only once for each program execution
	static {
		keywordsTable = new Hashtable<String, Token>();

		for ( Token s : Token.values() ) {
			String kw = s.toString();
			if ( Character.isAlphabetic(kw.charAt(0)) )
				keywordsTable.put( s.toString(), s);
		}


	}




    public void nextToken() {
        char ch;

        lastTokenPos = tokenPos;
        while (  (ch = input[tokenPos]) == ' ' || ch == '\r' ||
                 ch == '\t' || ch == '\n')  {
            // count the number of lines
          if ( ch == '\n')
            lineNumber++;
          tokenPos++;
          }
        if ( ch == '\0')
          token = Token.EOF;
        else
          if ( input[tokenPos] == '/' && input[tokenPos + 1] == '/' ) {
                // comment found
               while ( input[tokenPos] != '\0'&& input[tokenPos] != '\n' )
                 tokenPos++;
               nextToken();
          }
          else if ( input[tokenPos] == '/' && input[tokenPos + 1] == '*' ) {
             int posStartComment = tokenPos;
             int lineNumberStartComment = lineNumber;
             tokenPos += 2;
             while ( (ch = input[tokenPos]) != '\0' &&
                 (ch != '*' || input[tokenPos + 1] != '/') ) {
                if ( ch == '\n' )
                   lineNumber++;
                tokenPos++;
             }
             if ( ch == '\0' )
                error.showError( "Comment opened and not closed",
                      getLine(posStartComment), lineNumberStartComment);
             else
                tokenPos += 2;
             nextToken();
          }
          else {
            if ( Character.isLetter( ch ) ) {
                // get an identifier or keyword
                StringBuffer ident = new StringBuffer();
                while ( Character.isLetter( ch = input[tokenPos] ) ||
                        Character.isDigit(ch) ||
                        ch == '_' ) {
                    ident.append(input[tokenPos]);
                    tokenPos++;
                }
                if ( input[tokenPos] == ':' ) {
                    ident.append(input[tokenPos]);
                    tokenPos++;
                	stringValue = ident.toString();
                	token = Token.IDCOLON;
                }
                else {
                	stringValue = ident.toString();
                    // if identStr is in the list of keywords, it is a keyword !
                	Token value = keywordsTable.get(stringValue);
                	if ( value == null )
                		token = Token.ID;
                	else
                		token = value;
                }
            }
            else if ( Character.isDigit( ch ) ) {
                // get a number
                StringBuffer number = new StringBuffer();
                while ( Character.isDigit( input[tokenPos] ) ) {
                    number.append(input[tokenPos]);
                    tokenPos++;
                }
                token = Token.LITERALINT;
                try {
                   numberValue = Integer.valueOf(number.toString()).intValue();
                } catch ( NumberFormatException e ) {
                   error.showError("Number out of limits");
                }
                if ( numberValue > MaxValueInteger )
                   error.showError("Number out of limits");
            }
            else {
                tokenPos++;
                switch ( ch ) {
                    case '+' :
                      token = Token.PLUS;
                      break;
                    case '-' :
                      if ( input[tokenPos] == '>' ) {
                          tokenPos++;
                          token = Token.MINUS_GT;
                      }
                      else {
                          token = Token.MINUS;
                      }
                      break;
                    case '*' :
                      token = Token.MULT;
                      break;
                    case '/' :
                      token = Token.DIV;
                      break;
                    case '<' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Token.LE;
                      }
                      else
                        token = Token.LT;
                      break;
                    case '>' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Token.GE;
                      }
                      else
                        token = Token.GT;
                      break;
                    case '=' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Token.EQ;
                      }
                      else
                        token = Token.ASSIGN;
                      break;
                    case '!' :
                      if ( input[tokenPos] == '=' ) {
                         tokenPos++;
                         token = Token.NEQ;
                      }
                      else
                         token = Token.NOT;
                      break;
                    case '(' :
                      token = Token.LEFTPAR;
                      break;
                    case ')' :
                      token = Token.RIGHTPAR;
                      break;
                    case ',' :
                      token = Token.COMMA;
                      break;
                    case ';' :
                      token = Token.SEMICOLON;
                      break;
                    case '.' :
                      token = Token.DOT;
                      break;
                    case '&' :
                      if ( input[tokenPos] == '&' ) {
                         tokenPos++;
                         token = Token.AND;
                      }
                      else
                        error.showError("& expected");
                      break;
                    case '|' :
                      if ( input[tokenPos] == '|' ) {
                         tokenPos++;
                         token = Token.OR;
                      }
                      else
                        error.showError("| expected");
                      break;
                    case '{' :
                      token = Token.LEFTCURBRACKET;
                      break;
                    case '}' :
                      token = Token.RIGHTCURBRACKET;
                      break;
                    case '@' :
                    	metaobjectName = "";
                    	while ( Character.isAlphabetic(input[tokenPos]) ) {
                    		metaobjectName += input[tokenPos];
                    		++tokenPos;
                    	}
                    	if ( metaobjectName.length() == 0 )
                    		error.showError("Identifier expected after '@'");
                    	token = Token.ANNOT;
                    	break;
                    case '_' :
                      error.showError("'_' cannot start an indentifier");
                      break;
                    case '"' :
                       StringBuffer s = new StringBuffer();
                       while ( input[tokenPos] != '\0' && input[tokenPos] != '\n' )
                          if ( input[tokenPos] == '"' )
                             break;
                          else
                             if ( input[tokenPos] == '\\' ) {
                                if ( input[tokenPos+1] != '\n' && input[tokenPos+1] != '\0' ) {
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                }
                                else {
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                }
                             }
                             else {
                                s.append(input[tokenPos]);
                                tokenPos++;
                             }

                       if ( input[tokenPos] == '\0' || input[tokenPos] == '\n' ) {
                          error.showError("Nonterminated string");
                          literalStringValue = "";
                       }
                       else {
                          tokenPos++;
                          literalStringValue = s.toString();
                       }
                       token = Token.LITERALSTRING;
                       break;
                    default :
                      error.showError("Invalid Character: '" + ch + "'", false);
                }
            }
          }
        beforeLastTokenPos = lastTokenPos;
    }

      // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineNumberBeforeLastToken() {
        return getLineNumber( lastTokenPos );
    }

    private int getLineNumber( int index ) {
        // return the line number in which the character input[index] is
        int i, n, size;
        n = 1;
        i = 0;
        size = input.length;
        while ( i < size && i < index ) {
          if ( input[i] == '\n' )
            n++;
          i++;
        }
        return n;
    }


    public String getCurrentLine() {
        //return getLine(lastTokenPos);
        return getLine(tokenPos);
    }

    public String getLineBeforeLastToken() {
        return getLine(beforeLastTokenPos);
    }

    private String getLine( int index ) {
        // get the line that contains input[index]. Assume input[index] is at a token, not
        // a white space or newline

        int i;
        if ( input.length <= 1 )
           return "";
        i = index;
        if ( i <= 0 )
          i = 1;
        else
          if ( i >= input.length )
            i = input.length;

        while ( input[i] == '\n' || input[i] == '\r' )
           i--;

        StringBuffer line = new StringBuffer();
          // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
          i--;
        if ( input[i] == '\n' )
          i++;
          // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
       return stringValue;
    }

    public int getNumberValue() {
       return numberValue;
    }

    public String getLiteralStringValue() {
       return literalStringValue;
    }

	public String getMetaobjectName() {
		return metaobjectName;
	}

    private String metaobjectName;
          // current token
    public Token token;
    private String stringValue, literalStringValue;
    private int numberValue;

    private int  tokenPos;
      //  input[lastTokenPos] is the last character of the last token found
    private int lastTokenPos;
      //  input[beforeLastTokenPos] is the last character of the token before the last
      // token found
    private int beforeLastTokenPos;

    private char []input;

    // number of current line. Starts with 1
    private int lineNumber;

    private ErrorSignaller error;

}
