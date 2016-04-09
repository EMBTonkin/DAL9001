// why not put all the logical comparisons in one place? 

// Fields necessary will be color info (color name and hexadecimal for each color; see colorInfo.txt)
// and breed and gene rarities and a table to compare them to each other

// Methods:
	// canBreed(Dragon dragon1, Dragon dragon2): Can two dragons breed together?  Check MatingType and ancestry.  Returns Boolean (The data type, not the familiar)
	// colorRange(string color1, string color2): two colors enter, a list of color names of the potential range leaves.
		// done! -SS2
	
		// next 3 done?  -SS2
	// getColorHex(string color): returns the hexadecimal number associated with a color name
		// also getColorIndex(string color) returns the index of color
		// and getColorName(int index) returns the color-name of the given index
		
	// √ (done) rarityCompare(string gene1, string gene2): takes in the name of two genes (or two dragon breeds) and returns two percentages, saying the likely hood of each gene for the offspring.  If same, both are 100%
	// As the DAL program grows, more analysis will be added, and it will be here.  
	
// Aslo include comments and print functions for testing a stuff.  Good design, you know.


 
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


import java.util.concurrent.CopyOnWriteArrayList;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;

// testing imports
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

 
/**
 * Analysis class, contains various functions for analyzing dragons<br>
 * DAL9001
 * @author Lizzie Tonkin
 * @author StrykeSlammerII
 */ 
public class Analysis{

	// fields
    private HashMap<String,Integer> geneRarities; //A hashmap that takes the name of a gene or breed and returns the relative rarity, which can be used to index into the rarityTable
    private String[][] rarityTable  = {{"50/50","70/30","85/15","97/3","99/1"}, //A 2D array that you index into with two gene rarities to get the percent probability of the offspring having those genes.  
						{"30/70","50/50","75/25","90/10","99/1"},
						{"15/85","25/75","50/50","85/15","98/2"},
						{"3/97","10/90","15/85","50/50","97/3"},
						{"1/99","1/99","2/98","3/97","50/50"}}; // For some reason I cannot initialize this in the constructor like the other things.  Odd.  
    
	// make these final (and static? or do we assume/make the whole class a Singleton?) if possible; once it's created [at program initialization] it should never be edited.
	// ColorList is map of String (color name), String (hex # for the color) and Int (position on the color wheel)
		// and will usually be accessible by the color's name, so that's the Key.
    final private TreeMap<String, Object[]> ColorList; // Object[0] is type Integer, Object[1] is type String
    final private HashMap<Integer, String> ColorIndex;
						
    /**
	 * constructor that initializes various things for pre-processing.
	 */
    public Analysis() throws FileNotFoundException {
    	
    	// Pre-processing for rarity comparisons
    	// a string[] of all breed and gene names
		String[] names = {"Fae","Guardian","Mirror","Pearlcatcher","Ridgeback","Tundra","Spiral","Imperial","Snapper","Wildclaw","Nocturne","Coatl","Skydancer", 
				"Basic","Iridescent","Tiger","Clown","Speckle","Ripple","Bar","Crystal","Vipera","Piebald","Cherub", "Poison",
				"Shimmer","Stripes","Eye Spots","Freckle","Seraph","Current","Daub","Facet","Hypnotic","Paint","Peregrine", "Toxin","Butterfly",
				"Circuit","Gembond","Underbelly","Crackle","Smoke","Spines","Okapi","Glimmer","Thylacine","Stained"};
		// the relative rarities of the previous stuff, in the same order.  
		int[] values ={0,0,0,1,1,0,1,3,1,4,3,4,2, 
				 0,4,1,1,1,2,2,4,2,1,2,3,
				 4,1,1,1,2,1,1,4,1,1,1,3,4,
				 4,3,1,2,2,3,2,4,1,4};
		// add these key value pairs to the hashmap.  Simple!
		geneRarities = new HashMap<String,Integer>();
		for (int i = 0; i < names.length; i ++){
			geneRarities.put(names[i], values[i]);
		}
		
		
		// read ../colorInfo.txt into ColorList
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader( new FileReader( "../colorInfo.txt" ), 50); // 50 should be plenty, right? 3 for index, 2 commas, 7 for hexcolor, 38 for color word?
		}
		catch( FileNotFoundException e )
		{
			System.out.println( "colorInfo.txt missing from DAL9001 directory!" );
			throw e; // halt program if the data files are missing.
		}
		
			// put them in a Collection first
		TreeMap<String, Object[]> colors = new TreeMap<String, Object[]>();
		HashMap<Integer, String> indices = new HashMap<Integer, String>(68, 0.986f ); // set initial capacity and load factor to leave one empty bucket, so we don't waste time with any rehashes 
		
		try
		{
			String line = null;
			while( (line = reader.readLine()) != null )
			{
				int index = Integer.parseInt( line.split(",")[0] );
				String name = line.split(",")[1];
				
				Object[] output = new Object[2];
				output[0] = index; 
				output[1] = line.split(",")[2];
				colors.put( name, output );
				
				indices.put( index, name );
				
				//System.out.println( line + "->" + output[0] + "," + output[1] );
				//for( String[] s : colors )
				//	System.out.println( s[0] + "," + s[1] );
				//for( int index = 0; index < colors.size(); index++ )
				//	System.out.println( index + ": " + colors.get(index)[0] + ", " + colors.get(index)[1] );

			}
			reader.close();
		}
		catch( IOException e )
		{
			// unexpected read error! 
			System.out.println( e );
			ColorList = null;
			ColorIndex = null;
			return;
		}
		
		//for( String[] s : colors )
		//	System.out.println( s[0] + "," + s[1] );
		
			// then use constructor on that Collection
		ColorList = new TreeMap<String, Object[]>( colors );
		ColorIndex = new HashMap<Integer, String>( indices );
		//for( String[] s : ColorList )
		//	System.out.println( s[0] + "," + s[1] );
	}
	
	/**
	 * Find out if two dragons can breed.
	 * Currently, only checks if mating types are compatible.
	 * In the future it will check ancestry too (get on that SS2)
	 *
	 * @param drag1, the first of the two comparison Dragons
	 * @param drag2, the second of the two comparison Dragons
	 * @return boolean true if they can breed, false if not. 
	 */
	public boolean canBreed(Dragon drag1, Dragon drag2){
		// are the both male or both female?
		if (drag1.getMatingType()==drag2.getMatingType()) {
			return false;
		}
		
		// here is where we should do the ancestor check
		
		// otherwise, they can have at it.
		else{
			return true;
		}
	}
	
	
	/**
	 * Take two genes (or dragon breeds) and find the probabilities of having them in the offspring
	 *  
	 * @param gene1 String representing the first gene (or dragon breed)
	 * @param gene2 String representing the second gene (or dragon breed)
	 *
	 * @return string with the percentage of getting a gene1 and the percentage of geting a gene2, separated by '/' unless the two genes were the same, in which case it is '100'
	 */
	public String rarityCompare(String gene1, String gene2) {
		// test to see if the two genes are the same
		if (gene1.equals(gene2)){
			return "100/100";
		}
		// get the relative rarity of the two genes in question
		int value1 = geneRarities.get(gene1);
		int value2 = geneRarities.get(gene2);
		// compare them in the magical rarity 2D array to get the percentages
		String rarity = rarityTable[value1][value2];
		return rarity;
	}
	
	/**
	 * Obtain hex-value for the given color.
	 *  
	 * @param color Name of color to find a hex-value for.
	 *
	 * @return String representing the hex-value of the given color. 
	 * @author StrykeSlammerII
	 */
	public String getColorHex( String color )
	{
		Object[] output = ColorList.get( color );
		String myColor;
		
		if( output == null )
			myColor = "(null)";
		else
			myColor = (String)output[1];
		
		return myColor;
	}
	
	/**
	 * Obtain index for the given color.
	 *  
	 * @param color Name of color to find index of.
	 *
	 * @return 1-based index of the given color; -1 if color does not exist. 
	 * @author StrykeSlammerII
	 */
	public int getColorIndex( String color )
	{
		Object[] output = ColorList.get( color );
		int myIndex;
		
		if( output == null )
			myIndex = -1;
		else
			myIndex = (int)output[0];
		
		return myIndex;
	}
	
	/**
	 * Obtain color-name at the given index.
	 *  
	 * @param color Index to lookup.
	 *
	 * @return Indexed name, or null if index does not exist 
	 * @author StrykeSlammerII
	 */
	public String getColorName( int index )
	{
		String output = ColorIndex.get( index );
				
		return output;
	}
	
	/**
	 * return an array of color names along the color wheel from color1 to color2.
	 *  
	 * @param color1 One end of the color range
	 * @param color2 The other end of the color range
	 *
	 * @return array of strings, or null if either color is not indexed. 
	 * @author StrykeSlammerII
	 */
	public String[] getColorRange( String color1, String color2 )
	{
		if( color1 == color2 && (getColorIndex( color1 ) > -1 ) )
			return new String[]{ color1 };
		
		int index1 = getColorIndex( color1 );
		int index2 = getColorIndex( color2 ); 
		
		if( index1 * index2 < 2 ) // if both are not found, the product is 1; if both are index 1, we have already returned; if only 1 is not found, product is < 0
			return null;
		
		// index1 must be < index2
		if( index2 < index1 ) // 
		{
			int swap = index2;
			index2 = index1;
			index1 = swap;
		}
		
		int range = index2 - index1 + 1; // if both elements are the same, there's still a single element in the range
			//System.out.println( index1 + " - " + index2 + " = " + range );
			
		String[] output;
		if( range > ( ColorIndex.size() + 1 )/2 )
		{
			output = new String[ index1 + ( ColorIndex.size() + 1 - index2 ) ]; 
			
			int firstIndex = 0;
			while( ( output[ firstIndex ] = getColorName( index2 + firstIndex ) ) != null )
			{
				firstIndex++;
			}
			firstIndex--;
			
			for( int secondIndex = 1; secondIndex <= index1; secondIndex++ )
			{
				output[ firstIndex + secondIndex ] = getColorName( secondIndex );
			}
		}
		else
		{
			output = new String[range];
			for( int index0 = 0; index0 < range; index0++ )
			{
				output[ index0 ] = getColorName( index1 + index0 );
			}
		}
		
		
		return output;
	}
	
	/**
	 * Return List of Dragons that are ancestors of both dragon1 and dragon2. 
	 * This list only looks back 5 generations for each dragon to consider.
	 *
	 * @author StrykeSlammerII
	 */
	 // test function is in DragonTree.java
	public List<Dragon> CommonAncestors( Dragon dragon1, Dragon dragon2 )
	{
		ArrayList<Dragon> results = new ArrayList<Dragon>();
		
		ArrayList<Dragon> set1 = new ArrayList<Dragon>(); // all of dragon1's ancestors
		ArrayList<Dragon> set2 = new ArrayList<Dragon>(); // all of dragon2's ancestors
		
		// since we can't tell which ones were JUST added to set1 and set2...
		ArrayList<Dragon> lastGen1 = new ArrayList<Dragon>();
		ArrayList<Dragon> lastGen2 = new ArrayList<Dragon>(); 
		 // start with the ones we're testing...
		lastGen1.add( dragon1 );
		lastGen2.add( dragon2 ); 
		
		for( int generation = 1; generation < 6; generation++ )
		{
			// gotta save a working copy of both lastGen so we can save the new ones we find...
			Dragon[] testGen1 = lastGen1.toArray( new Dragon[0] );
			Dragon[] testGen2 = lastGen2.toArray( new Dragon[0] );
			lastGen1 = new ArrayList<Dragon>();
			lastGen2 = new ArrayList<Dragon>();
			
			// get ANY and ALL parents from both last sets of dragons found
			for( Dragon d : testGen1 )
			{
				Dragon d1 = d.getFather();
				Dragon d2 = d.getMother(); 
				
				lastGen1.add( d1 );
				lastGen1.add( d2 );
				
				set1.add( d1 );
				set1.add( d2 );
			}
			for( Dragon d : testGen2 )
			{
				Dragon d1 = d.getFather();
				Dragon d2 = d.getMother(); 
				
				lastGen2.add( d1 );
				lastGen2.add( d2 );
				
				set2.add( d1 );
				set2.add( d2 );
			}
			
		}
		
		for( Dragon d : set1 )
		{
			if( set2.contains( d ) )
				results.add( d );
		}
		
		return results;
	}
	 
	/**
	 * Wrapper method for CommonAncestors( dragon1, dragon2)--if that method returns non-empty list, CanBreed returns true. 
	 *
	 * @author StrykeSlammerII
	 */
	 // test function is in DragonTree.java
	public boolean CanBreed( Dragon dragon1, Dragon dragon2 )
	{
		List test = CommonAncestors( dragon1, dragon2 );
		if( test != null && test.size() > 0 )
			return true;
		// else
		return false;
	}
	
	/**
	 * Pretty-print method to verify colorinfo.txt was read correctly.
	 * @author StrykeSlammerII
	 */
	public void printColors()
	{
		for( int key : ColorIndex.keySet() )
		{
			System.out.println( key + ": " + ColorIndex.get( key ) + ", " + ColorList.get( ColorIndex.get( key ) )[1] );
		}

	}
	
	/**
	 * Given two dragons and certain color, gene, and species selections, 
	 * what is the probability the of the offspring of the two dragons matching the other ?
	 * note that a blank string signifies a wildcard parameters, meaning you don't care about the result in this category (species breed color).
	 * note mating type is not considered as it is always 50% regardless of parentage.
	 *
	 * @param drag1 the first potential parent Dragon
	 * @param drag2 the second potential parent Dragon
	 * @param breed string version of the preferred breed
	 * @param color1 string version of the preferred primary color
	 * @param gene1 string version of the preferred primary gene
	 * @param color2 string version of the preferred secondary color
	 * @param gene2 string version of the preferred secondary gene
	 * @param color3 string version of the preferred tertiary color
	 * @param gene3 string version of the preferred tertiary gene
	 *
	 * @return float of the probability of getting the desired outcome from the specified parents.
	 */
	public float probability(Dragon drag1, Dragon drag2, String breed, String color1, String gene1, String color2, String gene2, String color3, String gene3){
		String[] colors = {color1, color2, color3};
		String[] genes = {gene1, gene2, gene3};
		
		// start optimistically with a 100% probability
		float prob = 1;
		// if the dragons cannot breed, 0% probability of them getting the desired offspring.
		if (!canBreed(drag1, drag2)){
			return 0;
		}
		// if the first dragon has the breed
		if (drag1.getSpecies().equals(breed) ){
			String result = rarityCompare(drag1.getSpecies(),drag2.getSpecies()).split("/")[0];
			prob = prob * (Integer.parseInt(result)/ (float)100);
		}
		// if the second dragon has the breed
		else if (drag1.getSpecies().equals(breed) ) {
			String result = rarityCompare(drag1.getSpecies(),drag2.getSpecies()).split("/")[1];
			prob = prob * (Integer.parseInt(result)/ (float)100);
		}
		//If not a wildcard, then no chance so return 0.
		else if (!breed.equals("")){
			return 0;
		}
		// check all three layers of gene
		for (int i = 0; i<genes.length; i++){
			// if the first dragon has the gene
			if (drag1.getGene(i+1).equals(genes[i]) ){
				String result = rarityCompare(drag1.getGene(i+1),drag2.getGene(i+1)).split("/")[0];
				prob = prob * (Integer.parseInt(result)/ (float)100);
			}
			// if the second dragon has the gene
			else if (drag1.getGene(i+1).equals(genes[i]) ) {
				String result = rarityCompare(drag1.getGene(i+1),drag2.getGene(i+1)).split("/")[1];
				prob = prob * (Integer.parseInt(result)/ (float)100);
			}
			//If not a wildcard, then no chance so return.
			else if (!genes[i].equals("")){
				return 0;
			}
		}
		// check all three layers of color
		for (int i = 0; i<colors.length; i++){
			String[] range = getColorRange(drag1.getColor(i+1),drag2.getColor(i+1));
			// if the desired color is in the range
			if (Arrays.asList(range).contains(colors[i])){
				prob = prob * (float)1/range.length;
			}
			//If not a wildcard, then no chance so return.
			else if (!colors[i].equals("")){
				return 0;
			}
		}
		return prob;
	}
	
	
	/**
	 * Given a certain color, gene, species combination, which paring of Dragons in a list of dragons
	 * would have the best change of producing the desired result?
	 * number of returned pairings and if dragons can repeat is user specified.
	 * if dragons cannot repeat, the results are chosen by a greedy method of taking the best result, and skipping the rest that includes dragons in that result.
	 * note that a blank string signifies a wildcard parameters, meaning you don't care about the result in this category (species breed color).
	 * note mating type is not considered as it is always 50% regardless of parentage.
	 *
	 * @param allDragons an ArrayList of all the Dragons to check
	 * @param drag2 the second potential parent Dragon
	 * @param breed string version of the preferred breed
	 * @param color1 string version of the preferred primary color
	 * @param gene1 string version of the preferred primary gene
	 * @param color2 string version of the preferred secondary color
	 * @param gene2 string version of the preferred secondary gene
	 * @param color3 string version of the preferred tertiary color
	 * @param gene3 string version of the preferred tertiary gene
	 * @param numResults string version of an int of the number of dragon paris to be returned
	 * @param repetition string version of a boolean representing whether or not dragons can show up in multiple parings.
	 *
	 * @return String[] list of dragon pairs in order from highest probability to low, where each pair is a single string with the two IDs separated by '/'  can contain null if not enough pairs can be created for the desired number of output pairs.
	 */
	public String[] bestParents(ArrayList<Dragon> allDragons, String breed, String color1, String gene1, String color2, String gene2, String color3, String gene3, String numResults, String repetition){
		String[] colors = {color1, color2, color3};
		String[] genes = {gene1, gene2, gene3};
		// initialize preliminary results
		ArrayList<String[]> prelimresults = new ArrayList<String[]>();
		// get each probability for all dragon combos
		for (int i = 0; i<allDragons.size() ;i++){
			for (int j = i+1; j<allDragons.size() ;j++){
				float prob = this.probability(allDragons.get(i), allDragons.get(j), breed, color1, gene1, color2, gene2, color3, gene3);
				String[] thing = {allDragons.get(i).getID(),allDragons.get(j).getID(), Float.toString(prob)};
				// add ID of the two dragons and String version of probability as an array
				prelimresults.add(thing);
			}
		}
		// sort the pairs by their probability, highest to lowest
		Collections.sort(prelimresults, new StringArrayComparatorBy3rdIndex());
		//colate real results
		String[] results  = new String[Integer.parseInt(numResults)];
		// if dragon repetition allowed, just add first X pairs to real results
		if (Boolean.parseBoolean(repetition)){
			for (int i = 0; i<results.length ;i++){
				results[i] = prelimresults.get(i)[0]+"/"+prelimresults.get(i)[1];
			}
			return results;
		}
		// otherwise, things get tricky
		int index = 0; // index of results array
		int pointer = 0; // index of preliminary results array
		ArrayList<String> usedIDs = new ArrayList<String>();
		// while we have not filled up the results array
		while (pointer < prelimresults.size()){
			// id the next pair does not have a used dragon ID
			if (!usedIDs.contains(prelimresults.get(pointer)[0]) && !usedIDs.contains(prelimresults.get(pointer)[1])){
				// create the result, add these IDs to the used list.
				results[index] = prelimresults.get(pointer)[0]+"/"+prelimresults.get(pointer)[1];
				usedIDs.add(prelimresults.get(pointer)[0]);
				usedIDs.add(prelimresults.get(pointer)[1]);
				// increment index and pointers
				index+=1;
				pointer+=1;
				// If we've filled results, return them
				if (index >= results.length){
					return results;
				}
			}
			// otherwise move on to next pair in the preliminaries
			else{
				pointer +=1;
			}
		}
		// return non filled results, it's the best we could do.
		return results;
	}
	
	
	/**
	 * Comparator class implements Comparator for sorting the probability results array from highest to lowest
	 * Sorts them numerically by ID
	 */
	private class StringArrayComparatorBy3rdIndex implements Comparator<String[]> {
		// does the comparator thing, whatever that thing is.
		public int compare(String[] list1, String[] list2) {
			Float one = Float.parseFloat(list1[2]);
			Float two = Float.parseFloat(list2[2]);
			return two.compareTo(one);
		}
	}
	
	
	
	/**
	 * Handy dandy test function
	 * 
	 * @param args This parameter is unused.
	 */
	public static void main(String[] args) {
		Analysis module = null;
		try
		{
			module = new Analysis();
		}
		catch( FileNotFoundException e )
		{
			return;
		}

		// module.printColors();
		
		System.out.println( "Hex value of 'Fire' is " + module.getColorHex( "Fire" ) );
		System.out.println( "Hex value of 'Unobtanium' is " + module.getColorHex( "Unobtanium" ) );
		System.out.println( "Index of 'Fire' is " + module.getColorIndex( "Fire" ) );
		System.out.println( "Index of 'Unobtanium' is " + module.getColorIndex( "Unobtanium" ) );
		System.out.println( "Color # 15 is " + module.getColorName( 15 ) );
		System.out.println( "Color # -1 is " + module.getColorName( -1 ) );
		
		String[] colors = module.getColorRange( "Obsidian", "Obsidian" );
		String printable = "Range( Obsidian, Obsidian) = { ";
		for( String s : colors )
		{
			printable += s + ", ";
		}
		printable += "}";
		System.out.println( printable );
		
		colors = module.getColorRange( "Teal", "Azure" );
		printable = "Range( Teal, Azure ) = { ";
		for( String s : colors )
		{
			printable += s + ", ";
		}
		printable += "}";
		System.out.println( printable );
		
		
		colors = module.getColorRange( "Obsidian", "Fire" );
		printable = "Range( Obsidian, Fire ) = { ";
		for( String s : colors )
		{
			printable += s + ", ";
		}
		printable += "}";
		System.out.println( printable );
		
		colors = module.getColorRange( "Fire", "Obsidian" );
		printable = "Range( Fire, Obsidian ) = { ";
		for( String s : colors )
		{
			printable += s + ", ";
		}
		printable += "}";
		System.out.println( printable );
		
		
		colors = module.getColorRange( "Red", "Caribbean" );
		printable = "\nRange( Red, Caribbean ) = { ";
		for( String s : colors )
		{
			printable += s + ", ";
		}
		printable += "}";
		System.out.println( printable );
		
		colors = module.getColorRange( "Red", "Teal" );
		printable = "\nRange( Red, Teal ) = { ";
		for( String s : colors )
		{
			printable += s + ", ";
		}
		printable += "}";
		System.out.println( printable );
		
		colors = module.getColorRange( "Red", "Aqua" );
		printable = "\nRange( Red, Aqua ) = { ";
		for( String s : colors )
		{
			printable += s + ", ";
		}
		printable += "}";
		System.out.println( printable );
		
		
		colors = module.getColorRange( "Bob", "Bob" );
		printable = "Range( Bob, Bob ) = " + colors;
		System.out.println( printable );
		
		colors = module.getColorRange( "Bob", "Teal" );
		printable = "Range( Bob, Teal ) = " + colors;
		System.out.println( printable );
		
		colors = module.getColorRange( "Teal", "Bob" );
		printable = "Range( Teal, Bob ) = " + colors;
		System.out.println( printable );
		
		// Test a few genes on the rarity comparer.  Expected output is 3/97, 1/99, 50/50, 100/100
		String output = module.rarityCompare("Glimmer","Spines");
		System.out.println(output);
		output = module.rarityCompare("Glimmer","Underbelly");
		System.out.println(output);
		output = module.rarityCompare("Glimmer","Circuit");
		System.out.println(output);
		output = module.rarityCompare("Glimmer","Glimmer");
		System.out.println(output);
		

		try {
		
			System.out.println("\nTest Probabilities");
			
			// create the entire document object :/
			File file = new File("../demo.drg");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList nList = doc.getElementsByTagName("Dragon");
			
			// select the dragons for testing
			Dragon testSubject1 = new Dragon(nList.item(0));
			Dragon testSubject2 = new Dragon(nList.item(1));
			
			// set the dragons to the parameters we want
			testSubject1.setMatingType(true);
			testSubject1.setSpecies("Spiral");
			testSubject1.setGene(1,"Basic");
			testSubject1.setGene(2,"Shimmer");
			testSubject1.setGene(3,"Underbelly");
			testSubject1.setColor(1,"White");
			testSubject1.setColor(2,"White");
			testSubject1.setColor(3,"White");
			
			testSubject2.setMatingType(false);
			testSubject2.setSpecies("Spiral");
			testSubject2.setGene(1,"Basic");
			testSubject2.setGene(2,"Shimmer");
			testSubject2.setGene(3,"Underbelly");
			testSubject2.setColor(1,"White");
			testSubject2.setColor(2,"White");
			testSubject2.setColor(3,"White");
			
			System.out.println("Compair identical dragons for matching child\nExpected output 1");
			float result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			testSubject2.setColor(1,"Ice");
			System.out.println("One color difference\nExpected output 0.5");
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			testSubject2.setColor(2,"Ice");
			System.out.println("Two one color differences\nExpected output 0.25");
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			testSubject2.setColor(2,"White");
			testSubject2.setColor(1,"Platinum");
			System.out.println("One two color difference\nExpected output 0.33333");
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			testSubject2.setColor(1,"White");
			testSubject2.setSpecies("Tundra");
			System.out.println("Spiral vs Tundra difference\nExpected output 0.3");
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			testSubject2.setSpecies("Spiral");
			testSubject2.setGene(2,"Basic");
			System.out.println("Shimmer vs Basic difference\nExpected output 0.01");
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			testSubject2.setSpecies("Tundra");
			System.out.println("Shimmer Spiral vs Basic Tundra difference\nExpected output 0.003");
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			System.out.println("Check all possible inputs with a 0\nNext 7 expected results 0");
			testSubject2.setSpecies("Spiral");
			testSubject2.setGene(2,"Shimmer");
			result = module.probability(testSubject1, testSubject2, "Tundra","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","Black", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Tiger", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "Black", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Basic", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "Black", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "Basic");
			System.out.println(result);
			
			System.out.println("Test Wildcard inputs\nNext 7 expected results 1");
			result = module.probability(testSubject1, testSubject2, "","White", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","", "Basic", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "", "White", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "", "Underbelly");
			System.out.println(result);
			result = module.probability(testSubject1, testSubject2, "Spiral","White", "Basic", "White", "Shimmer", "White", "");
			System.out.println(result);
			
			System.out.println("Test with color range, gene mismatch, and wildcards\nExpected result 0.3375");
			testSubject2.setSpecies("Imperial");// 0.9
			testSubject2.setGene(1,"Toxin");	// no affect
			testSubject2.setGene(2,"Shimmer");	// 1
			testSubject2.setGene(3,"Crackle");	// 0.75
			testSubject2.setColor(1,"Red");		// no affect
			testSubject2.setColor(2,"Ice");		// 0.5
			testSubject2.setColor(3,"White");	// 1
			
			result = module.probability(testSubject1, testSubject2, "Spiral","", "", "White", "Shimmer", "White", "Underbelly");
			System.out.println(result);
			
			System.out.println("Stupid floating point error\n");
		
		} catch (Exception e) {
		System.out.println(e.getMessage());
    	}
	}
	
}
	