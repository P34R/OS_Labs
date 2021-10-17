package myOS.DisjunctTest;
import java.util.Optional;

import os.lab1.compfuncs.basic.Disjunction;
public class FuncFDisjunc {
		public static int start(int x) throws InterruptedException {
			Optional<Boolean> result = Disjunction.trialF(x);
			if (result.isPresent())
				return result.get()? 1:0;
			throw new IllegalArgumentException("F Computation can't be done");
			
		}
		public static void main(String[] args) {
			if (args.length!=1) {
				System.out.println("No argument for Func F");
				System.exit(0);
			}
			try {
				System.exit(start(Integer.parseInt(args[0])));
			} catch (NumberFormatException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
