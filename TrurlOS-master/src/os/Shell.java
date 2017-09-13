package os;

import host.Control;
import util.Globals;
import util.Utils;

import java.awt.*;
import java.util.Date;
import java.util.ArrayList;


public class Shell {
	private String promptString = ">";
	private ArrayList<ShellCommand> commandList = new ArrayList<ShellCommand>();

	public Shell() {
		
	}
	
	public void init() {
		commandList.add(new ShellCommand(shellInvalidCommand, "", ""));
		commandList.add(new ShellCommand(shellVer, "ver", "- Displays the current version data."));
		commandList.add(new ShellCommand(shellHelp, "help", "- Displays summary descriptions of common commands."));
		commandList.add(new ShellCommand(shellShutdown, "shutdown", "- Shuts down the virtual OS but leaves the underlying host / hardware simulation running."));
		commandList.add(new ShellCommand(shellCls, "cls", "- Clears the screen and resets the cursor position."));
		commandList.add(new ShellCommand(shellMan, "man", "<topic> - Displays the MANual page for <topic>."));
		commandList.add(new ShellCommand(shellTrace, "trace", "<on | off> - Turns the OS trace on or off."));
		commandList.add(new ShellCommand(shellDate, "date", "- Displays the current date and time."));
		commandList.add(new ShellCommand(shellLoc, "whereami", "- Displays current location...or does it??"));
		commandList.add(new ShellCommand(shellText, "color", "<color> - Changes text color of terminal window (supported colors: green, red, blue, reset)"));
		//I'm lazy.  Don't want to implement rot13 encryption.  Maybe there's something cooler anyway to do...
		//commandList.add(new ShellCommand(shellRot13, "rot13", "<string> - Does rot13 obfuscation on <string>."));
		putPrompt();
		
	}

	public static ShellCommandFunction shellText = new ShellCommandFunction(){
		public Object execute(ArrayList<String> in){
			if (in.size() > 0){
				String color = in.get(0);
				if (color.equals("green")){
					Globals.world.setColor(0, 255,0);
				}else if (color.equals("red")){
					Globals.world.setColor(255,0,0);
				}else if (color.equals("blue")){
					Globals.world.setColor(0,0,255);
				}else if (color.equals("reset")){
					Globals.world.setColor(255,255,255);
				}else {
					Globals.standardOut.putText("Please enter one of the following colors: green, blue, red, reset");
				}
			}

			else {

				Globals.standardOut.putText("Usage: color <color>. Please supply a color.");
			}

			return null;
		}


	};

	public static ShellCommandFunction shellLoc = new ShellCommandFunction(){
		public Object execute(ArrayList<String> in){
			Globals.standardOut.putText("Check Google Maps!");
			return null;
		}
	};

	// date command logic

	public static ShellCommandFunction shellDate = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in){
			Date date = new Date();
			Globals.standardOut.putText("The current date and time is: " + date.toString());
			return null;
		}
	};
	
	public static ShellCommandFunction shellPrompt = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			if (in.size() > 0) {
				Globals.osShell.setPrompt(in.get(0));
        } else {
            Globals.standardOut.putText("Usage: prompt <string>.  Please supply a string.");
        }
			return null;
		}
	};
	
	public static ShellCommandFunction shellHexDump = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			if (in.size() > 0) {
	            Globals.standardOut.putText(Utils.hexDump(String.join(" ", in.toArray(new String[]{}))));
        } else {
            Globals.standardOut.putText("Usage: hexdump <string>.  Please supply a string.");
        }
			return null;
		}
	};
	
	public static ShellCommandFunction shellMan = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			if(in.size() > 0) {
				String topic = in.get(0);
				if(topic.equals("help")) {
					Globals.standardOut.putText("Help displays a list of (hopefully) valid commands.");
				} else {
					Globals.standardOut.putText("No manual entry for " + topic + ".");
				}
			} else {
				Globals.standardOut.putText("Usage: man <topic>.  Please supply a topic.");
			}
			return null;
		}
	};
	
	public static ShellCommandFunction shellTrace = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			if(in.size() > 0) {
				String setting = in.get(0);
				if(setting.equals("on")) {
					if(!Globals.trace) {
						Globals.trace = true;
						Globals.standardOut.putText("Trace ON");
					}
				} else if(setting.equals("off")) {
					if(Globals.trace) {
						Globals.trace = false;
						Globals.standardOut.putText("Trace OFF");
					}
				} else {
					Globals.standardOut.putText("Usage: trace <on | off>.  Please supply an argument.");
				}
			}
			return null;
		}
	};
	
	public static ShellCommandFunction shellVer = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.putText(Globals.name + " version " + Globals.version);
			return null;
		}
	};
	
	public static ShellCommandFunction shellHelp = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.putText("Commands:");
			for(ShellCommand s : Globals.osShell.commandList) {
				Globals.standardOut.advanceLine();
				Globals.standardOut.putText("  " + s.getCommand() + " " + s.getDescription());
			}
			return null;
		}
	};
	
	public static ShellCommandFunction shellShutdown = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.putText("Shutting down...");
			Control.kernel.kernelShutdown();
			return null;
		}
	};
	
	public static ShellCommandFunction shellCls = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.clearScreen();
			Globals.standardOut.resetXY();
			return null;
		}
	};

	public static ShellCommandFunction shellInvalidCommand = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.putText("Invalid Command. ");
			return null;
		}
	};

	protected void setPrompt(String string) {
		promptString = string;
	}
	
	public void putPrompt() {
		Globals.standardOut.putText(promptString);
	}

	public void handleInput(String buffer) {
		Control.kernel.kernelTrace("Shell Command~" + buffer);
		UserCommand userCommand = parseInput(buffer);
		String command = userCommand.getCommand();
		
		ShellCommand function = commandList.get(0);
		for(ShellCommand sc : commandList) {
			if(sc.getCommand().equals(command)) {
				function = sc;
			}
		}
		
		execute(function, userCommand);
	}

	private void execute(ShellCommand function, UserCommand userCommand) {
		Globals.standardOut.advanceLine();
		function.function().execute(userCommand);
		if(Globals.standardOut.getXPos() > 0) {
			Globals.standardOut.advanceLine();
		}
		putPrompt();
	}
	
	public UserCommand parseInput(String buffer) {
		UserCommand retVal;
		buffer = buffer.trim();
		buffer = buffer.toLowerCase();
		String[] parts = buffer.split(" ");
		String name = parts[0];
		retVal = new UserCommand(name);
		for(int i = 1; i < parts.length; i++) {
			String arg = parts[i].trim();
			if(!arg.equals("")) {
				retVal.add(arg);
			}
		}
		return retVal;
	}
}
