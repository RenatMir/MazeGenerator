package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class MyPanel extends JPanel {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private int COLS = 45;
    private double CELL = WIDTH / (double)COLS;

    private Node[][] matrix;

    MyPanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        initialize();


        SwingWorker swingWorker;
        swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                generateMaze();
                return null;
            }
        };swingWorker.execute();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        for(int i = 0; i < COLS; i++){
            for (int j = 0; j < COLS; j++){
                g2d.setColor(Color.DARK_GRAY);
                if(matrix[i][j].walls[0])
                    g2d.draw(new Line2D.Double(CELL * j,CELL * i, CELL * j + CELL , CELL * i )); // top
                if(matrix[i][j].walls[1])
                    g2d.draw(new Line2D.Double(CELL * j,CELL * i, CELL * j , CELL * i + CELL)); // left
                if(matrix[i][j].walls[2])
                    g2d.draw(new Line2D.Double(CELL * j,CELL * i + CELL, CELL * j + CELL , CELL * i + CELL)); // bottom
                if(matrix[i][j].walls[3])
                    g2d.draw(new Line2D.Double(CELL * j + CELL,CELL * i , CELL * j + CELL , CELL * i +CELL)); // right


                g2d.setColor(new Color(0x3B79FF));
                if(matrix[i][j].isPath)
                    g2d.fill(new Rectangle2D.Double(CELL * j + 1, CELL * i + 1, CELL , CELL ));

                g2d.setColor(new Color(0x530080));
                if(matrix[i][j].isCurrent)
                    g2d.fill(new Rectangle2D.Double(CELL * j + 1, CELL * i + 1, CELL , CELL ));
            }
        }


    }
    public void generateMaze(){
        Node start = matrix[0][0];
        Node current;
        Node next;

        start.isVisited = true;
        current = start;
        do{

            current.isCurrent = true;
            current.isPath = true;
            repaint();


            next = checkNeighbors(current);

            if(next != null) {


                next.isVisited = true;
                current.isCurrent = false;
                next.parent = current;
                removeWall(current, next);
                current = next;
            }
            else {
                current.isCurrent = false;
                current = current.parent;
            }
            if(current.parent == null){
                System.out.println("Finish");
                break;
            }
        }while(true);
    }
    public void removeWall(Node current, Node next){
        int x = current.x - next.x;
        if(x == 1){
            current.walls[0] = false;
            next.walls[2] = false;
        }
        else if( x == -1){
            current.walls[2] = false;
            next.walls[0] = false;
        }
        int y = current.y - next.y;
        if(y == 1){
            current.walls[1] = false;
            next.walls[3] = false;
        }
        else if(y == -1 ){
            current.walls[3] = false;
            next.walls[1] = false;
        }
        repaint();
    }
    public Node checkNeighbors(Node current){

        Random rand = new Random();
        ArrayList<Node> list = new ArrayList<>();

        if(checkIndex(current.x+1, current.y))
            list.add(matrix[current.x+1][current.y]);

        if(checkIndex(current.x-1, current.y))
            list.add(matrix[current.x-1][current.y]);

        if(checkIndex(current.x, current.y-1))
            list.add(matrix[current.x][current.y-1]);

        if(checkIndex(current.x, current.y+1))
            list.add(matrix[current.x][current.y + 1]);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();

        if(list.size() != 0)
            return list.get(rand.nextInt(list.size()));
        else
            return null;
    }
    public boolean checkIndex(int x, int y){
        return x >= 0 && x < matrix.length
                && y >= 0 && y < matrix.length && !matrix[x][y].isVisited;
    }

    public void initialize(){
        matrix = new Node[COLS][COLS];
        for(int i = 0; i < COLS; i++){
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = new Node(i, j);
            }
        }
    }
    static class Node{
        int x , y;
        boolean[] walls;
        boolean isVisited;
        boolean isCurrent;
        boolean isPath;
        Node parent;
        Node(int x, int y){
            this.x = x;
            this.y = y;
             walls = new boolean[]{true, true, true, true};
        }

        @Override
        public String toString(){
            return x + "|" + y;
        }
    }
}
