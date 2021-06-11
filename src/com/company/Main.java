package com.company;

public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(10);
        Problem p = new Problem() {
            @Override
            public double fit(double x) {
                return x*x*x*x + 3*x*x*x + 2*x + 6 ;
                // x=19 , f(x)=441
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f0 > f1;
            }
        };
        double x = sa.solve(p, 100, 0.99, -10, 10);
        System.out.println("최소가 되는 x값"+x);
        System.out.println("최소가 되는 y값"+p.fit(x));
        System.out.println("History");
        System.out.println(sa.hist);
    }
}