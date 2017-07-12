/**
 * Brittany Postnikoff
 * COMP4060
 * Polyhedra project
 * Application window class
 * 2016-03-24
 */

// Import statements
import java.io.*;
import java.math.*;
import java.util.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.JFrame;

public class ApplicationWindow extends JFrame
{
    public static void main(String[] args)
    {        
        // Variables to read in the file.
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        String line = null;
        String fileName;
        Boolean fileRead = false;
        Scanner scanner = new Scanner(System.in);
        int command = 3;

        // Files to work with
        String cubeFile = "shapes/Cube.txt"; 
        String tetrahedronFile = "shapes/Tetrahedron.txt";
        String octahedronFile = "shapes/Octahedron.txt";  
        String dodecahedronFile = "shapes/Dodecahedron.txt";
        String golfballFile = "shapes/GolfBall.txt";
        String stellatedDodecahedronFile = "shapes/StellatedDodecahedron.txt";
        String stellatedOctahedronFile = "shapes/StellatedOctahedron.txt";
        fileName = cubeFile;

        // Polyhedron variables
        String graphName = "COMP4420 Project: ";
        int numVertices = 0;
        int numFaces = 0; //V-E+F=2
        int numEdges = 0;
        Vertex[] polyhedronVertices = null;
        Vertex newVertex;
        String vertexDetails;
        Face[] polyhedronFaces;
        Vertex viewpoint;
        int red;
        int green;
        int blue;
        double shapeScale;
        double radians;
        String axis;

        // Window variables
        ApplicationWindow window;

        // Open the file and process the innards.
        try {
            // Open the chosen fle.
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            // Retrieve the graph name, read the number of vertices, and 
            //     set of a vertex array.
            graphName += bufferedReader.readLine();
            numVertices = Integer.parseInt(bufferedReader.readLine());
            polyhedronVertices = new Vertex[numVertices];

            // Create the vertices read in, with name, and set up an
            //     adjacency list of vertices.
            for (int i = 0; i < numVertices; i++) {
                vertexDetails = bufferedReader.readLine();
                newVertex = new Vertex(vertexDetails);
                polyhedronVertices[i] = newVertex;
            }

            // Throw away the "0" line.
            line = bufferedReader.readLine();

            // Add x, y, and z coordinates to the created vertices.
            for (int j = 0; j < numVertices; j++) {
                vertexDetails = bufferedReader.readLine();
                polyhedronVertices[j].setCoordinates(vertexDetails);
                polyhedronVertices[j].normalize();
            }

            fileRead = true;
        }
        catch (FileNotFoundException ex) {
            System.out.println("File " + fileName + " not found.");
        }
        catch (IOException ex) {
            System.out.println("IOException occurred.");
        } finally {            
            try {                
                if (bufferedReader != null) {
                    bufferedReader.close();
                }                
            }
            catch (IOException e) {
                System.out.println("IOException occurred.");
            }
        }

        if (true == fileRead) {
            // Defaults to display the shape on the screen
            //     at the beginning of running the program. 
            viewpoint = new Vertex();
            viewpoint.setXCoordinate(0);
            viewpoint.setYCoordinate(0);
            viewpoint.setZCoordinate(300);
            red = 20;
            green = 215;
            blue = 45;
            shapeScale = 100;
            radians = .2;
            axis = "x";

            // Tallies the number of edges 
            for (int k = 0; k < numVertices; k++) {
                numEdges += polyhedronVertices[k].getNumAdjacentVertices();
            }

            // Calculates the number of faces, and then
            //     finds them
            numFaces = (numEdges/2) - numVertices + 2;
            polyhedronFaces = findFaces(numFaces, polyhedronVertices);

            // Set up the window
            window = new ApplicationWindow();
            setUpWindow(window, graphName);

            //Process the faces to update their properties.
            for (int k = 0; k < polyhedronFaces.length; k++) {
                polyhedronFaces[k].processFace(viewpoint);
            }        

            // Sort faces, draw polygon, add polygon to window
            polyhedronFaces = sortFaces(polyhedronFaces);
            DrawPolygon dp = new DrawPolygon(polyhedronFaces, red, green, blue, shapeScale);
            window.add(dp);            

            // Takes input until user enters "exit".
            do {
                //Process the faces to update their properties.
                for (int k = 0; k < polyhedronFaces.length; k++) {
                    polyhedronFaces[k].processFace(viewpoint);
                }        

                // Sort the faces, and repaint the polyhedron
                polyhedronFaces = sortFaces(polyhedronFaces);
                window.repaint(); 

                // Display choice menu to user
                System.out.println("1: rotate axis, 2: rotate degree, 3: Set red value, \n" 
                    + "4: Set green value, 5: Set blue value, 6: scale, \n"
                    + "7: set viewpoint, 8: exit");

                // Take in command
                System.out.print("Enter command: ");
                command = scanner.nextInt();

                // React to command
                switch (command) {
                    case 1:
                    axis = rotateShapeAxis(polyhedronVertices, radians, scanner);
                    break;
                    case 2:
                    radians = rotateShapeDegree(polyhedronVertices, axis, scanner);
                    break;
                    case 3: 
                    dp.setRed(scanner);
                    break;
                    case 4:
                    dp.setGreen(scanner);                        
                    break;
                    case 5:
                    dp.setBlue(scanner);
                    break;
                    case 6:
                    dp.setScale(scanner);
                    break;
                    case 7:
                    viewpoint = setViewpoint(viewpoint, scanner, shapeScale);
                    break;
                    case 8:
                    break;
                    default:
                    System.out.println("That was not a correct option. Please choose again.");
                    break;
                }    

                System.out.println("");

            } while (command != 8);
        }

        System.out.println("\nDone processing.");
    }

    // Sets up the basic window properties
    public static void setUpWindow(ApplicationWindow appWindow, String title) {
        appWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        appWindow.setSize(1000, 1000);
        appWindow.setResizable(false);
        appWindow.setTitle(title);
        appWindow.setVisible(true);       
    }

    // Rotates the shape by an axis received from input
    public static String rotateShapeAxis(Vertex[] vertices, double radians, Scanner scanner) {
        String axis;

        do {
            System.out.print("Please enter the axis: ");
            axis = scanner.next();
        } while(!(axis.equals("x") || axis.equals("X") 
            || axis.equals("y") || axis.equals("Y")
            || axis.equals("z") || axis.equals("Z")));

        for (int k = 0; k < vertices.length; k++) {
            vertices[k].rotateVertex(axis, radians);
            vertices[k].normalize();
        } 

        return axis;
    }

    // Rotates the shape by a degree received from input
    public static double rotateShapeDegree(Vertex[] vertices, String axis, Scanner scanner) {
        double radians;

        do{
            System.out.print("Please enter the degree: ");
            radians = scanner.nextDouble();
        }while(!(radians > 0 && radians < 360));

        radians = Math.toRadians(radians);

        for (int k = 0; k < vertices.length; k++) {
            vertices[k].rotateVertex(axis, radians);
            vertices[k].normalize();
        }         

        return radians;
    }

    // Find the faces of the polyhedron
    public static Face[] findFaces(int numberFaces, Vertex[] polyhedronVertices) {
        Face[] shapeFaces = new Face[numberFaces];
        int starting, past, present, future;
        int foundFaces = 0;
        Face temporaryFace;

        for (int j = 0; j < polyhedronVertices.length; j++) {
            for (int i = 0; i < polyhedronVertices[j].getNumAdjacentVertices(); i++) {
                temporaryFace = new Face();
                past = polyhedronVertices[j].getName();
                starting = past;
                temporaryFace.addVertex(polyhedronVertices[j]);
                present = polyhedronVertices[past-1].getVertex(i);
                temporaryFace.addVertex(polyhedronVertices[present-1]);
                future = polyhedronVertices[present-1].getNextVertex(past);

                while (starting != future) {
                    temporaryFace.addVertex(polyhedronVertices[future-1]);
                    past = present;
                    present = future; 
                    future = polyhedronVertices[present-1].getNextVertex(past);
                }

                if (temporaryFace.newFace(shapeFaces, foundFaces)) {
                    shapeFaces[foundFaces] = temporaryFace;
                    foundFaces++;
                }
            }
        }    

        return shapeFaces;
    }      

    // Set the viewpoint that the image will be viewed from
    public static Vertex setViewpoint(Vertex viewpoint, Scanner scanner, double shapeScale) {
        int x, y, z;

        System.out.print("Please enter x coordinate: ");
        x = scanner.nextInt();
        viewpoint.setXCoordinate(x);
        System.out.print("Please enter y coordinate: ");
        y = scanner.nextInt();
        viewpoint.setYCoordinate(y);
        do {
            System.out.print("Please enter z coordinate: ");
            z = scanner.nextInt();
        } while(!(z >= 100));
        viewpoint.setZCoordinate(z);

        return viewpoint;
    }

    // Sorts the faces with further faces coming first
    //     using a bubble sort.
    public static Face[] sortFaces(Face[] faces) {
        Face tempFace;

        for (int i = 0; i < faces.length - 1; i++) {
            for (int j = 0; j < faces.length - i - 1; j++ ) {
                if (faces[j].getDistance() < faces[j+1].getDistance()) {
                    tempFace = faces[j];
                    faces[j] = faces[j+1];
                    faces[j+1] = tempFace;
                }
            }
        }

        return faces;
    }   
}
