// Display stuff.  
// I (Lizzie) am going to figure out how this file is going to work later.
// If you finish all the well outlined file's first though, feel free to take a stab!

/// so is this the 'view' or 'controller'?  -SS2
/// It's the view. -LT


// imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
		panel.paintGrid( g, dragons );
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
		this.header.paintActive(g,this.active);
	}
	
	
	/**
	 * Display various comparison results
	 * 
	 * @param toBeActive either the Dragon object becoming active, or null signifying remove active dragon.
	 */
	public void compareDragon(Dragon activeDrag, Dragon hoverDrag){
		Graphics g = header.getGraphics();
		this.header.paintCompare(g,activeDrag,hoverDrag);
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
		}

		/**
		 * Method overridden from JComponent that is responsible for
		 * drawing components on the screen.  The supplied Graphics
		 * object is used to draw.
		 * 
		 * @param g		the Graphics object used for drawing
		 * @param dragons a HashSet of dragons currently being displayed
		 */
		public void paintGrid(Graphics g, HashSet<Dragon> dragons){
			super.paintComponent(g);
			// create an iterator
			Iterator<Dragon> iterator = dragons.iterator(); 

			// draw each dragon's image in it's position
			while (iterator.hasNext()){
				Dragon current = iterator.next();
				g.drawImage(current.getDragonDisplay().getImage(), current.getX(), current.getY(), null);
				g.drawRect(current.getX(), current.getY(), (int) current.getDragonDisplay().getBoundingBox().getWidth(), (int) current.getDragonDisplay().getBoundingBox().getHeight());
				// draw lines
				int[] coords = current.getDragonDisplay().getMotherLine();
				g.drawLine(coords[0],coords[1],coords[2],coords[3]);
				coords = current.getDragonDisplay().getFatherLine();
				g.drawLine(coords[0],coords[1],coords[2],coords[3]);
			}
			
			// if there is an active dragon draw a green frame around it.
			if (active != null){
				g.setColor(new Color(31,172,38));
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(5));
				g2.drawRect(active.getX(), active.getY(), (int) active.getDragonDisplay().getBoundingBox().getWidth(), (int) active.getDragonDisplay().getBoundingBox().getHeight());
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
			this.setBackground(new Color(218,214,201));
			
			//create a scroll area with a text area inside to display the dragon's notes.
			this.text = new JTextArea ( 8, 20 ); 
			text.setEditable ( false ); // set textArea non-editable
			this.scroll = new JScrollPane ( text );
			scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
			this.add ( scroll );
		}

		


		/**
		 * Sets the header panel with info about the now active dragon, or clears the panel
		 * 
		 * @param g		the Graphics object used for drawing
		 * @param toBeActive either the Dragon object becoming active, or null signifying remove active dragon.	
		 */
		public void paintActive(Graphics g, Dragon active){
			super.paintComponent(g);
			
			// set up font flavor
			g.setFont(new Font("Arial", Font.PLAIN, 14)); 
			g.setColor(Color.black);
			
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
   		
   		
   		
   		
   		/**
		 * Method updates the header panel with the comparison result of two dragons
		 * 
		 * @param g		the Graphics object used for drawing
		 * @param active the active dragon
		 * @param hover the dragon we are comparing the active dragon with
		 */
		public void paintCompare(Graphics g, Dragon active, Dragon hover){
			super.paintComponent(g);
			// set up font flavor
			g.setFont(new Font("Arial", Font.PLAIN, 14)); 
			g.setColor(Color.black);
			
			// make sure have opposite mating types
			if (!analysis.canBreed(active,hover)){
				g.drawString("Incompatible Mating Types :(", 35, 45);
				return;
			}
			
			// find if name, ID, or hatchday is longer, set longer as base length
			int widthName = g.getFontMetrics().stringWidth(active.getName());
			int widthID = g.getFontMetrics().stringWidth(active.getID());
			int widthBirthday = g.getFontMetrics().stringWidth(active.getHatchDay());
			int winner = Math.max(widthName, widthID);
			int base = Math.max(winner,widthBirthday);
			
			// write the info to left of dragon
			g.drawString(active.getName(), 35, 45);
			g.drawString(active.getID(), 35, 65);
			g.drawString(active.getHatchDay(), 35, 85);
			
			// Add image of the dragon
			g.drawImage(active.getDragonDisplay().getImage(), base+45, 35, null);
			g.drawRect(base+45,35,75,75);
			
			// get the rarity comparison results for genes and species
			String species = analysis.rarityCompare(active.getSpecies(),hover.getSpecies());
			String gene1 = analysis.rarityCompare(active.getGene(1),hover.getGene(1));
			String gene2 = analysis.rarityCompare(active.getGene(2),hover.getGene(2));
			String gene3 = analysis.rarityCompare(active.getGene(3),hover.getGene(3));
			
			// write active dragon's genes and percent chance of offspring getting genes
			g.drawString(active.getSpecies()+" "+species.split("/")[0]+"%", base+130, 45);
			g.drawString(active.getGene(1)+" "+gene1.split("/")[0]+"%", base+130, 65);
			g.drawString(active.getGene(2)+" "+gene2.split("/")[0]+"%", base+130, 85);
			g.drawString(active.getGene(3)+" "+gene3.split("/")[0]+"%", base+130, 105);
			
			// find which was longest
			int width1 =  g.getFontMetrics().stringWidth(active.getSpecies()+" "+species.split("/")[0]+"%");
			int width2 =  g.getFontMetrics().stringWidth(active.getGene(1)+" "+gene1.split("/")[0]+"%");
			int width3 =  g.getFontMetrics().stringWidth(active.getGene(2)+" "+gene2.split("/")[0]+"%");
			int width4 =  g.getFontMetrics().stringWidth(active.getGene(3)+" "+gene3.split("/")[0]+"%");
			int winner1 = Math.max(width1, width2);
			int winner2 = Math.max(width3, width4);
			base += Math.max(winner1,winner2);
			
			// for the three layers of color, find the possibilities and display them
			int[] numColors = {0,0,0};
			for (int i = 0; i < 3; i++){
				String[] range = analysis.getColorRange(active.getColor(i+1), hover.getColor(i+1));
				numColors[i]=range.length;
				for (int j = 0; j < range.length; j++){
					g.setColor(Color.decode(analysis.getColorHex(range[j])));
					g.fillRect(base+140+j*10,52+i*20,10,15);
					// outlines
					//g.setColor(Color.black);
					//g.drawRect(base+140+j*10,52+i*20,10,15);
				}
			}
			
			// find out which color was longest
			winner1 = Math.max(numColors[0], numColors[1]);
			base+= Math.max(numColors[2], winner1)*10;
			
			//reset color
			g.setColor(Color.black);
			
			// write hover dragon's genes and percent chance of offspring getting genes
			g.drawString(hover.getSpecies()+" "+species.split("/")[1]+"%", base+150, 45);
			g.drawString(hover.getGene(1)+" "+gene1.split("/")[1]+"%", base+150, 65);
			g.drawString(hover.getGene(2)+" "+gene2.split("/")[1]+"%", base+150, 85);
			g.drawString(hover.getGene(3)+" "+gene3.split("/")[1]+"%", base+150, 105);
			
			// find which was longest
			width1 =  g.getFontMetrics().stringWidth(hover.getSpecies()+" "+species.split("/")[1]+"%");
			width2 =  g.getFontMetrics().stringWidth(hover.getGene(1)+" "+gene1.split("/")[1]+"%");
			width3 =  g.getFontMetrics().stringWidth(hover.getGene(2)+" "+gene2.split("/")[1]+"%");
			width4 =  g.getFontMetrics().stringWidth(hover.getGene(3)+" "+gene3.split("/")[1]+"%");
			winner1 = Math.max(width1, width2);
			winner2 = Math.max(width3, width4);
			base += Math.max(winner1,winner2);
			
			// draw image of hover dragon
			g.drawImage(hover.getDragonDisplay().getImage(), base+160, 35, null);
			g.drawRect(base+160,35,75,75);
			
			// draw basic info of hover dragon
			g.drawString(hover.getName(), base+245, 45);
			g.drawString(hover.getID(), base+245, 65);
			g.drawString(hover.getHatchDay(), base+245, 85);
				
   		}
		
		
	} //end of DisplayPanel
}


// Sorry, no test Function.  Please see DAL9001.java for testing.
