public interface Problem {
    double fit(double x);
    boolean isNeighborBetter(double f0, double f1);
    // 후보해의 적합도와 이웃해의 적합도를 받아서 비교
}
