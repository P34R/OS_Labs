public class MysProcess {
  public int cputime;
  public int cpudone;
  public int ionext;
  public int numblocked;

  public MysProcess(int cputime, int cpudone, int ionext, int numblocked) {
    this.cputime = cputime;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
  } 	
}
