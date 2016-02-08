// why not put all the logical comparisons in one place? 

// Fields necessary will be color info (color name and hexadecimal for each color; see colorInfo.txt)
// and breed and gene rarities and a table to compare them to each other

// Methods:
	// canBreed(Dragon dragon1, Dragon dragon2): Can two dragons breed together?  Check MatingType and ancestry.  Returns Boolean (The data type, not the familiar)
	// colorRange(string color1, string color2): two colors enter, a list of color names of the potential range leaves.
	// getColorHex(string color): returns the hexadecimal number associated with a color name
	// √ (done) rarityCompare(string gene1, string gene2): takes in the name of two genes (or two dragon breeds) and returns two percentages, saying the likely hood of each gene for the offspring.  If same, both are 100%
	// As the DAL program grows, more analysis will be added, and it will be here.  
	
// Aslo include comments and print functions for testing a stuff.  Good design, you know.


 
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
 
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
				"Shimmer","Stripes","Eye-Spots","Freckle","Seraph","Current","Daub","Facet","Hypnotic","Paint","Peregrine", "Toxin","Butterfly",
				"Circuit","Gembond","Underbelly","Crackle","Smoke","Spines","Okapi","Glimmer","Thylacine"};
		// the relative rarities of the previous stuff, in the same order.  
		int[] values ={0,0,0,1,1,0,1,3,1,4,3,4,2, 
				 0,4,1,1,1,2,2,4,2,1,2,3,
				 4,1,1,1,2,1,1,4,1,1,1,3,4,
				 4,3,1,2,2,3,2,4,1};
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
			return "100";
		}
		// get the relative rarity of the two genes in question
		int value1 = geneRarities.get(gene1);
		int value2 = geneRarities.get(gene2);
		// compare them in the magical rarity 2D array to get the percentages
		String rarity = rarityTable[value1][value2];
		return rarity;
	}
	
	public void printColors()
	{
		for( int key : ColorIndex.keySet() )
		{
			System.out.println( key + ": " + ColorIndex.get( key ) + ", " + ColorList.get( ColorIndex.get( key ) )[1] );
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

		module.printColors();
		
		// Test a few genes on the rarity comparer.  Expected output is 3/97, 1/99, 50/50, 100
		String output = module.rarityCompare("Glimmer","Spines");
		System.out.println(output);
		output = module.rarityCompare("Glimmer","Underbelly");
		System.out.println(output);
		output = module.rarityCompare("Glimmer","Circuit");
		System.out.println(output);
		output = module.rarityCompare("Glimmer","Glimmer");
		System.out.println(output);
	}
	
}
	