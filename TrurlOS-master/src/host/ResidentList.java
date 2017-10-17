package host;
import jdk.nashorn.internal.objects.Global;
import util.Globals;
import host.MMU;
import java.util.LinkedList;
import java.util.Iterator;



public class ResidentList {

    private static int pcbCounter = 0;
    private int pid = 0;

    /**
     *
     * Will load the user program into memory via the MMU and create a new PCB for tracking purposes
     *
     * @param prg Array of numbers (program)
     * @return The PID associated with this newly created process
     */


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

    /**
     *
     *
     * This method will take a PID number and return the segment that the program is currently loaded into
     *
     * @param pid The PID # of the process we are trying to locate
     * @return The segment that the process is currently occupying
     *
     */

    public int findSegment(int pid){
        int target = -1;
        for (int i = 0; i < Globals.processList.size(); i ++){
            if (Globals.processList.get(i).getPID() == pid){
                target = Globals.processList.get(i).getSegment();
                break;
            }
        }
        return target;
    }

    /**
     *
     * This method will remove a process from the process list (ex: terminated process)
     *
     * @param pid The pid # of the process that is to be removed.
     *
     */


    public void removeProcess(int pid){
        for (int i = 0; i < Globals.processList.size(); i++){
            if (Globals.processList.get(i).getPID() == pid){
                Globals.processList.remove(i);
            }
        }
    }

}
