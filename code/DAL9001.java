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
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

import java.util.Arrays;
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
		Control control = new Control();
		
		//Text is here, this can be changed dynamically
		JLabel status = new JLabel("       Control Panel       ");
		
		JButton open = new JButton("Open");
		open.addActionListener(control);
		
		JButton save = new JButton("Save");
		save.addActionListener(control);
		
		JButton add = new JButton("Add");
		add.addActionListener(control);
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(control);
		
		JButton quit = new JButton("Quit");
		quit.addActionListener(control);
		
		
		
		JPanel panel = new JPanel();
		
		panel.add(status);
		panel.add(open);
		panel.add(save);
		panel.add(add);
		panel.add(reset);
		panel.add(quit);
		panel.setPreferredSize(new Dimension(200, 1000)); // nessasarry to set all three for a fixed size.
		panel.setMaximumSize(new Dimension(200, 1000));
		panel.setMinimumSize(new Dimension(200, 1000));
		panel.setBackground(new Color(113,30,15));
		
		
		// bring our beautiful creation together.
		this.add(panel); 
		this.add(this.canvas);
		
		this.pack();
		this.setVisible(true);
		
		// initialize variables
		this.activeDragon = null;
		this.tree = new DragonTree();
		// initialize state of no selected dragon.
		canvas.setActiveDragon(this.activeDragon);
		
	}
	
	
	/**
	 * Creates a dialog with option boxes and stuff for dragons.
	 * @return a list of string versions of all chosen parameters
	 */
	public String[] addDragonDialog(){
		String[] breedNames = {"Fae","Guardian","Mirror","Pearlcatcher","Ridgeback","Tundra","Spiral","Imperial","Snapper","Wildclaw","Nocturne","Coatl","Skydancer"};
		String[] geneNames1	= {"Basic","Iridescent","Tiger","Clown","Speckle","Ripple","Bar","Crystal","Vipera","Piebald","Cherub", "Poison"};
		String[] geneNames2 = {"Basic","Shimmer","Stripes","Eye Spots","Freckle","Seraph","Current","Daub","Facet","Hypnotic","Paint","Peregrine", "Toxin","Butterfly"};
		String[] geneNames3 = {"Basic","Circuit","Gembond","Underbelly","Crackle","Smoke","Spines","Okapi","Glimmer","Thylacine","Stained"};
		String[] colorNames = {"Maize","White","Ice","Platinum","Silver","Gray","Charcoal","Coal","Black","Obsidian","Midnight","Shadow","Mulberry","Thistle","Lavender","Purple","Violet","Royal","Storm","Navy","Blue","Splash","Sky","Stonewash","Steel","Denim","Azure","Caribbean","Teal","Aqua","Seafoam","Jade","Emerald","Jungle","Forest","Swamp","Avocado","Green","Leaf","Spring","Goldenrod","Lemon","Banana","Ivory","Gold","Sunshine","Orange","Fire","Tangerine","Sand","Beige","Stone","Slate","Soil","Brown","Chocolate","Rust","Tomato","Crimson","Blood","Maroon","Red","Carmine","Coral","Magenta","Pink","Rose"};
		String[] matingTypes = {"Female", "Male"};
		JTextField name = new JTextField();
		JTextField ID = new JTextField();
		JTextField image = new JTextField();
		JTextField hatchDay = new JTextField();
		JComboBox<String> matingtype = new JComboBox<String>(matingTypes);
		JComboBox<String> breed = new JComboBox<String>(breedNames);
		JComboBox<String> color1 = new JComboBox<String>(colorNames);
		JComboBox<String> color2 = new JComboBox<String>(colorNames);
		JComboBox<String> color3 = new JComboBox<String>(colorNames);
		JComboBox<String> gene1 = new JComboBox<String>(geneNames1);
		JComboBox<String> gene2 = new JComboBox<String>(geneNames2);
		JComboBox<String> gene3 = new JComboBox<String>(geneNames3);
		
		Dragon[] motherNames = {null};
		Dragon[] fatherNames = {null};
		Dragon[] allParents = this.tree.getAllDragons();
		for (int i =0; i<allParents.length; i++){
			if (allParents[i].getMatingType()){
				Dragon[] temp = {allParents[i]};
				motherNames = concatinate(motherNames, temp);
			}
			else{
				Dragon[] temp = {allParents[i]};
				fatherNames = concatinate(fatherNames, temp);
			}
		}
		JComboBox<Dragon> fathers = new JComboBox<Dragon>(fatherNames);
		JComboBox<Dragon> mothers = new JComboBox<Dragon>(motherNames);
		
		JTextArea text = new JTextArea ( 8, 20 ); 
		text.setEditable ( true ); // set textArea non-editable
		JScrollPane scroll = new JScrollPane ( text );
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		GridLayout experimentLayout = new GridLayout(0,2);
		
		JPanel container = new JPanel();
		container.setLayout(experimentLayout);
		container.add(new JLabel("Name"));
		container.add(name);
		container.add(new JLabel("ID"));
		container.add(ID);
		container.add(new JLabel("Image Name"));
		container.add(image);
		container.add(new JLabel("Hatch Day"));
		container.add(hatchDay);
		container.add(new JLabel("Mating Type"));
		container.add(matingtype);
		container.add(new JLabel("Breed"));
		container.add(breed);
		container.add(new JLabel("Mother"));
		container.add(mothers);
		container.add(new JLabel("Father"));
		container.add(fathers);
		container.add(new JLabel("Primary"));
		container.add(new JLabel(" "));
		container.add(color1);
		container.add(gene1);
		container.add(new JLabel("Secondary"));
		container.add(new JLabel(" "));
		container.add(color2);
		container.add(gene2);
		container.add(new JLabel("Tertiary"));
		container.add(new JLabel(" "));
		container.add(color3);
		container.add(gene3);
		//container.add(new JLabel("Notes"));
		//container.add(scroll);
		
		JComponent[] inputs = new JComponent[] {
				container,
				new JLabel("Notes"),
				scroll,
		};
		JOptionPane.showMessageDialog(null, inputs, "Enter Dragon Information", JOptionPane.PLAIN_MESSAGE);
		Dragon mother = (Dragon) mothers.getSelectedItem();
		String motherID = "";
		if (mother != null){
			motherID = mother.getID();
		}
		Dragon father = (Dragon) fathers.getSelectedItem();
		String fatherID = "";
		if (father != null){
			fatherID = father.getID();
		}
		String[] results = {name.getText(),ID.getText(),image.getText(),hatchDay.getText(),
							(String)matingtype.getSelectedItem(),(String)breed.getSelectedItem(),
							motherID,fatherID,
							(String)color1.getSelectedItem(),(String)gene1.getSelectedItem(),
							(String)color2.getSelectedItem(),(String)gene2.getSelectedItem(),
							(String)color3.getSelectedItem(),(String)gene3.getSelectedItem(),
							text.getText(),
							};
		System.out.println(Arrays.toString(results));
		// recursive validation
		for (int i=0;i<4;i++){
			if (results[i].equals("")){
				JOptionPane.showMessageDialog(this,
                                "Please fill out all the fields.");
				return addDragonDialog(results);
			}
		}
		return results;
	}
	
	/**
	 * Creates a dialog with option boxes and stuff for dragons.
	 * @return a list of string versions of all chosen parameters
	 */
	public String[] addDragonDialog(String[] input){
		String[] breedNames = {"Fae","Guardian","Mirror","Pearlcatcher","Ridgeback","Tundra","Spiral","Imperial","Snapper","Wildclaw","Nocturne","Coatl","Skydancer"};
		String[] geneNames1	= {"Basic","Iridescent","Tiger","Clown","Speckle","Ripple","Bar","Crystal","Vipera","Piebald","Cherub", "Poison"};
		String[] geneNames2 = {"Basic","Shimmer","Stripes","Eye Spots","Freckle","Seraph","Current","Daub","Facet","Hypnotic","Paint","Peregrine", "Toxin","Butterfly"};
		String[] geneNames3 = {"Basic","Circuit","Gembond","Underbelly","Crackle","Smoke","Spines","Okapi","Glimmer","Thylacine","Stained"};
		String[] colorNames = {"Maize","White","Ice","Platinum","Silver","Gray","Charcoal","Coal","Black","Obsidian","Midnight","Shadow","Mulberry","Thistle","Lavender","Purple","Violet","Royal","Storm","Navy","Blue","Splash","Sky","Stonewash","Steel","Denim","Azure","Caribbean","Teal","Aqua","Seafoam","Jade","Emerald","Jungle","Forest","Swamp","Avocado","Green","Leaf","Spring","Goldenrod","Lemon","Banana","Ivory","Gold","Sunshine","Orange","Fire","Tangerine","Sand","Beige","Stone","Slate","Soil","Brown","Chocolate","Rust","Tomato","Crimson","Blood","Maroon","Red","Carmine","Coral","Magenta","Pink","Rose"};
		String[] matingTypes = {"Female", "Male"};
		JTextField name = new JTextField();
		name.setText(input[0]);
		JTextField ID = new JTextField();
		ID.setText(input[1]);
		JTextField image = new JTextField();
		image.setText(input[2]);
		JTextField hatchDay = new JTextField();
		hatchDay.setText(input[3]);
		JComboBox<String> matingtype = new JComboBox<String>(matingTypes);
		matingtype.setSelectedItem(input[4]);
		JComboBox<String> breed = new JComboBox<String>(breedNames);
		breed.setSelectedItem(input[5]);
		JComboBox<String> color1 = new JComboBox<String>(colorNames);
		color1.setSelectedItem(input[8]);
		JComboBox<String> color2 = new JComboBox<String>(colorNames);
		color2.setSelectedItem(input[10]);
		JComboBox<String> color3 = new JComboBox<String>(colorNames);
		color3.setSelectedItem(input[12]);
		JComboBox<String> gene1 = new JComboBox<String>(geneNames1);
		gene1.setSelectedItem(input[9]);
		JComboBox<String> gene2 = new JComboBox<String>(geneNames2);
		gene2.setSelectedItem(input[11]);
		JComboBox<String> gene3 = new JComboBox<String>(geneNames3);
		gene3.setSelectedItem(input[13]);
		
		Dragon[] motherNames = {null};
		Dragon[] fatherNames = {null};
		Dragon[] allParents = this.tree.getAllDragons();
		for (int i =0; i<allParents.length; i++){
			if (allParents[i].getMatingType()){
				Dragon[] temp = {allParents[i]};
				motherNames = concatinate(motherNames, temp);
			}
			else{
				Dragon[] temp = {allParents[i]};
				fatherNames = concatinate(fatherNames, temp);
			}
		}
		JComboBox<Dragon> mothers = new JComboBox<Dragon>(motherNames);
		Dragon current = null;
		if (!input[6].equals("")){
			current = this.tree.getDragonByID(input[6]);
		}
		mothers.setSelectedItem(current);
		JComboBox<Dragon> fathers = new JComboBox<Dragon>(fatherNames);
		current = null;
		if (!input[7].equals("")){
			current = this.tree.getDragonByID(input[7]);
		}
		fathers.setSelectedItem(current);
		
		JTextArea text = new JTextArea ( 8, 20 ); 
		text.setEditable ( true ); // set textArea non-editable
		text.setText(input[14]);
		JScrollPane scroll = new JScrollPane ( text );
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		GridLayout experimentLayout = new GridLayout(0,2);
		
		JPanel container = new JPanel();
		container.setLayout(experimentLayout);
		container.add(new JLabel("Name"));
		container.add(name);
		container.add(new JLabel("ID"));
		container.add(ID);
		container.add(new JLabel("Image Name"));
		container.add(image);
		container.add(new JLabel("Hatch Day"));
		container.add(hatchDay);
		container.add(new JLabel("Mating Type"));
		container.add(matingtype);
		container.add(new JLabel("Breed"));
		container.add(breed);
		container.add(new JLabel("Mother"));
		container.add(mothers);
		container.add(new JLabel("Father"));
		container.add(fathers);
		container.add(new JLabel("Primary"));
		container.add(new JLabel(" "));
		container.add(color1);
		container.add(gene1);
		container.add(new JLabel("Secondary"));
		container.add(new JLabel(" "));
		container.add(color2);
		container.add(gene2);
		container.add(new JLabel("Tertiary"));
		container.add(new JLabel(" "));
		container.add(color3);
		container.add(gene3);
		//container.add(new JLabel("Notes"));
		//container.add(scroll);
		
		JComponent[] inputs = new JComponent[] {
				container,
				new JLabel("Notes"),
				scroll,
		};
		
		JOptionPane.showMessageDialog(null, inputs, "Enter Dragon Information", JOptionPane.PLAIN_MESSAGE);
		Dragon mother = (Dragon) mothers.getSelectedItem();
		String motherID = "";
		if (mother != null){
			motherID = mother.getID();
		}
		Dragon father = (Dragon) fathers.getSelectedItem();
		String fatherID = "";
		if (father != null){
			fatherID = father.getID();
		}
		String[] results = {name.getText(),ID.getText(),image.getText(),hatchDay.getText(),
							(String)matingtype.getSelectedItem(),(String)breed.getSelectedItem(),
							motherID,fatherID,
							(String)color1.getSelectedItem(),(String)gene1.getSelectedItem(),
							(String)color2.getSelectedItem(),(String)gene2.getSelectedItem(),
							(String)color3.getSelectedItem(),(String)gene3.getSelectedItem(),
							text.getText(),
							};
		
		System.out.println(Arrays.toString(results));
		
		// recursive validation
		// right now can't get cancel button so just return empty set
		for (int i=0;i<4;i++){
			if (results[i].equals("")){
				JOptionPane.showMessageDialog(this,
                                "2 failures, Dragon creation aborted.");
				return null;
				//return addDragonDialog(results[0],results[1]);
			}
		}
		
		return results;
	}
	
	
	
	/**
	 * Creates a file dialog and then loads a selected DRG file into the program
	 */
	public void openDRGFile(){
		// create a file dialog, and set it to only take .drg files
		FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".drg");
			}
		});
		fd.setVisible(true);
		
		// analyze the results
		String filename = fd.getDirectory()+fd.getFile();
		if (fd.getFile() == null){
			return;
		}
		else{
			this.tree = new DragonTree(filename);
			// once we load the tree, display it.
			HashSet<Dragon> dragons = (HashSet<Dragon>) this.tree.getDragonsByExalted(false);
			canvas.update(dragons);
		}
	}
	
	/**
	 * Creates a file dialog and then saves the current DragonTree object
	 */
	public void saveDRGFile(){
		// create a file dialog, and set it to only take .drg files
		FileDialog fd = new FileDialog(this, "Choose destination", FileDialog.SAVE);
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".drg");
			}
		});
		fd.setFile(this.tree.getFilename());
		fd.setVisible(true);
		
		String filename = fd.getDirectory()+fd.getFile();
		// return if they had a blank filename
		if (fd.getFile() == null){
			return;
		}
		// otherwise, save the file and update the object's filename
		else{
			this.tree.save(filename);
			this.tree.setFilename(fd.getFile());
		}		
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
			else if( event.getActionCommand().equalsIgnoreCase("Open") ) {
				openDRGFile();
			}
			else if( event.getActionCommand().equalsIgnoreCase("Save") ) {
				saveDRGFile();
			}
			else if( event.getActionCommand().equalsIgnoreCase("Reset") ) {
				System.out.println("Nothing to reset yet");
			}
			
			
			else if( event.getActionCommand().equalsIgnoreCase("Add") ) {
				System.out.println("Add a dragon");
				String[] results = addDragonDialog();
				Dragon n00b = new Dragon(tree.getDocument());
				n00b.setName(results[0]);
				n00b.setID(results[1]);
				n00b.setHatchDay(results[3]);
				if (results[4].equals("Male")){
					n00b.setMatingType(false);
				}
				else{
					n00b.setMatingType(true);
				}
				n00b.setSpecies(results[5]);
				
				String[] parents = n00b.getParents();
				if (!results[6].equals("")) {
					String[] mom = {results[6]};
					String[] moreparents = tree.concatinate(parents, mom);
					n00b.setParents(moreparents);
				}
				parents = n00b.getParents();
				if (!results[7].equals("")){
					String[] dad = {results[7]};
					String[] moreparents = tree.concatinate(parents, dad);
					n00b.setParents(moreparents);
				}
				n00b.setColor(1,results[8]);
				n00b.setColor(2,results[10]);
				n00b.setColor(3,results[12]);
				n00b.setGene(1,results[9]);
				n00b.setGene(2,results[11]);
				n00b.setGene(3,results[13]);
				n00b.setComment(results[14]);
				n00b.setImage(results[2]);
				n00b.setExalted(false);
				
				tree.addDragon(n00b);
				
				// display the changes
				HashSet<Dragon> dragons = (HashSet<Dragon>) tree.getDragonsByExalted(false);
				canvas.update(dragons);
				System.out.println("Dragon added!");	
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
			// update the dragon display area to reflect which dragon is active
			canvas.update(dragons);
		}
		
		// calculations for comparison of two dragons
		public void mouseMoved(MouseEvent e) {
			if (activeDragon != null){
				// find if we are hovering over anything, and if so what
				Dragon hoverDragon = null;
				HashSet<Dragon> dragons = (HashSet<Dragon>) tree.getDragonsByExalted(false);
				// create an iterator
				Iterator<Dragon> iterator = dragons.iterator(); 
				// check to see if hover was within any bounding boxes
				while (iterator.hasNext()){
					Dragon current = iterator.next();
					if (current.getDragonDisplay().getBoundingBox().contains(e.getX(),e.getY())){
						// If the hover was in a box, set the hover dragon variable to that Dragon 
						hoverDragon = current;
					}
				}
				// if hovering over something and it's not the active one than us, analyze match
				if ((hoverDragon != null) && (!hoverDragon.getID().equals(activeDragon.getID()))){
					canvas.compareDragon(activeDragon,hoverDragon);
				} 
				// If hovering over nothing reset to normal active dragon display
				else{
					canvas.setActiveDragon(activeDragon);
				}
			}
		}
		
		// get the starting point for dragging
		public void mousePressed(MouseEvent e) {
			baseX = e.getX();
			baseY = e.getY();
		}
			
		// image moving calculations
		public void mouseDragged(MouseEvent e) {
			int dx = e.getX()-baseX;
			int dy = e.getY()-baseY;
			// Get the active dragons
			HashSet<Dragon> dragons = (HashSet<Dragon>) tree.getDragonsByExalted(false);
			// create an iterator
			Iterator<Dragon> iterator = dragons.iterator(); 
			// update x and y values by the delta
			while (iterator.hasNext()){
				Dragon current = iterator.next();
				current.setX(current.getX()+dx);
				current.setY(current.getY()+dy); 
				current.getDragonDisplay().translate(dx,dy);
			}
			// need to reset the base stuff every time this function is called
			baseX = e.getX();
			baseY = e.getY();
			// display the changes
			canvas.update(dragons);
			
		}

	}	
	
	
	/**
	 * Because I can
	 *
	 * Code copied from http://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java because I was lazy today
	 *  
	 * @param a a Dragon[] to be concatenated with b
	 * @param b a Dragon[] to be concatenated with a
	 * @return Dragon[] concatenation of a and b
	 */
	public Dragon[] concatinate(Dragon[] a, Dragon[] b){
		int aLen = a.length;
		int bLen = b.length;
		Dragon[] c= new Dragon[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
		
	// Main function
	// Runs the program, also used for testing since we are testing UI.
	public static void main(String[] args)  {
		DAL9001 thing = new DAL9001();
	}
}