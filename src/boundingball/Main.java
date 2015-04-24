/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package boundingball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ndunn
 */
public class Main extends JPanel {

    private static final int FRAMES_PER_SECOND = 30;
    private static final int NUM_BALLS = 6; // Set to 20 or less
    private static final int MS_TO_WAIT = 1000 / FRAMES_PER_SECOND;
    private Ball[] ball;
    private static final int INITIAL_Y_VELOCITY = 10;
    private static final int INITIAL_X_VELOCITY = 5;

    private static final double ACCELERATION = 1.00;
    // What proportion of the velocity is retained on a bounce?  if 1.0, no energy
    // is lost, and the ball will bounce infinitely.
    private static final double COEFFICIENT_OF_RESTITUTION = 0.9;
    // While the ball is rolling along the bottom of the screen, its x velocity
    // is multiplied by this amount each frame.
    private static final double COEFFICIENT_OF_FRICTION = 0.9;
    private Timer animationTimer;
    private TimerTask animationTask;

    public Main() {
        ball = new Ball[NUM_BALLS];
        for (int i = 0; i < NUM_BALLS; i++) {
        	ball[i] = new Ball(200, 0, INITIAL_X_VELOCITY*i, INITIAL_Y_VELOCITY*i, 20-i, 20-i);
        	// will crash if more than 20 balls
        }
 

        animationTimer = new Timer("Ball Animation");
        animationTask = new AnimationUpdator();
    }

    public void start() {
        // Update at FRAMES_PER_SECOND hz
        animationTimer.schedule(animationTask, 0, MS_TO_WAIT);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.clearRect(0, 0, getWidth(), getHeight());
        int upperLeftX, upperLeftY;
        for ( int i = 0; i < NUM_BALLS; i++ ) {
           upperLeftX = (int) (ball[i].getX() - ball[i].getWidth()/2);
           upperLeftY = (int) (ball[i].getY() - ball[i].getHeight()/2);
           switch (i) {
      			case 0: g.setColor(Color.red);
      				break;
      			case 1: g.setColor(Color.blue);
      				break;
      			case 2: g.setColor(Color.green);
      				break;
      			case 3: g.setColor(Color.magenta);
      				break;
      			case 4: g.setColor(Color.pink);
      				break;
      			case 5: g.setColor(Color.orange);
      				break;
           		default: g.setColor(Color.pink);
           			break;
           }
          
           g.fillOval(upperLeftX, upperLeftY, ball[i].getWidth(), ball[i].getHeight());
           g.setColor(Color.black);
   		   g.drawString(Integer.toString(i), upperLeftX, upperLeftY);
   		   
        }
    }

    private class AnimationUpdator extends TimerTask {

        @Override
        public void run() {

            // a = (v2-v1)/t
            // a*t = (v2-v1)
            // (a*t)+v1 = v2
        	double v2;
        	int maxY, maxX, minX;
        	for (int i = 0; i < NUM_BALLS; i++) {
        		
        		v2 = (ACCELERATION * 1) + ball[i].getyVelocity();

        		ball[i].setyVelocity(v2);
        		ball[i].update();
        		      		
        		maxY = getHeight() - (ball[i].getHeight() / 2);
        		maxX = getWidth() - (ball[i].getWidth() / 2);
        		minX = 0 + ball[i].getWidth() / 2;
        		
        		// Ball is out of bounds in y dimension
        		if (ball[i].getY() > maxY) {
        			ball[i].setY(maxY);
        			ball[i].setyVelocity(-COEFFICIENT_OF_RESTITUTION * ball[i].getyVelocity());
        		}
        		else if (ball[i].getY() < 0) {
        			ball[i].setY(0);
        			ball[i].setyVelocity(-COEFFICIENT_OF_RESTITUTION * ball[i].getyVelocity());
        		}

        		// Ball is out of bounds in x dimension
        		if (ball[i].getX() > maxX) {
        			ball[i].setX(maxX);
        			ball[i].setxVelocity(-COEFFICIENT_OF_RESTITUTION * ball[i].getxVelocity());
        		}
        		else if (ball[i].getX() < minX) {
        			ball[i].setX(minX);
        			ball[i].setxVelocity(-COEFFICIENT_OF_RESTITUTION * ball[i].getxVelocity());
        		}

        		// ball is rolling along the bottom
        		if (ball[i].getY() == maxY) {
        			ball[i].setxVelocity(COEFFICIENT_OF_FRICTION * ball[i].getxVelocity());
        			if (Math.abs(ball[i].getyVelocity()) < 1.00000000000001)
        				v2 = 0.0;
        		}
        		
        		if ((ball[i].hasStopped() == 1) && (v2 < 0.0001) && (ball[i].getY() >= (maxY - ball[i].getHeight())))
    				ball[i].startOver();
        		

        	}
    	repaint();
        }

    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        JFrame frame = new JFrame();
        Main panel = new Main();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.start();
    }

}
