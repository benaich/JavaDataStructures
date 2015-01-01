/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FunctionalProgramming;

import java.util.function.Predicate;
import java.util.Arrays;
import java.util.List;

public class PredicateDemo{
	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9);

		System.out.println("Test");
		printFilter(numbers, x -> x%2==0);

	}

	public static <T> void printFilter(List<T> l, Predicate<T> p){
		for (T a: l ) {
			if(p.test(a)) System.out.println(a);
		}
	}
}
