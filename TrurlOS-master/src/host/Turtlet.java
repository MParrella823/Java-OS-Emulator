package host;

// <pre>
// Copy this file in its entirety to a file named Turtlet.java.
// Compile it before compiling the Turtle.java file.
// You can put Turtle commands inside the paint method of an applet if
// you declare them as Turtlets rather than as Turtles using something like
// new Turtlet (page, 100, 150), where page is paint's Graphics parameter
// and you replace 100 by any x-coordinate and 150 by any y-coordinate to
// obtain the starting point of your Turtlet.

import java.awt.*;

public class Turtlet extends Object
{	public static final double DEGREE = Math.PI / 180; 
	public static final Color RED = Color.red, BLUE = Color.blue,
	               BLACK = Color.black,      GRAY = Color.gray, 
	               YELLOW = Color.yellow,    PINK = Color.pink,
	               ORANGE = Color.orange,    GREEN = Color.green,
	               MAGENTA = Color.magenta,  WHITE = Color.white;
	private static java.awt.Graphics thePage;
	//////////////////////////////////
	private double heading = 0;         // heading initially east
	private double xcor, ycor;          // current position of Turtle


	/** Set the drawing Color to the given value.  Made an instance method  
	 *  only so that it cannot be called until thePage is assigned, although
	 *  the drawing color is a property of thePage, not of one Turtle. */

	public void switchTo (Color given)
	{	thePage.setColor (given);
	}	//======================


	/** Write words without changing the Turtle's state.  */

	public void say (String message)
	{	thePage.drawString (message, round (xcor), round (ycor));
	}	//======================


	/** Supply the nearest int value to methods requiring ints. */

	private int round (double x)
	{	return (int) (x + 0.5);
	}	//======================


	/** Make a circle of the given radius, Turtle at center. */

	public void swingAround (double radius)
	{	if (radius > 0.0)
		{	int rad = round (2 * radius);
			thePage.drawOval (round (xcor - radius), 
				      round (ycor - radius), rad, rad);
		}
	}	//======================


	/** Fill a circle of the given radius, Turtle at center. */

	public void fillCircle (double radius)
	{	if (radius > 0.0)
		{	int rad = round (2 * radius);
			thePage.fillOval (round (xcor - radius), 
				     round (ycor - radius), rad, rad);
		}
	}	//======================


// the Turtle class, completed

	/** Rotate left by left degrees; MOVE for forward steps. */

	public Turtlet move (double left, double forward)
	{	heading += left * DEGREE;
		xcor += forward * Math.cos (heading);
		ycor -= forward * Math.sin (heading);
		return this;
	}	//======================


	/** Rotate left by left degrees; PAINT for forward steps. */

	public Turtlet paint (double left, double forward)
	{	heading += left * DEGREE;
		double x = xcor + forward * Math.cos (heading);
		double y = ycor - forward * Math.sin (heading);
		thePage.drawLine (round (xcor), round (ycor), 
			            round (x), round (y));
		xcor = x;
		ycor = y;
		return this;
	}	//======================


	/** Fill a rectangle of the given width and height, Turtle at center. */

	public void fillBox (double width, double height)
	{	if (width > 0.0 && height > 0.0)
		{	thePage.fillRect (round (xcor - width / 2), 
				            round (ycor - height / 2), 
				            round (width), round (height));
		}
	}	//======================


	/** Pause the animation for wait milliseconds.  Made a class method
	 *  so that an applet can pause an animation "between turtles". */

	public static void sleep (int wait)
	{	try
		{	Thread.sleep (wait);
		}catch (InterruptedException ex)
		{}
	}	//======================


	/** Create a Turtlet on a given Component at a given starting position. 
	 *  All Turtlets must be created from within the Component's paint()
	 *  method or from a method called by it.  All Turtles live in
	 *  the same world at any given time, so changing the page is analogous
	 *  to spaceshipping the entire Turtle population to a new world. */

	public Turtlet (java.awt.Graphics page, double xstart, double ystart)
	{	if (page == null)
			throw new NullPointerException ("You did not give a "
				             + "world where turtles can live!");
		thePage = page;
		xcor = xstart;
		ycor = ystart;
	}	//======================
}
// </pre>

