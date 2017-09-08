package os;

import java.util.HashMap;

public class Interrupt {
	public final int irq;
	public final HashMap<String, String> params;
	
	public Interrupt(int irq, HashMap<String, String> params) {
		this.irq = irq;
		this.params = params;
	}
}
