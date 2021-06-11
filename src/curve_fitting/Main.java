package curve_fitting;

public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(1000);
        // 초기온도, 온도 감쇠율

        int[] x = {2080,3400,4200,5600,6300,6400,7000}; //비트코인 가격
        int[] y = {62,65,84,146,161,180,230};				//그래픽카드 가격

        Problem p = new Problem() {
            @Override
            public double fit(int[] x, int[] y, double a, double b) {
                double f = 0; //처음엔 0으로 초기화

                for (int i = 0; i < x.length; i++) { // 길이만큼 루프를 돌고
                    f = f + (a * x[i] + b -y[i]); // 그 고정된 값과 실제 차이 를 뺀 값을 더한다.
                }
                return f;
            }

        };


        sa.solve(p,100,0.99, x, y);


        System.out.println(sa.a0 + " x + (" + sa.b0+" )");

    }

}








