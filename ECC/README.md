ECC shared key
==============
Bob
---
	p a prime number
	E(a,b) where 
		0 < a and b < p
		-(4a3+27b2) != 0
		y^2 = x^3 + ax + b
	P a point generator in E
	n random integer => secret
	compute nP

	send E(a,b), P, nP to Alice

Alice
-----
	m random integer => secret
	compute mP 
	send mP to Bob
	
shared key = mnP = nmP

ECC public key
==============

Bob
---
	p a prime number
	E(a,b) where 
		0 < a and b < p
		-(4a3+27b2) != 0
		y^2 = x^3 + ax + b
	P point generator in E
	n random integer => private key
	nP => public key

	share E(a,b), P, nP to Alice

Alice
-----
	want to send a M point to Bob
	m random integer => private key
	mP  => public key
        map message to a set of points (M) in E
	send (mP, M + nmP) to Bob

Bob
---
	nmP = n * mP
	M = M + nmP - nmP = M


Chararters Mapping to Points
============================
	- E: eleptic curve
	- P: point generator
	- S: set of mapping points 
	- A: a non singular matrix
	- A-1: inverse of matrix A
	- m: bob's secret key
	- n: alice's secret key

Alice do the cnryption
----------------------
	send msg = "hello"
	size = 5 not dided by 3 (need to add 1 : $ for padding)
	size = 6
	Coding:
	map msg characters to list of points in E: S => [P(1,0), P(1,7), P(2,3), P(2,3), P(2,0), $]
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

	encryption points: Ci = Q + m(nP)
	C = [
			C1	C2	C3
			..	..	..
			..	..	Cn
		]
	send(mP, C)

Bob do the decryption
---------------------
	receive(mP, C)
	bob decrypt the points using his private key n : Di = Ci - n(mP) = Qi + m(nP) - n(mP) = Qi
	D = [
			Q1	Q2	Q3
			..	..	..
			..	..	Qn
		]
	Decoding:
	M = D * A-1


References
==============
	+ [1] http://en.wikipedia.org/wiki/Elliptic_curve_cryptography
	+ [2] https://www.certicom.com/index.php/ecc-tutorial
	+ [3] http://www.eccworkshop.org/


	books:
	+ [4] William Stalings
	+ [5] Stallings, W. Cryptography and Network Security, (2003) Prentice Hall, 3rd Edition.
	+ [6] V. S. Miller. Use of Elliptic Curves in Cryptography. Advances in Cryptology CRYPTO’85, pp. 417-426, 1986.
	+ [7] N. Koblitz. Elliptic Curve Cryptosystems. Mathematics of Computation, Vol. 48, No. 177, pp. 203-209, 1987.
	+ [8] Zhu Yufei, Zhang Yajuan. Introduction to elliptic curve cryptosystem. Beijing: Science press, 10, 130 (in chinese), 2006.


