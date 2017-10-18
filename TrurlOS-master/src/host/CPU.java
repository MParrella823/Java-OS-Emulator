package host;

import jdk.nashorn.internal.objects.Global;
import util.Globals;

public class CPU {
	private int pc = 0; // the program counter.
	private int acc = 0; //the accumulator.
	private int xreg = 0; //on register.
	private int yreg = 0;  //the y register.
	private int zflag = 0; //the zflag.

	private boolean isExecuting = false;
	
	public void cycle() {
		Control.kernel.kernelTrace("CPU Cycle");
		Control.opcodes(Globals.residentList.getcurrentpid());
	}

	public boolean isExecuting() {
		return isExecuting;
	}


	//pops top value off stack and goes to that position
	//change to work with processlist/residentlist
	public void jmp(int pid){
		int index=Control.pop();
        Globals.processList.get(pid).setMemLocation(index);//sets current mem location to index
	}


	//pops the top three values off of stack, if second two are equal, then it branches to first popped value
	public void beq(int pid) {
		int potentialindex=Control.pop();
		int num1=Control.pop();
		int num2=Control.pop();
		if(num1==num2){
            Globals.processList.get(pid).setMemLocation(potentialindex);//sets current memlocation to first popped value
		}else {
            Globals.processList.get(pid).incrementMemLocation();//increments memory location
		}
	}

	//pushes a location from the address to the stack, if less than 0 uses reverse addressing
	public void idlocation(int pid){
        int nextnum=Globals.processList.get(pid).getnextmemvalue();//gets the next number
		if(nextnum<0){
			int rellocation=nextnum*-1;//make positive
			int count=0;
			int position=255;
			while(count<rellocation){
				++count;
				--position;
			}
            int num=Globals.processList.get(pid).getMemValue(position);
			Control.push(num);
            Globals.processList.get(pid).incrementMemLocation();
            Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
		}
		
		//value that is stored at that location is put on top of stack
		else {
            int num=Globals.processList.get(pid).getMemValue(nextnum);
			Control.push(num);//push value to top of stack
            Globals.processList.get(pid).incrementMemLocation();
            Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
		}
	}


	public void syscode(int pid){

	}





}
