package host;
import jdk.nashorn.internal.objects.Global;
import util.Globals;
import host.MMU;
import java.util.LinkedList;
import java.util.Iterator;



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
        pcbCounter.setPID(pid);
        pcbCounter.setStackLimit(Globals.prg_count);
        Globals.processList.addFirst(pcbCounter);
        pcbCounter.updatePCBdisplay();
        return pid;
    }

    public int findSegment(int pid){
        int target = -1;
        for (int i = 0; i < Globals.processList.size(); i ++){
            if (Globals.processList.get(i).getPID() == pid){
                target = Globals.processList.remove(i).getSegment();
                break;
            }

        }
        return target;

    }

}
