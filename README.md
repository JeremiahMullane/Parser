# Parser
by Jeremiah Mullane
Parser for a theoretical programming language "Jay" written in Java. Practicum for CS321 Programming Languages at Ithaca College

"Jay" is a toy programming language with simplified syntax for learning purposes.
This Repo also contains a Tokenizer for "Jay".
The tokenizer pulls a character stream from a text file and generates an array of tokens to be passed to the Parser.
The parser generates a syntax tree based on the tokenstream, ensuring the program conforms to Jay syntax.
The parse tree is then ready to be passed on to a Code Generator for final conversion into executable code (not part of this project).

The Tokenizer and the Parser comnprise the two Front-End components of a fully functional Compiler.
