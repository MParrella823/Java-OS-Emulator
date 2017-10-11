package host;

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
    private int topindex;//changed name for clarity, top of stack
    private int[] processes = new int[5];

    public PCB(){
        this.pid = 0;
        this.processState = "init";
        this.stackLimit = 0;
        this.currInstruction = 0;
        this.currPrgCount = 0;
        this.topindex = 256;
    }

    public int getPID(){
        return this.pid;
    }

    public void setPID(int pid){
        this.pid = pid;
    }

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

}
