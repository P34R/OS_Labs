package myOS.ConjuncTest;
import java.io.IOException;
import java.util.List;

import myOS.ProcessInitializer;
import os.lab1.compfuncs.basic.*;
public class ConjuncManager {
	private Process process_F;
	private Process process_G;
	public ConjuncManager(int arg) throws IOException, InterruptedException{
		List<String> args =List.of(String.valueOf(arg));
		ProcessBuilder pbF = ProcessInitializer.Initialize("myOS.ConjuncTest.FuncFConjunc",  args);
        process_F = pbF.start();
        ProcessBuilder pbG = ProcessInitializer.Initialize("myOS.ConjuncTest.FuncGConjunc",  args);
        process_G = pbG.start();
       //process_F.waitFor();
        //process_G.waitFor();
        Thread.currentThread().sleep(2000);
        System.out.println("Result: "+ (process_F.exitValue()+process_G.exitValue()));
	}
}
