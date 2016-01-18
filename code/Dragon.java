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

// Also include comments and print functions for testing a stuff.  Good design, you know.


// added a getter for the XML Node. May be bad design but simplifies interaction with DragonTree's document.


import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.DOMConfiguration;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;

import java.util.Arrays;
import java.lang.StringBuilder;


// next few imports just for debug/test printing, can be safely removed for production build -SS2
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.OutputStreamWriter;


/**
 * Dragon class, contains dragon information and streamlines data access and manipulation<br>
 * DAL9001
 * @author Lizzie Tonkin
 * @author ADD YOUR NAME OR FAKE NAME HERE
 */ 
public class Dragon{
	
	private Element us; // Us, that is this dragon.  The XML node
	
	// constructor for when there is already an XML object with the proper values
	public Dragon(Node aDragon) {
		us = (Element) aDragon;
		//System.out.println(us.getElementsByTagName("id").item(0).getTextContent());
	}

	/**
		constructor for new completely Dragon--must generate new (empty) XML tree per sample:
		---
		<Dragon>
		  <id>1</id>
		  <name>Scale</name>
		  <parents/> 
		  <children/>
		  
		  <exalted>False</exalted>
		  <matingType>True</matingType>
		  
		  <species>Mirror</species> 
		  <primary>Blue Basic</primary>
		  <secondary>Blue Basic</secondary>
		  <tertiary>Blue Underbelly</tertiary>
		  
		  <comment>Favorite Dragon</comment>
		  
		  <DAL9000> // here is some stuff my program would have.
			<image>194792.gif</image>
			<gen>1</gen>
			
			<ancestors2/>
			<ancestors3/>
			<ancestors4/>
			<ancestors5/>
			
			<descendants2>6</descendants2>
			<descendants3/>
			<descendants4/>
			<descendants5/>
			
			<xPos>200</xPos>
			<yPos>100</yPos>
		  </DAL9000>
		</Dragon>
		---
		@author StrykeSlammerII
		
		Test code is in DragonTree.java; possibly bad design but this class has no self-contained print/save functionality, yet. 
	*/
	public Dragon()
	{
		// apparently we need a Document to do the work?
		Document doc;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); // seems convoluted but here we are.
		}
		catch( ParserConfigurationException pce )
		{
			System.out.println( pce.getMessage() );
			doc = null;
		}
		
		DOMConfiguration config = doc.getDomConfig();
		config.setParameter( "namespaces", false );
		
		// create primary Element and children Elements
		us = doc.createElement("Dragon");
		us.appendChild(doc.createElement("id"));
		us.appendChild(doc.createElement("Scale"));
		us.appendChild(doc.createElement("parents"));
		us.appendChild(doc.createElement("children"));
		us.appendChild(doc.createElement("exalted"));
		us.appendChild(doc.createElement("matingType"));
		us.appendChild(doc.createElement("species"));
		us.appendChild(doc.createElement("primary"));
		us.appendChild(doc.createElement("tertiary"));
		us.appendChild(doc.createElement("comment"));
		
		// and children of DAL9000 tag
		Element dal = doc.createElement("DAL9000");
		dal.appendChild(doc.createElement("image"));
		Element generation = doc.createElement("gen");
		generation.appendChild( doc.createTextNode( "1" ) ); // defaults to gen1
		dal.appendChild(generation);
		dal.appendChild(doc.createElement("ancestors2"));
		dal.appendChild(doc.createElement("ancestors3"));
		dal.appendChild(doc.createElement("ancestors4"));
		dal.appendChild(doc.createElement("ancestors5"));
		dal.appendChild(doc.createElement("descendants2"));
		dal.appendChild(doc.createElement("descendants3"));
		dal.appendChild(doc.createElement("descendants4"));
		dal.appendChild(doc.createElement("descendants5"));
		dal.appendChild(doc.createElement("xPos"));
		dal.appendChild(doc.createElement("yPos"));
		
		// System.out.println( generation + ", size: " + generation.getElementsByTagName("*").getLength() );
		
		// System.out.println( dal + ", size: " + dal.getElementsByTagName("*").getLength() );
		
		us.appendChild( dal );		
		// System.out.println( us + ", size: " + us.getElementsByTagName("*").getLength() );
		
		doc.appendChild( us );
		
		// System.out.println( doc + ", size: " + doc.getElementsByTagName("*").getLength() );
		
		
		// apparently printing a Document to System.out is long:
		try
		{
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		
			transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
		}
		catch( Exception e )
		{
			System.out.println( e.getMessage() );
		}
		

		
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
	public String[] getParents(){
		return us.getElementsByTagName("parents").item(0).getTextContent().split(" ");	
	}
	
	/**
	 * Set the parents of this dragon
	 * 
	 * @param parents string the parents as space seperated ID.  
	 */
	public void setParents(String[] parents){
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < parents.length; i++) {
		   strBuilder.append(parents[i]);
		   strBuilder.append(" ");
		}
		String newString = strBuilder.toString();
		us.getElementsByTagName("parents").item(0).setTextContent(newString);
	}
	
	/**
	 * Get the Node of this dragon
	 * 
	 * @return the full XML Node backing store
	 */
	public Node getNode(){
		return us.cloneNode(true);	
	}
	
	/**
	 * Get the children of this dragon
	 * 
	 * @return string the children as space seperated ID.  
	 */
	public String[] getChildren(){
		return us.getElementsByTagName("children").item(0).getTextContent().split(" ");	
	}
	
	/**
	 * Set the children of this dragon
	 * 
	 * @param parents string the children as space seperated ID.  
	 */
	public void setChildren(String[] children){
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < children.length; i++) {
		   strBuilder.append(children[i]);
		   strBuilder.append(" ");
		}
		String newString = strBuilder.toString();
		us.getElementsByTagName("children").item(0).setTextContent(newString);
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
	 * Get the mating type of the dragon.  True for female, False for male
	 * 
	 * @return Boolean True for female, False for male
	 */
	public boolean getMatingType(){
		return Boolean.parseBoolean(us.getElementsByTagName("matingType").item(0).getTextContent());	
	}
	
	/**
	 * Set the mating type of the dragon.  True for female, False for male
	 * 
	 * @param exalted Boolean True for female, False for male
	 */
	public void setMatingType(boolean matingType){
		us.getElementsByTagName("matingType").item(0).setTextContent(String.valueOf(matingType));
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
		System.out.println("\nTest getChildren().  Expected result: [3, 4]");
		System.out.println(Arrays.toString(testSubject.getChildren()));
		
		System.out.println("\nTest setChildren().  Expected result: [3, 4, 5]");
		String[] testArray = {"3","4","5"};
		testSubject.setChildren(testArray);
		System.out.println(Arrays.toString(testSubject.getChildren()));
		
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