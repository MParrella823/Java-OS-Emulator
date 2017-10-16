package host;
import util.Globals;
import host.MMU;


public class ResidentList {

    private static int pcbCounter;
    private int pid = 0;

    public int loadProcess(int[] prg){
        pcbCounter++;
        pid++;
        PCB pcbCounter = new PCB();
        pcbCounter.setSegment(MMU.getNextSegment());
        Globals.standardOut.putText("Segment: " + pcbCounter.getSegment());
        Globals.mmu.loadIntoSegment(pcbCounter.getSegment(), prg);
        pcbCounter.setPID(pid);

        pcbCounter.setStackLimit(Globals.prg_count);
        pcbCounter.updatePCBdisplay();

        return pid;
    }















}
