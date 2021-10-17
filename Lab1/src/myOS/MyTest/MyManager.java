package myOS.MyTest;
import java.io.IOException;
import java.util.List;

import myOS.ProcessInitializer;
public class MyManager {
	private Process process_F;
	private Process process_G;
	public MyManager(int arg) throws IOException, InterruptedException{
		List<String> args =List.of(String.valueOf(arg));
		ProcessBuilder pbF = ProcessInitializer.Initialize("myOS.MyTest.MyFuncF",  args);
        process_F = pbF.start();
        ProcessBuilder pbG = ProcessInitializer.Initialize("myOS.MyTest.MyFuncG",  args);
        process_G = pbG.start();
        process_F.waitFor();
        process_G.waitFor();
        System.out.println("Result: "+ (process_F.exitValue()+process_G.exitValue()));
	}
}
