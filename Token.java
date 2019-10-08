//Jeremiah Mullane
//Programming Languages
//Jay Tokenizer: Class Token

//Class representing a Token for the Jay language
public class Token
{
	public String type;     // token type
	public String value;    // token value

	//constructor
	public Token ()
	{
		type = "";
		value = "";
	}//end constructor

	//accessor methods
	public String getType()
	{
		return type;
	}//end getType

	public String getValue()
	{
		return value;
	}//end getValue

}//end class Token