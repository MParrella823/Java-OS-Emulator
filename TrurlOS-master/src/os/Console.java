package os;

import javafx.scene.Cursor;
import sun.awt.Graphics2Delegate;
import util.Globals;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.lang.*;




public class Console implements Input, Output{
	private String buffer = "";
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
			//            _DrawingContext.drawText(this.currentFont, this.currentFontSize, this.currentXPosition, this.currentYPosition, text);
			Globals.world.drawText(XPos, YPos, string);
			int offset = Globals.world.measureText(XPos, string);
			XPos += offset;
		}
	}

	@Override
	public void advanceLine() {
		XPos = 0;
		YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();
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
		while(! Globals.kernelInputQueue.isEmpty()) {
			String next = Globals.kernelInputQueue.removeFirst();
			int x = Globals.world.measureText(XPos, next);
		    if(next.length() > 1) continue; //TODO: handle special key strokes...
			if(next.equals("\n") || next.equals("\r") || next.equals("" + ((char)10))){
				Globals.osShell.handleInput(buffer);
				buffer = "";
			}else if(next.equals("8")) {
				//Globals.standardOut.putText(Integer.toString(getXPos()));
				if (XPos > 7) { //keep cursor from going past prompt symbol (>)
					XPos = XPos - x;
					Globals.world.clearRect(getXPos(),getYPos(),600,0 );
					break;
				}
				else{
					XPos = 7;
				}
			}
			else {
				putText("" + next);
				buffer += next;
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

}
