package os;

import host.Control;
import host.TurtleWorld;
import util.Globals;
import util.Utils;

import java.awt.*;
import java.awt.image.Kernel;
import java.util.Date;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Shell {
	private String promptString = ">";
	private ArrayList<ShellCommand> commandList = new ArrayList<ShellCommand>();
	private static Integer shellnum=0;

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
		commandList.add(new ShellCommand(shellCount, "count", "Displays the amount of shell commands previously used. Count does not increase count."));
		commandList.add(new ShellCommand(shellStatus, "status", "<message> - Changes the status bar message"));
		commandList.add(new ShellCommand(shellLoad, "load", "- Loads a program from the 'TextArea' window"));
		commandList.add(new ShellCommand(shellKill, "trap", "- Will cause the infamous BSOD error!"));
		commandList.add(new ShellCommand(shellRun, "run", "<PID> - Execute loaded PID in memory"));
		//I'm lazy.  Don't want to implement rot13 encryption.  Maybe there's something cooler anyway to do...
		//commandList.add(new ShellCommand(shellRot13, "rot13", "<string> - Does rot13 obfuscation on <string>."));
		putPrompt();
	}

	public static ShellCommandFunction shellRun = new ShellCommandFunction() {
		public Object execute(ArrayList<String> input) {
			if (input.size() > 0){
				String in = input.get(0);
				try {
					int pid = Integer.parseInt(in);
				}
				catch (NumberFormatException e){
					Globals.standardOut.putText("Error: PID must be a numerical value.");
				}


			}

			return null;
		}
	};


	public static ShellCommandFunction shellKill = new ShellCommandFunction(){
		public Object execute(ArrayList<String> in) {
			if (in.size() > 0){
				String message = "";
				for (int i = 0; i < in.size(); i++){
					message += " " + in.get(i);
				}
				Control.kernel.kernelTrapError(message);
			}
			return null;
		}
	};

	public static ShellCommandFunction shellLoad = new ShellCommandFunction() {
        public Object execute(ArrayList<String> input) {
            boolean flag = false;
            String line = Globals.userProgramInput.getText();
            StringTokenizer str = new StringTokenizer(line, " ");//use whitespace as delimiter to process TextArea input
			while (str.hasMoreTokens()){
				String test = str.nextToken();//take the first delimited string item
				try{//try to parse a number from the string
					double x = Double.parseDouble(test);
					if ((x - Math.floor(x)) > 0){
						flag = true;
						break;
					}
				}
				catch (NumberFormatException e){ //if string is not numerical, ensure it is a alphabet character and set the flag accordingly
					for (int i = 0; i < test.toCharArray().length; i ++){
						char c = test.charAt(i);
						if (Character.isAlphabetic(c)){
							flag = true;
							break;
						}
					}
				}

			}//end while
            if (flag == true) {
                Globals.standardOut.putText("Error: Program input cannot contain letters or non-integers!");
            }
			int[] prg = new int[85];
            String[] userInput = Globals.userProgramInput.getText().split("\\s+"); //Parse TextBox input from user
			for (int i = 0; i < userInput.length; i++){
				try{
					prg[i] = Integer.parseInt(userInput[i]);
				}
				catch (NumberFormatException e){
				}
			}
			//Globals.mmu.loadIntoSegment(0, prg);
			Globals.pcb.loadProcess(prg);
			Globals.standardOut.putText("pid: " + Globals.pcb.getPID());

			//for (int i = 0; i < Globals.mmu.getSegmentLimit(0); i++){
				//Globals.standardOut.putText("" + Globals.mmu.getData(0, i) + " ");

			//}
			//TODO: Add PID code to create new PID and print out PID number
            return null;

        }
    };

	public static ShellCommandFunction shellStatus = new ShellCommandFunction() {
        public Object execute(ArrayList<String> in) {
        	String message = "";
			for (int i = 0; i < in.size(); i++){
					message += " " + in.get(i);
			}
			Globals.world.changeStatus(message);
				return null;
        }
    };


	public static ShellCommandFunction shellCount = new ShellCommandFunction() {
		public Object execute(ArrayList<String> input) {
			Globals.standardOut.putText("Count: " + Integer.toString(shellnum));
			return null;
		}
	};

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
			++shellnum;
			return null;
		}
	};

	public static ShellCommandFunction shellLoc = new ShellCommandFunction(){
		public Object execute(ArrayList<String> in){
			Globals.standardOut.putText("Check Google Maps!");
			++shellnum;
			return null;
		}
	};

	// date command logic

	public static ShellCommandFunction shellDate = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in){
			Date date = new Date();
			Globals.standardOut.putText("The current date and time is: " + date.toString());

			++shellnum;
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
			++shellnum;
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
			++shellnum;
			return null;
		}
	};
	
	public static ShellCommandFunction shellMan = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			if(in.size() > 0) {
				String topic = in.get(0);
				if(topic.equals("help")) {
					Globals.standardOut.putText("Help displays a list of (hopefully) valid commands.");
				}else if (topic.equals("ver")){
					Globals.standardOut.putText("Displays the latest version of the project. May or may not be accurate...");
				}else if (topic.equals("whereami")) {
					Globals.standardOut.putText("Provides sage-like advice for finding your current location");
				}else if (topic.equals("load")){
					Globals.standardOut.putText("Will read input from the TextArea and attempt to load a program");
				}
				else{
					Globals.standardOut.putText("No manual entry for " + topic + ".");
				}
			}else{
				Globals.standardOut.putText("Usage: man <topic>.  Please supply a topic.");
			}
			++shellnum;
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
			++shellnum;
			return null;
		}
	};
	
	public static ShellCommandFunction shellVer = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.putText(Globals.name + " version " + Globals.version);
			++shellnum;
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
			++shellnum;
			return null;
		}
	};
	
	public static ShellCommandFunction shellShutdown = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.putText("Shutting down...");
			Control.kernel.kernelShutdown();
			++shellnum;
			return null;
		}
	};
	
	public static ShellCommandFunction shellCls = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.clearScreen();
			Globals.standardOut.resetXY();
			++shellnum;
			return null;
		}
	};

	public static ShellCommandFunction shellInvalidCommand = new ShellCommandFunction() {
		public Object execute(ArrayList<String> in) {
			Globals.standardOut.putText("Invalid Command. ");
			//shellnum only increases for valid commands
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
			if(arg.equals('\b')){
				parts[i]= "";
			}
			if(!arg.equals("")) {
				retVal.add(arg);
			}
		}

		return retVal;
	}
}
