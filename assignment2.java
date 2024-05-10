import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage; 
import java.io.*;
import java.util.Scanner;


/*
 * This program will transform a shape given by coordinates in a .txt file provided by the user.
 * The square.txt file is included with this program for testing purposes. 
 * To compile the program type the following into the terminal (assuming you are in the same folder as the program files):
 * javac --release 8 assignment2.java
 * To run the file type the following into the terminal: 
 * java assignment2
 * The user is then promted for the file to read and the transformations to apply
 * Once transformations are complete, a window will display these transformations with the original shape in black
 * and the transformed shape in blue. 
 * The final transformed shape will be written by its x and y coordinates to a user-defined .txt file
 */

public class assignment2 extends JPanel{
    private BufferedImage canvas;
    public static int[][] matrix;
    public static int[][] originalMatrix;
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Assignment 2");
        assignment2 panel = new assignment2();    
        
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a file to change: ");
        String datalines = input.nextLine();
        try {
            inputLines(datalines);
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        panel.displayPixels(originalMatrix, Color.BLACK);

        //perform transformations and display them until user is done
        int response = 6;
        while (response != 0) {
            System.out.println("Select the transformation you would like to make to the image:\n1. Basic Translate\n2. Basic Scale\n3. Basic Rotate\n4. Scale\n5. Rotate");
            System.out.println("Select 0 to end the transformations");
            response = input.nextInt();
            switch(response) {
                case 0:
                    panel.outputLines(matrix);
                    break;
                case 1:
                    System.out.println("How much would you like to translate x by?");
                    int Tx = input.nextInt();
                    System.out.println("How much would you like to translate y by?");
                    int Ty = input.nextInt();
                    panel.basicTranslate(Tx,Ty);
                    break;
                case 2:
                    System.out.println("How much would you like to scale x by?");
                    double Sx = input.nextDouble();
                    System.out.println("How much would you like to scale y by?");
                    double Sy = input.nextDouble();
                    panel.basicScale(Sx,Sy);
                    break;
                case 3:
                    System.out.println("What angle would you like to rotate by (counterclockwise)?");
                    double angle = input.nextDouble();
                    panel.basicRotate(angle);
                    break;
                case 4:
                    System.out.println("How much would you like to scale x by?");
                    double Scx = input.nextDouble();
                    System.out.println("How much would you like to scale y by?");
                    double Scy = input.nextDouble();
                    System.out.println("What is the x coordinate of the center of scale?");
                    int Cx = input.nextInt();
                    System.out.println("What is the y coordinate of the center of scale?");
                    int Cy = input.nextInt();
                    panel.scale(Scx,Scy,Cx,Cy);
                    break;
                case 5:
                    System.out.println("What angle would you like to rotate by (counterclockwise)?");
                    double angles = input.nextDouble();
                    System.out.println("What is the x coordinate of the center of rotation?");
                    int Crx = input.nextInt();
                    System.out.println("What is the y coordinate of the center of rotation?");
                    int Cry = input.nextInt();
                    panel.rotate(angles,Crx,Cry);
                    break;
            }   
        }
        
        panel.displayPixels(matrix, Color.BLUE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        input.close();
        
    }

      //create a bufferedimage, fill with white background
   public assignment2() {
    canvas = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
    fillCanvas(Color.WHITE);
   }

    //set correct size of window
    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }
 
    //allow canvas to be drawn
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }

    //make the canvas white
    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }

    public static int inputLines(String datalines) throws Exception {
        int num = 0;
        FileReader file = new FileReader(datalines);
        FileReader file2 = new FileReader(datalines);
        Scanner sc = new Scanner(file);
        Scanner matrixScanner = new Scanner(file2);

        while (sc.hasNextLine()) {
            sc.nextLine();
            num += 1;
        }
        
        matrix = new int[num][2];
        int j = 0; //matrix column, i = matrix row
        for (int i =0; i < num; i++) {
            matrix[i][j] = matrixScanner.nextInt();
            j++;
            matrix[i][j] = matrixScanner.nextInt();
            j=0;
        }
        //save the original matrix
        originalMatrix = matrix;
        sc.close();
        matrixScanner.close();
        System.out.println(num);
        return num;
    }

    public void outputLines(int [][]matrix) throws IOException{
        Scanner in = new Scanner(System.in);
        System.out.println("Give a file name for the translated matrix to be stored in: \n(Please include .txt in the name)");
        String fileName = in.nextLine();
        in.close();
        FileWriter writer = new FileWriter(fileName);
        int len = matrix.length;
        for (int i = 0; i < len; i++) {
            writer.write(matrix[i][0] + " " + matrix[i][1] + "\n");
        }
        writer.close();
    }

    public void basicTranslate(int Tx, int Ty){
        //assumes matrix has already been created
        //multiply each row by the translate matrix

        int translateMatrix[][] = {
            {1,  0,  0},
            {0,  1,  0},
            {Tx, Ty, 1}
        };

        //matrix multiplication, but I took out the multiplications that will equal zero
        for (int i = 0; i < matrix.length; i++) {
            int pointMatrix[] = {matrix[i][0], matrix[i][1], 1};
            matrix[i][0] = (pointMatrix[0]*translateMatrix[0][0]) + pointMatrix[2]*translateMatrix[2][0];
            matrix[i][1] = (pointMatrix[1]*translateMatrix[1][1]) + pointMatrix[2]*translateMatrix[2][1];
        }
    }

    public void basicScale(double Sx, double Sy){
        double scaleMatrix[][] = {
            {Sx, 0, 0},
            {0, Sy, 0},
            {0,  0, 1}
        };

        //matrix multiplication, but I took out the multiplications that will equal zero
        for (int i = 0; i < matrix.length; i++) {
            int pointMatrix[] = {matrix[i][0], matrix[i][1]};
            double x = (pointMatrix[0]*scaleMatrix[0][0]);
            double y = (pointMatrix[1]*scaleMatrix[1][1]);
            matrix[i][0] = (int) x;
            matrix[i][1] = (int) y;
        }
    }

    public void basicRotate(double angle){
        double rotateMatrix[][] = {
            {Math.cos(angle), -(Math.sin(angle)), 0},
            {Math.sin(angle), Math.cos(angle),    0},
            {0,               0,                  1}
        };

        //matrix multiplication, but I took out the multiplications that will equal zero
        for (int i = 0; i < matrix.length; i++) {
            int pointMatrix[] = {matrix[i][0], matrix[i][1], 1};
            double x = pointMatrix[0]*rotateMatrix[0][0] + pointMatrix[1]*rotateMatrix[1][0];
            double y = (pointMatrix[0]*rotateMatrix[0][1]) + pointMatrix[1]*rotateMatrix[1][1];
            matrix[i][0] = (int) x;
            matrix[i][1] = (int) y;
        }
    }

    public void scale(double Sx, double Sy, int Cx, int Cy){
        basicTranslate(-Cx, -Cy);
        basicScale(Sx,Sy);
        basicTranslate(Cx, Cy);
    }

    public void rotate(double angle, int Cx, int Cy){
        basicTranslate(-Cx, -Cy);
        basicRotate(angle);
        basicTranslate(Cx, Cy);
    }

    public void displayPixels(int[][] matrix, Color c){
        //draw a line between point one and two, two and three, three and four, and so on
        int i;
        for (i = 0; i < matrix.length-1; i++){
            Bresneham(matrix[i][0], matrix[i][1], matrix[i+1][0], matrix[i+1][1], c);
        }
        //to close the shape and draw the final line
        Bresneham(matrix[i][0], matrix[i][1], matrix[0][0], matrix[0][1], c);
    }

    public void Bresneham(int x0, int y0, int x1, int y1, Color c) {
        int delta_y = y1 - y0;
        int delta_x = x1 - x0;
        int y = y0;
        int x = x0;
        int color = c.getRGB();
        //first case, delta_x > delta_y and both delta_x and delta_y >0
        if(delta_x > delta_y && delta_x > 0 && delta_y > 0) {
            int E = 2 * (delta_y - delta_x);
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x <= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y += 1;
                    E += inc2;
                }
                x += 1;
            }//while
        } //if delta y is bigger, there will be one pixel y on the y axis, so switch delta_x and delta_y
        else if (delta_y > delta_x && delta_x >0 && delta_y > 0) {
            int E = 2 * (delta_x - delta_y);
            int inc1 = 2 * delta_x;
            int inc2 = 2 * (delta_x - delta_y);
            while(y <= y1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    x += 1;
                    E += inc2;
                }
                y += 1;
            }//while
        } // if delta x == delta y
        else if (delta_x == delta_y && delta_x > 0 && delta_y > 0){
            int E = 0;
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x <= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y += 1;
                    E += inc2;
                }
                x += 1;
            }//while
        } //vertical line - delta x = 0 and delta y > 0 
        else if(delta_x == 0 && delta_y > 0) {
            while(y <= y1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                y += 1;
            }//while
        } //vertical line - delta x = 0 and delta y < 0 
        else if(delta_x == 0 && delta_y < 0) {
            while(y >= y1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                y -= 1;
            }//while
        } //horizontal line - delta y = 0 and delta x > 0
        else if(delta_y == 0 && delta_x > 0) {
            while(x <= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                x += 1;
            }//while
        } //horizontal line - delta y = 0 and delta x < 0
        else if(delta_y == 0 && delta_x < 0) {
            while(x >= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                x -= 1;
            }//while
        } //if delta y < 0 and delta x > 0 and delta x > the ablsolute value of delta y)
        else if (delta_y < 0 && delta_x > 0 && delta_x > (delta_y * -1)) {
            delta_y *= -1;
            int E = 2 * (delta_y - delta_x);
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x <= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y -= 1;
                    E += inc2;
                }
                x += 1;
            }//while
        } //if delta y < 0 and delta x > 0 and delta x = the ablsolute value of delta y)
        else if(delta_y < 0 && delta_x > 0 && delta_x == (delta_y * -1)){
            delta_y *= -1;
            int E = 0;
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x <= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y -= 1;
                    E += inc2;
                }
                x += 1;
            }//while
        } //if delta y < 0 and delta x > 0 and delta x < the ablsolute value of delta y
        else if(delta_y < 0 && delta_x > 0 && delta_x < (delta_y * -1)){
            delta_y *= -1;
            int E = 2 * (delta_x - delta_y);
            int inc1 = 2 * delta_x;
            int inc2 = 2 * (delta_x - delta_y);
            while(y >= y1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    x += 1;
                    E += inc2;
                }
                y -= 1;
            }//while
        } //if delta x < 0 and delta y > 0 and delta y > the ablsolute value of delta x
        else if((delta_x < 0 && delta_y > 0 && delta_y > (delta_x*-1))) {
            delta_x *= -1;
            int E = 2 * (delta_x - delta_y);
            int inc1 = 2 * delta_x;
            int inc2 = 2 * (delta_x - delta_y);
            while(y <= y1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    x -= 1;
                    E += inc2;
                }
                y += 1;
            }//while
        } //if delta x < 0 and delta y > 0 and delta x = the ablsolute value of delta y)
        else if (delta_x < 0 && delta_y > 0 && delta_y == (delta_x*-1)) {
            delta_x *= -1;
            int E = 0;
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x >= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y += 1;
                    E += inc2;
                }
                x -= 1;
            }//while
        } //if delta x < 0 and delta y > 0 and delta x < the ablsolute value of delta y)
        else if (delta_x < 0 && delta_y > 0 && delta_y < (delta_x*-1)) {
            delta_x *= -1;
            int E = 2 * (delta_y - delta_x);
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x >= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y += 1;
                    E += inc2;
                }
                x -= 1;
            }//while 
        } //both delta x and delta y < 0 and delta x < delta y
        else if(delta_x < 0 && delta_y < 0 && delta_x < delta_y) {
            delta_x *= -1;
            delta_y *= -1;
            int E = 2 * (delta_y - delta_x);
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x >= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y -= 1;
                    E += inc2;
                }
                x -= 1;
            }//while
        } // both delta x and delta y < 0 and delta x == delta y
        else if (delta_x < 0 && delta_y < 0 && delta_x == delta_y) {
            delta_x *= -1;
            delta_y *= -1;
            int E = 0;
            int inc1 = 2 * delta_y;
            int inc2 = 2 * (delta_y - delta_x);
            while(x >= x1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    y -= 1;
                    E += inc2;
                }
                x -= 1;
            }//while
        } // both delta x and delta y < 0 and delta x > delta y
        else if (delta_x < 0 && delta_y < 0 && delta_x > delta_y) {
            delta_x *= -1;
            delta_y *= -1;
            int E = 2 * (delta_x - delta_y);
            int inc1 = 2 * delta_x;
            int inc2 = 2 * (delta_x - delta_y);
            while(y >= y1) {
                if(x >= 0 && x < 500 && y >= 0 && y < 500)
                    canvas.setRGB(x, y, color);
                if(E < 0) {
                    E += inc1;
                }else {
                    x -= 1;
                    E += inc2;
                }
                y -= 1;
            }//while
        }
        repaint();
    }


}