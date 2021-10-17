package myOS.MyTest;
public class MyFuncF {
		public static int start(int x) throws InterruptedException {
			return x*x;			
		}
		public static void main(String[] args) throws NumberFormatException, InterruptedException {
			if (args.length!=1) {
				System.out.println("No argument for Func F");
				System.exit(0);
			}
			System.exit(start(Integer.parseInt(args[0])));
		}
}
