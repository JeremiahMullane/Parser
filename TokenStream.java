//Jeremiah Mullane
//Programming Languages
//Jay Tokenizer: Class TokenStream

import java.io.*;

//class representing a file to be tokenized written in the Jay language
public class TokenStream
{
	private boolean isEof = false; //end of file flag
	private boolean foundInvalidToken = false; //invalid token flag
	private char nextChar = ' '; // next character in input stream
	private BufferedReader input;

	//constructor
	public TokenStream (String fileName)
	{ 
        	try
		{
			input = new BufferedReader (new FileReader(fileName));
		}//end try
		catch (FileNotFoundException e)
		{
			System.out.println("File not found: " + fileName);
			System.exit(1);
		}//end catch
	}//end constructor

	//identifies, classifies, and returns the next token in the file
	public Token nextToken()
	{ 
		Token t = new Token();   
		t.type = "Other";
		t.value = "";

		//first check for whitespace and bypass it
		while (isWhiteSpace(nextChar))
		{
			nextChar = readChar();
		}//end while

        	//then check for a comment, and bypass it
        	//in the case of a single '/', identify as division operator
		while (nextChar=='/')
		{
			nextChar = readChar();
			if (nextChar=='/')
			{
				while (! isEndOfLine(nextChar))
				{
					nextChar = readChar();
				}//end while
				while (isWhiteSpace(nextChar))
				{
				nextChar = readChar();
				}//end while
			}//end if
			else
			{
				t.type = "Operator";
				t.value = "/";
				nextChar = readChar();
				return t;
			}//end else
		}//end while
        
		// Then check for an Operator; recover 2-character operators
		// as well as 1-character ones
		if (isOperator(nextChar))
		{
			t.type = "Operator";
			t.value += nextChar;
			if (nextChar == '&')
			{
				nextChar = readChar();
				if (nextChar == '&')
				{
					t.value += nextChar;
					nextChar = readChar();
				}//end if
				else
				{
					t.type = "Other";
					foundInvalidToken = true;
				}//end else
			}//end if
			else if (nextChar == '|')
			{
				nextChar = readChar();
				if (nextChar == '|')
				{
					t.value += nextChar;
					nextChar = readChar();
				}//end if
				else
				{
					t.type = "Other";
					foundInvalidToken = true;
				}//end else
			}//end else if
	    		else if (nextChar == '=' || nextChar == '!' ||
				 nextChar == '<' || nextChar == '>')
			{
				nextChar = readChar();
				if (nextChar == '=')
				{
					t.value += nextChar;
					nextChar = readChar();
				}//end if
			}//end else if
			else nextChar = readChar();
		}//end if

		//Then check for a Separator
		else if (isSeparator(nextChar))
		{
			t.type = "Separator";
			t.value += nextChar;
			nextChar = readChar();
		}//end else if
   
		//Then check for an Identifier, Keyword, or Boolean(Literal)
		else if (isLetter(nextChar))
		{
			t.value += nextChar;
			nextChar = readChar();
			while(isLetter(nextChar) || isDigit(nextChar))
			{
				t.value += nextChar;
				nextChar = readChar();
			}//end while
			if (isKeyword(t.value)) t.type = "Keyword";
			else if (isBoolean(t.value)) t.type = "Literal";
			else t.type = "Identifier";
		}//end else if

		//Then check for an Iteger(Literal)
		else if (isDigit(nextChar))
		{
			t.type = "Literal";
			t.value += nextChar;
			nextChar = readChar();
			while(isDigit(nextChar))
			{
				t.value += nextChar;
				nextChar = readChar();
			}//end while
		}//end else if
		return t;
	}//end nextToken

	//reads the next character in the input file
	private char readChar ()
	{
		int i = 0;
		if (isEof) return (char)0;
		System.out.flush();
		try
		{ 
			i = input.read(); 
		}//end try
		catch (IOException e)
		{
			System.exit(-1);
		}//end catch
		if (i == -1)
		{
			isEof=true; 
			return (char)0;
		}//end if
		return (char)i;
	}//end readChar


	private boolean isKeyword (String s)
	{
		return (s.equals("boolean") || s.equals("else") || s.equals("if") || 
			s.equals("int") || s.equals("main") || s.equals("void") || 
			s.equals("while"));
	}//end isKeyword


	private boolean isBoolean (String s)
	{
		return (s.equals("true") || s.equals("false"));
	}//end isBoolean
  

	private boolean isWhiteSpace (char c)
	{
		return (c==' ' || c=='\t' || c=='\r' || c=='\n' || c=='\f');
	}//end isWhiteSpace


	private boolean isEndOfLine (char c)
	{
		return (c=='\r' || c=='\n');
	}//end isEndOfLine


	private boolean isSeparator(char c)
	{
		return (c=='[' || c==']' || c=='(' || c==')' || c==':' ||
			c=='{' || c=='}' || c==';' || c==',' || c=='.');
	}//end isSeparator
  

	private boolean isOperator(char c)
	{
		return (c=='=' || c=='+' || c=='-' || c=='*' || c=='/' ||
			c=='<' || c=='>' || c=='!' || c=='&' || c=='|');
	}//end isOperator
  

	private boolean isLetter(char c)
	{
		return (c=='a' || c=='b' || c=='c' || c=='d' || c=='e' || c=='f' ||
			c=='g' || c=='h' || c=='i' || c=='j' || c=='k' || c=='l' ||
			c=='m' || c=='n' || c=='o' || c=='p' || c=='q' || c=='r' ||
			c=='s' || c=='t' || c=='u' || c=='v' || c=='w' || c=='x' ||
			c=='y' || c=='z' || c=='A' || c=='B' || c=='C' || c=='D' ||
			c=='E' || c=='F' || c=='G' || c=='H' || c=='I' || c=='J' ||
			c=='K' || c=='L' || c=='M' || c=='N' || c=='O' || c=='P' ||
			c=='Q' || c=='R' || c=='S' || c=='T' || c=='U' || c=='V' ||
			c=='W' || c=='X' || c=='Y' || c=='Z');
	}//end isLetter
  

	private boolean isDigit(char c)
	{
		return (c=='0' || c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || 
			c=='6' || c=='7' || c=='8' || c=='9');
	}//end isDigit


	public boolean eof ()
	{
		return isEof;
	}//end eof

	
	public boolean error ()
	{
		return foundInvalidToken;
	}//end error

}//end class TokenStream

