package host;


import os.Interrupt;
import util.Globals;

import java.util.HashMap;

public class scheduler {


    public static void addprocess(PCB process){
            Globals.world.updateProcessGUI();
            Globals.readyqueue.add(process);
            if (Globals.OSclock + 1 % Globals.quantum == 0){
                if(Globals.cpu.isExecuting()) {
                    HashMap schedmap = new HashMap<>();
                    schedmap.put("0", "");
                    Globals.kernelInterruptQueue.add(new Interrupt(0, schedmap));
                }
            }
    }

    public static void schedule(){
        if (!Globals.readyqueue.isEmpty()) {
          int pid1=Globals.readyqueue.getLast().getPID();
          Globals.pcb.copyPCB(Globals.readyqueue.removeFirst());//removes process from beginning of queue and copies to cpuPCB
          //we should check how .last /first work to make sure that its functioning as a queue
          int pid2=Globals.pcb.getPID();
          Globals.readyqueue.addLast(Globals.pcb);//adds the copied process to the end of the queue
          Globals.standardOut.putText("Switching from "+pid1+" to "+pid2);
        }
    }
}
