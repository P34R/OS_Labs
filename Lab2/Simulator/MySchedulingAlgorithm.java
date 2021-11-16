// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Vector;
import java.io.*;

public class MySchedulingAlgorithm {

public static Results run(int runtime, ArrayDeque<MysProcess> processQueue, Results result, int quantum) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    //int previousProcess = 0;
    int size = processQueue.size();
    int currentProcessQuantum = 0;
    int completed = 0;
    ArrayDeque<MysProcess> completedProcesses=new ArrayDeque<>();
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Preemptive";
    result.schedulingName = "Round-Robin";
    try{
        PrintStream out = new PrintStream((new FileOutputStream(resultsFile)));
        MysProcess process = processQueue.pollFirst();
        out.println(process.id);
        out.println("Process: " + process.id  + " registered... (" + process.cputime + " " + quantum + " " + process.cpudone + " "  + process.ioblocking+" "+process.ionext+")");
        while(comptime < runtime){
            if (process.cpudone==process.cputime){
                completed++;
                out.println("Process: " + process.id  + " completed... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.ioblocking+" "+process.ionext+") <------ Done");
                if (completed==size){

                    result.compuTime=comptime;
                    out.close();
                    processQueue=completedProcesses;
                    return result;
                }
                completedProcesses.add(process);
                process=processQueue.pollFirst();
                currentProcessQuantum=0;
                /*for (i=(currentProcess+1)%size;i<size;i++){
                  process= (MysProcess) processVector.elementAt(i);
                  if (process.cpudone<process.cputime){
                    currentProcess=i;
                    break;
                  }
                  else if (i==size-1) i=-1;
                }*/
              out.println("Process: " + process.id + " registered... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.ioblocking+" "+process.ionext+")");
            }
            if(process.ionext==process.ioblocking){
                out.println("Process: " + process.id  + " I/O blocked... (" + process.cputime + " " + quantum + " " + process.cpudone + " "  + process.ioblocking+" "+process.ionext+")");
                process.numblocked++;
                process.ionext = 0;
                currentProcessQuantum=0;
                processQueue.addLast(process);
                process=processQueue.pollFirst();
                out.println("Process: " + process.id  + " registered... (" + process.cputime + " " + quantum + " " + process.cpudone + " "  + process.ioblocking+" "+process.ionext+")");
            }
            if (currentProcessQuantum==quantum){
                out.println("Process: " + process.id  + " quantum done... (" + process.cputime + " " + quantum + " " + process.cpudone + " "  + process.ioblocking+" "+process.ionext+")");
                currentProcessQuantum=0;
                processQueue.addLast(process);
                process=processQueue.pollFirst();
                out.println("Process: " + process.id  + " registered... (" + process.cputime + " " + quantum + " " + process.cpudone + " " + process.ioblocking+" "+process.ionext+")");
            }
            process.cpudone++;
            process.ionext++;
            currentProcessQuantum++;
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