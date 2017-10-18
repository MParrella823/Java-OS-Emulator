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


	public static int pop(){
		int pid=Globals.residentList.getcurrentpid();
		int index=Globals.processList.get(pid).getTop();//get index of top from stack
		int num=Globals.processList.get(pid).getMemValue(index);//gets value from top of stack
		Globals.processList.get(pid).setMemValue(index,0);//sets top of stack value to 0
		Globals.processList.get(pid).setTop(index+1);//increase top of stack

		return num;
	}

	public static void push(int value){
		int pid=Globals.residentList.getcurrentpid();
		Globals.processList.get(pid).setTop(Globals.processList.get(pid).getTop()-1);//decrement top of stack
		Globals.processList.get(pid).setMemValue(Globals.processList.get(pid).getTop(),value);//sets value to new top of stack
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
				Globals.cpu.jmp(pid);
			//pops the top three values off of stack, if second two are equal, then it branches to first popped value
			case(1):
				Globals.cpu.beq(pid);
			//pushes a location from the address to the stack, if less than 0 uses reverse addressing
			case(2):
				Globals.cpu.idlocation(pid);
			//handles system calls
			case(3):
				Globals.cpu.syscode(pid);



		}
	}

	public static void resetOS() {
		//I guess this must be a hard reset.  This may not be easy to do in Java.  
	}
}
