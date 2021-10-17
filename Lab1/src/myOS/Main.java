package myOS;
import myOS.ConjuncTest.ConjuncManager;
import myOS.DisjunctTest.DisjuncManager;
import myOS.MyTest.MyManager;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import myOS.DisjunctTest.DisjuncManager;

public class Main {
		public static void main(String[] args) {
			
			Scanner input = new Scanner(System.in);
			while(true) {
				int x=0;
				System.out.print("x: ");
				try {
					x=input.nextInt();
				} catch(InputMismatchException e) {
					System.out.println("ERROR: x should be Integer");
					break;
				}
				try {
					//DisjuncManager manager1=new DisjuncManager(x);
					//ConjuncManager manager2=new ConjuncManager(x);
					MyManager manager3= new MyManager(x);
					break;
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} 
}
			
