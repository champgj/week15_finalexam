package curve_fitting;
import java.util.Random;

class SimulatedAnnealing {
    private double t;
    private int niter;

    public double a0;
    public double b0;

    public SimulatedAnnealing(int niter) {
        this.niter = niter;
    }

    public double solve(Problem p, double t, double a, int[] x, int[] y) {
        // 교수님이 짜주신거랑 거의 같지만, 로어바운드 어퍼바운드 대신에 x y 배열을 파라미터로 넣음

        Random r = new Random();

        a0 = r.nextDouble()*500;
        b0 = r.nextDouble()*50;

        double f0 = p.fit(x, y, a0, b0);

        for (int i=0; i<niter; i++) {

            int kt = (int) t;
            for (int j = 0; j < kt; j++) {

                double a1 = r.nextDouble()*500;
                double b1 = r.nextDouble()*50;

                double f1 = p.fit(x, y,a1, b1);

                if (f0 > f1) {
                    a0 = a1;
                    b0 = b1;

                    f0 = f1;

                } else {
                    double d = f1 - f0;
                    double p0 = Math.exp(-d/t); // e^(-d/t) 확률
                    // 이동하는 것이 더 안 좋음에도 불구하고 이동하는 확률

                    if (r.nextDouble() < p0) {
                        a0 = a1;
                        b0 = b1;

                        f0 = f1;

                    }

                }
            }
            t *= a;
        }
        return f0;
    }
}