package host;

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
}
