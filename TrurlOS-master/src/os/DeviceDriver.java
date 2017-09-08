package os;

public class DeviceDriver {
	protected String version = "0.1";
	protected String status = "unloaded";
	protected boolean preemtable = false;
	
	protected DriverEntry driverEntry = null;
	protected EventDispatch isr = null;
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
