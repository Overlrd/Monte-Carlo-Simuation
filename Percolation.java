import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private boolean[][] sitesMatrix;
    private int[][] oneDSitesCoord;
    private int numOpenSites;

    private int virtualTop;
    private int virtualBottom;

    private WeightedQuickUnionUF UnionFind;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        N = n;
        UnionFind = new WeightedQuickUnionUF((N * N) + 2);
        sitesMatrix = new boolean[N + 1][N + 1];
        oneDSitesCoord = new int[N + 1][N + 1];

        virtualTop = N * N + 1;
        virtualBottom = N * N;
        numOpenSites = 0;

        int i = 0;
        for (int x = 1; x < N + 1; x++) {
            for (int y = 1; y < N + 1; y++) {
                sitesMatrix[x][y] = false;
                oneDSitesCoord[x][y] = i;
                if (x == 1) {
                    UnionFind.union(virtualTop, oneDSitesCoord[x][y]);
                } else if (x == N) {
                    UnionFind.union(virtualBottom, oneDSitesCoord[x][y]);
                }
                i++;
            }
        }
    }

    private void checkCoordinateRange(int row, int col) {
        if ((row < 1 || row > N) || (col < 1 || col > N)) {
            throw new IllegalArgumentException();
        }
    }

    public void open(int row, int col) {
        checkCoordinateRange(row, col);

        if (!isOpen(row, col)) {
            sitesMatrix[row][col] = true;
            final int rowNeighbours[] = { row, row + 1, row, row - 1 };
            final int colNeighbours[] = { col + 1, col, col - 1, col };

            for (int i = 0; i < 4; i++) {
                try {
                    if (isOpen(rowNeighbours[i], colNeighbours[i])) {
                        UnionFind.union(oneDSitesCoord[rowNeighbours[i]][colNeighbours[i]], oneDSitesCoord[row][col]);
                    }

                } catch (Exception e) {
                }
            }

            numOpenSites++;
        }
    }

    public boolean isOpen(int row, int col) {
        checkCoordinateRange(row, col);
        return sitesMatrix[row][col] == true;
    }

    public boolean isFull(int row, int col) {
        checkCoordinateRange(row, col);
        boolean isSiteFull = false;
        if (isOpen(row, col)) {
            int siteRoot = UnionFind.find(oneDSitesCoord[row][col]);
            isSiteFull = UnionFind.connected(siteRoot, virtualTop);
        }
        return isSiteFull;
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        if (N == 1) {
            return isOpen(1, 1);
        } else {
            int siteRoot = UnionFind.find(virtualBottom);
            return UnionFind.connected(siteRoot, virtualTop);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java PercolationSimulation <gridSize> <trials>");
            return;
        }

        int N = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        double threshold;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(N);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, N + 1);
                int col = StdRandom.uniform(1, N + 1);
                percolation.open(row, col);
            }

            threshold = (double) percolation.numberOfOpenSites() / (N * N);
            System.out.println("Threshold = " + threshold);
        }
    }
}
