import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf2;
    private int[][] gridToInt;
    private int[][] intToGrid;
    private int gridSize;

    public Percolation(int n)               // create n-by-n grid, with all sites blocked
    {
        gridSize = n;
        grid = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 1);
        uf2 = new WeightedQuickUnionUF(n * n + 2);
        gridToInt = new int[n][n];
        intToGrid = new int[n * n][2];
        int count = 0;
        for (int i = 0; i < gridToInt.length; i++) {
            int[] row = gridToInt[i];
            for (int j = 0; j < row.length; j++) {
                row[j] = count;
                intToGrid[count] = new int[]{i, j};
                count += 1;
            }
        }

    }

    private ArrayList<Integer> getNeighbors(int r, int c, int numRows, int numCols) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        boolean topEdge = r == 0;
        boolean bottomEdge = r == numRows - 1;
        boolean leftEdge = c == 0;
        boolean rightEdge = c == numCols - 1;
        if (!leftEdge)
            neighbors.add(gridToInt[r][c - 1]);

        if (!rightEdge)
            neighbors.add(gridToInt[r][c + 1]);

        if (!topEdge)
            neighbors.add(gridToInt[r - 1][c]);
        else
            neighbors.add(gridSize * gridSize);

        if (!bottomEdge)
            neighbors.add(gridToInt[r + 1][c]);
        else
            neighbors.add(gridSize * gridSize + 1);


        return neighbors;
    }

    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        i = i - 1;
        j = j - 1;
        ArrayList<Integer> neighbors = getNeighbors(i, j, gridSize, gridSize);
        for (int n : neighbors) {
            if (n >= intToGrid.length || isOpen(intToGrid[n][0] + 1, intToGrid[n][1] + 1)) {
                uf2.union(n, gridToInt[i][j]);
                if (n <= intToGrid.length)
                    uf.union(n, gridToInt[i][j]);
            }
        }
        grid[i][j] = true;
    }

    public boolean isOpen(int i, int j)     // is site (row i, column j) open?
    {
        i = i - 1;
        j = j - 1;
        return grid[i][j];
    }

    public boolean isFull(int i, int j)     // is site (row i, column j) full?
    {
        i = i - 1;
        j = j - 1;
        int t = gridToInt[i][j];
        return uf.connected(t, gridSize * gridSize);
//        return false;
    }

    public boolean percolates()             // does the system percolate?
    {
        return uf2.connected(gridSize * gridSize + 1, gridSize * gridSize);
    }

    public static void main(String[] args)  // test client (optional)
    {

    }
}