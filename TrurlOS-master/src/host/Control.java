package host;

import os.Kernel;
import util.Globals;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Control {
	private static final String Global = null;
	public static Kernel kernel;
	public static CPU cpu;
	public static TurtleWorld frame;
	
	public static void hostInit() {
		frame = new TurtleWorld(1900, 1600);
		//frame.addKeyListener(Devices.devices);
		Globals.world = frame;
		Globals.userProgramInput = TextArea.createAndShowGUI();
	}
	
	public static void hostLog(String message, String source) {
		if(source == null) source = "?";
		int clock = util.Globals.OSclock;
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		System.out.println("{clock: " + clock + ", source: " + source + ", message: " + message + ", now: " + timeStamp);
	}
	
	public static void startOS() {  //TODO: this should be connected to a button somehow...
		//disable the start button.
		//enable shutdown and reset buttons.
		//refocus to console.
		cpu = new CPU();
		//            _hardwareClockID = setInterval(Devices.hostClockPulse, CPU_CLOCK_INTERVAL);
		//      as seen above, must do something with clocks....

		Globals.host = new Thread() {
			public void run() {
				try {
					while(true) {
						sleep(Globals.CPU_CLOCK_INTERVAL);
						Devices.devices.hostClockPulse();
					}
				} catch (InterruptedException e) {
					System.err.println("Weird.  That's not supposed to happen...");
				}
				Devices.devices.hostClockPulse();
			}
		};
		Globals.host.start();
		
		kernel = new Kernel();
		kernel.kernelBootstrap();
		Globals.world.focus();
	}
	
	public static void haltOS() {
		Control.hostLog("Emergency Halt", "host");
		Control.hostLog("Attempting Kernel Shutdown", "host");
		kernel.kernelShutdown();
		Globals.host.stop();
        //			clearInterval(_hardwareClockID);
		//some clock stuff here as well.

	}

	//change to work with residentlist/processlist
	public static int pop(){
		//segment set to 0 for now, will change for project 3
		int num=Globals.mmu.getData(0,Globals.pcb.getTop());//get value from stack
		Globals.mmu.setData(Globals.pcb.getTop(),0,0);//clears stack position
		Globals.pcb.setTop(Globals.pcb.getTop()+1);//increase top of stack
		return num;
	}
	//change to work with residentlist/processlist
	public static void push(int value){
		Globals.pcb.setTop(Globals.pcb.getTop()-1);//decrements top of stack
		//set as 0 for now, change for future project
		Globals.mmu.setData(Globals.pcb.getTop(),0,value);
	}

	/**
	 * In order for opcode to be correct, the current memory value must be set to the desired opcode
	 * @param pid
	 */
	public static void opcodes(int pid){
		int address=Globals.residentList.findcurrentaddress(pid);
		int code=Globals.residentList.findcurrentvalue(pid);//current memory value
		switch (code){

			//pops top value off stack and goes to that position
			case(0):
				Globals.cpu.jmp();
			//pops the top three values off of stack, if second two are equal, then it branches to first popped value
			case(1):
				Globals.cpu.beq();
			//pushes a location from the address to the stack, if less than 0 uses reverse addressing
			case(2):
				Globals.cpu.idlocation();
			//handles system calls
			case(3):
				Globals.cpu.syscode();



		}
	}

	public static void resetOS() {
		//I guess this must be a hard reset.  This may not be easy to do in Java.  
	}
}
