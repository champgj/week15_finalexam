# 1. 모의 담금질 기법

## 1.1) 모의 담금질 기법이란

### 1.1.1) 모의 담금질 기법의 개념

제목에서 알 수 있다시피 담금질을 하는 것과 비슷하다.

높은 온도의 액체상태인 물질이 온도가 점차 낮아지면서 고체로 바뀌게 되는데

그 과정을 모방한 알고리즘이다.

온도가 높을 때는 분자가 자유롭게 움직이듯이 해의 탐색도 특정패턴 없이 움직이고

온도가 낮을 때는 분자의 움직임이 줄어들 듯이 해의 탐색도 패턴을 가지게 된다.

처음엔 T온도가 높아 자주 explore 하다가, 시간이 지나 T온도 가 내려갈 수록 한 점에 수렴하게 되는 것이다.



### 1.1.2) Pseudo Code

1. 임의의 후보해 s를 선택한다.

2. 초기 T를 정한다.

3. 반복

4. for i = 1 to kT {  // kT는 T에서의 for-루프 반복 횟수이다.
5. ​         s의 이웃해 중에서 랜덤하게 하나의 해 s'를 선택한다.

6. ​         d = (s'의 값) - (s의 값)

7. ​         if (d < 0)    // 이웃해인 s'가 더 우수한 경우

8. ​                  s ← s'

9. ​         else       // s'가 s보다 우수하지 않은 경우

10. ​                  q ← (0,1) 사이에서 랜덤하게 선택한 수

11. ​                  if ( q < p ) s ← s'  // p는 자유롭게 탐색할 확률이다.

​     }

12.  		T ← aT // 1보다 작은 상수 a 를 T에 곱하여 새로운 T를 계산

13. until (종료 조건이 만족될 때까지)

14. return s



### 1.1.3) 완벽한 이해!

**더 쉽게 이해할 수 있는 예시**가 있어서 참고용으로 가져왔다.

(출처 : naver)

![image-20210609154923256](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210609154923256.png)





Global Minima에 도착하는 목적을 가지고 어떤 공을 Hill에서 굴리는 예를 들었을 때

시작위치에서 출발하면, 처음엔 공이 Local Minima에 빠지게 된다.

하지만 이 언덕 전체를 살짝 흔들어주면 공이 Local Minima를 빠져나갈 수 있다.

**점점 작은 세기로 흔들다가 공이 넘어갈 수 있을 정도의 세기까지 점점 세게 흔드는 것이다.**

그러다가 공이 다시 움직여 Global Minima에 도착하고 더이상 움직이지 않으며, 흔들어줘야 하는 세기0이 되었을 때 종료하면 된다.



우리가 배운 것들과 접목시켜보자면

**Local Minima => 지역 최적해**

**Global Minima => 전역 최적해**

**언덕을 흔들어준다. => 적합도가 더 안좋아질 지라도 움직이는 것**

**언덕을 흔드는 세기 = > 온도 T , 확률p**

와 대응 하는 개념으로 생각하면 이해가 쉬울 것 같다.



### 1.1.4) 모의 담금질 기법으로 풀 수 있는 문제

전역 최적해를 구하는 문제에서 모의 담금질 기법으로 해결이 가능할 수 있다.

- **TSP 여행자문제(외판원)**
- **Hill climb**
- **동전 뒤집기**
- **n차 함수에서의 최댓값 혹은 최솟값 --> 이번 과정에서 이에 대해 다루겠다.**



# 2. 모의 담금질 구현코드

## 2.1) 설계과정



1. 교수님이 작성, 설명 해주신 코드를 바탕으로 3, 4 차 함수에서도 쓰일 수 있게 각각의 파라미터 값을 수정

2. 하나의 독립변수로 설명되는 종속변수 데이터를 구한 후

   curve fitting을 위한 선형 또는 비선형 모델을 선정.

3. 가장 적합한 파라미터 값을 위에서 구현한 모의담금질 기법을 이용하여 추정

4. 성능분석 및 모의담금질 기법에 대해 새로 알게 된 것들.



## 2.2) Simulated Annealing

### 2.2.1) simulated Annealing

```java
import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    private int niter;
    public ArrayList<Double> hist;

    public SimulatedAnnealing(int niter) {
        this.niter = niter;
        hist = new ArrayList<>();
    }

    public double solve(Problem p, double t, double a, double lower, double upper) {
        Random r = new Random();
        double x0 = r.nextDouble() * (upper - lower) + lower;
        return solve(p, t, a, x0, lower, upper);
    }

    public double solve(Problem p, double t, double a, double x0, double lower, double upper) {
        Random r = new Random();
        double f0 = p.fit(x0);
        hist.add(f0);

        for (int i=0; i<niter; i++) {
            int kt = (int) t;
            for(int j=0; j<kt; j++) {
                double x1 = r.nextDouble() * (upper - lower) + lower;
                double f1 = p.fit(x1);

                if(p.isNeighborBetter(f0, f1)) {
                    x0 = x1;
                    f0 = f1;
                    hist.add(f0);
                } else {
                    double d = Math.sqrt(Math.abs(f1 - f0));
                    double p0 = Math.exp(-d/t);
                    if(r.nextDouble() < 0.0001) {
                        x0 = x1;
                        f0 = f1;
                        hist.add(f0);
                    }
                }
            }
            t *= a;
        }
        return x0;
    }
}

```







### 2.2.2) Problem - 문제 인터페이스

 ```java
public interface Problem {
    double fit(double x);
    boolean isNeighborBetter(double f0, double f1);
}
 ```







### 2.2.3) MAIN - 문제를 지정, SA 실행

 ```java
public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(10);
        Problem p = new Problem() {
            @Override
            public double fit(double x) {
                return -x*x + 38*x + 80;
                // x=19 , f(x)=441
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f0 < f1;
            }
        };
        double x = sa.solve(p, 100, 0.99, 0, 0, 31);
        System.out.println(x);
        System.out.println(p.fit(x));
        System.out.println(sa.hist);
    }
}
 ```



## 2.3) 출력결과







# 3. 데이터모델

## 3.1) 모델

### 3.1.1) 데이터 입력 (독립변수->종속변수)



## 3.2) SA로 풀었을 때의 결과



## 3.3) 출력결과 







# 4. 성능비교

## 4.1) 시간복잡도





## 4.2) 



# p.s.) 추가적으로 알게된 것

모의 담금질 방법은 그 해의 수렴성이 증명되어 있는 장점이 있지만 원래의 모의 담금질 방법은 수렴 속도가 매우 느리기 때문에 복잡한 문제에 적용하기 힘들다.

대표적인 해결방법으로는  greedy 선택방법을 적용한 모의 담금질 방법이 있는 것 같다.

greedy 선택방법은 무조건 좋은 해를 선택하기 때문에, 확률적으로 좋지 않은 해를 선택할 가능성이 있는 선택방법에 비해 빠른 수렴속도를 얻을 수 있다고 한다.

 또한 greedy 방법에서는 선택 가능한 상태들의 비용함수 값의 우열관계만을 이용하여 선택하기 때문에 

비용 함수의 크기 조정에 무관하게 적용할 수 있다는 장점이 있다.
