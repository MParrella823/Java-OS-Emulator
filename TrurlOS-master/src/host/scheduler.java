package host;


import os.Interrupt;
import util.Globals;

import java.util.HashMap;

public class scheduler {


    public static void addprocess(PCB process){
        if (Globals.cpu.isExecuting()){
            Globals.readyqueue.add(process);
            if (Globals.OSclock + 1 % Globals.quantum == 0){
                HashMap schedmap = new HashMap<>();
                schedmap.put("0", "");
                Globals.kernelInterruptQueue.add(new Interrupt(0, schedmap));
            }


        }

    }



    public static void schedule(int quantum){

        if (!Globals.readyqueue.isEmpty()) {
           Globals.readyqueue.addLast(Globals.pcb);
           Globals.pcb.copyPCB(Globals.readyqueue.removeFirst());
           



        }



    }
}
