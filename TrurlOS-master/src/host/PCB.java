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
    private int currSP;
    private int[] processes = new int[5];

    public PCB(){
        this.pid = 0;
        this.processState = "init";
        this.stackLimit = 0;
        this.currInstruction = 0;
        this.currPrgCount = 0;
        this.currSP = 0;

    }

    public int getPID(){
        return this.pid;
    }




}
