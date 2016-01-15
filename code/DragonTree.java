// class to hold the Dragons
// is the Model in the Model View Controller scheme.  

//Needs these methods:
	// a init method that reads in dragons from a file and places them in dragon objects
	// another version where it starts with no file and creates an empty XML object
	// √ (done) save(str fileName): take the current dragon objects and save them in a .drg file
	// getDragon(int ID): a method to get a dragon by it's ID
	// getGeneration(int gen): a method to get all dragons in a certain 'generation'
	// getActiveDragons(): a method to get all dragons that are not 'exalted'
	// addDragon(Dragon dragon, Dragon mother, Dragon father): adds a dragon to the tree structure, where the parants cane be Null to signify first gen (might re-work this parameter wise.  Feel free to throw out ideas)
	// exalt(Dragon dragon): exalt the current dragon.  Could do any number of things depending on object's internal structure, so do this one last.
	// include any helper functions you might need.
	
// Aslo include comments and print functions for testing a stuff.  Good design, you know.




import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import javax.xml.transform.Transformer;
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
	
	// constructor for for a family tree with an existing document
	public DragonTree(String newfilename) {
		try {
		
		filename = newfilename;
		
		// create the entire document object
		File file = new File(filename);
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		tree = dBuilder.parse(file);
		
		// list of all the Dragon notes to loop through
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
	 * Save the DragonTree in it's current state to a file.  Will be in DRG format
	 * 
	 * @param filename String the name of the file you want to save to.  Don't forget the .drg extension!
	 */ 
	public void save(String filename){
		try {
		// write the content into xml file
		// to be honest I have no idea what is happening here.  Copied from http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
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
	public static void main(String[] args) {
		DragonTree derg = new DragonTree("../demo.drg");
		derg.save("TEST.drg");
		
		System.out.println(derg.filename);
	}

}