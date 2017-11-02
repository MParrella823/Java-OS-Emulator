package host;

import jdk.nashorn.internal.objects.Global;
import util.Globals;

public class CPU {
	private int pc = 0; // the program counter.
	private int acc = 0; //the accumulator.
	private int xreg = 0; //on register.
	private int yreg = 0;  //the y register.
	private int zflag = 0; //the zflag.

    private PCB cpuPCB;

	private boolean isExecuting = false;

	public void cycle() {
        cpuPCB = Globals.pcb;
		Control.kernel.kernelTrace("CPU Cycle");
		opcodes(cpuPCB.getPID());
	}

	public void startExecution(){

		isExecuting=true;







	}

	public boolean isExecuting() {
		return isExecuting;
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
	public  void opcodes(int pid){

		int address= cpuPCB.getMemLocation();
		int code = cpuPCB.getMemValue(address);
		switch (code){

			//pops top value off stack and goes to that position
			case(0):
				jmp(pid);
				break;
			//pops the top three values off of stack, if second two are equal, then it branches to first popped value
			case(1):
				beq(pid);
				break;
			//pushes a location from the address to the stack, if less than 0 uses reverse addressing
			case(2):
				idlocation(pid);
				break;
			//handles system calls
			case(3):
				sys(pid);
				break;
			case(4):
				iarith(pid);
				break;
			case(8):
				push(0);
				Globals.processList.get(pid).incrementMemLocation();//increments memory location
				break;
			case(9):
				push(1);
				Globals.processList.get(pid).incrementMemLocation();//increments memory location
				break;
			//pushes a duplicate of the value on top of the stack
			case(10):
				push(Globals.processList.get(pid).getTop());
				Globals.processList.get(pid).incrementMemLocation();//increments memory location
				break;
			case(11):
				downval(pid);
				break;
			case(13):
				nextval(pid);
				break;
			case(14):
				store(pid);
				break;
			case(15):
				Globals.cpu.halt();
				break;



		}
	}


	//pops top value off stack and goes to that position
	//change to work with processlist/residentlist
	public static void jmp(int pid){
		int index=pop();
		Globals.processList.get(pid).setMemLocation(index);//sets current mem location to index
	}


	//pops the top three values off of stack, if second two are equal, then it branches to first popped value
	public static void beq(int pid) {
		int potentialindex=pop();
		int num1=pop();
		int num2=pop();
		if(num1==num2){
			Globals.processList.get(pid).setMemLocation(potentialindex);//sets current memlocation to first popped value
		}else {
			Globals.processList.get(pid).incrementMemLocation();//increments memory location
		}
	}

	//pushes a location from the address to the stack, if less than 0 uses reverse addressing
	public static void idlocation(int pid){
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
			push(num);
			Globals.processList.get(pid).incrementMemLocation();
			Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
		}

		//value that is stored at that location is put on top of stack
		else {
			int num=Globals.processList.get(pid).getMemValue(nextnum);
			push(num);//push value to top of stack
			Globals.processList.get(pid).incrementMemLocation();
			Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
		}
	}

	public static void sys(int pid){
		int nextnum=Globals.processList.get(pid).getnextmemvalue();//gets the next number
		switch (nextnum){
			//Prints the value on top of the stack
			case(1):
				Integer output=Globals.processList.get(pid).getTop();
				Globals.standardOut.putText(output.toString());
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//Interprets the value on top of the stack as an ASCII character and then prints it
			case(2):
				int output2=Globals.processList.get(pid).getTop();
				char finished= (char) output2;
				Globals.standardOut.putText(Character.toString(finished));
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//prints a new line to the output
			case(3):
				Globals.standardOut.putText("/n");
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//do not implement
			case(4):
				break;
		}
	}


	public static void iarith(int pid){
		int nextnum=Globals.processList.get(pid).getnextmemvalue();//gets the next number
		switch(nextnum){

			//adds top two values on stack together
			case (1):
				int num1=pop();
				int num2=pop();
				num1=num1+num2;
				push(num1);
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			// Subtracts the second to top value on the stack from the top value on the stack
			case (2):
				int num3=pop();
				int num4=pop();
				num3=num3-num4;
				push(num3);
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//multiplies the top two values on the stack together
			case (3):
				int num5=pop();
				int num6=pop();
				num5=num5*num6;
				push(num5);
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			//divides the top value on the stack by the second to top value on the stack
			case(4):
				int num7=pop();
				int num8=pop();
				num7=num7/num8;
				push(num7);
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
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
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;

		}

	}

	/**
	 * moves the top element on the stack val levels deep into the stack.
	 * The argument val can take on any nonnegative integer value including 0, but for reasons of space,
	 * the effects on the stack are only shown for the cases when val = 1 and val = 2
	 * @param pid
	 */
	public static void downval(int pid){
		int nextnum=Globals.processList.get(pid).getnextmemvalue();//gets the next number
		switch (nextnum){
			case (1): {
				int topindex = Globals.processList.get(pid).getTop();//gets index of top of stack
				int num1 = Globals.processList.get(pid).getMemValue(topindex);//num on top of stack
				int num2 = Globals.processList.get(pid).getMemValue((topindex + 1));//num second to top of stack
				Globals.processList.get(pid).setMemValue(topindex, num2);
				Globals.processList.get(pid).setMemValue((topindex + 1), num1);
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			}
			case(2): {
				int topindex=Globals.processList.get(pid).getTop();//gets index of top of stack
				int num1=Globals.processList.get(pid).getMemValue(topindex);//num on top of stack
				int num2=Globals.processList.get(pid).getMemValue((topindex+1));//num second to top of stack
				int num3=Globals.processList.get(pid).getMemValue((topindex+2));//num third to top of stack
				Globals.processList.get(pid).setMemValue(topindex,num2);
				Globals.processList.get(pid).setMemValue((topindex+1), num3);
				Globals.processList.get(pid).setMemValue((topindex+2), num1);
				Globals.processList.get(pid).incrementMemLocation();
				Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
				break;
			}
		}
	}

	public static void nextval(int pid){
		int nextnum=Globals.processList.get(pid).getnextmemvalue();//gets the next number
		push(nextnum);
		Globals.processList.get(pid).incrementMemLocation();
		Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
	}

	//pops a value off the top of the stack and stores in in a specified address in memory (location)
	public static void store(int pid){
		int address=Globals.processList.get(pid).getnextmemvalue();//gets the next number
		int value=pop();
		Globals.processList.get(pid).setMemValue(address,value);
		Globals.processList.get(pid).incrementMemLocation();
		Globals.processList.get(pid).incrementMemLocation();//increments memory location twice to pass over parameter
	}

	public void halt(){
		isExecuting=false;
	}




}

