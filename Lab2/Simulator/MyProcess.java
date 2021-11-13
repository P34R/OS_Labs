public class MyProcess {
  public int cputime;
  public int cpudone;
  public int ioblocking;
  public int ionext;
  public int numblocked;
  public int id;

  public MyProcess(int cputime, int cpudone,int ioblocking, int ionext, int numblocked,int id) {
    this.cputime = cputime;
    this.cpudone = cpudone;
    this.ioblocking=ioblocking;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.id=id;
  } 	
}
