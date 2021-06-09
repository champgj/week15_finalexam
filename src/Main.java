public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(100);


        Problem p = new Problem() {
            @Override
            public double fit(double x) {
                return -x*x*x + 38*x + 80; // 이 부분에서 문제만 바꾸어주면 된다.

                // x= 19, f(x) = 441
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                if (f0 < f1) return true;
                else return false;
            }
        };


        double x = sa.solve(p, 100, 0.99,0,0,31 );


        System.out.println(x);
        System.out.println(p.fit(x));
        System.out.println(sa.hist);

    }
}
