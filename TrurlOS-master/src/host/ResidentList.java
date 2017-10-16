package host;
import host.PCB;
import util.Globals;

public class ResidentList {

    private int pcbCounter = 0;
    private int pid = 0;

    public  int loadProcess(int[] prg){
        pcbCounter++;
        pid++;
        PCB pcbCounter = new PCB();
        Globals.mmu.loadIntoSegment(0, prg);
        pcbCounter.setPID(pid);
        pcbCounter.updatePCBdisplay();
        return pid;
    }











}
