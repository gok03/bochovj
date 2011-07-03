/**
 * Midi Drawer, a program that translates drawings to midi messages
 * 
 * Distributed under artistic license:
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 * 
 */
package bochovj.midiDraw.features;


import bochovj.midiDraw.DrawerApplet;
import bochovj.midiDraw.FeatureExtractor;
import bochovj.draw.Point;
import bochovj.draw.StrokesBuffer;

/**
 * A simple feature that gives the emptiness of the drawing
 * @author bochovj
 *
 */
public class Emptiness extends FeatureExtractor {

    private double factor = 50;
    
    private int pointsCounter;
    
    public Emptiness(StrokesBuffer strokesBuff) {
	super(strokesBuff);
	pointsCounter = 0;
    }

    /* (non-Javadoc)
     * @see bochovj.IFeatureExtractor#extractFeature(java.util.Queue)
     */
    @Override
    public int extractFeature() {
	
	//Use A*ln(1/(F* x+1))+127, which is 127 when x=0, 0, when x=M (need to compute A)
	//F is an adjusting factor, that needs to be fine tuned
	
	int M = DrawerApplet.xSize * DrawerApplet.ySize;
	double A = -(127 / Math.log(1/((factor*M)+1)));
	
	double emptiness =( A*Math.log(1/((factor*pointsCounter)+1)) )+127;
	System.out.println("Emptiness: "+emptiness);
	return (int)emptiness;
    }

    @Override
    protected void addNewPoint(Point p) {
	pointsCounter ++;
    }

    @Override
    protected void addNewStroke() {
	// Nothing to do
    }

    

}