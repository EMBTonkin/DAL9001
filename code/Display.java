// Display stuff.  
// I (Lizzie) am going to figure out how this file is going to work later.
// If you finish all the well outlined file's first though, feel free to take a stab!

/// so is this the 'view' or 'controller'?  -SS2
/// It's the view. -LT


// imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

import java.util.HashSet;
import java.util.Iterator;


/**
 * The Display class is the View of the model view controller
 * Will have various functions to visualize data generated/ requested by the user.
 */
class Display extends JPanel{		
	
	private HeaderPanel header;
	private DisplayPanel panel;
	
	/**
	 * Constructor creates a JPanel with two sub panels,
	 * 		the DisplayPanel which is where the dragon tree visualization lives
	 * 		the HeaderPanel which is where specific dragon data is displayed
	 */
	public Display(){
		super();
		
		panel = new DisplayPanel();
		header = new HeaderPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
   		this.add(header); 
		this.add(panel); 
		
	}	
	
	
	/**
	 * Updates the display
	 * @param dragons a HashSet of dragons currently being displayed
	 */
	public void update(HashSet<Dragon> dragons) {
		Graphics g = panel.getGraphics();
		panel.paintComponent( g, dragons );
		Graphics g2 = header.getGraphics();
		header.paintComponent( g2 );
		this.requestFocus();
	}
	
	
	/**
	 * This inner class provides the panel on which dragon family elements
	 * are drawn.
	 */
	private class DisplayPanel extends JPanel{
		/**
		 * Creates the panel
		 */
		public DisplayPanel(){
			super();
			this.setPreferredSize(new Dimension(1250, 750));
			this.setBackground(new Color(100,100,0));
		}

		/**
		 * Method overridden from JComponent that is responsible for
		 * drawing components on the screen.  The supplied Graphics
		 * object is used to draw.
		 * 
		 * @param g		the Graphics object used for drawing
		 * @param dragons a HashSet of dragons currently being displayed
		 */
		public void paintComponent(Graphics g, HashSet<Dragon> dragons){
			super.paintComponent(g);
			// create an iterator
			Iterator<Dragon> iterator = dragons.iterator(); 

			// draw each dragon's image in it's position
			while (iterator.hasNext()){
				Dragon current = iterator.next();
				g.drawImage(current.getDragonDisplay().getImage(), current.getX(), current.getY(), null);
				g.drawRect(current.getX(), current.getY(), (int) current.getDragonDisplay().getBoundingBox().getWidth(), (int) current.getDragonDisplay().getBoundingBox().getHeight());

			}
		
			
		}
		
	} //end of DisplayPanel



	/**
	 * This inner class provides the panel on which specific dragon data is displayed
	 */
	private class HeaderPanel extends JPanel{
		/**
		 * Creates the panel.
		 */
		public HeaderPanel()
		{
			super();
			// Necessary to set all three for fixed size.
			this.setPreferredSize(new Dimension(1500, 150));
			this.setMaximumSize(new Dimension(1500, 150));
			this.setMinimumSize(new Dimension(1500, 150));
			this.setBackground(new Color(0,255,255));
		}

		/**
		 * Method overridden from JComponent that is responsible for
		 * drawing components on the screen.  The supplied Graphics
		 * object is used to draw.
		 * 
		 * @param g		the Graphics object used for drawing
		 */
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			// test image and text used as an example so I can remember when I come back.
			BufferedImage image = null;
		
			//add placeholder dragon image
			try {
				image= ImageIO.read(new File("../images/MissingNo.gif"));
			
			
			}
			catch (IOException e) {
				System.out.println("Invalid image ABANDON SHIP\n");
				System.out.println(e);
			}
		
			g.drawImage(image, 35, 35, null);
		
			g.setColor(Color.black);
			g.drawString("Test text because text", 150, 35);
		
		}
		
	} //end of DisplayPanel
}


// Sorry, no test Function.  Please see DAL9001.java for testing.