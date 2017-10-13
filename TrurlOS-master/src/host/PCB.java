package host;
import host.TurtleWorld;
import util.Globals;

/**
 *
 * PCB class will handle/keep track of all Process Control Block
 * attributes such as PID, Process State, Instruction, Stack Limit, etc.
 *
 */

public class PCB {

    private int pid;
    private String processState = null;
    private int stackLimit;
    private int currInstruction;
    private int currPrgCount;
    private int topindex;
    private int[] processes = new int[5];
    private int pidCounter = 0;

    public PCB(){
        this.pid = pidCounter;
        this.processState = "init";
        this.stackLimit = 0; // End of program size
        this.currInstruction = 0;
        this.currPrgCount = 0;
        this.topindex = 0; //"Top Index"

    }

    /**
     *
     * Returns process PID number
     *
     * @return int Process PID number
     */

    public int getPID(){
        return this.pid;
    }

    /**
     *
     * Loads program from User Text Input into memory and updates PCB parameters
     *
     *
     * @param bytes The program entered into the User TextBox
     */

    public void loadProcess(int[] bytes){
        pidCounter++;
        this.pid = pidCounter;
        Globals.mmu.loadIntoSegment(0, bytes);
        this.processState = "READY";
        updatePCBdisplay();
        // Add program length counter and update stack limit
        // Top index always = 255? (At initial program load)
    }

    /**
     *
     * Will update the visual status of the PCB parameters
     *
     */

    public void updatePCBdisplay(){
        Globals.world.setProcessState(Globals.world.PCBPainter, this.processState);
        Globals.world.setPID(Globals.world.PCBPainter, this.pid);
        Globals.world.setCurrSP(Globals.world.PCBPainter, this.topindex);
        Globals.world.setInstruction(Globals.world.PCBPainter, this.currInstruction);
        Globals.world.setProgCount(Globals.world.PCBPainter, this.currPrgCount);
        Globals.world.setStackLim(Globals.world.PCBPainter, this.stackLimit);
    }

    /**
     *
     * Sets the current process's Process State parameter in the PCB
     *
     * @param state String - The process's current state
     */

    public void setProcessState(String state){
        this.processState = state;
        Globals.world.setProcessState(Globals.world.PCBPainter, this.processState);
    }

    public String getProcessState(){
        return this.processState;
    }

    public void setStackLimit(int sL){
        this.stackLimit = sL;
    }

    public int getStackLimit(){
        return this.stackLimit;
    }

    public int getCurrInstruction(){
        return this.currInstruction;
    }

    public void setCurrInstruction(int cI){
        this.currInstruction = cI;
    }

    public int getCurrPrgCount(){
        return this.getCurrPrgCount();
    }

    public void setCurrPrgCount(int PC){
        this.currPrgCount = PC;
    }

    public int getTop(){
        return this.topindex;
    }

    public void setTop(int SP){
        this.topindex = SP;
    }



}
