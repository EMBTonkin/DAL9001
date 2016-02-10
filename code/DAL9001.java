// The magical file that somehow brings it all together.

// This will be a group effort once we figure the display stuff out.  

// Remember the good design principals
	// Comments!
	// Test functions.
	// Object Oriented Design
	// Model-View-Controller (whatever that means)
	// Get and Set functions (don't directly edit an object's data)
	// Avoid the singularity (no malevolent sentient programs here)
	
	
// need to sort through these later and see what is actually being used. -LT

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;


/**
 * Displays Dragon stuff graphically using Swing. 
 * Based on series of projects from CS 231.
 * @author Lizzie Tonkin, lovingly ripped off of bseastwo and professor Kyle Burke
 */
public class DAL9001 extends JFrame
{
	private Display canvas;
	
	/**
	 * Initializes a display window.  But soon, so much more!
	 */
	public DAL9001()
	{
		// setup the window
		super("DAL9001");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		// create a panel which will be the View of our MVC
		this.canvas = new Display();
		BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS); // top to bottom
    	this.setLayout(boxLayout);
		
		// create the side bar, which will be the Controller of our MVC 
		
		JButton reset = new JButton("Reset");
		JButton quit = new JButton("Quit");
		JLabel status = new JLabel("Text is here, this can be changed");
		
		JPanel panel = new JPanel();
		
		panel.add(quit);
		panel.add(reset);
		panel.add(status);
		panel.setPreferredSize(new Dimension(200, 1000)); // nessasarry to set all three for a fixed size.
		panel.setMaximumSize(new Dimension(200, 1000));
		panel.setMinimumSize(new Dimension(200, 1000));
		panel.setBackground(new Color(255,0,0));
		
		
		// bring our beautiful creation together.
		this.add(panel); 
		this.add(this.canvas);
		
		this.pack();
		this.setVisible(true);
		
		Graphics g = canvas.getGraphics();
		canvas.update( g );
		
	}
	
	
		
	// Main function
	// Runs the program, also used for testing since we are testing UI.
	public static void main(String[] args)  {
		DAL9001 thing = new DAL9001();
	}
}