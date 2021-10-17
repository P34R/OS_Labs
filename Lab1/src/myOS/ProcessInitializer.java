package myOS;
import java.util.ArrayList;
import java.util.List;

public class ProcessInitializer {
	public static ProcessBuilder Initialize(String className, List<String> args) {
		List<String> commandsToDo= new ArrayList<>();
		commandsToDo.add(System.getProperty("java.home")+"\\" + "bin" + "\\" + "java");
		commandsToDo.add("-cp");
		commandsToDo.add(System.getProperty("java.class.path"));
		commandsToDo.add(className);
		commandsToDo.addAll(args);
		
		ProcessBuilder pb=new ProcessBuilder (commandsToDo);
		
		return pb;
	}
}
