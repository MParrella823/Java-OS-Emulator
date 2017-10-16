package host;
import util.Globals;


public class ResidentList {

    private int pcbCounter;
    private int pid = 0;

    public int loadProcess(int[] prg){
        pcbCounter++;
        pid++;
        PCB pcbCounter = new PCB();
        pcbCounter.setSegment(getNextSegment());
        Globals.mmu.loadIntoSegment(pcbCounter.getSegment(), prg);
        pcbCounter.setPID(pid);
        Globals.standardOut.putText("Segment: " + pcbCounter.getSegment());
        pcbCounter.setStackLimit(Globals.prg_count);
        pcbCounter.updatePCBdisplay();

        return pid;
    }

    public int getNextSegment(){
        int segment = -1;
        int test = 0;

        for (int i = 0; i < Globals.mmu.getSegmentCount(); i ++) {
            for (int x = 0; x < 255; x++) {
                test += Globals.mmu.getData(i, x);
            }
            if (test == 0){
                segment = i;
            }
        }
        if (segment == -1){
            Globals.standardOut.putText("Error: No free segments in memory!");
        }
        return segment;
    }













}
