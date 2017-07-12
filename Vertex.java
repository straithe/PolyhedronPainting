/**
 * Brittany Postnikoff
 * COMP4060
 * Polyhedra project
 * Vertex class
 * 2016-03-03
 */

import java.util.*;
import java.math.*;

public class Vertex
{
    private double xCoordinate;
    private double yCoordinate;
    private double zCoordinate;
    private ArrayList<Integer> adjacentVertices;
    private int vertexName;
    private double scale;

    // Vertex constructor
    public Vertex() {
        vertexName = 999;
    }    

    // Vertex constructor
    public Vertex(String vertexString) {
        String[] vertexDetails = vertexString.split(" ");
        vertexName = Integer.parseInt(vertexDetails[0]) * (-1);
        adjacentVertices = new ArrayList<Integer>();

        for (int i = 1; i < vertexDetails.length; i++) {
            adjacentVertices.add(Integer.parseInt(vertexDetails[i]));
        }
    }   

    // Returns the x-coordinate of the vertex.
    public double getXCoordinate() {
        return xCoordinate;
    }

    // Reads in a value to set the x coordinate of the vertex. 
    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    // Returns the y-coordinate of the vertex.
    public double getYCoordinate() {
        return yCoordinate;
    }

    // Reads in a value to set the y-coordinate of the vertex.
    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    // Returns the z-coordinate of the vertex. 
    public double getZCoordinate() {
        return zCoordinate;
    }   

    // Reads in a value to set the z-coordinate of the vertex. 
    public void setZCoordinate(double zCoordinate) {
        this.zCoordinate = zCoordinate;
    }    

    // Set the coordinates of the vertex. 
    public void setCoordinates(String coordinateString) {
        String[] coordinateDetails = coordinateString.split(" ");

        xCoordinate = Double.parseDouble(coordinateDetails[0]);
        yCoordinate = Double.parseDouble(coordinateDetails[2]);
        zCoordinate = Double.parseDouble(coordinateDetails[4]);
    }

    // Gets the adjacency vertices
    public ArrayList getAdjacentVertices() {
        return adjacentVertices;
    }

    // Returns a Vertex at a specific position. 
    public int getVertex(int index) {
        return adjacentVertices.get(index);
    }

    // Gets the name of the Vertex.
    public int getName() {
        return vertexName;
    }

    // Gets the next vertex in the adjacency list
    public int getNextVertex(int searchTerm) {
        int nextVertex = 0;
        int position = 0;

        position = (adjacentVertices.indexOf(searchTerm)+1) % adjacentVertices.size();      

        return adjacentVertices.get(position);
    }

    // Get the number of adjacent vertices.
    public int getNumAdjacentVertices() {
        return adjacentVertices.size();
    }

    // Cross product 
    public Vertex crossProduct(Vertex secondVertex) {
        Vertex temporaryVertex = new Vertex();

        temporaryVertex.setXCoordinate( (yCoordinate * secondVertex.getZCoordinate())
            - (zCoordinate * secondVertex.getYCoordinate()));        
        temporaryVertex.setYCoordinate((-1) * (zCoordinate * secondVertex.getXCoordinate())
            - (xCoordinate * secondVertex.getZCoordinate()));
        temporaryVertex.setZCoordinate( (xCoordinate * secondVertex.getYCoordinate())
            - (yCoordinate * secondVertex.getXCoordinate()));

        return temporaryVertex;
    }

    // Multiplies the current vertex and another. 
    public double dotProduct(Vertex multiplyVertex) {
        double scalar;

        scalar = (xCoordinate * multiplyVertex.getXCoordinate())
        + (yCoordinate * multiplyVertex.getYCoordinate())
        + (zCoordinate * multiplyVertex.getZCoordinate());

        return scalar;
    }

    // Normalizes the Vertex on the screen
    public void normalize() {
        double distance = Math.sqrt(xCoordinate*xCoordinate + yCoordinate*yCoordinate +
                zCoordinate*zCoordinate);

        xCoordinate = xCoordinate/distance;
        yCoordinate = yCoordinate/distance;
        zCoordinate = zCoordinate/distance;
    }    

    // Rotates the vertex about an axis by a given degree
    public void rotateVertex(String axis, double theta) {
        normalize();

        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        double matrix[][] = null;
        double[][] xMatrix = {{1,0,0},{0,cos,-sin},{0,sin,cos}};
        double[][] yMatrix = {{cos,0,sin},{0,1,0},{-sin,0,cos}};
        double[][] zMatrix = {{cos,-sin,0},{sin,cos,0},{0,0,1}};
        double xCoordinate2, yCoordinate2, zCoordinate2;

        // Chooses which matrix we are multiplying by.
        if (axis.equals("x") || axis.equals("X")) {
            matrix = xMatrix;          
        } else if (axis.equals("y") || axis.equals("Y")) {
            matrix = yMatrix;               
        } else if (axis.equals("z") || axis.equals("Z")){
            matrix = zMatrix;         
        } else {
            System.out.println("That is not a valid axis");
        }

        // Multiplies our coordinates by a matrix to give us new coordinates.
        xCoordinate2 = (matrix[0][0] * xCoordinate)
        + (matrix[0][1] * yCoordinate)
        + (matrix[0][2] * zCoordinate);
        yCoordinate2 = (matrix[1][0] * xCoordinate)
        + (matrix[1][1] * yCoordinate)
        + (matrix[1][2] * zCoordinate);
        zCoordinate2 = (matrix[2][0] * xCoordinate)
        + (matrix[2][1] * yCoordinate)
        + (matrix[2][2] * zCoordinate);

        xCoordinate = xCoordinate2;
        yCoordinate = yCoordinate2;
        zCoordinate = zCoordinate2;           
    }

    // Subtracts the current vertex from another, and returns a vertex
    public Vertex subtractVertex(Vertex subtractVertex) {
        Vertex tempVertex = new Vertex();

        tempVertex.setXCoordinate(xCoordinate - subtractVertex.getXCoordinate());
        tempVertex.setYCoordinate(yCoordinate - subtractVertex.getYCoordinate());
        tempVertex.setZCoordinate(zCoordinate - subtractVertex.getZCoordinate());

        return tempVertex;
    }

    // Returns a string describing the vertices.
    public String toString() {
        String vertexString = "Vertex name: " + vertexName; //+ "\nx: " + xCoordinate
        //+ "\ny: " + yCoordinate + "\nz: " + zCoordinate + "\n";
        return vertexString;
    }
}
