// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class MyScheduling {
  private static int id=0;
  private static int processnum = 5;
  private static int quantum = 100;
  private static int runtime = 1000;
  private static int ioblocked=600;
  private static ArrayDeque<MysProcess> processQueue = new ArrayDeque<>();
  private static Results result = new Results("null","null",0);
  private static String resultsFile = "Summary-Results";

  private static void Init(String file) {
    File f = new File(file);
    String line;
    int cputime = 0;
    double X = 0.0;
    try {   
      //BufferedReader in = new BufferedReader(new FileReader(f));
      DataInputStream in = new DataInputStream(new FileInputStream(f));
      while ((line = in.readLine()) != null) {
        if (line.startsWith("numprocess")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processnum = Common.s2i(st.nextToken());
        }
        if (line.startsWith("quantum")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          quantum = Common.s2i(st.nextToken());
        }
        if (line.startsWith("io")){
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          ioblocked = Common.s2i(st.nextToken());
        }
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          cputime = Common.s2i(st.nextToken());
          if (cputime >0)
            processQueue.addLast(new MysProcess(cputime, 0,ioblocked, 0, 0,processQueue.size()));
          else {
            System.out.println("Error, cputime of process should be >0");
            return;
          }
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      in.close();
    } catch (IOException e) { /* Handle exceptions */ }
  }

  private static void debug() {
    int i = 0;
    System.out.println("processnum " + processnum);
    System.out.println("quantum " + quantum);
    int size = processQueue.size();
    for (MysProcess process : processQueue) {
      System.out.println("process " + process.id + " " + process.cputime + " " + " " + process.cpudone +" "+ process.ioblocking + " " + process.numblocked);
    }
    System.out.println("runtime " + runtime);
  }

  public static void main(String[] args) {
    int i=0;
    if (args.length != 1) {
      System.out.println("Usage: 'java Scheduling <INIT FILE>'");
      System.exit(-1);
    }
    File f = new File(args[0]);
    //File f=new File("Simulator\\Myscheduling.conf");
    System.out.println(f.getAbsolutePath());
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");

    Init(args[0]);

    //Init("Simulator\\Myscheduling.conf");
    if (processQueue.size() < processnum) {
      Iterator<MysProcess> it=processQueue.iterator();
      while (processQueue.size() < processnum) {
        int cputime = 300;
        if (it.hasNext())
          cputime = it.next().cputime;
        processQueue.add(new MysProcess(cputime,0,0,0,0,processQueue.size()));
      }
    }
    ArrayDeque<MysProcess> processesCopy=processQueue.clone();
    result = MySchedulingAlgorithm.run(runtime, processQueue, result,quantum);
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Quantum: " + quantum);
      out.println("Process #\tCPU Time\tQuantum\tIO Blocking\tCPU Completed\tCPU Blocked");
      for (MysProcess process: processesCopy) {
        out.print(Integer.toString(process.id));
        if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
        out.print(Integer.toString(process.cputime));
        if (process.cputime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(Integer.toString(quantum));
        if (quantum < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(Integer.toString(process.cpudone));
        if (process.ioblocking<100){out.print(" (ms)\t\t");} else {out.print(" (ms)\t");}
        out.print(Integer.toString(process.ioblocking));
        if (process.cpudone < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.println(process.numblocked + " times");
        i++;
      }
      out.close();
    } catch (IOException e) { System.out.println(e); }
  System.out.println("Completed.");
  }
}

