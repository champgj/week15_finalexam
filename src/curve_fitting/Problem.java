package curve_fitting;

public interface Problem {
    double fit( int[] x, int[] y,double a, double b);
    //boolean isNeighborBetter(double cost0, double cost1)
}