ECC shared key
==============
Bob
	p a prime number
	E(a,b) where 
		0 < a and b < p
		-(4a3+27b2) != 0
		y^2 = x^3 + ax + b
	P point in E
	n random integer => secret
	compute nP

	send E(a,b), P, nP to Alice
Alice 
	m random integer => secret
	compute mP 
	send mP to Bob
	
shared key = mnP = nmP

ECC public key
==============

Bob
	p a prime namber
	E(a,b) where 
		0 < a and b < p
		-(4a3+27b2) != 0
		y^2 = x^3 + ax + b
	P point in E
	n random integer => private key
	nP => public key

	share E(a,b), P, nP to Alice
Alice
	want to send a M point to Bob
	m random integer => private key
	mP  => public key
	send (mP, M + nmP) to Bob

Bob
	nmP = n * mP
	M = M + nmP - nmP = M


Chararters Mapping to Points
============================
	- E(): eleptic curve
	- P: point generator
	- S: set of mapping points 
	- A: a non singular matrix
	- A-1: inverse of matrix A
	- m: bob's secret key
	- n: alice's secret key

send msg = "hello"
	size = 5 not divisible by 3 (need to add 1 : $ for padding)
	size = 6
	Coding:
	map msg character using S => [P(1,0), P(1,7), P(2,3), P(2,3), P(2,0), $]
	create a matrix M of 3 rows like so
	M = [	
			P(1,0) P(1,7)
			P(2,3) P(2,3)	
			P(2,0)	$
		]
					
	choose A as a non singular matrix of 3*3
	result S =  AM
	S = [	
			Q1(x,y) Q2(x2,y2) Q3(x3,y3)
			  ...	   ...		...  
			  ...      ...    Qn(xn,yn)
		] 

	encryption points: C = (mP, Q + m(nP))
	C = [
			C1	C2	C3
			..	..	..
			..	..	Cn
		]
	send(mP, Q + m(nP))

	decryption points: D = Q + m(nP) - n(mP) = Q
	D = [
			Q1	Q2	Q3
			..	..	..
			..	..	Qn
		]
	Decoding:
	M = D * A-1



Biobliographie
==============
	+ [1] http://en.wikipedia.org/wiki/Elliptic_curve_cryptography
	+ [2] https://www.certicom.com/index.php/ecc-tutorial
	+ [3] http://www.eccworkshop.org/

	books:
	+ [4] William Stalings


