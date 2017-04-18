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
    public int startX;
    public int startY;
    public int endX;
    public int endY;

    public GridMap(String name, int[][] map, int startX, int startY, int endX, int endY) {
        this.name = name;
        this.map = map;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public GridMap(String name, String[][] map) {
        GridMap temp = ConvertMapToGridWorld(map);
        this.name = temp.name;
        this.map = temp.map;
        this.startX = temp.startX;
        this.startY = temp.startY;
        this.endX = temp.endX;
        this.endY = temp.endY;
    }

    public int[][] getMap() {
        return map;
    }

    public int getSize() {
        return this.map.length * this.map[0].length;
    }

    public static GridMap LoadMap(String filename) throws FileNotFoundException, IOException {
        ArrayList data = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] cols = line.split(",");
            data.add(cols);
        }

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

        int startX = 0, startY = 0;
        int endX = width - 1, endY = height - 1;

        int[][] grid = new int[width][height];
        // Convert string to int but also rotate and invert for BURLAP.
        for (int x = 0; x < width; x++) {
            for (int y = height - 1; y >= 0; y--) {
                // int val = Integer.parseInt(map[y][x]); // Normal coordinate system.
                // int val = Integer.parseInt(map[height - 1 - x][y]); // Square BURLAP coordinate system from Juan Jose.
                int val = Integer.parseInt(map[y][x]); // Rectangle safe BURLAP coordinate system.
                if (val == 2) { startX = x; startY = height - 1 - y; val = 0; }
                if (val == 3) { endX = x; endY = height - 1 - y; val = 0; }
                grid[x][height - 1 - y] = val;
            }
        }

        return new GridMap(null, grid, startX, startY, endX, endY);
    }
}