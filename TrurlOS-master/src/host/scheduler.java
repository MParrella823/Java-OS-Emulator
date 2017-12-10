package host;


import os.Interrupt;
import util.Globals;

import java.util.HashMap;

public class scheduler {


    public static void addprocess(PCB process){
            Globals.world.updateProcessGUI();
            Globals.readyqueue.add(process);




    }

    public static void schedule(){
        if (!Globals.readyqueue.isEmpty()) {

          Globals.readyqueue.add(Globals.pcb);
          Globals.pcb = Globals.readyqueue.removeFirst();
          Control.hostLog("Switching from PID: "+ Globals.readyqueue.peekLast().getPID()+" to  PID: " + Globals.pcb.getPID(), "Scheduler");
          Globals.standardOut.advanceLine();
          Globals.world.updateProcessGUI();
          Globals.pcb.updatePCBdisplay();
          Globals.pcb.updateMemdisplay();
        }
    }
}
