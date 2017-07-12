/**
 * Brittany Postnikoff
 * COMP4060
 * Polyhedra Project
 * Face class
 * 2016-03-03
 */

import java.util.*;

public class Face
{
    // Face properties
    private Vertex              centerPoint;
    private double              distance;
    private ArrayList<Vertex>   faceVertices;  
    private double              lightIntensity;    
    private Vertex              normalVertex;
    private int                 numVertices;

    // Face constructor class
    public Face() {
        centerPoint     = new Vertex();
        normalVertex    = new Vertex();
        distance        = 0;
        lightIntensity  = 0;
        faceVertices    = new ArrayList();
        numVertices     = 0;
    }

    // Add a vertex to the face.
    public void addVertex (Vertex newVertex) {
        faceVertices.add(newVertex);
        numVertices += 1;        
    }

    // Get the center point of the face.
    public Vertex getCenterPoint() {
        return centerPoint;
    }

    // Set the center point of the face
    public void setCenterPoint() {
        double xCoordinate = 0;
        double yCoordinate = 0;
        double zCoordinate = 0;

        // Get the value of all the xCoordinates of the vertices of the face.
        for (int i = 0; i < faceVertices.size(); i++) {
            xCoordinate += (faceVertices.get(i)).getXCoordinate();
            yCoordinate += (faceVertices.get(i)).getYCoordinate();    
            zCoordinate += (faceVertices.get(i)).getZCoordinate();
        }

        // Get the average of the coordinates
        xCoordinate /= faceVertices.size();
        yCoordinate /= faceVertices.size();
        zCoordinate /= faceVertices.size();

        // Set the center point
        centerPoint.setXCoordinate(xCoordinate);
        centerPoint.setYCoordinate(yCoordinate);
        centerPoint.setZCoordinate(zCoordinate);
    }    

    // Get the distance of P from V
    public double getDistance() {
        return distance;
    }

    // Set the distance of P from V.
    public void setDistance(Vertex viewpoint) {
        Vertex temporary = new Vertex();

        temporary = viewpoint.subtractVertex(centerPoint);

        distance = Math.sqrt((temporary.getXCoordinate() * temporary.getXCoordinate())
            + (temporary.getYCoordinate() * temporary.getYCoordinate())
            + (temporary.getZCoordinate() * temporary.getZCoordinate()));
    }    

    // Get the intensity of the light on the a face.
    public double getIntensity() {
        return lightIntensity;
    }    

    // Set the intensity of the light on a face.
    public void setIntensity(Vertex viewpoint) {
        Vertex temporary = new Vertex();
        double multiplyAmount;

        temporary = viewpoint.subtractVertex(centerPoint);

        // n * (V-P)
        multiplyAmount = normalVertex.dotProduct(temporary);

        // n * (V-P) / distance
        lightIntensity = multiplyAmount / distance;
    }

    // Get the unit normal to the face.
    public Vertex getNormalUnit() {
        return normalVertex;
    }

    // Set the unit normal to the face.
    public void setNormalUnit() {
        Vertex currentVertex = new Vertex();
        Vertex nextVertex = new Vertex();

        // Finds the vector between two other vectors?
        // B-A and C-A
        currentVertex = faceVertices.get(1).subtractVertex(faceVertices.get(0));
        nextVertex = faceVertices.get(2).subtractVertex(faceVertices.get(0));

        normalVertex = currentVertex.crossProduct(nextVertex);
        double distance;

        //A calculation of the length of the cross product result
        distance = Math.sqrt((normalVertex.getXCoordinate() * normalVertex.getXCoordinate())
            + (normalVertex.getYCoordinate() * normalVertex.getYCoordinate())
            + (normalVertex.getZCoordinate() * normalVertex.getZCoordinate()));

        // Dividing the vertex points by length to get the unit normal.
        normalVertex.setXCoordinate(normalVertex.getXCoordinate()/distance);
        normalVertex.setYCoordinate(normalVertex.getYCoordinate()/distance);
        normalVertex.setZCoordinate(normalVertex.getZCoordinate()/distance);
    }    

    // Returns the vertices of the face
    public ArrayList<Vertex> getVertices() {
        return faceVertices;
    }    

    // Gets number of vertices in a face.
    public int getNumVertices() {
        return faceVertices.size();
    }

    // Gets a specific vertex based on an index input
    public Vertex getVertex(int index) {
        return faceVertices.get(index);
    }    

    // Update all of the properties of the object face
    public void processFace(Vertex viewpoint) {
        setCenterPoint();
        setDistance(viewpoint);
        setNormalUnit();
        setIntensity(viewpoint);
    }

    // Compares the current face to an input face to see if they have the same Vertices.
    // If the faces have the same set of vertices, they are the same face. 
    // ( For the purposes of this assignment. ) 
    public boolean newFace(Face[] shapeFaces, int numFaces) {
        boolean isNew = true;
        int matches = 0;

        for (int j = 0; j < numFaces; j++) {
            
            matches = 0;            
            for (int i = 0; i < numVertices; i++) {
                for (int k = 0; k < shapeFaces[j].getNumVertices(); k++) {
                    if (shapeFaces[j].getVertices().get(k).getName() == faceVertices.get(i).getName()) {
                        matches++;
                    }
                }                               
            }

            if (matches == numVertices) {
                isNew = false;
            }            
        }                     

        return isNew;
    }

    //String representation of object
    public String toString() {
        return faceVertices + "";
    }
}
