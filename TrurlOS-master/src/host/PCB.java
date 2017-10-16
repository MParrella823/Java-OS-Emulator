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

    private String processState = null;
    private int stackLimit;
    private int currInstruction;
    private int currPrgCount;
    private int topindex;
    private int memLocation;
    public int memValue;
    private int pid;

    public PCB(){
        this.processState = "NEW";
        this.stackLimit = 0; // End of program size
        this.currInstruction = 0;
        this.currPrgCount = 0;
        this.topindex = 255; //"Top Index"

    }

    public void setPID(int pid){
        this.pid = pid;
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

    public void updateMemdisplay(){
        Globals.world.setMemLocation(Globals.world.MemPainter, this.memLocation);
        Globals.world.setMemValue(Globals.world.MemPainter, this.memValue);
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
