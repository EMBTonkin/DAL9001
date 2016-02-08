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
			output = new String[ index1 + ( ColorIndex.size() + 1 - index2 ) + 1 ]; 
			
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
		
		colors = module.getColorRange( "Bob", "Bob" );
		printable = "Range( Bob, Bob ) = " + colors;
		System.out.println( printable );
		
		colors = module.getColorRange( "Bob", "Teal" );
		printable = "Range( Bob, Teal ) = " + colors;
		System.out.println( printable );
		
		colors = module.getColorRange( "Teal", "Bob" );
		printable = "Range( Teal, Bob ) = " + colors;
		System.out.println( printable );
		
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
	