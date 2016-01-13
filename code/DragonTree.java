// class to hold the Dragons
// is the Model in the Model View Controller scheme.  

//Needs these methods:
	// a init method that reads in dragons from a file and places them in dragon objects
	// another version where it starts with no file and creates an empty XML object
	// save(str fileName): take the current dragon objects and save them in a .drg file
	// getDragon(int ID): a method to get a dragon by it's ID
	// getGeneration(int gen): a method to get all dragons in a certain 'generation'
	// getActiveDragons(): a method to get all dragons that are not 'exalted'
	// addDragon(Dragon dragon, Dragon mother, Dragon father): adds a dragon to the tree structure, where the parants cane be Null to signify first gen
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


public class DragonTree{
	
	private String filename;
	private Document tree;
	
	// constructor for for a family tree with an existing document
	public DragonTree(String newfilename) {
		try {
		
		filename = newfilename;
		
		// create the entire document object
		File file = new File(filename);
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		
		// list of all the Dragon notes to loop through
		NodeList nList = doc.getElementsByTagName("Dragon");
		// soon to be list of our dragon objects
		Dragon[] allDragonList = new Dragon[nList.getLength()];
		for (int i = 0; i < nList.getLength(); i++) {
			allDragonList[i]= new Dragon(nList.item(i));
			//System.out.println(nList.item(i).getElementsByTagName("Name").item(0).getTextContent());
		
		}
		System.out.println("help?");
		System.out.println(allDragonList[0].getID());
		
		allDragonList[0].setID("37");
		System.out.println(allDragonList[0].getID());
		
		
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("TEST.drg"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
		
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
		
		
		System.out.println(derg.filename);
	}

}