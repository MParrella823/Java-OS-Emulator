package host;
import jdk.nashorn.internal.objects.Global;
import util.Globals;
import host.MMU;


public class ResidentList {

    private static int pcbCounter = 0;
    private int pid = 0;

    public int loadProcess(int[] prg){
        pcbCounter++;
        pid++;
        PCB pcbCounter = new PCB();
        pcbCounter.setSegment(MMU.getNextSegment());
        Globals.standardOut.putText("Segment: " + pcbCounter.getSegment());
        Globals.mmu.loadIntoSegment(pcbCounter.getSegment(), prg);
        pcbCounter.setPID(ResidentList.pcbCounter);
        pcbCounter.setStackLimit(Globals.prg_count);
        pcbCounter.updatePCBdisplay();


        return pid;
    }















}
