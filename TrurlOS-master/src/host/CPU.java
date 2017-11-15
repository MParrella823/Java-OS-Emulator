package host;

import jdk.nashorn.internal.objects.Global;
import util.Globals;
import host.MMU;
import host.Memory;
import host.PCB;

public class CPU {
	private int pc = 0; // the program counter.
	private int acc = 0; //the accumulator.
	private int xreg = 0; //on register.
	private int yreg = 0;  //the y register.
	private int zflag = 0; //the zflag.
    private PCB cpuPCB = new PCB();
    private int prg_count = 0;


	private boolean isExecuting = false;

	public void cycle() {
		Control.kernel.kernelTrace("CPU Cycle");
		if(isExecuting()==true)
		opcodes();
	}

	public void startExecution(){
		isExecuting=true;
        cpuPCB.copyPCB(Globals.pcb);
		Globals.residentList.removereadyqueue(cpuPCB);
	}

	public boolean isExecuting() {
		return isExecuting;
	}

	public  int pop(){
		int index=cpuPCB.getTop();//get index of top from stack
		int num=cpuPCB.getMemValue(index);//gets value from top of stack
		cpuPCB.setMemValue(index,0);//sets top of stack value to 0
		cpuPCB.setTop(index+1);//increase top of stack

		return num;
	}

	public  void push(int value){
		int index=cpuPCB.getTop();//get index of top from stack
		cpuPCB.setTop(index-1);//decrement top of stack
		cpuPCB.setMemValue((index-1),value);//sets value to new top of stack
	}


	public  void opcodes(){

		int address= cpuPCB.getMemLocation();
		int code = cpuPCB.getMemValue(address);

		switch (code){


			//pops top value off stack and goes to that position
			case(0):
			    cpuPCB.setCurrInstruction(0);
                Globals.pcb.copyPCB(cpuPCB);
				jmp();
				break;
			//pops the top three values off of stack, if second two are equal, then it branches to first popped value
			case(1):
                cpuPCB.setCurrInstruction(1);
                Globals.pcb.copyPCB(cpuPCB);
			    beq();
                break;
			//pushes a location from the address to the stack, if less than 0 uses reverse addressing
			case(2):
                cpuPCB.setCurrInstruction(2);
				Globals.pcb.copyPCB(cpuPCB);
                cpuPCB.updatePCBdisplay();
                idlocation();
    			break;
			//handles system calls
			case(3):
				sys();
                cpuPCB.setCurrInstruction(3);
                Globals.pcb.copyPCB(cpuPCB);

				break;
			case(4):
                cpuPCB.setCurrInstruction(4);
                Globals.pcb.copyPCB(cpuPCB);
			    iarith();

				break;
			case(8):
                cpuPCB.setCurrInstruction(8);
                Globals.pcb.copyPCB(cpuPCB);
                push(0);
				cpuPCB.incrementMemLocation();//increments memory location
				break;
			case(9):
                cpuPCB.setCurrInstruction(9);
                Globals.pcb.copyPCB(cpuPCB);
                push(1);
				cpuPCB.incrementMemLocation();//increments memory location
				break;
			//pushes a duplicate of the value on top of the stack
			case(10):
                cpuPCB.setCurrInstruction(10);
                Globals.pcb.copyPCB(cpuPCB);
                push(cpuPCB.getTop());
				cpuPCB.incrementMemLocation();//increments memory location
				break;
			case(11):
				downval();
                cpuPCB.setCurrInstruction(11);
                Globals.pcb.copyPCB(cpuPCB);
				break;
			case(13):
                cpuPCB.setCurrInstruction(13);
                Globals.pcb.copyPCB(cpuPCB);
			    nextval();
				break;
			case(14):
                cpuPCB.setCurrInstruction(14);
                Globals.pcb.copyPCB(cpuPCB);
			    store();
				break;
			case(15):
				//TODO: Create "halt" interrupt..
			    cpuPCB.setStackLimit(0);
                cpuPCB.setCurrInstruction(15);
                cpuPCB.setProcessState("COMPLETED");
                Globals.pcb.copyPCB(cpuPCB);
                Globals.pcb.updatePCBdisplay();
                Globals.pcb.updateMemdisplay();
                Globals.mmu.clearSegment(Globals.pcb.getSegment());
                Globals.residentList.removeProcess(Globals.pcb.getPID());
				break;



		}
	}


	//pops top value off stack and goes to that position
	//change to work with processlist/residentlist
	public void jmp(){
		int index=pop();
		cpuPCB.setMemLocation(cpuPCB.getSegmentStart() + index);//sets current mem location to index
	}


	//pops the top three values off of stack, if second two are equal, then it branches to first popped value
	public void beq() {
		int potentialindex=pop();
		int num1=pop();
		int num2=pop();
		if(num1==num2){
			cpuPCB.setMemLocation(potentialindex);//sets current memlocation to first popped value
		}else {
			cpuPCB.incrementMemLocation();//increments memory location
		}
	}

	//pushes a location from the address to the stack, if less than 0 uses reverse addressing
	public void idlocation(){
		int nextnum=cpuPCB.getnextmemvalue();//gets the next number
		if(nextnum<0){
			int rellocation=nextnum*-1;//make positive
			int count=0;
			int position=cpuPCB.getSegmentLimit();
			while(count<rellocation){
				++count;
				--position;
			}
			int num=cpuPCB.getMemValue(position);
			push(num);
			cpuPCB.incrementMemLocation();
			cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
		}

		//value that is stored at that location is put on top of stack
		else {
			int num=cpuPCB.getMemValue(nextnum);
			push(num);//push value to top of stack
			cpuPCB.incrementMemLocation();
			cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
		}
	}

	public void sys(){
		int nextnum=cpuPCB.getnextmemvalue();//gets the next number
		switch (nextnum){
			//Prints the value on top of the stack
			case(1):
				Integer output=cpuPCB.getTop();
				Globals.standardOut.putText(output.toString());
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//Interprets the value on top of the stack as an ASCII character and then prints it
			case(2):
				int output2=cpuPCB.getTop();
				char finished= (char) output2;
				Globals.standardOut.putText(Character.toString(finished));
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//prints a new line to the output
			case(3):
				Globals.standardOut.putText("/n");
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//do not implement
			case(4):
				break;
		}
	}


	public void iarith(){
		int nextnum=cpuPCB.getnextmemvalue();//gets the next number
		switch(nextnum){

			//adds top two values on stack together
			case (1):
				int num1=pop();
				int num2=pop();
				num1=num1+num2;
				push(num1);
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			// Subtracts the second to top value on the stack from the top value on the stack
			case (2):
				int num3=pop();
				int num4=pop();
				num3=num3-num4;
				push(num3);
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//multiplies the top two values on the stack together
			case (3):
				int num5=pop();
				int num6=pop();
				num5=num5*num6;
				push(num5);
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//divides the top value on the stack by the second to top value on the stack
			case(4):
				int num7=pop();
				int num8=pop();
				num7=num7/num8;
				push(num7);
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			/**
			 * This instruction compares the top two values on the stack. If the two values are equal, a zero is pushed.
			 * If the top value is greater, a one is pushed.
			 * If the top value is less than the second to top value, a negative 1 is pushed
			 */
			case(5):
				int num9=pop();
				int num10=pop();
				if(num9==num10)
					push(0);
				else if(num9>num10)
					push(1);
				else
					push(-1);
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;

		}

	}


	public void downval(){
		int nextnum=cpuPCB.getnextmemvalue();//gets the next number
		switch (nextnum){
			case (1): {
				int topindex = cpuPCB.getTop();//gets index of top of stack
				int num1 = cpuPCB.getMemValue(topindex);//num on top of stack
				int num2 = cpuPCB.getMemValue((topindex + 1));//num second to top of stack
				cpuPCB.setMemValue(topindex, num2);
				cpuPCB.setMemValue((topindex + 1), num1);
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			}
			case(2): {
				int topindex=cpuPCB.getTop();//gets index of top of stack
				int num1=cpuPCB.getMemValue(topindex);//num on top of stack
				int num2=cpuPCB.getMemValue((topindex+1));//num second to top of stack
				int num3=cpuPCB.getMemValue((topindex+2));//num third to top of stack
				cpuPCB.setMemValue(topindex,num2);
				cpuPCB.setMemValue((topindex+1), num3);
				cpuPCB.setMemValue((topindex+2), num1);
				cpuPCB.incrementMemLocation();
				cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			}
		}
	}

	public void nextval(){
		int nextnum=cpuPCB.getnextmemvalue();//gets the next number
		push(nextnum);
		cpuPCB.incrementMemLocation();
		cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
	}

	//pops a value off the top of the stack and stores in in a specified address in memory (location)
	public  void store(){
		int address=cpuPCB.getnextmemvalue();//gets the next number
		int value=pop();

		if(address<0){
			int rellocation=address*-1;//make positive
			int count=0;
			int position=cpuPCB.getSegmentLimit();
			while(count<rellocation){
				++count;
				--position;
			}
			cpuPCB.setMemValue(position,value);
		}
		else {
			cpuPCB.setMemValue(address, value);
		}
		cpuPCB.incrementMemLocation();
		cpuPCB.incrementMemLocation();//increments memory location twice to pass over parameter
	}

	public void halt(){
		isExecuting=false;
	}




}

