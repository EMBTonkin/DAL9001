// Dragon class that holds one dragon instance.

// Note that if a function exists it might just be a dummy function for other things
// Similarly, if you finish a function, not is as done in these notes

// Needs two fields, 
	//one pointing to this dragon in the XML tree
	//one for a DragonDisplay object

//Should have Get and Set methods for all bits of data/ fields
// eg getID() and setID(int ID)
// all the data bits are the ones in https://gist.github.com/EMBTonkin/6e824478e44fe907434f under the Dragon tag
// for space separated values just return them all in one go (make one getColors method, not three)

// Data should be stored directly in the XML thing and interacted directly with it
// so no ID field.  Just change the ID in the XML.  
// am I being unclear?  Probably.  Get in touch via the most convenient medium and I will clarify.

// Aslo include comments and print functions for testing a stuff.  Good design, you know.



import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;


public class Dragon{
	
	private Element us; // Us, that is this dragon.  The XML node
	
	// constructor for when there is already an XML object with the proper values
	public Dragon(Node aDragon) {
		us = (Element) aDragon;
		//System.out.println(us.getElementsByTagName("id").item(0).getTextContent());
	}

	
	/**
	 * Get the ID of this dragon
	 * 
	 * @return string the ID number.  
	 */
	public String getID(){
		return us.getElementsByTagName("id").item(0).getTextContent();	
	}
	
	/**
	 * Set the ID of this dragon
	 * 
	 * @param newID string the ID number.  
	 */
	public void setID(String newID){
		us.getElementsByTagName("id").item(0).setTextContent(newID);
	}
	
	
	/**
	 * Get the Name of this dragon
	 * 
	 * @return string the name.  
	 */
	public String getName(){
		return us.getElementsByTagName("name").item(0).getTextContent();	
	}
	
	/**
	 * Set the Name of this dragon
	 * 
	 * @param newName string the Name.  
	 */
	public void setName(String newName){
		us.getElementsByTagName("name").item(0).setTextContent(newName);
	}
	
	
	/**
	 * Get the parents of this dragon
	 * 
	 * @return string the parents as space seperated ID.  
	 */
	public String getParents(){
		return us.getElementsByTagName("parents").item(0).getTextContent();	
	}
	
	/**
	 * Set the parents of this dragon
	 * 
	 * @param parents string the parents as space seperated ID.  
	 */
	public void setParents(String parents){
		us.getElementsByTagName("parents").item(0).setTextContent(parents);
	}
	
	
	/**
	 * Get the children of this dragon
	 * 
	 * @return string the children as space seperated ID.  
	 */
	public String getChildren(){
		return us.getElementsByTagName("children").item(0).getTextContent();	
	}
	
	/**
	 * Set the children of this dragon
	 * 
	 * @param parents string the children as space seperated ID.  
	 */
	public void setChildren(String children){
		us.getElementsByTagName("children").item(0).setTextContent(children);
	}
	
	
	/**
	 * Get the hatch day of this dragon
	 * 
	 * @return string the hatch day formated DD-MM-YYYY.  
	 */
	public String getHatchDay(){
		return us.getElementsByTagName("hatchDay").item(0).getTextContent();	
	}
	
	/**
	 * Set the children of this dragon
	 * 
	 * @param date string the hatch day formated DD-MM-YYYY.  
	 */
	public void setHatchDay(String date){
		us.getElementsByTagName("hatchDay").item(0).setTextContent(date);
	}
	
	
	/**
	 * Get the exalt status of the dragon.  True for exalted, False for still around
	 * 
	 * @return Boolean True for exalted, False for still around
	 */
	public boolean getExalted(){
		return Boolean.parseBoolean(us.getElementsByTagName("exalted").item(0).getTextContent());	
	}
	
	/**
	 * Set the exalt status of the dragon. 
	 * 
	 * @param exalted Boolean True for exalted, False for still around
	 */
	public void setExalted(boolean exalted){
		us.getElementsByTagName("exalted").item(0).setTextContent(String.valueOf(exalted));
		
	}
	
	
	
	
	
	
	
	
	/**
	 * Handy dandy test function
	 * 
	 * @param args This parameter is unused.
	 */
	public static void main(String[] args) {
		try {
		
		// create the entire document object
		File file = new File("../demo.drg");
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		
		// list of all the Dragon notes to loop through
		NodeList nList = doc.getElementsByTagName("Dragon");
		// select the fist dragon for testing
		Dragon testSubject = new Dragon(nList.item(0));
		
		// test the ID getter and setter
		System.out.println("\nTest getID().  Expected result: '1'");
		System.out.println(testSubject.getID());
		
		System.out.println("\nTest setID().  Expected result: '101'");
		testSubject.setID("101");
		System.out.println(testSubject.getID());
		
		// test the name getter and setter
		System.out.println("\nTest getName().  Expected result: 'Scale'");
		System.out.println(testSubject.getName());
		
		System.out.println("\nTest setName().  Expected result: 'SharkDude'");
		testSubject.setName("SharkDude");
		System.out.println(testSubject.getName());
		
		// I'm going to be lazy and assume the rest of the string getters and setters work since I did a lot of copy/pasting so the theory is the same barring tyops
		
		// test the exalt getter and setter
		System.out.println("\nTest getExalted().  Expected result: 'false'");
		System.out.println(testSubject.getExalted());
		
		System.out.println("\nTest setExalted().  Expected result: 'true'");
		testSubject.setExalted(true);
		System.out.println(testSubject.getExalted());
		
		} catch (Exception e) {
		System.out.println(e.getMessage());
    	}
		
		
	}

}