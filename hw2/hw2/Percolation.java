
/******************************************************************************
 *  Compilation:  javac InteractivePercolationVisualizer.java
 *  Execution:    java InteractivePercolationVisualizer N
 *  Dependencies: PercolationVisualizer.java Percolation.java
 *
 *  This program takes the grid size N as a command-line argument.
 *  Then, the user repeatedly clicks sites to open with the mouse.
 *  After each site is opened, it draws full sites in light blue,
 *  open sites (that aren't full) in white, and blocked sites in black.
 *
 ******************************************************************************/
package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int N;
    private final WeightedQuickUnionUF sites;
    // site without bottomSite
    private final WeightedQuickUnionUF sites2; // no virtual bottom to handle backwash
    private final int topSite; // virtual top
    private final int bottomSite; // virtual bottom
    private final boolean[][] flagOpen;
    private int numOpen = 0;

    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        topSite = N * N;
        bottomSite = N * N + 1;

        sites = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 0; i < N; i++) {
            sites.union(topSite, xyTo1D(0, i));
        }

        for (int i = 0; i < N; i++) {
            sites.union(bottomSite, xyTo1D(N - 1, i));
        }

        sites2 = new WeightedQuickUnionUF(N * N + 1);
        for (int i = 0; i < N; i++) {
            sites2.union(topSite, xyTo1D(0, i));
        }

        flagOpen = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                flagOpen[i][j] = false;
            }
        }
    }

    public void open(int row, int col) {
        validateRange(row, col);
        if (flagOpen[row][col]) {
            return;
        }
        flagOpen[row][col] = true;
        numOpen++;

        unionOpenNeighbor(row, col, row - 1, col);
        unionOpenNeighbor(row, col, row + 1, col);
        unionOpenNeighbor(row, col, row, col - 1);
        unionOpenNeighbor(row, col, row, col + 1);
    }

    public boolean isOpen(int row, int col) {
        validateRange(row, col);
        return flagOpen[row][col];
    }

    public boolean isFull(int row, int col) {
        validateRange(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        return sites2.connected(xyTo1D(row, col), topSite);
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        if (numOpen == 0) {
            return false;
        }

        return sites.connected(topSite, bottomSite);
    }

    private void unionOpenNeighbor(int row, int col, int newRow, int newCol) {
        if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) {
            return;
        }
        if (flagOpen[newRow][newCol]) {
            sites.union(xyTo1D(row, col), xyTo1D(newRow, newCol));
            sites2.union(xyTo1D(row, col), xyTo1D(newRow, newCol));
        }

    }

    private int xyTo1D(int row, int col) {
        return row * N + col;
    }

    private void validateRange(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static void main(String[] args) {
    }
}
