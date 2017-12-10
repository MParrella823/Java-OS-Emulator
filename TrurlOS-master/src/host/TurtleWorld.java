package host;
import java.util.LinkedList;


// <pre>
/*  Copy this file in its entirety to a file named Turtle.java.
 *  Compile the Turtlet class and then compile this class, before trying to 
 *  compile any program that uses Turtles.
 *  This class draws to an Image object and lets the frame's paint method 
 *  show the Image whenever the frame repaints itself. It is for 
 *  Turtle commands that are given in or from a main application. */

import os.ShellCommandFunction;
import util.Globals;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Date;


/** A TurtleWorld is a JFrame on which an Image object is drawn each time 
 *  the JFrame is repainted.  Each Turtle draws on that Image object. */

public class TurtleWorld extends javax.swing.JFrame implements MouseListener{

	private static final int EDGE = 13, TOP = 60;  // around the JFrame
	private Image itsPicture;
	private Image buttonSpace;
	private Image PCBstatus;
	private Image Memstatus;
	private Image Procstatus;
	public Graphics ProcPainter;
	public Graphics MemPainter;
	private Graphics itsPage;
	private Graphics buttonPainter;
	public Graphics PCBPainter;
	private FontMetrics itsMetrics;
	private int width;
	private int height;
	private boolean startActive = true, haltActive = false;
	public Date date = new Date();
	public boolean memWrite; //If true, writing to memory

	public static ProcessEntry[] processArray = new ProcessEntry[3];

	ProcessEntry p1 = new ProcessEntry(50,60);
	ProcessEntry p2 = new ProcessEntry(50, 80);
	ProcessEntry p3 = new ProcessEntry(50, 100);




	public TurtleWorld (int width, int height)
	{	super ("What's a computer?");  // set the title for the frame
		this.width = width;
		this.height = height;
		addMouseListener(this);
		createButtons();
		createPCBStatus();
		createMemStatus();
		createProcessStatus();

		//statusBar();
		setDefaultCloseOperation (EXIT_ON_CLOSE); // no WindowListener
		setSize (width + 2 * EDGE, height + TOP + EDGE);
		toFront();  // put this frame in front of the BlueJ window
		setVisible (true);  // cause a call to paint
        begin (width, height);
		setFocusTraversalKeysEnabled(false);

		processArray[0] = p1;
		processArray[1] = p2;
		processArray[2] = p3;


	}	//======================

	public void createButtons() {
		buttonSpace = new java.awt.image.BufferedImage(533, 30, java.awt.image.BufferedImage.TYPE_INT_RGB);
		buttonPainter = buttonSpace.getGraphics();

		drawStartButton(buttonPainter, true);
		drawHaltButton(buttonPainter, false);
		drawStatusBar(buttonPainter);



		message();
		repaint();
	}

	public void createPCBStatus(){
		PCBstatus = new java.awt.image.BufferedImage(800, 130, java.awt.image.BufferedImage.TYPE_INT_RGB);
		PCBPainter = PCBstatus.getGraphics();
		drawPCBstatus(PCBPainter);

		repaint();
	}

	public void createMemStatus(){
		Memstatus = new java.awt.image.BufferedImage(800, 130, BufferedImage.TYPE_INT_RGB);
		MemPainter = Memstatus.getGraphics();
		drawMemStatus(MemPainter);

        repaint();
	}

	public void createProcessStatus(){
		Procstatus = new java.awt.image.BufferedImage(800,130, BufferedImage.TYPE_INT_RGB);
		ProcPainter = Procstatus.getGraphics();
		drawProcessStatus(ProcPainter);
		repaint();
	}

	public void begin (int width, int height)
	{	itsPicture = new java.awt.image.BufferedImage (width, height,
			           java.awt.image.BufferedImage.TYPE_INT_RGB);
		itsPage = itsPicture.getGraphics();
		itsPage.setColor (Color.black);
		itsPage.fillRect (0, 60, width-280, height);
		itsPage.setColor (Color.white);
		itsPage.setFont(new Font("monospaced", Font.PLAIN, 12));  //monospaced is easy to read and deal with...
		itsMetrics = itsPage.getFontMetrics();

		repaint();
	}	//======================





	public Graphics getPage()
	{	return itsPage; // itsPicture.getGraphics(); => NO COLORS
	}	//======================


	public void paint (Graphics g)
	{	if (itsPicture != null)
			g.drawImage (itsPicture, EDGE, TOP, this);
			g.drawImage(buttonSpace, EDGE, TOP-28, 533, 30, this);
			g.drawImage(PCBstatus,EDGE+533,TOP-28, 800, 128,this);
			g.drawImage(Memstatus, EDGE+850,TOP-28,800, 130, this);
			g.drawImage(Procstatus, EDGE+1125, TOP-28, 800,130, this);




	}	//======================


	public void drawText(int xPos, int yPos, String string) {
//		if(true) throw new RuntimeException();


		itsPage.drawString(string, xPos, yPos);

//		System.out.println("painting \"" + string + "\"" + " at (" + xPos + ", " + yPos + ")");
		repaint();

	}


	public int measureText(int xPos, String string) {
		return itsMetrics.stringWidth(string);
	}


	public int fontHeightMargin() {
		return itsMetrics.getLeading();
	}


	public int fontDescent() {
		return itsMetrics.getDescent();
	}


	public int fontSize() {
		return itsMetrics.getAscent();
	}


	public int width() {
		return width;
	}


	public int height() {

		return height;
	}


	public void clearRect(int x, int y, int width, int height) {
		itsPage.clearRect(x, y, width, height);
		repaint();

	}



	public int startYPos() {
		return 160;
	}


	public void focus() {
		toFront();  // put this frame in front of the BlueJ window
	}

	public void setColor(int r, int g, int b) {
		itsPage.setColor(new Color(r, g, b));
	}


	
	public void drawStartButton(Graphics g, boolean activated) {
		if(activated) g.setColor(new Color(0, 128, 0));
		else g.setColor(Color.gray);
		g.fillRect(0, 0, 80, 30);
		g.setColor(Color.black);
		g.drawString("START", 20, 22);
	}
	
	public void drawHaltButton(Graphics g, boolean activated) {
		if(activated) g.setColor(new Color(0, 128, 0));
		else g.setColor(Color.gray);
		g.fillRect(80, 0, 80, 30);
		g.setColor(Color.black);
		g.drawString("HALT", 105, 22);
	}


	public void drawStatusBar(Graphics g){
		buttonPainter.setColor(Color.red);
		buttonPainter.fillRect(265, 0, 250, 30);
		buttonPainter.setColor(Color.black);
		buttonPainter.drawString("Date/Time: " + date.toString(), 268, 12);
		buttonPainter.drawString("STATUS: " + "I'm a computer!", 268, 27);
	}

	public void clearPCBdisplay(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0,0,300,128);
		repaint();
		g.drawString("Stack Limit: 0", 5, 40);
		drawPCBstatus(PCBPainter);

	}



	public void drawPCBstatus(Graphics g){
		String s = "init";
		g.setColor(Color.green);
		g.drawRect(0,0, 300, 128);
		g.drawString("Instruction: " + s, 5,20);
		g.drawString("Stack Limit: " + s, 5, 40);
		g.drawString("Program Counter: " + s, 5, 60);
		g.drawString("Process State: " + s, 5, 80);
		g.drawString("Current SP: " + s, 5, 100);
		g.drawString("PID: " + s, 5, 120);
	}

	public void drawProcessStatus(Graphics g){
		String s = "init";
		g.setColor(Color.red);
		g.drawRect(0,0,250,128);
		g.setColor(Color.white);
		g.drawString("Ready Queue:", 5, 15);
		g.setColor(Color.yellow);
		g.drawString("1. " + s, 50, 60);
		g.drawString("2. " + s, 50, 80);
		g.drawString("3. " + s, 50, 100);
	}

	public void updateProcessGUI(){
		String p1 = "null";
		String p2 = "null";
		String p3 = "null";
		int i = 0;
		if (!Globals.readyqueue.isEmpty()) {

			p1 = "PID: " + Globals.readyqueue.getFirst().getPID();

			if (Globals.readyqueue.size() > 1 && Globals.readyqueue.size() < 3) {
				p1 = "PID: " + Globals.readyqueue.get(i).getPID();
				p2 = "PID: " + Globals.readyqueue.get(i + 1).getPID();
			}

			if (Globals.readyqueue.size() > 2) {
				p1 = "PID: " + Globals.readyqueue.get(i).getPID();
				p2 = "PID: " + Globals.readyqueue.get(i+1).getPID();
				p3 = "PID: " + Globals.readyqueue.peekLast().getPID();
			}

			setProcessOne(ProcPainter, p1);
			setProcessTwo(ProcPainter, p2);
			setProcessThree(ProcPainter, p3);

		}




	}

	public void setProcessOne(Graphics g, String s){

		g.setColor(Color.black);
		g.fillRect(60,40, 100, 20);
		g.setColor(Color.yellow);
		g.drawString("1. ", 50, 60);
		g.drawString("" + s, 65, 60);
	}

	public void clearProcessOne(Graphics g){
		g.setColor(Color.black);
		g.fillRect(60, 40, 100, 20);
		g.setColor(Color.yellow);
		g.drawString("1. ", 50, 60);
	}

	public void setProcessTwo(Graphics g, String s){
		g.setColor(Color.black);
		g.fillRect(60, 60, 100, 20);
		g.setColor(Color.yellow);
		g.drawString("2. ", 50, 80);
		g.drawString("" + s, 65, 80);
	}

	public void clearProcessTwo(Graphics g){
		g.setColor(Color.black);
		g.fillRect(60,60,100,20);
		g.setColor(Color.yellow);
		g.drawString("2. ", 50, 80);
	}

	public void setProcessThree(Graphics g, String s){
		g.setColor(Color.black);
		g.fillRect(60, 80, 100,20);
		g.setColor(Color.yellow);
		g.drawString("3. ", 50, 100);
		g.drawString("" + s, 65, 100);

	}

	public void clearProcessThree(Graphics g){
		g.setColor(Color.black);
		g.fillRect(60,80,100,20);
		g.setColor(Color.yellow);
		g.drawString("3. ", 50, 100);
	}





	public void drawMemStatus(Graphics g){
		String s = "init";
		g.setColor(Color.orange);
		g.drawRect(0,0, 250, 128);
		g.setColor(Color.white);
		g.drawString("Memory Status:", 5, 15);
		g.drawString("Location: " + s, 50, 60 );
		g.drawString("Value: " + s, 50, 100);
	}

	public void setMemLocation(Graphics g, int x){
		g.setColor(Color.black);
		g.fillRect(50, 40, 100, 20);
		g.setColor(Color.white);
		g.drawString("Location: ", 50, 60);
		g.setColor(Color.green);
		g.drawString(""+ x, 120, 60);

	}

	public void setMemValue(Graphics g, int x){
		g.setColor(Color.black);
		g.fillRect(50,80,100, 20);
		g.setColor(Color.white);
		g.drawString("Value: ", 50, 100);
		if (this.memWrite){
			g.setColor(Color.red);
			g.drawString("" + x, 120, 100);
		}
		else{
			g.setColor(Color.blue);
			g.drawString("" + x, 120, 100);
		}
	}

	public void setInstruction(Graphics g, int s){
		g.setColor(Color.black);
		g.fillRect(65, 5, 100, 20);
		g.setColor(Color.green);
		g.drawString("Instruction: " + s, 5, 20);
	}

	public void setStackLim(Graphics g, int s){
		g.setColor(Color.black);
		g.fillRect(65,25, 100, 20);
		g.setColor(Color.green);
		g.drawString("Stack Limit: " + s, 5, 40);
	}

	public void setProgCount(Graphics g, int s){
		g.setColor(Color.black);
		g.fillRect(65, 45, 100, 20);
		g.setColor(Color.green);
		g.drawString("Program Counter: " + s, 5, 60);

	}

	public void setProcessState(Graphics g, String s){
		g.setColor(Color.black);
		g.fillRect(85, 65, 100, 20);
		g.setColor(Color.green);
		g.drawString("Process State: " + s, 5, 80);
		repaint();
	}

	public void setCurrSP(Graphics g, int s){
		g.setColor(Color.black);
		g.fillRect(70, 85, 100, 20);
		g.setColor(Color.green);
		g.drawString("Current SP: " + s, 5, 100);
	}

	public void setPID(Graphics g, int s){
		g.setColor(Color.black);
		g.fillRect(30, 105, 100, 20);
		g.setColor(Color.green);
		g.drawString("PID: " + s, 5, 120);
	}

	public void changeStatus(String s){
		buttonPainter.setColor(Color.red);
		buttonPainter.fillRect(265, 0, width()-265, 30);
		buttonPainter.setColor(Color.black);
		buttonPainter.drawString("Date/Time: " +  date.toString(), 268, 12);
		buttonPainter.drawString("STATUS: " + s, 268, 27);
	}



	public void message()
	{
		buttonPainter.setColor(Color.black);
		buttonPainter.fillRect(165, 0, 100, 30);  //just clear the whole thing!
		buttonPainter.setColor(Color.white);
		buttonPainter.drawString(haltActive ? "System Up..." : "System Down...", 165, 22);
	}

	public void scrollText(){
		itsPage.copyArea(0,128,getWidth(),getHeight(),0, -20);
		repaint();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX() < 4 + 81 && e.getX() > 3 && e.getY() > 30 && e.getY() <= 60) {
			if(startActive) {
				Control.startOS();
				startActive = false;
				haltActive = true;
				drawStartButton(buttonPainter, startActive);
				drawHaltButton(buttonPainter, haltActive);
				message();
				repaint();
			}
		} else if(e.getX() < 4 + 81 + 80 && e.getX() > 3 + 80 && e.getY() > 30 && e.getY() <= 60) {
			if(haltActive) {
				Control.haltOS();
				startActive = true;
				haltActive = false;
				drawStartButton(buttonPainter, startActive);
				drawHaltButton(buttonPainter, haltActive);
				message();
				repaint();
			}
		}
		
	}



	@Override
	public void mousePressed(MouseEvent e) {

		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		
	}

	@Override
	public void mouseExited(MouseEvent e) {


	}

	public void clearProcess(int pid){
		ProcessEntry test = Globals.residentList.getProcess(pid).getEntry();
		test.clear();
	}

	public void clearAllProcess(){
		processArray[0].clear();
		processArray[1].clear();
		processArray[2].clear();
	}

	public class ProcessEntry{
		private int x;
		private int y;
		private boolean occupied;
		private String s;
		private Graphics sketcher;
		private int count = 0;
		private int num;

		public ProcessEntry(int x, int y){
			count ++;
			this.occupied = false;
			this.x = x;
			this.y = y;
			this.s = "init";
			this.num = count;
		}

		public void write(String s){
			this.s = s;
			sketcher = Globals.world.ProcPainter;

			if (!this.occupied){
				sketcher.setColor(Color.black);
				sketcher.fillRect(this.x + 10, this.y - 20, 100, 20);
				sketcher.setColor(Color.yellow);
				sketcher.drawString("" + this.num + ".", this.x, this.y);
				sketcher.drawString("" + s, this.x +15, this.y);
			}

			this.occupied = true;


		}

		public void clear(){
			sketcher = Globals.world.ProcPainter;
			sketcher.setColor(Color.black);
			sketcher.fillRect(this.x + 10, this.y - 20, 100, 20);
			sketcher.setColor(Color.yellow);
			sketcher.drawString("" + this.num + ".", this.x, this.y);
			this.occupied = false;

		}





	}//end sub-class




	public static ProcessEntry findAvailableEntry(){

		for (int i = 0; i < processArray.length; i++){
			if (!processArray[i].occupied){
				return processArray[i];
			}
		}
		return null;
	}


}
// </pre>

