// class to hold the Dragons
// is the Model in the Model View Controller scheme.  

//Needs these methods:
	// a init method that reads in dragons from a file and places them in dragon objects
	// another version where it starts with no file and creates an empty XML object
		/// done?  -SS2
	// √ (done) save(str fileName): take the current dragon objects and save them in a .drg file
	// getDragon(int ID): a method to get a dragon by it's ID
		/// done for String ID rather than int ID, plz check -SS2
	// getGeneration(int gen): a method to get all dragons in a certain 'generation'
		/// done? -SS2
	// getActiveDragons(): a method to get all dragons that are not 'exalted'
		/// tweaked to getDragonsByExalted( bool ) so we can get all exalted dragons too, just in case. -SS2
	// addDragon(Dragon newdragon, Dragon mother, Dragon father): adds a dragon to the tree structure, where the parants can be Null to signify first gen (might re-work this parameter wise.  Feel free to throw out ideas)
		/// shouldn't the parents (null or not) be handled during creation of the newdragon? I think this is done if you concur :) -SS2
	// exalt(Dragon dragon): exalt the current dragon.  Could do any number of things depending on object's internal structure, so do this one last.
	// include any helper functions you might need.
	
// Also include comments and print functions for testing and stuff.  Good design, you know.




import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * DragonTree class, contains dragons and organizes them.  This is the Model of our Model View Controller.  I think.<br>
 * DAL9001
 * @author Lizzie Tonkin
 * @author ADD YOUR NAME OR FAKE NAME HERE
 */ 
public class DragonTree{
	
	private String filename; // I'm sure we'll find a use for this.
	private Document tree; // the raw XML document object
	private Dragon[] allDragonList; //a simple list of all the dragons in Dragon object form
		/*** ^^^^ 1) why do we need to cache tree.getElementsByTagName("Dragon") ?
		          2) and if we do, why not just keep it as a NodeList? 
		          -SS2
		          
		          1) this is a list of Dragon Objects, not a list of Nodes -LT
		*/
	
	// constructor for a family tree with an existing document
	public DragonTree(String newfilename) {
		try {
		
		String[] filepaths = newfilename.split("/");
		
		filename = filepaths[filepaths.length-1];
		
		// create the entire document object
		File file = new File(newfilename);
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		tree = dBuilder.parse(file);
		
		// list of all the Dragon nodes to loop through
		NodeList nList = tree.getElementsByTagName("Dragon");
		// soon to be list of our dragon objects
		allDragonList = new Dragon[nList.getLength()];
		for (int i = 0; i < nList.getLength(); i++) {
			allDragonList[i]= new Dragon(nList.item(i));
		}
		
		// being lazy with the exceptions
		} catch (Exception e) {
		System.out.println(e.getMessage());
    	}
	}
	
	/**
		Constructor for new/blank DragonTree.
		@author StrykeSlammerII
	*/
	public DragonTree()
	{
		filename = "Untitled.drg";
		
		try
		{
			tree = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); // seems convoluted but here we are.
		}
		catch( ParserConfigurationException pce )
		{
			System.out.println( pce.getMessage() );
			tree = null;
		}
		
		
		/*
			create header XML per specification:
			---
			<drg>
				<Family>
					<title>Master Demo Group</title>
				</Family>
			</drg>
			---
		*/
		
		Element baseNode = tree.createElement( "drg" );
		tree.appendChild( baseNode );
		
		Element newNode = tree.createElement( "Family" );
		baseNode.appendChild( newNode );
		baseNode = newNode;
		
		newNode = tree.createElement( "title" );
		baseNode.appendChild( newNode );
		newNode.appendChild( tree.createTextNode( "Master Demo Group" ) );
		
		allDragonList = new Dragon[0];
	}
	
	/**
		Add a new Dragon to the lair!
		@author StrykeSlammerII
		@param newDragon the dragon to be added to the lair
	*/
	public void addDragon( Dragon newDragon )
	{	
		
		
		// set the Dragon fields that could not be set earlier
		// set mother and father fields (if the parents exist, of course)
		// if there are parents, then use them to update ancestors
		// also set Gen, which defaults to 1 (start at 0 since we add one to previous gen)
		int[] gen= {0,0};
		String[] parentIDs = newDragon.getParents();
		for (int i = 0; i < parentIDs.length; i++){
			Dragon gotten = this.getDragonByID(parentIDs[i]);
			if (gotten.getMatingType()){
				newDragon.setMother(gotten);
			}
			else{
				newDragon.setFather(gotten);
			}
			gen[i] = gotten.getGen();
			
			// add parent's ancestors to our own one gen up
			
			for (int j = 1; j < 5; j++){
				String[] both = concatinate(gotten.getAncestors(j), newDragon.getAncestors(j+1));
				newDragon.setAncestors(j+1, both);
			}
			
			
		}
		newDragon.setGen(Math.max(gen[0]+1,gen[1]+1));
		
		// for all ancestors, add self as a child
		String[] self = {newDragon.getID()};
		// for all the levels
		for (int i = 1; i < 6; i++){
			//get the ancestors of that level
			String[] ansestors = newDragon.getAncestors(i);
			for (int j = 0; j < ansestors.length; j++){
				// for each of the ancestors add self as a descendant
				Dragon ansestor = this.getDragonByID(ansestors[j]);
				String[] both = concatinate(ansestor.getDescendants(i), self);
				ansestor.setDescendants(i, both);
			}
		}
		
		// set default y based on the gen
		newDragon.setY(newDragon.getGen()*100);
		// set the default x based on number of dragons in that gen
		newDragon.setX(getGeneration(newDragon.getGen()).size()*100+100);
		// yes this is extremely broken but it's the best I can do right now -LT
		// have to re-do the display stuff with the new x and y
		DragonDisplay placeholder = new DragonDisplay(newDragon);
		newDragon.setDragonDisplay(placeholder);
		
		
		// something is wrong somewhere
		// The dragon object living in the list of dragon objects gets disconnected from the node in the XML structure
		// This means that moving the dragon around on screen works, but when you save the new position is not saved
		// this would have drastic ramifications once we add the edit button in.
		// I need to come back and fix this, and I will, but I have a job interview where I 
		// plan on presenting this so I am trying to get the basic algorithm working for that.
		// Then I will make this less extremely bad -LT
		// PS future employers if you find this please forgive me and it's not SSII's fault.
		/*
		// soon to be list of our dragon objects
		Dragon[] newDragonList = new Dragon[allDragonList.length + 1];
		int i = 0;  // need this index to stick around after the loop
		for (; i < allDragonList.length; i++) 
		{
			newDragonList[i]= allDragonList[i];
		}
		newDragonList[i] = newDragon;
		
		allDragonList = newDragonList;
		*/
		
		// System.out.println( "DragonTree.addDragon() : allDragonList.getLength() = " + allDragonList.length );  
			
			
		// add newDragon's Node into the "Family" Element
		Node newNode = newDragon.getNode();
		tree.adoptNode( newNode );
		tree.getElementsByTagName("Family").item(0).appendChild( newNode ); // only a single Family tag thus far in the XML spec
		
		
		// Re-create all the dragon objects because something is wrong somewhere (see above)
		NodeList nList = tree.getElementsByTagName("Dragon");
		allDragonList = new Dragon[nList.getLength()];
		for (int i = 0; i < nList.getLength(); i++) {
			allDragonList[i]= new Dragon(nList.item(i));
		}
		
		
	}
	
	
	
	/**
	 * Get ALL the dragons 
	 * I can't believe I forgot we needed this one
	 * @return list of ALL the Dragons
	 */
	public Dragon[] getAllDragons(){
		return allDragonList;
	}
	
	
	/**
		@author StrykeSlammerII
		@return Dragon with the requested ID #, or 'null' if ID not found.
		@param ID The dragon ID# to search for (as a string) 
	*/
	public Dragon getDragonByID( String ID )
	{
		//System.out.println( allDragonList.length + " dragons to search through." );
		
		for( Dragon d : allDragonList )
		{
			//System.out.println( "getDragonByID: param = '" + ID + "' ?= '" + d.getID() + "'" );
			
			if( d.getID().equals(ID) )
				return d; // only one dragon can have a given ID, so no need to keep looking.
		}
		
		return null;
	}
	
	/**
		@author StrykeSlammerII
		@param gen match all dragons that have this generation
		@return (Set of type Dragon) all dragons in this DragonTree with the requested generation 
	*/
	public Set<Dragon> getGeneration( int gen )
	{
		HashSet<Dragon> outList = new HashSet<Dragon>();
		
		for( Dragon d : allDragonList )
		{
			if( d.getGen() == gen )
			{
				outList.add(d);
			}
		}
		
		return outList;
		
	}
	
	/**
		@author StrykeSlammerII
		@param exalted do we want exalted dragons (true) or active dragons (false)
		@return (Set of type Dragon) all dragons in this DragonTree with the requested exalt status 
	*/
	public Set<Dragon> getDragonsByExalted( boolean exalted )
	{
		HashSet<Dragon> outList = new HashSet<Dragon>();
		
		for( Dragon d : allDragonList )
		{
			if( d.getExalted() == exalted )
			{
				outList.add(d);
			}
		}
		
		return outList;
		
	}
	
	/**
	 * Get the Document which maintains this DragonTree's XML backing store
	 * 
	 * @return the XML Document
	 */
	public Document getDocument(){
		return tree;	
	}
	
	/**
	 * Save the DragonTree in it's current state to a file.  Will be in DRG format
	 * 
	 * @param filename String the name of the file you want to save to.  Don't forget the .drg extension!
	     query: filename field should be set, or can be checked against '' (if new/default DragonTree) so is this param extraneous? -SS2  
	 */ 
	public void save(String filename){
		try {
		// write the content into xml file
		// to be honest I have no idea what is happening here.  Copied from http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		
		DOMSource source = new DOMSource(tree);
		StreamResult result = new StreamResult(new File(filename));

		transformer.transform(source, result);
	
		
		// being lazy with the exceptions
		} catch (Exception e) {
		System.out.println(e.getMessage());
    	}
	}
	
	
	/**
	 * Get the default name of the file this object will be saved as
	 *
	 * @return string the name
	 */
	public String getFilename(){
		return this.filename;
	}
	
	/**
	 * Set the default name of the file this object will be saved as
	 *
	 * @return string the name
	 */
	public void setFilename(String newName){
		this.filename = newName;
	}
	
	
	
	/**
	 * Because there is a lot of array concatenation in ancestor management, here's a function
	 *
	 * Code copied from http://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java because I was lazy today
	 *  
	 * @param a a String[] to be concatenated with b
	 * @param b a String[] to be concatenated with a
	 * @return String[] concatenation of a and b
	 */
	public String[] concatinate(String[] a, String[] b){
		int aLen = a.length;
		int bLen = b.length;
		String[] c= new String[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
	
	
	/**
	 * Handy dandy test function
	 * 
	 * @param args This parameter is unused.
	 */
	public static void main(String[] args) 
	{
		DragonTree lair = new DragonTree();
		// add a 'blank' dragon template
		Dragon derg = new Dragon( lair.getDocument() );
		lair.addDragon( derg );
				
		// give the previously-added dragon an ID
		derg.setID( "15" );
		System.out.println( "derg ID set to " + derg.getID() );
		// search for different ID, should get "null"
		derg = lair.getDragonByID( "1" );
		System.out.println( "'" + derg + "' should be 'null'" );
		// search for same ID, should get the above Dragon
		derg = lair.getDragonByID( "15" );
		System.out.println( "'" + derg + "' should have ID 15" );
		
		// test search-for-gen
			// set previous derg's gen
		derg.setGen( 1 );
			// add two more dergs at gen2
		derg = new Dragon( lair.getDocument() );
		derg.setGen( 2 );
		derg.setName( "Bob" );
		lair.addDragon( derg );
		
		derg = new Dragon( lair.getDocument() );
		derg.setGen( 2 );
		derg.setName( "Vila" );
		lair.addDragon( derg );
		
		Set<Dragon> s = lair.getGeneration( 1 );
		System.out.println( "Gen1: " );
		for( Dragon d : s )
			System.out.println( "ID: " + d.getID() + ", Name: " + d.getName() );

		s = lair.getGeneration( 2 );
		System.out.println( "Gen2: " );
		for( Dragon d : s )
			System.out.println( "ID: " + d.getID() + ", Name: " + d.getName() );
			
		s = lair.getGeneration( 3 );
		System.out.println( "Gen3: " );
		for( Dragon d : s )
			System.out.println( "ID: " + d.getID() + ", Name: " + d.getName() );
			
		// check for Exalted--FALSE should be all dragons, TRUE should be empty set
		s = lair.getDragonsByExalted( false );
		System.out.println( "Active dragons: " );
		for( Dragon d : s )
			System.out.println( "ID: " + d.getID() + ", Name: " + d.getName() );
		
		s = lair.getDragonsByExalted( true );
		System.out.println( "Exalted dragons (should be null) " );
		for( Dragon d : s )
			System.out.println( "ID: " + d.getID() + ", Name: " + d.getName() );
		
		
		// write to test file 
		lair.save( lair.filename );
		System.out.println( "filename: '" + lair.filename + "'" );
		
		// read demo Lair
		lair = new DragonTree("../demo.drg");
		// verify filename has changed
		System.out.println(lair.filename);
		// write out to verify identical
		lair.save("TEST.drg");
		
		
		// with the demo file, test adding a new gen 1 dragon
		derg = new Dragon( lair.getDocument() );
		derg.setName( "LizzieIsGreat" );
		derg.setID( "7" );
		derg.setMatingType( true );
		lair.addDragon(derg);
		
		// verify that the mother is not a physical Dragon object
		System.out.println("\nCheck that a mother dragon does not exist");
		System.out.println(derg.getMother());
		
		// verify that the father is mpt a physical Dragon object
		System.out.println("\nCheck that a father dragon does not exist");
		System.out.println(derg.getFather());
		
		// check auto assigned gen
		System.out.println("\nCheck Generation assignment (expect 1)");
		System.out.println(derg.getGen());
		
		// check auto assigned x and y
		System.out.println("\nCheck X and Y assignment (expect 400, 100)");
		System.out.println(derg.getX()+", "+ derg.getY());
		
		// check ancestors
		System.out.println("\nCheck Ancestor generations (expect [],,[],[],[],[] but not necessarily in order)");
		for (int i = 1; i <6;i++){
			System.out.println(Arrays.toString(derg.getAncestors(i)));
		}
		
		
		// add a second gen dragon, so we can test a third gen dragon from two second gen dragons
		derg = new Dragon( lair.getDocument() );
		String[] parentalUnits = {"7","5"};
		derg.setParents(parentalUnits);
		derg.setName( "SkySlammerIIisOK" );
		derg.setID( "8" );
		derg.setMatingType(false);
		lair.addDragon(derg);
		
		// verify that the mother is a physical Dragon object
		System.out.println("\nCheck that a mother dragon exists");
		System.out.println(derg.getMother());
		
		// verify that the father is a physical Dragon object
		System.out.println("\nCheck that a father dragon exists (and is different)");
		System.out.println(derg.getFather());
		
		// check auto assigned gen
		System.out.println("\nCheck Generation assignment (expect 2)");
		System.out.println(derg.getGen());
		
		// check auto assigned x and y
		System.out.println("\nCheck X and Y assignment (expect 300, 200)");
		System.out.println(derg.getX()+", "+ derg.getY());
		
		// check ancestors
		System.out.println("\nCheck Ancestor generations (expect [7,5],[],[],[],[] but not necessarily in order)");
		for (int i = 1; i <6;i++){
			System.out.println(Arrays.toString(derg.getAncestors(i)));
		}
		
		
		// with the demo file, test adding a new gen 3 dragon
		derg = new Dragon( lair.getDocument() );
		String[] parentalUnits2 = {"4","8"};
		derg.setParents(parentalUnits2);
		derg.setName( "HarryPotter" );
		derg.setID( "9" );
		lair.addDragon(derg);
		
		// verify that the mother is a physical Dragon object
		System.out.println("\nCheck that a mother dragon exists");
		System.out.println(derg.getMother());
		
		// verify that the father is a physical Dragon object
		System.out.println("\nCheck that a father dragon exists (and is different)");
		System.out.println(derg.getFather());
		
		// check auto assigned gen
		System.out.println("\nCheck Generation assignment (expect 3)");
		System.out.println(derg.getGen());
		
		// check auto assigned x and y
		System.out.println("\nCheck X and Y assignment (expect 200, 300)");
		System.out.println(derg.getX()+", "+ derg.getY());
		
		// check ancestors
		System.out.println("\nCheck Ancestor generations (expect [4,8],[1,2,7,5],[],[],[] but not necessarily in order within the list)");
		for (int i = 1; i <6;i++){
			System.out.println(Arrays.toString(derg.getAncestors(i)));
		}
		
		// spot check descendants
		derg = lair.getDragonByID("1");
		System.out.println("\nCheck Descendant generations of a first gen dragon (expect [3,4],[6,9],[],[],[] but not necessarily in order within the list)");
		for (int i = 1; i <6;i++){
			System.out.println(Arrays.toString(derg.getDescendants(i)));
		}
		
		// spot check descendants
		derg = lair.getDragonByID("3");
		System.out.println("\nCheck Descendant generations of a second gen dragon (expect [6],[],[],[],[] but not necessarily in order within the list)");
		for (int i = 1; i <6;i++){
			System.out.println(Arrays.toString(derg.getDescendants(i)));
		}
		
	}

}