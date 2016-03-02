// This object might not be necessary,
// then again it might.
// This Object will take shape as the Display file figures out what it is doing. 

// Aslo include comments and print functions for testing a stuff.  Good design, you know.






// *******************************************
// *************** Stuff to do ***************
// *******************************************
//Every dragon needs an image, (check)
//bounding box (test for clicks in box, also the outline) (check)
//two lines, pointing to parents
//some sort of pointer back to owner dragon (check)
//who knows what else

// imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;



/**
 * Object containing objects that are related to displaying the dragon's image
 */
class DragonDisplay{		
	
	private Dragon ourDragon;
	private BufferedImage image;
	private Rectangle boundingBox;
	
	/**
	 * Create the display objects bassed off of the Dragon's data, and store, but do not display them.
	 * @param drag the Dragon object that this DragonDisplay belongs to.
	 */
	public DragonDisplay(Dragon drag){
		
		this.ourDragon = drag;
		
		//create image object
		String imagename = this.ourDragon.getImage();
		try {
			this.image= ImageIO.read(new File(imagename));
		}
		catch (IOException e) {
			// if image could not be found, use the placeholder image
			System.out.println("Invalid image:"+imagename+"\nUsed placeholder image\n");
			System.out.println(e);
			try {
				this.image= ImageIO.read(new File("../images/MissingNo.gif"));
			}
			catch (IOException ee) {
				// OK, now we really have a problem
				System.out.println("Invalid image: coul not find placeholder image\nABANDON SHIP\n");
				System.out.println(ee);
				return;
			}
		}
		//need to add 150 to Y because the rectangle counts the header as part of the grid.  Odd.
		boundingBox = new Rectangle(ourDragon.getX(),ourDragon.getY()+150,image.getWidth(), image.getHeight()); 
		
	}	
	
	/**
	 * Assuming constant motion on all levels, moves all objects involved in displaying a dragon
	 * @param dx intager how much x moved.
	 * @param dy intager how much y moved.
	 */
	 public void translate(int dx, int dy){
	 	this.boundingBox.translate(dx,dy);
	 	
	 }
	
	/**
	 * Get the dragon associated with this object.
	 * @return Dragon.
	 */
	public Dragon getDragon(){
		return this.ourDragon;
	}
	
	/**
	 * Get the graphical representation of this dragon.  So pretty.
	 * @return BufferedImage for this dragon.
	 */
	public BufferedImage getImage(){
		return this.image;
	}
	
	/**
	 * Get the Rectangle that occupies the same space as the image, and provides dimensions for the frame.
	 * @return Rectangle for this dragon.
	 */
	public Rectangle getBoundingBox(){
		return this.boundingBox;
	}
	
	
	
// Since this is so closely related to UI, once again, no test function besides just running the program and seeing what happens.	
}