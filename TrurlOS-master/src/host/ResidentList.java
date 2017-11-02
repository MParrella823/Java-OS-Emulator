package host;

import util.Globals;
import os.Kernel;
import host.MMU;



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
        pcbCounter.setSegment(Globals.mmu.getNextSegment());


        Globals.mmu.loadIntoSegment(pcbCounter.getSegment(), prg);
        pcbCounter.setMemLocation(Globals.mmu.getSegmentAddress(pcbCounter.getSegment()));
        pcbCounter.setMemValue(pcbCounter.getMemLocation(), Globals.mmu.getData(pcbCounter.getSegment(),pcbCounter.getMemLocation()));
        pcbCounter.setPID(pid);
        pcbCounter.setStackLimit(Globals.prg_count);
        Globals.processList.addFirst(pcbCounter);
        pcbCounter.setProcessState("NEW");

        pcbCounter.updatePCBdisplay();
        pcbCounter.updateMemdisplay();
        Globals.pcb.copyPCB(pcbCounter);

        return pid;
    }

    /**
     *
     * findSegment will take a PID number, search the list of loaded processes
     * and return the segment that the program is currently loaded into
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
     * @return the current pid
     */
    public int getcurrentpid(){

        return pid;
    }


    public PCB getProcess(int pid){
        int x = -1;
        for (int i = 0; i < Globals.processList.size(); i ++){
            if (Globals.processList.get(i).getPID() == pid){
                x = i;
                break;

            }
        }
        return Globals.processList.get(x);
    }


    /**
     *
     * @param pid
     * @return The current memory address
     */
    public int findcurrentaddress(int pid){
        int address = -1;
        for (int i = 0; i < Globals.processList.size(); i++){
            if (Globals.processList.get(i).getPID() == pid){
                address = Globals.processList.peek().getMemLocation();
            }
        }

       return address;
    }



    public int findcurrentvalue(int pid){

        for (int i = 0; i < Globals.processList.size(); i ++){
            if (Globals.processList.peek().getPID() == pid){
                return Globals.processList.peek().getMemValue(Globals.processList.peek().getMemLocation());
            }
        }
        return -1;

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

    public int getPID(){
        return this.pid;
    }
}
