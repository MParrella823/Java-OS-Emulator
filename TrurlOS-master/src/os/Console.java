package os;


import com.sun.prism.Graphics;
import host.TurtleWorld;
import javafx.scene.Cursor;
import sun.awt.Graphics2Delegate;
import sun.awt.image.ImageWatched;



import util.Globals;


import java.lang.*;
import java.util.Collections;
import java.util.LinkedList;




public class Console implements Input, Output{
	private String buffer = "";
	private static LinkedList<String> scrollBuffer  = new LinkedList();
	private static LinkedList<String> tabBuffer= new LinkedList<>();
	LinkedList<String> alldone= new LinkedList<>();//used for tab completion
	LinkedList<String> allset = new LinkedList<>();//used for line completion
	private int spacecounter=0;//number of characters up before space/enter
	private int entercounter=0;//number of characters before an enter/line ends
	private int udpos=0;//used to track the up/down position
	private int XPos, YPos;
	public Console() {

	}

	public void init() {

		clearScreen();
		resetXY();
	}



	@Override
	//Writes character input into console
	public void putText(String string) {
		if(!string.equals("")) {
			if (string.contains("\r")){
				scrollBuffer.addLast("\n");
			}
			scrollBuffer.addLast(string);
			Globals.world.drawText(XPos, YPos, string);
			int offset = Globals.world.measureText(XPos, string);
			XPos += offset;
		}
	}

	@Override
	public void advanceLine() {
		XPos = 0;
		if (getYPos() >= 372){  //Check YPos for scrolling purposes
			resetXY(); //reset xy to start drawing text
			clearScreen(); //clear screen for repaint lines
			scrollText();
		}
		else{
			YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();
		}
	}

	@Override
	public void clearScreen() {
		Globals.world.clearRect(0, 0, Globals.world.width(), Globals.world.height());
	}

	@Override
	public void resetXY() {
		XPos = 0;
		YPos = Globals.world.startYPos();
	}

	@Override
	public void handleInput() {
		while (!Globals.kernelInputQueue.isEmpty()) {
			String next = Globals.kernelInputQueue.removeFirst();
			tabBuffer.addLast(next);
			int x = Globals.world.measureText(XPos, next);

			if(tabBuffer.peekLast().equals("38")){
				traversal(makeline(tabBuffer),true);
				++udpos;
			}

			if (tabBuffer.peekFirst().equals("9:0")) {//if tab is pressed before other keys are
				tabBuffer.remove(0);
			} else if (tabBuffer.peekLast().equals("9:0") && tabBuffer.size() > 1) {//if tab is pressed after at least 1 other key
				String lookfor = tabBuffer.get(tabBuffer.size() - 2); //first character of tab word
				tabBuffer.remove(tabBuffer.size() - 2); //removes the extra character
				searchword((makeword(tabBuffer)), lookfor);
			}

			if (next.length() > 1) continue; //TODO: handle special key strokes...

			if (next.equals("\n") || next.equals("\r") || next.equals("" + ((char) 10))) {
				scrollBuffer.addLast(next);
				Globals.osShell.handleInput(buffer);
				buffer = "";
			} else if (next.equals("8")) { //if backspace is pressed..
						if (XPos > 7) { //keep cursor from going past prompt symbol (>)
							buffer = buffer.substring(0, buffer.length() - 1); //remove the last character from the buffer
							XPos = XPos - x; //move the x position backwards 1 character width
							clearChar(next);
							scrollBuffer.removeLast();
						}
						else {
							if (buffer.length() == 1) { //Only 1 character in buffer case
								buffer = "";
								scrollBuffer.removeLast();

								XPos = 7;
							} else if (buffer.length() == 0) { //Empty buffer string case
								buffer = "";

								XPos = 7;
							} else {
								buffer = buffer.substring(0, buffer.length() - 1);
								scrollBuffer.removeLast();
								XPos = 7;
							}
						}
					} else {
						putText("" + next);
						buffer += next;
					}
				}
			}

			public void traversal(LinkedList<String> lines, boolean up){
				if(up==true){//up traversal
					putText(lines.peek());
					buffer=lines.peekLast();
				}
				else{//down traversal

				}

			}

			//creates linked list of lines from a list of characters
			public LinkedList<String> makeline (LinkedList<String> characters) {
			StringBuilder temp= new StringBuilder();
			while(characters.size()>entercounter+1){
				if(characters.get(entercounter).equals("\n")){
					allset.addLast(temp.toString());
					temp.delete(0,entercounter);//clears the temporary string
				} else if(characters.get(entercounter).equals("38")){

				} else if(characters.get(entercounter).equals("40")){

				} else{
					temp.append(characters.get(entercounter));
				}
				++entercounter;

			}
				return allset;
			}

			//takes individual characters list and creates a linked list with each node containing a word/spaces/enters
			public LinkedList<String> makeword (LinkedList characters){
				StringBuilder temp = new StringBuilder();

				while (characters.size() > spacecounter + 1) {
					if (characters.get(spacecounter).equals(" ")) {
						alldone.addLast(temp.toString());
						alldone.addLast(" ");
					} else if (characters.get(spacecounter).equals("\n")) {
						alldone.addLast(temp.toString());
						alldone.addLast("\n");
					} else if (characters.get(spacecounter).equals("9:0")) {


					} else {
						temp.append(characters.get(spacecounter));
					}
					++spacecounter;
				}
				return alldone;
			}



	//takes linked list of words and a character and searches the list for a word that starts with that character, then prints to screen
	public void searchword(LinkedList<String> words, String find){

		Collections.sort(words);
		int traverse=0;
		while(words.size()>traverse){
			if(words.get(traverse).equals("")){
				++traverse;
			}
			else if(!(((words.get(traverse)).substring(0,1)).equals(find))){
				++traverse;
			}
			else {
				String allset=words.get(traverse);
				putText(allset.substring(1));
				buffer=allset;//sets the tab completed word as the buffer
				traverse=999;
			}
		}
	}


	@Override
	public int getXPos() {
		return XPos;
	}

	public int getYPos(){
		return YPos;
	}

	/**
	 *
	 * @function clearChar(String) will remove the character  from the console window itself by repainting a rectangle with the same color as the background
	 *
	 * @param s String type - the character that is to be measured and removed from the screen
	 */

	public void clearChar(String s){

	    int x = Globals.world.measureText(XPos, s);
		// Need to save r,g,b values of text color so it can be reset after changing color for backspace..
		int r = Globals.world.getPage().getColor().getRed();
		int g = Globals.world.getPage().getColor().getGreen();
		int b = Globals.world.getPage().getColor().getBlue();

        Globals.world.setBackground(Globals.world.getBackground());
        Globals.world.setColor(0,0,0); //Set color to background color for repainting over characters
        Globals.world.getPage().fillRect(getXPos(),getYPos()-12, x, 14);//Repaint over character(s)
        Globals.world.setColor(r,g,b);//Return text color to original color
        Globals.world.repaint();
    }


	/**
	 *
	 * scrollText will be responsible for removing items from the linked list buffer and
	 * drawing it to the console
	 *
	 */

	public void scrollText(){
		// loop through the buffer until the designated y position is reached..
		while(!scrollBuffer.isEmpty() && getYPos() < 372) {
			String line = scrollBuffer.removeFirst();
			if (line.length() == 1) { //if line contains a single character
				Globals.world.drawText(XPos, YPos, line);
				int offset = Globals.world.measureText(XPos, line);
				XPos += offset;
				if (scrollBuffer.peekFirst() != null) {
					String buff = scrollBuffer.peekFirst();
					if (buff.equals("\n")) { //will check for full word inputs
						XPos = 0;
						YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();
					}
				}
			}
			else{//if line contains an entire word
				XPos = 0;
				Globals.world.drawText(XPos, YPos, line);
				YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();

			}
		}
	}

    /**
     *
     * Print Center method is used solely for visual effect of a kernel trap error.  Nothing more, nothing less.
     *
     * @param s String - the message you'd like displayed in the middle of the console window
     */

	public void printCenter(String s){
	    XPos = 250;
	    YPos = 200;
	    putText(s.toUpperCase());
    }

}




