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

    private String processState;
    private int stackLimit;
    private int currInstruction;
    private int currPrgCount;
    private int topindex;
    private int memLocation;//current mem location, needs to be updated in opcode methods
    public int memValue;
    private int pid;
    private int segment;
    private static int pcbCount = 0;


    public PCB(){

        this.stackLimit = 0; // End of program size
        this.currInstruction = 0;
        this.currPrgCount = 0;
        this.topindex = 255; //"Top Index"
        this.processState = "NEW";
        pcbCount++;
    }

    public int getSegment(){
        return this.segment;
    }

    public static int getPcbCount(){
        return pcbCount;
    }

    public void setSegment(int segment){
        this.segment = segment;
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

    public int getMemLocation(){return memLocation;}

    public void setMemLocation(int value){
        this.memLocation=value;
        this.updateMemdisplay();

    }

    public void incrementMemLocation(){
        this.memLocation++;

        this.updateMemdisplay();
        this.updatePCBdisplay();
    }

    public int getMemValue(int index){
        return Globals.mmu.getData(segment,index);
    }

    public void setMemValue(int index, int value){

        Globals.mmu.setData(index, segment, value);
        this.memValue = value;

        this.updateMemdisplay();

    }
    //returns the memory value after the current memory value
    public int getnextmemvalue(){

        return getMemValue(memLocation+1);
    }

    public void copyPCB(PCB target){

        this.memValue = target.memValue;
        this.currInstruction = target.currInstruction;
        this.processState = target.processState;
        this.pid = target.pid;
        this.currPrgCount = target.currPrgCount;
        this.stackLimit = target.stackLimit;
        this.segment = target.segment;
        this.memLocation = target.memLocation;
        this.topindex = target.topindex;


    }





}
