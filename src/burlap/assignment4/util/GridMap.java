package burlap.assignment4.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to hold a grid world map with single agent starting coordinates and goal state ending coordinates.
 * @author David Duffy
 */
public class GridMap {
    public String name;    
    protected int[][] map;
    
    public Position startPosition;
    public ArrayList<Position> endPositions;

    final public static int WALL_LOCATION_VALUE = 1; 
    final public static int AGENT_START_LOCATION_VALUE = 2;
    final public static int TERMINAL_GOAL_LOCATION_VALUE = 3;
    final public static int TERMINAL_TRAP_LOCATION_VALUE = 4;
    final public static int TEMPORARY_TRAP_LOCATION_VALUE = 5;

    public GridMap(String name, int[][] map, Position startPosition, ArrayList<Position> endPositions) {
        this.name = name;
        this.map = map;
        this.startPosition = startPosition;
        this.endPositions = endPositions;
    }

    public GridMap(String name, String[][] map) {
        GridMap temp = ConvertMapToGridWorld(map);
        this.name = temp.name;
        this.map = temp.map;
    }

    public int[][] getMap() {
        return map;
    }

    public int getSize() {
        return this.map.length * this.map[0].length;
    }

    public static GridMap LoadMap(String filename) throws FileNotFoundException, IOException {
        ArrayList<String[]> data = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] cols = line.split(",");
            data.add(cols);
        }
        br.close();

        int height = data.size();
        String[][] map = new String[height][];
        for (int i = 0; i < height; i++) {
            map[i] = (String[])data.get(i);
        }

        GridMap grid = ConvertMapToGridWorld(map);
        String name = new File(filename).getName();
        int index = name.lastIndexOf('.');
        if (index > 0 && index <= name.length() - 2) {
            name = name.substring(0, index);
        }

        grid.name = name;
        return grid;
    }

    private static GridMap ConvertMapToGridWorld(String[][] map) {
        int height = map.length;
        int width = map[0].length;
        
        Position startPosition = null;
        ArrayList<Position> endPositions = new ArrayList<Position>();
        		
        int[][] grid = new int[width][height];
        // Convert string to int but also rotate and invert for BURLAP.
        for (int x = 0; x < width; x++) {
            for (int y = height - 1; y >= 0; y--) {
                // int val = Integer.parseInt(map[y][x]); // Normal coordinate system.
                // int val = Integer.parseInt(map[height - 1 - x][y]); // Square BURLAP coordinate system from Juan Jose.
                int val = Integer.parseInt(map[y][x]); // Rectangle safe BURLAP coordinate system.
                
                if(val == AGENT_START_LOCATION_VALUE){
                	startPosition = new Position(x, height - 1 - y);
                	//val = 0;
                }
                if(val == TERMINAL_GOAL_LOCATION_VALUE || val == TERMINAL_TRAP_LOCATION_VALUE){
                	endPositions.add(new Position(x, height - 1 - y));
                	//val = 0;
                }
                grid[x][height - 1 - y] = val;
            }
        }
        
        //just in case there is no start position
        if(startPosition == null){
        	int startX = 0;
        	int startY = 0;
        	startPosition = new Position(startX, startY);
        	grid[startX][height -1 - startY] = GridMap.AGENT_START_LOCATION_VALUE;
        }
        //just in case there is no end position
        if(endPositions.isEmpty()){
        	int endX = width - 1;
        	int endY = height - 1;
        	endPositions.add(new Position(endX, endY));
        	grid[endX][height -1 - endY] = GridMap.TERMINAL_GOAL_LOCATION_VALUE;
        }

        return new GridMap(null, grid, startPosition, endPositions);
    }
}