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
 
/**
 * Analysis class, contains various functions for analyzing dragons<br>
 * DAL9001
 * @author Lizzie Tonkin
 * @author ADD YOUR NAME OR FAKE NAME HERE
 */ 
public class Analysis{

	// fields
    private HashMap<String,Integer> geneRarities; //A hashmap that takes the name of a gene or breed and returns the relative rarity, which can be used to index into the rarityTable
    private String[][] rarityTable  = {{"50/50","70/30","85/15","97/3","99/1"}, //A 2D array that you index into with two gene rarities to get the percent probability of the offspring having those genes.  
						{"30/70","50/50","75/25","90/10","99/1"},
						{"15/85","25/75","50/50","85/15","98/2"},
						{"3/97","10/90","15/85","50/50","97/3"},
						{"1/99","1/99","2/98","3/97","50/50"}}; // For some reason I cannot initialize this in the constructor like the other things.  Odd.  
    
    
    /**
	 * constructor that initializes various things for pre-processing.
	 */
    public Analysis() {
    	
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
	 * Handy dandy test function
	 * 
	 * @param args This parameter is unused.
	 */
	public static void main(String[] args) {
		Analysis modual = new Analysis();
		
		// Test a few genes on the rarity comparer.  Expected output is 3/97, 1/99, 50/50, 100
		String output = modual.rarityCompare("Glimmer","Spines");
		System.out.println(output);
		output = modual.rarityCompare("Glimmer","Underbelly");
		System.out.println(output);
		output = modual.rarityCompare("Glimmer","Circuit");
		System.out.println(output);
		output = modual.rarityCompare("Glimmer","Glimmer");
		System.out.println(output);
	}
	
}
	