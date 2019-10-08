//Jeremiah Mullane
//Programming Languages
//Jay Tokenizer: fib.j test program in Jay

// compare result = the nth Fibonacci number
void main () {
	int n, fib0, fib1, temp, result;
	n=8;
	fib0 = 0;
	fib1 = 1;
	while (n > 0) {
		temp = fib0;
		fib0 = fib1;
		fib1 = fib0 + temp;
		n = n - 1;
	}
	result = fib0;
}