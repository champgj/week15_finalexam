import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    // 매개변수
    // T, x0, kT, p, niter


    private int niter;

    public ArrayList<Double> hist;

    public SimulatedAnnealing(int niter) {
        this.niter = niter;
        hist = new ArrayList<Double>();
    }


    public double solve(Problem p,double t, double a,double lower, double upper){
        // 문제 ,초기온도, 알파 냉각률, 로어바운드,어퍼바운드
        // 문제 p가 아니라 확률 p (안좋음에도 불구하고 걔를 다음 후보해로 선정하는) 그 p도 t온도에 따라서
        // 작아지게끔 설계를 할 것

        Random r = new Random();
        double x0 = r.nextDouble() * (upper-lower)+lower;
        double f0 = p.fit(x0);

        return solve(p,t,a,x0,lower,upper);
    }
    public double solve(Problem p, double t, double a, double x0, double lower,double upper){
        Random r = new Random();
        double f0 = p.fit(x0);
        hist.add(f0);

        //REPEAT
        for(int i = 0; i<niter;i++){
            int kt = (int) t; // 몇번 돌지 조정 가능
            for(int j = 0; j<kt;j++){
                double x1 = r.nextDouble() * (upper - lower) + lower;
                double f1 = p.fit(x1);


                if(p.isNeighborBetter(f0,f1)){
                    x0 = x1;
                    f0 = f1;
                    hist.add(f0);
                }else{
                    double d = Math.sqrt(Math.abs(f1- f0));
                    double p0 = Math.exp(-d/t);
                    if(r.nextDouble() < 0.0000001){//p0
                        x0=x1;
                        f0 = f1;
                        hist.add(f0);
                    }
                }


            }
            t *=a; // a에 1보다 큰 값을 쓰면 발산해버린다.

        }



        return x0;
    }

}










// kt 변화 niter 설정, 등등 파라미터 조정으로 값을 찾는 방식 조절.










//초기온도 T
// 후보해  x0
// 후보해의 적합도 f0


//REPEAT
//kT = -x^2
// for i : kT (kT : 반복횟수, 온도가 낮아질수록 자유도가 낮아져야됨. 값이 작아져야 함) --> 발산이 될 가능성이 줄어든다 kT = 1000
// for i : kT -->커진다 -->
// 담금질
// x1 = 이웃해 결정
// 이웃해의 적합도 f1
// if(f1 <-- f0 )///이웃해가 더 좋은 경우/// (f1 > f0)  최대값이라면 적합도가 높은 것에 맞춰져야 하고 최소값이라면 적합도 값이 낮은것
// x0 = x1
// f0 = f1

// else if(r<p) 이웃해가 더 좋지 않은데도 불구하고 p : 0.25
// x0 = x1
// f0 = f1
// end
// T = a * T (냉각율, cooling ratio)
//END (종료조건이 만족하면)