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
import java.awt.Point;
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
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.HashSet;	
import java.util.Iterator;

import java.io.FileNotFoundException;

/**
 * Displays Dragon stuff graphically using Swing. 
 * Based on series of projects from Lizzie's CS231.
 * @author Lizzie Tonkin
 */
public class DAL9001 extends JFrame{
	
	private Display canvas; // the view
	private DragonTree tree; // the model
	private int baseX; // used to handle mouse dragging
	private int baseY; // used to handle mouse dragging
	private Dragon activeDragon;
	private Analysis analysis;
	
	
	/**
	 * Initializes a display window.  But soon, so much more!
	 */
	public DAL9001(){
		// setup the window
		super("DAL9001");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// load function libraries
		try{
			this.analysis = new Analysis();
		}
		catch(FileNotFoundException e){
			quit();
		}
		
		// create a panel which will be the View of our MVC
		this.canvas = new Display(this.analysis);
		BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS); // top to bottom
    	this.setLayout(boxLayout);
    	MouseControl mousycontrol = new MouseControl();
    	this.canvas.addMouseListener(mousycontrol); // why is java dumb?
		this.canvas.addMouseMotionListener(mousycontrol);
		
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
		
		// initialize variables
		this.activeDragon = null;
		// initialize state of no selected dragon.
		canvas.setActiveDragon(this.activeDragon);
		
	}
	
	
	/**
	 * Loads a DRG file into the program
	 *
	 * @param filename String file path to the file we will open.
	 */
	public void openDRGFile(String filename){
		this.tree = new DragonTree(filename);
		// once we load the tree, display it.
		HashSet<Dragon> dragons = (HashSet<Dragon>) this.tree.getDragonsByExalted(false);
		canvas.update(dragons);
	}
	
	
	/**
	 * Quits the program.
	 */
	public void quit(){
		this.dispose();
	}	
	
	
	
	
	/**
	 * Inner class that will hopefully to all the listening for key and button stuff
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
	
	/**
	 * Inner class that will hopefully to all the listening for mouse stuff
	 */
	private class MouseControl extends MouseInputAdapter{
			
		// determine which if any dragon was clicked
		public void mouseClicked(MouseEvent e) {
			Dragon clickedDragon = null;
			HashSet<Dragon> dragons = (HashSet<Dragon>) tree.getDragonsByExalted(false);
			// create an iterator
			Iterator<Dragon> iterator = dragons.iterator(); 
			// check to see if click was within any bounding boxes
			while (iterator.hasNext()){
				Dragon current = iterator.next();
				if (current.getDragonDisplay().getBoundingBox().contains(e.getX(),e.getY())){
					// If the click was in a box, set the clicked dragon variable to that Dragon 
					clickedDragon = current;
					System.out.println("You clicked dragon number "+current.getID());
				}
			}
			// if we clicked a dragon, set it to the active dragon
			if (clickedDragon != null){
				activeDragon = clickedDragon;
				canvas.setActiveDragon(clickedDragon);
			}
			// Otherwise we clicked nothing and if there is something active, de-activate it
			else if (activeDragon != null){
				activeDragon = null;
				canvas.setActiveDragon(clickedDragon);
			}
		}
		
		// get the starting point for dragging
		public void mousePressed(MouseEvent e) {
			baseX = e.getX();
			baseY = e.getY();
		}
			
		// image moving calculations
		public void mouseDragged(MouseEvent e) {
			// Get the active dragons
			HashSet<Dragon> dragons = (HashSet<Dragon>) tree.getDragonsByExalted(false);
			// create an iterator
			Iterator<Dragon> iterator = dragons.iterator(); 
			// update x and y values by the delta
			while (iterator.hasNext()){
				Dragon current = iterator.next();
				current.setY(current.getY()+e.getY()-baseY);  
				current.setX(current.getX()+e.getX()-baseX); 
				//need to add 150 to Y because the rectangle counts the header as part of the grid.  Odd.
				current.getDragonDisplay().getBoundingBox().setLocation(current.getX(),current.getY()+150);
			}
			// need to reset the base stuff every time this function is called
			baseX = e.getX();
			baseY = e.getY();
			// display the changes
			canvas.update(dragons);
			
		}

	}	
		
	// Main function
	// Runs the program, also used for testing since we are testing UI.
	public static void main(String[] args)  {
		DAL9001 thing = new DAL9001();
		thing.openDRGFile("../demo.drg");
	}
}