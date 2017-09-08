package os;

import java.util.ArrayList;

public class UserCommand extends ArrayList<String> {
	private String command = "";
	
	public UserCommand(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
}
