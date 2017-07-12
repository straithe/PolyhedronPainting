/**
 * Brittany Postnikoff
 * COMP4060
 * Polyhedra project
 * DrawPolygon Class
 * 2016-03-24
 */

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon; 
import javax.swing.*;
import javax.swing.JFrame;
import java.math.*;
import java.util.*;

public class DrawPolygon extends JComponent
{
    private Face[] faces;
    private Face face;
    private double screenHeight = 1000;
    private double screenWidth = 1000;
    private double scale;
    private int red;
    private int green;
    private int blue;
    private Color shapeColor;

    public DrawPolygon(Face[] polyhedronFaces, int red, int green, int blue, double scale)
    {
        super();
        faces = polyhedronFaces;
        this.scale = scale;
        this.red = red;
        this.green = green;
        this.blue = blue;
        shapeColor = new Color(this.red, this.green, this.blue);
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);       
        g.translate(getWidth()/2, getHeight()/2);

        double intensity;
        double x;
        double y;

        for (int i = 0; i < faces.length; i++) {            
            Path2D p = new Path2D.Double();            

            // Translate coordinate from a 3D coordinate to a
            //     2D coordinate
            x = faces[i].getVertex(0).getXCoordinate()
            * faces[i].getDistance()
            / (faces[i].getDistance() 
                - faces[i].getVertex(0).getZCoordinate());
            y = faces[i].getVertex(0).getYCoordinate()
            * faces[i].getDistance()
            / (faces[i].getDistance() 
                - faces[i].getVertex(0).getZCoordinate());
            // Begin the polygon line
            p.moveTo(scale(x), scale(y));

            for ( int j = 1; j < faces[i].getNumVertices(); j++) {
                // Translate coordinate from a 3D coordinate to a
                //     2D coordinate
                x = faces[i].getVertex(j).getXCoordinate() 
                * faces[i].getDistance()
                / (faces[i].getDistance()
                    - faces[i].getVertex(j).getZCoordinate());
                y = faces[i].getVertex(j).getYCoordinate()
                * faces[i].getDistance()
                / (faces[i].getDistance()
                    - faces[i].getVertex(j).getZCoordinate());

                // Move line to next polyhedron vertex
                p.lineTo(scale(x), scale(y));
            }          

            // Calculate face intensity
            intensity = faces[i].getIntensity();
            // Close the polyhedron path
            p.closePath();

            if (intensity >= 0 ) {  
                // Adjust the face based on intesnity
                faceColor(intensity);
                // Set the color of the face
                ((Graphics2D) g).setPaint(shapeColor);
                // Fill the face
                ((Graphics2D) g).fill(p);
            }
            else 
            {
                ((Graphics2D) g).setPaint(Color.BLACK);
                ((Graphics2D) g).fill(p);
            }         
        }
    }

    // Chooses the colour for the polyhedron
    public void faceColor(double intensity) {
        Color newColor;
        double intensityR = 0;
        double intensityG = 0;
        double intensityB = 0;

        intensityR = red * (intensity);
        intensityG = green * (intensity);
        intensityB = blue* (intensity);

        newColor = new Color((int)intensityR, (int)intensityG, (int)intensityB);
        this.shapeColor = newColor;
    }

    // Set the red value
    public void setRed(Scanner scanner) {
        red = -1;

        while(!(red >= 0 && red <= 255)) {
            System.out.print("Please enter a red value(0-255): ");
            red = scanner.nextInt();
        }
    }

    // Set the green value
    public void setGreen(Scanner scanner) {
        green = -1;

        while(!(green >= 0 && green <= 255)) {
            System.out.print("Please enter a green value(0-255): ");
            green = scanner.nextInt();       
        }
    }

    // Set the blue value
    public void setBlue(Scanner scanner) {
        blue = -1;

        while(!(blue >= 0 && blue <= 255)) {
            System.out.print("Please enter a blue value(0-255): ");
            blue = scanner.nextInt();       
        }
    }

    // Set the scale
    public void setScale(Scanner scanner) {
        do {        
            System.out.print("Please enter a scale < 0: ");
            scale = scanner.nextInt();
        }while(!(scale > 0));
    }

    // Scales / zooms into the points of the polyhedron
    public double scale(double number) {
        return number * scale * 3;
    }
}    