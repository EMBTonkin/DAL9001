// Display stuff.  
// I (Lizzie) am going to figure out how this file is going to work later.
// If you finish all the well outlined file's first though, feel free to take a stab!

/// so is this the 'view' or 'controller'?  -SS2
/// It's the view. -LT


// imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;

import java.util.HashSet;
import java.util.Iterator;


/**
 * The Display class is the View of the model view controller
 * Will have various functions to visualize data generated/ requested by the user.
 */
class Display extends JPanel{		
	
	private HeaderPanel header;
	private DisplayPanel panel;
	private Dragon active;
	private Analysis analysis;
	
	/**
	 * Constructor creates a JPanel with two sub panels,
	 * 		the DisplayPanel which is where the dragon tree visualization lives
	 * 		the HeaderPanel which is where specific dragon data is displayed
	 */
	public Display(Analysis ana){
		super();
		
		panel = new DisplayPanel();
		header = new HeaderPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
   		this.add(header); 
		this.add(panel); 
		
		this.analysis = ana;
		this.active = null;
		
	}	
	
	
	/**
	 * Updates the bit where the dragon family tree is displayed
	 * @param dragons a HashSet of dragons currently being displayed
	 */
	public void update(HashSet<Dragon> dragons) {
		Graphics g = panel.getGraphics();
		panel.paintComponent( g, dragons );
		this.requestFocus();
	}
	
	
	/**
	 * Display or remove display of active dragon stats
	 * 
	 * @param toBeActive either the Dragon object becoming active, or null signifying remove active dragon.
	 */
	public void setActiveDragon(Dragon toBeActive){
		Graphics g = header.getGraphics();
		this.active = toBeActive;
		if (toBeActive == null){
			System.out.println("Deactivate");
			this.header.paintComponent(g,this.active);
			
		}
		else{
			System.out.println("Make it active");
			this.header.paintComponent(g,this.active);
		}
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
		 private JScrollPane scroll;
		 private JTextArea text;
		 
		public HeaderPanel()
		{
			super();
			// Necessary to set all three for fixed size.
			this.setPreferredSize(new Dimension(1500, 150));
			this.setMaximumSize(new Dimension(1500, 150));
			this.setMinimumSize(new Dimension(1500, 150));
			this.setBackground(new Color(0,255,255));
			
			//create a scroll area with a text area inside to display the dragon's notes.
			this.text = new JTextArea ( 8, 20 ); 
			text.setEditable ( false ); // set textArea non-editable
			this.scroll = new JScrollPane ( text );
			scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
			this.add ( scroll );
		}

		


		/**
		 * Method overridden from JComponent that is responsible for
		 * drawing components on the screen.  The supplied Graphics
		 * object is used to draw.
		 * 
		 * @param g		the Graphics object used for drawing
		 * @param toBeActive either the Dragon object becoming active, or null signifying remove active dragon.	
		 */
		public void paintComponent(Graphics g, Dragon active){
			super.paintComponent(g);
			// if nothing is active, clear space by doing nothing
			if (active == null){
				this.scroll.setVisible(false);
				return;
				
			}
			
			// find if name, ID, or hatchday is longer, set longer as base length
			int widthName = g.getFontMetrics().stringWidth(active.getName());
			int widthID = g.getFontMetrics().stringWidth(active.getID());
			int widthBirthday = g.getFontMetrics().stringWidth(active.getHatchDay());
			int winner = Math.max(widthName, widthID);
			int base = Math.max(winner,widthBirthday);
			
			// write the info to left of dragon
			g.setFont(new Font("Arial", Font.PLAIN, 14)); 
			g.setColor(Color.black);
			g.drawString(active.getName(), 35, 45);
			g.drawString(active.getID(), 35, 65);
			g.drawString(active.getHatchDay(), 35, 85);
			
			// Add image of the dragon
			g.drawImage(active.getDragonDisplay().getImage(), base+45, 35, null);
			g.drawRect(base+45,35,75,75);
			
			// write info to right of dragon
			String TMT = "Male";
			if (active.getMatingType()){
				TMT = "Female";
			}
			g.drawString(TMT+" "+active.getSpecies(), base+130, 45);
			g.drawString(active.getColor(1)+" "+active.getGene(1), base+130, 65);
			g.drawString(active.getColor(2)+" "+active.getGene(2), base+130, 85);
			g.drawString(active.getColor(3)+" "+active.getGene(3), base+130, 105);
			
			// find which was longest
			int width1 =  g.getFontMetrics().stringWidth(TMT+" "+active.getSpecies());
			int width2 =  g.getFontMetrics().stringWidth(active.getColor(1)+" "+active.getGene(1));
			int width3 =  g.getFontMetrics().stringWidth(active.getColor(2)+" "+active.getGene(2));
			int width4 =  g.getFontMetrics().stringWidth(active.getColor(3)+" "+active.getGene(3));
			int winner1 = Math.max(width1, width2);
			int winner2 = Math.max(width3, width4);
			base += Math.max(winner1,winner2);
			
			// draw color rectangles. (redundant, but I like 'em)
			g.setColor(Color.decode(analysis.getColorHex(active.getColor(1))));
			g.fillRect(base+140,35,55,75);
			g.setColor(Color.decode(analysis.getColorHex(active.getColor(2))));
			g.fillRect(base+205,35,55,75);
			g.setColor(Color.decode(analysis.getColorHex(active.getColor(3))));
			g.fillRect(base+270,35,55,75);
			g.setColor(Color.black);
			//add some frames
			g.drawRect(base+140,35,55,75);
			g.drawRect(base+205,35,55,75);
			g.drawRect(base+270,35,55,75);
			
			//set the scroll box to visible and set the text
			this.scroll.setVisible(true);
			this.scroll.updateUI();
   			this.text.setText(active.getComment());
		}
		
		
	} //end of DisplayPanel
}


// Sorry, no test Function.  Please see DAL9001.java for testing.