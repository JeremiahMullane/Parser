public class parser {

// Jay parser - does parsing and displays the abstract syntax.

public static void main(String args[])
{
	TokenStream MyProgram = new TokenStream(args[0]);
	ConcreteSyntax C  = new ConcreteSyntax(MyProgram);

	System.out.println("Begin parsing... " + args[0]);
	Program A = C.program();   // parse and generate abstract syntax tree
	A.display();               // display abstract syntax tree
}//end main
}//end parser
