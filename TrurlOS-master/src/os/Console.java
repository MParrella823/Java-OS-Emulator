package os;

import com.sun.prism.Graphics;
import host.TurtleWorld;
import javafx.scene.Cursor;
import sun.awt.Graphics2Delegate;
import sun.awt.image.ImageWatched;
import util.Globals;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.lang.*;
import java.util.Collections;
import java.util.LinkedList;




public class Console implements Input, Output{
	private String buffer = "";
	private static LinkedList<String> scrollBuffer  = new LinkedList();
	private static LinkedList<String> tabBuffer= new LinkedList<>();
	private static LinkedList<String> tabMover=new LinkedList<>();
	private int XPos, YPos;
	public Console() {

	}

	public void init() {
		// TODO Auto-generated method stub
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

			//up arrow
			if(tabBuffer.peekLast().equals("38:0")){

			}


			if(tabBuffer.peekFirst().equals("9:0")){//if tab is pressed before other keys are
				tabBuffer.remove(0);
			}
			else if(tabBuffer.peekLast().equals("9:0")&&tabBuffer.size()>1){//if tab is pressed after at least 1 other key
				String lookfor=tabBuffer.get(tabBuffer.size()-2); //first character of tab word
				searchword((makeword(tabBuffer)), lookfor);
			}

			if (next.length() > 1) continue; //TODO: handle special key strokes...

			if (next.equals("\n") || next.equals("\r") ||  next.equals("" + ((char) 10))) {
				//Globals.standardOut.putText("YPos:"+getYPos());
				scrollBuffer.addLast(next);
				//	Globals.standardOut.putText("Size: " + scrollBuffer.size());

				Globals.osShell.handleInput(buffer);
				buffer = "";
			}
			else if (next.equals("8")) { //if backspace is pressed..
				if (XPos > 7) { //keep cursor from going epast prompt symbol (>)
					buffer = buffer.substring(0, buffer.length() - 1); //remove the last character from the buffer

					XPos = XPos - x; //move the x position backwards 1 character width
					clearChar(next);
					scrollBuffer.removeLast();
				} else {
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
			}
			else {
				putText("" + next);
				buffer += next;
			}
		}
	}

	//creates linked list of lines from linked list of words; not finished need to fix makeword method first, will be very similar
	public LinkedList<String> makeline(LinkedList<String> words){
		LinkedList<String> alldone=new LinkedList<>();
		return alldone;
	}

	//takes individual characters list and creates a linked list with each node containing a word/spaces/enters
	public LinkedList<String> makeword(LinkedList characters){

		LinkedList<String> alldone= new LinkedList<>();
		int spacecounter=0;//number of characters up before space/enter

		StringBuilder temp=new StringBuilder();

		while (characters.size()>spacecounter) {
			if(characters.get(spacecounter).equals(" ")) {
				alldone.addLast(temp.toString());
				alldone.addLast(" ");
			}
			else if(characters.get(spacecounter).equals("\n")) {
				alldone.addLast(temp.toString());
				alldone.addLast("\n");
			}
			else {
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
			if(((words.get(traverse)).substring(0,1)).equals(find)){
				String allset=words.get(traverse);
				putText(allset.substring(1));
				buffer=allset;

			}
			++traverse;
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

		// Globals.standardOut.putText("" + getYPos());
		Globals.world.setBackground(Globals.world.getBackground());
		Globals.world.setColor(0,0,0);
		Globals.world.getPage().fillRect(getXPos(),getYPos()-12, x, 14);
		Globals.world.setColor(255,255,255);
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
}




