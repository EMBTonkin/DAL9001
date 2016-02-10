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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;


/**
 * Displays Dragon stuff graphically using Swing. 
 * Based on series of projects from Lizzie's CS231.
 * @author Lizzie Tonkin
 */
public class DAL9001 extends JFrame{
	
	private boolean running;
	private Display canvas;
	private DragonTree tree;
	
	
	/**
	 * Initializes a display window.  But soon, so much more!
	 */
	public DAL9001(){
		// setup the window
		super("DAL9001");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.running = true;
		
		// create a panel which will be the View of our MVC
		this.canvas = new Display();
		BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS); // top to bottom
    	this.setLayout(boxLayout);
		
		
		// create the side bar, which will be the Controller of our MVC 
		JButton quit = new JButton("Quit");
		Control control = new Control();
		quit.addActionListener(control);
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(control);
		
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
	
	
	/**
	 * Loads a DRG file into the program
	 *
	 * @param filename String file path to the file we will open.
	 */
	public void openDRGFile(String filename){
		this.tree = new DragonTree(filename);
	}
	
	
	/**
	 * Quits the program.
	 */
	public void quit(){
		this.dispose();
	}	
	
	
	/**
	 * Inner class that will hopefully to all the listening for key mouse and button stuff
	 * right now just button stuff, and even then just Quits.
	 */
	private class Control extends KeyAdapter implements ActionListener{
		public void actionPerformed(ActionEvent event){
			
			// if the quit button is pressed call the quit function
			if( event.getActionCommand().equalsIgnoreCase("Quit") ) {
				quit();
			}
			else if( event.getActionCommand().equalsIgnoreCase("Reset") ) {
				System.out.println("Nothing to reset yet");
			}
		}	
	}
		
	// Main function
	// Runs the program, also used for testing since we are testing UI.
	public static void main(String[] args)  {
		DAL9001 thing = new DAL9001();
		thing.openDRGFile("../demo.drg");
	}
}