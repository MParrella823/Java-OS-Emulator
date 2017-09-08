package os;

public class ShellCommand {
	private ShellCommandFunction func;
	private String command = "";
	private String description = "";
	
	public ShellCommand(ShellCommandFunction f, String c, String d) {
		this.func = f;
		this.command = c;
		this.description = d;
	}
	
	public String getCommand() {  return command;  }
	public String getDescription() {  return description;  }
	public ShellCommandFunction function() {  return func;  }
}
