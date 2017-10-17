package host;

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
	}

	public boolean isExecuting() {
		return isExecuting;
	}

	/*
	//pops top value off stack and goes to that position
	public void jmp(){
		int index=Control.pop();
		Globals.pcb.setCurrPrgCount(index);//sets program counter to index
	}


	//pops the top three values off of stack, if second two are equal, then it branches to first popped value
	public void beq() {
		int potentialindex=Control.pop();
		int num1=Control.pop();
		int num2=Control.pop();
		if(num1==num2){
			Globals.pcb.setCurrPrgCount(potentialindex);//sent program counter to first popped value
		}else {
			Globals.pcb.setCurrPrgCount(Globals.pcb.getCurrPrgCount()+1);//increment program counter
		}
	}

	//pushes a location from the address to the stack, if less than 0 uses reverse addressing
	public void idlocation(){
		//set to 0 just for this project
		int nextnum=Globals.mmu.getData(0,Globals.pcb.getCurrPrgCount()+1);//gets the next number
		if(nextnum<0){
			int rellocation=nextnum*-1;//make positive
			int count=0;
			int position=255;
			while(count<rellocation){
				++count;
				--position;
			}
			int num=Globals.mmu.getData(0,position);//change from 0
			Control.push(num);
		}
		
		//value that is stored at that location is put on top of stack
		else {
			//0 for now change for next project
			int num=Globals.mmu.getData(0,nextnum);//get value of index
			Control.push(num);//push value to top of stack
		}
	}
	*/

	public void syscode(){

	}





}
