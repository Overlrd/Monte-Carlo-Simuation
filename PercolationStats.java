import edu.princeton.cs.algs4.*;

public class PercolationStats {
    private int N;
    private int trials;
    private double[] results;

    public PercolationStats(int n, int trials) {
        N = n;
        this.trials = trials;

        results = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(N);
            results[i] = runTrial(percolation);
        }
    }

    private double runTrial(Percolation percolation) {
        int opens = 0;

        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, N + 1);
            int col = StdRandom.uniform(1, N + 1);
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
                opens++;
            }
        }

        double result = ((double) opens / (double) (N * N));
        return result;
    }

    public double mean() {
        return StdStats.mean(results);
    }

    public double stddev() {
        return StdStats.stddev(results);
    }

    public double confidenceLo() {
        return StdStats.min(results);
    }

    public double confidenceHi() {
        return StdStats.max(results);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java PercolationSimulation <gridSize> <trials>");
            return;
        }

        int N = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(N, trials);
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
