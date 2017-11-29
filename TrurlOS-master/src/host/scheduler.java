package host;


import util.Globals;

public class scheduler {


    public static void addprocess(PCB process){
        Globals.readyqueue.add(process);

        if (Globals.cpu.isExecuting()){

            scheduler.schedule(Globals.quantum);
        }

    }



    public static void schedule(int quantum){

        if (Globals.OSclock % quantum == 0){

            Globals.kernelInterruptQueue.add()

        }




    }
}
