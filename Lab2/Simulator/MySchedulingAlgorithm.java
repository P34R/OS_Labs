// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class MySchedulingAlgorithm {

public static Results run(int runtime, Vector processVector, Results result, int quantum) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Preemptive";
    result.schedulingName = "Round-Robin";
    try{
        PrintStream out = new PrintStream((new FileOutputStream(resultsFile)));
        MysProcess process = (MysProcess) processVector.elementAt(currentProcess);
        out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.cpudone + ")");
        while(comptime < runtime){
            if (process.cpudone==process.cputime){
                completed++;
                out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.cpudone + ") <------ Done");
                if (completed==size){

                    result.compuTime=comptime;
                    out.close();
                    return result;
                }
                for (i=(currentProcess+1)%size;i<size;i++){
                  process= (MysProcess) processVector.elementAt(i);
                  if (process.cpudone<process.cputime){
                    currentProcess=i;
                    break;
                  }
                  else if (i==size-1) i=-1;
                }
              out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.cpudone + ")");
            }
            if(process.ionext==quantum){
                out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.cpudone + ")");
                process.numblocked++;
                process.ionext = 0;
                previousProcess = currentProcess;
                i=(currentProcess+1)%size;
                while (previousProcess==currentProcess){
                    process=(MysProcess) processVector.elementAt(i);
                    if(process.cpudone<process.cputime&&previousProcess!=i){
                        currentProcess=i;
                        break;
                    }
                    if (i==currentProcess) break;
                    i=(i+1)%size;
                }
                process=(MysProcess) processVector.elementAt(currentProcess);
                out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.cpudone + ")");
            }
            process.cpudone++;
            process.ionext++;
            comptime++;
        }
        out.close();
    }catch(IOException e){
        System.out.println(e);
    }
    result.compuTime=comptime;
    return result;
  }
}