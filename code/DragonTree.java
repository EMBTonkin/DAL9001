// class to hold the Dragons
// is the Model in the Model View Controller scheme.  

//Needs these methods:
	// a init method that reads in dragons from a file and places them in dragon objects
	// another version where it starts with no file and creates an empty XML object
	// √ (done) save(str fileName): take the current dragon objects and save them in a .drg file
	// getDragon(int ID): a method to get a dragon by it's ID
	// getGeneration(int gen): a method to get all dragons in a certain 'generation'
	// getActiveDragons(): a method to get all dragons that are not 'exalted'
	// addDragon(Dragon newdragon, Dragon mother, Dragon father): adds a dragon to the tree structure, where the parants cane be Null to signify first gen (might re-work this parameter wise.  Feel free to throw out ideas)
		/// shouldn't the parents (null or not) be handled during creation of the newdragon? I think this is done if you concur :) -SS2
	// exalt(Dragon dragon): exalt the current dragon.  Could do any number of things depending on object's internal structure, so do this one last.
	// include any helper functions you might need.
	
// Also include comments and print functions for testing a stuff.  Good design, you know.




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
		*/
	
	// constructor for a family tree with an existing document
	public DragonTree(String newfilename) {
		try {
		
		filename = newfilename;
		
		// create the entire document object
		File file = new File(filename);
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
		filename = "";
		
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
		// add newDragon's Node into the "Family" Element
		Node newNode = newDragon.getNode();
		tree.adoptNode( newNode );
		tree.getElementsByTagName("Family").item(0).appendChild( newNode ); // only a single Family tag thus far in the XML spec
		
		// soon to be list of our dragon objects
		Dragon[] newDragonList = new Dragon[allDragonList.length + 1];
		int i = 0;  // need this index to stick around after the loop
		for (; i < allDragonList.length; i++) 
		{
			newDragonList[i]= allDragonList[i];
		}
		newDragonList[i] = newDragon;
		
		allDragonList = newDragonList;
		
		// System.out.println( "DragonTree.addDragon() : allDragonList.getLength() = " + allDragonList.length );  
		
	}
	
	/**
		@author StrykeSlammerII
		@return Dragon with the requested ID #, or 'null' if ID not found.
		@param ID The dragon ID# to search for (as a string) 
	*/
	public Dragon getDragonByID( String ID )
	{
		System.out.println( allDragonList.length + " dragons to search through." );
		
		for( Dragon d : allDragonList )
		{
			System.out.println( "getDragonByID: param = '" + ID + "' ?= '" + d.getID() + "'" );
			
			if( d.getID() == ID )
				return d; // only one dragon can have a given ID, so no need to keep looking.
		}
		
		return null;
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
	 * Handy dandy test function
	 * 
	 * @param args This parameter is unused.
	 */
	public static void main(String[] args) 
	{
		DragonTree lair = new DragonTree();
		// add a 'blank' dragon template
		Dragon derg = new Dragon();
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
		
		// write to test file 
		lair.save( "empty.drg" );
		System.out.println( "filename: '" + lair.filename + "'" );
		
		// read demo Lair
		lair = new DragonTree("../demo.drg");
		// write out to verify identical
		lair.save("TEST.drg");
		// also verify filename has changed
		System.out.println(lair.filename);
	}

}