// Dragon class that holds one dragon instance.

// Note that if a function exists it might just be a dummy function for other things
// Similarly, if you finish a function, note is as done in these notes

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
	private DragonDisplay displayStuff; // the image objects and other things
	
	// constructor for when there is already an XML object with the proper values
	public Dragon(Node aDragon) {
		this.us = (Element) aDragon;
		//System.out.println(us.getElementsByTagName("id").item(0).getTextContent());
		
		// create, then add the Dragon Display object.
		DragonDisplay placeholder = new DragonDisplay(this);
		this.displayStuff = placeholder;
	}

	/**
		constructor for new completely Dragon--must generate new (empty) XML tree per sample:
		---
		<Dragon>
		  <id>1</id>
		  <name>Scale</name>
		  <parents></parents> 
		  <children>
			<int>3</int>
			<int>4</int>
		  </children>
		  
		  <hatchDay>10-05-2015</hatchDay>
		  <exalted>False</exalted>
		  <matingType>True</matingType>
		  
		  <species>Mirror</species> 
		  <primary>
			<color>Blue</color>
			<gene>Basic</gene>
		  </primary>
		  <secondary>
			<color>White</color>
			<gene>Basic</gene>
		  </secondary>
		  <tertiary>
			<color>Red</color>
			<gene>Underbelly</gene>
		  </tertiary>
		  
		  <comment>Favorite Dragon</comment>
		  
		  <DAL9000> // here is some stuff my program would have.
			<image>../images/7410122.gif</image>
			<gen>1</gen>
			
			<ancestors2></ancestors2>
			<ancestors3></ancestors3>
			<ancestors4></ancestors4>
			<ancestors5></ancestors5>
			
			<descendants2>
			  <int>6</int>
			</descendants2>
			<descendants3></descendants3>
			<descendants4></descendants4>
			<descendants5></descendants5>
			
			<xPos>0</xPos>
			<yPos>0</yPos>
		  </DAL9000>
		</Dragon>
		---
		@author StrykeSlammerII
		
		@param doc is the main Document passed in from DragonTree, so there is no confusion as to what Document all these Nodes are attached to. 
		Test code is in DragonTree.java; possibly bad design but this class has no self-contained print/save functionality, yet. 
	*/
	public Dragon(Document doc)
	{	
		
		// create primary Element and children Elements
		us = doc.createElement("Dragon");
		us.appendChild(doc.createElement("id"));
		us.appendChild(doc.createElement("name"));
		us.appendChild(doc.createElement("parents")); // empty array thus far
		us.appendChild(doc.createElement("children")); // empty array thus far
		us.appendChild(doc.createElement("hatchDay"));
		us.appendChild(doc.createElement("exalted"));
		us.appendChild(doc.createElement("matingType"));
		us.appendChild(doc.createElement("species"));
		Element primary = doc.createElement("primary");
		primary.appendChild(doc.createElement("color"));
		primary.appendChild(doc.createElement("gene"));
		us.appendChild( primary );
		Element secondary = doc.createElement("secondary");
		secondary.appendChild(doc.createElement("color"));
		secondary.appendChild(doc.createElement("gene"));
		us.appendChild( secondary );
		Element tertiary = doc.createElement("tertiary");
		tertiary.appendChild(doc.createElement("color"));
		tertiary.appendChild(doc.createElement("gene"));
		us.appendChild( tertiary );
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
		Element xpos = doc.createElement("xPos");
		xpos.appendChild(doc.createTextNode("0"));
		dal.appendChild(xpos);
		Element ypos = doc.createElement("yPos");
		ypos.appendChild(doc.createTextNode("0"));
		dal.appendChild(ypos);
		
		// System.out.println( generation + ", size: " + generation.getElementsByTagName("*").getLength() );
		
		// System.out.println( dal + ", size: " + dal.getElementsByTagName("*").getLength() );
		
		us.appendChild( dal );		
		// System.out.println( us + ", size: " + us.getElementsByTagName("*").getLength() );
		
		doc.getDocumentElement().appendChild( us ); // can't append an Element to a document (??) -SS2
		
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
		
		// create, then add the Dragon Display object.
		DragonDisplay placeholder = new DragonDisplay(this);
		this.displayStuff = placeholder;
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
	 * Get the DragonDisplay of this dragon
	 * 
	 * @return the DragonDisplay object, which you can then get stuff from.
	 */
	public DragonDisplay getDragonDisplay(){
		return this.displayStuff;	
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
	 * @return string[] a list of the parent ID strings.  
	 */
	public String[] getParents(){
		Element eElement= (Element) us.getElementsByTagName("parents").item(0);
		NodeList list = eElement.getElementsByTagName("int");
		String[] parents = new String[list.getLength()];
		for (int i = 0; i < list.getLength(); i++) {
			parents[i] = list.item(i).getTextContent();
		}
		return parents;
	}
	
	/**
	 * Set the parents of this dragon
	 * 
	 * @param parents string[] a list of the parent ID strings.  
	 */
	public void setParents(String[] parents){
		Element eElement= (Element) us.getElementsByTagName("parents").item(0);
		NodeList list = eElement.getElementsByTagName("int");
		int startLength = list.getLength();
		for (int i = 0; i < startLength; i++) {
			eElement.removeChild(list.item(0));//always remove the first item because as you remove them they shuffle up.  You know what I mean
		}
		for (int i = 0; i < parents.length; i++) {
			Element child = eElement.getOwnerDocument().createElement("int");
			child.appendChild(eElement.getOwnerDocument().createTextNode(parents[i]));
			eElement.appendChild(child);
		}
	}
	
	
	/**
	 * Get the children of this dragon
	 * 
	 * @return string[] the children as a list of String IDs.  
	 */
	public String[] getChildren(){
		Element eElement= (Element) us.getElementsByTagName("children").item(0);
		NodeList list = eElement.getElementsByTagName("int");
		String[] children = new String[list.getLength()];
		for (int i = 0; i < list.getLength(); i++) {
			children[i] = list.item(i).getTextContent();
		}
		return children;
	}
	
	/**
	 * Set the children of this dragon
	 * 
	 * @param children string[] the children as a list of String IDs.  
	 */
	public void setChildren(String[] children){
		Element eElement= (Element) us.getElementsByTagName("children").item(0);
		NodeList list = eElement.getElementsByTagName("int");
		int startLength = list.getLength();
		for (int i = 0; i < startLength; i++) {
			eElement.removeChild(list.item(0));//always remove the first item because as you remove them they shuffle up.  You know what I mean
		}
		for (int i = 0; i < children.length; i++) {
			Element child = eElement.getOwnerDocument().createElement("int");
			child.appendChild(eElement.getOwnerDocument().createTextNode(children[i]));
			eElement.appendChild(child);
		}
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
	 * Set the hatch day of this dragon
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
	 * Get the Species of this dragon
	 * 
	 * @return string the species.  
	 */
	public String getSpecies(){
		return us.getElementsByTagName("species").item(0).getTextContent();	
	}
	
	/**
	 * Set the Species of this dragon
	 * 
	 * @param newSpecies string the Species.  
	 */
	public void setSpecies(String newSpecies){
		us.getElementsByTagName("species").item(0).setTextContent(newSpecies);
	}
	
	
	/**
	 * get a Color of this dragon
	 * 
	 * @param level int is a number representing Primary, Secondary, or Tertiary
	 * @return string the color name at this level
	 */
	public String getColor(int level){
		// default to level 1 (primary)
		Element eElement = (Element) us.getElementsByTagName("primary").item(0);
		// if it's not really primary fix it
		if (level == 2){
			eElement = (Element) us.getElementsByTagName("secondary").item(0);
		}
		if (level == 3){
			eElement = (Element) us.getElementsByTagName("tertiary").item(0);
		}
		return eElement.getElementsByTagName("color").item(0).getTextContent();
	}
	
	/**
	 * Set a Color of this dragon
	 * 
	 * @param level int is a number representing Primary, Secondary, or Tertiary
	 * @param color string the name (capitalized).  
	 */
	public void setColor(int level, String color){
		// default to level 1 (primary)
		Element eElement = (Element) us.getElementsByTagName("primary").item(0);
		// if it's not really primary fix it
		if (level == 2){
			eElement = (Element) us.getElementsByTagName("secondary").item(0);
		}
		if (level == 3){
			eElement = (Element) us.getElementsByTagName("tertiary").item(0);
		}
		eElement.getElementsByTagName("color").item(0).setTextContent(color);
	}
	
	
	/**
	 * get a gene of this dragon
	 * 
	 * @param level int is a number representing Primary, Secondary, or Tertiary
	 * @return string the gene name at this level
	 */
	public String getGene(int level){
		// default to level 1 (primary)
		Element eElement = (Element) us.getElementsByTagName("primary").item(0);
		// if it's not really primary fix it
		if (level == 2){
			eElement = (Element) us.getElementsByTagName("secondary").item(0);
		}
		if (level == 3){
			eElement = (Element) us.getElementsByTagName("tertiary").item(0);
		}
		return eElement.getElementsByTagName("gene").item(0).getTextContent();
	}
	
	/**
	 * Set the Color of this dragon
	 * 
	 * @param level int is a number representing Primary, Secondary, or Tertiary
	 * @param color string the name (capitalized).  
	 */
	public void setGene(int level, String gene){
		// default to level 1 (primary)
		Element eElement = (Element) us.getElementsByTagName("primary").item(0);
		// if it's not really primary fix it
		if (level == 2){
			eElement = (Element) us.getElementsByTagName("secondary").item(0);
		}
		if (level == 3){
			eElement = (Element) us.getElementsByTagName("tertiary").item(0);
		}
		eElement.getElementsByTagName("gene").item(0).setTextContent(gene);
	}
	
	
	/**
	 * Get the comment of this dragon
	 * 
	 * @return string the comment.  
	 */
	public String getComment(){
		return us.getElementsByTagName("comment").item(0).getTextContent();	
	}
	
	/**
	 * Set the comment of this dragon
	 * 
	 * @param comment string the comment.  
	 */
	public void setComment(String comment){
		us.getElementsByTagName("comment").item(0).setTextContent(comment);
	}
	
	
	/**
	 * Get the image location of this dragon
	 * 
	 * @return string the the image file name.  
	 */
	public String getImage(){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		return eElement.getElementsByTagName("image").item(0).getTextContent();
	}
	
	/**
	 * Set the image location of this dragon
	 * 
	 * @param imageName string the image file name.  
	 */
	public void setImage(String imageName){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		eElement.getElementsByTagName("image").item(0).setTextContent(imageName);
	}
	
	
	/**
	 * Get the 'generation' of this dragon
	 * 
	 * @return int the generation designation.  
	 */
	public int getGen(){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		return Integer.parseInt( eElement.getElementsByTagName("gen").item(0).getTextContent() );
	}
	
	/**
	 * Set the 'generation' of this dragon
	 * 
	 * @param imageName int the generation designation.  
	 */
	public void setGen(int gen){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		eElement.getElementsByTagName("gen").item(0).setTextContent( Integer.toString(gen));
	}
	
	
	/**
	 * Get the ancestors of this dragon
	 * 
	 * @param level int generations from current dragon
	 * @return string[] the ancestors as a list of String IDs.  
	 */
	public String[] getAncestors(int level){
		Element DAL9000 = (Element) us.getElementsByTagName("DAL9000").item(0);
		//start with default, which is level 2
		Element eElement = (Element) DAL9000.getElementsByTagName("ancestors2").item(0);
		if (level == 1){
			return this.getParents();
		}
		if (level == 3){
			eElement = (Element) DAL9000.getElementsByTagName("ancestors3").item(0);
		}
		if (level == 4){
			eElement = (Element) DAL9000.getElementsByTagName("ancestors4").item(0);
		}
		if (level == 5){
			eElement = (Element) DAL9000.getElementsByTagName("ancestors5").item(0);
		}
		NodeList list = eElement.getElementsByTagName("int");
		String[] ansestors = new String[list.getLength()];
		for (int i = 0; i < list.getLength(); i++) {
			ansestors[i] = list.item(i).getTextContent();
		}
		return ansestors;
	}
	
	/**
	 * Set the ancestors of this dragon
	 * 
	 * @param level int generations from current dragon
	 * @param parents string[] the ancestors as a list of String IDs.  
	 */
	public void setAncestors(int level, String[] ancestors){
		Element DAL9000 = (Element) us.getElementsByTagName("DAL9000").item(0);
		//start with default, which is level 2
		Element eElement = (Element) DAL9000.getElementsByTagName("ancestors2").item(0);
		if (level == 1){
			this.setParents(ancestors);
			return;
		}
		if (level == 3){
			eElement = (Element) DAL9000.getElementsByTagName("ancestors3").item(0);
		}
		if (level == 4){
			eElement = (Element) DAL9000.getElementsByTagName("ancestors4").item(0);
		}
		if (level == 5){
			eElement = (Element) DAL9000.getElementsByTagName("ancestors5").item(0);
		}
		NodeList list = eElement.getElementsByTagName("int");
		int startLength = list.getLength();
		for (int i = 0; i < startLength; i++) {
			eElement.removeChild(list.item(0));//always remove the first item because as you remove them they shuffle up.  You know what I mean
		}
		for (int i = 0; i < ancestors.length; i++) {
			Element child = eElement.getOwnerDocument().createElement("int");
			child.appendChild(eElement.getOwnerDocument().createTextNode(ancestors[i]));
			eElement.appendChild(child);
		}
	}
	
	
	/**
	 * Get the descendants of this dragon
	 * 
	 * @param level int generations from current dragon
	 * @return string[] the descendants as a list of String IDs.  
	 */
	public String[] getDescendants(int level){
		Element DAL9000 = (Element) us.getElementsByTagName("DAL9000").item(0);
		//start with default, which is level 2
		Element eElement = (Element) DAL9000.getElementsByTagName("descendants2").item(0);
		if (level == 1){
			return this.getChildren();
		}
		if (level == 3){
			eElement = (Element) DAL9000.getElementsByTagName("descendants3").item(0);
		}
		if (level == 4){
			eElement = (Element) DAL9000.getElementsByTagName("descendants4").item(0);
		}
		if (level == 5){
			eElement = (Element) DAL9000.getElementsByTagName("descendants5").item(0);
		}
		NodeList list = eElement.getElementsByTagName("int");
		String[] descendants = new String[list.getLength()];
		for (int i = 0; i < list.getLength(); i++) {
			descendants[i] = list.item(i).getTextContent();
		}
		return descendants;
	}
	
	/**
	 * Set the descendants of this dragon
	 * 
	 * @param level int generations from current dragon
	 * @param parents string[] the descendants as a list of String IDs.  
	 */
	public void setDescendants(int level, String[] descendants){
		Element DAL9000 = (Element) us.getElementsByTagName("DAL9000").item(0);
		//start with default, which is level 2
		Element eElement = (Element) DAL9000.getElementsByTagName("descendants2").item(0);
		if (level == 1){
			this.setChildren(descendants);
			return;
		}
		if (level == 3){
			eElement = (Element) DAL9000.getElementsByTagName("descendants3").item(0);
		}
		if (level == 4){
			eElement = (Element) DAL9000.getElementsByTagName("descendants4").item(0);
		}
		if (level == 5){
			eElement = (Element) DAL9000.getElementsByTagName("descendants5").item(0);
		}
		NodeList list = eElement.getElementsByTagName("int");
		int startLength = list.getLength();
		for (int i = 0; i < startLength; i++) {
			eElement.removeChild(list.item(0));//always remove the first item because as you remove them they shuffle up.  You know what I mean
		}
		for (int i = 0; i < descendants.length; i++) {
			Element child = eElement.getOwnerDocument().createElement("int");
			child.appendChild(eElement.getOwnerDocument().createTextNode(descendants[i]));
			eElement.appendChild(child);
		}
	}
	
	
	/**
	 * Get the x position of this dragon
	 * 
	 * @return int the x position.  
	 */
	public int getX(){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		return Integer.parseInt( eElement.getElementsByTagName("xPos").item(0).getTextContent() );
	}
	
	/**
	 * Set the x position of this dragon
	 * 
	 * @param imageName int x position.  
	 */
	public void setX(int x){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		eElement.getElementsByTagName("xPos").item(0).setTextContent( Integer.toString(x));
	}
	
	
	/**
	 * Get the y position of this dragon
	 * 
	 * @return int the y position.  
	 */
	public int getY(){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		return Integer.parseInt( eElement.getElementsByTagName("yPos").item(0).getTextContent() );
	}
	
	/**
	 * Set the y position of this dragon
	 * 
	 * @param imageName int y position.  
	 */
	public void setY(int y){
		Element eElement = (Element) us.getElementsByTagName("DAL9000").item(0);
		eElement.getElementsByTagName("yPos").item(0).setTextContent( Integer.toString(y));
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
		
		// test the image name getter and setter
		System.out.println("\nTest getImage().  Expected result: '194792.gif'");
		System.out.println(testSubject.getImage());
		
		System.out.println("\nTest setImage().  Expected result: 'splorge.jpg'");
		testSubject.setImage("splorge.jpg");
		System.out.println(testSubject.getImage());
		
		
		// test the image generation getter and setter
		System.out.println("\nTest getGen().  Expected result: '1'");
		System.out.println(testSubject.getGen());
		
		System.out.println("\nTest setGen().  Expected result: '5'");
		testSubject.setGen(5);
		System.out.println(testSubject.getGen());
		
		
		// test the color getter and setter for each level
		System.out.println("\nTest getColor() on primary.  Expected result: 'Blue'");
		System.out.println(testSubject.getColor(1));
		
		System.out.println("\nTest getColor() on secondary.  Expected result: 'White'");
		System.out.println(testSubject.getColor(2));
		
		System.out.println("\nTest getColor() on tertiarty.  Expected result: 'Red'");
		System.out.println(testSubject.getColor(3));
		
		System.out.println("\nTest getColor() on non 1 2 3 number.  Expected result: 'Blue'");
		System.out.println(testSubject.getColor(5));
		
		System.out.println("\nTest setColor()on primary.  Expected result: 'Purple'");
		testSubject.setColor(1,"Purple");
		System.out.println(testSubject.getColor(1));
		
		System.out.println("\nTest setColor()on secondary.  Expected result: 'Maroon'");
		testSubject.setColor(2,"Maroon");
		System.out.println(testSubject.getColor(2));
		
		System.out.println("\nTest setColor()on secondary.  Expected result: 'Yellow'");
		testSubject.setColor(3,"Yellow");
		System.out.println(testSubject.getColor(3));
		
		
		// test the ancestor getter and setter for each level
		System.out.println("\nTest setAncestors() and getAncestors() on all five levels.  Expected result: sequential numbers starting at 3, with lists also increasing, but the last one is empty because.");
		String[] list = {"3"};
		testSubject.setAncestors(1,list);
		String[] list2 = {"4","5"};
		testSubject.setAncestors(2,list2);
		String[] list3 = {"6","7","8"};
		testSubject.setAncestors(3,list3);
		String[] list4 = {"9","10","11","12"};
		testSubject.setAncestors(4,list4);
		System.out.println(Arrays.toString(testSubject.getAncestors(1)));
		System.out.println(Arrays.toString(testSubject.getAncestors(2)));
		System.out.println(Arrays.toString(testSubject.getAncestors(3)));
		System.out.println(Arrays.toString(testSubject.getAncestors(4)));
		System.out.println(Arrays.toString(testSubject.getAncestors(5)));
		
		
		// test some edge cases with the third level of descendants because why not?
		System.out.println("\nTest setDescendants() and setDescendants() on the third level, checking some edge cases.  Expected result: [] then [9, 10, 11, 12] then [] then [1, 2, 3]");
		System.out.println(Arrays.toString(testSubject.getDescendants(3)));
		String[] listy = {"9","10","11","12"};
		testSubject.setDescendants(3,listy);
		System.out.println(Arrays.toString(testSubject.getDescendants(3)));
		String[] listy2 = {};
		testSubject.setDescendants(3,listy2);
		System.out.println(Arrays.toString(testSubject.getDescendants(3)));
		String[] listy3 = {"1","2","3"};
		testSubject.setDescendants(3,listy3);
		System.out.println(Arrays.toString(testSubject.getDescendants(3)));
		
		} catch (Exception e) {
		System.out.println(e.getMessage());
    	}
		
	}

}