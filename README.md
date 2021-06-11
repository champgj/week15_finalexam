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

7. ​         if (d < 0)    // 이웃해 s'가 더 우수한 경우

8. ​                  s ← s'

9. ​         else       // 이웃해 s'가 우수하지 않은 경우

10. ​                  q ← (0,1) 사이에서 랜덤하게 선택한 수

11. ​                  if ( q < p ) s ← s'  // p는 자유롭게 탐색할 확률이다.

    }

12. T ← aT      // 1보다 작은 상수 a 를 T에 곱하여 새로운 T를 계산

13. until      (종료 조건이 만족될 때까지)

14. return s



### 1.1.3) 완벽한 이해를 위한 참고자료

**더 쉽게 이해할 수 있는 예시**가 있어서 참고용으로 가져왔다.

(출처 : naver)

![image-20210609154923256](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210609154923256.png)





Global Minima에 도착하는 목적을 가지고 어떤 공을 Hill에서 굴리는 예를 들었을 때

시작위치에서 출발하면, 처음엔 공이 Local Minima에 빠지게 된다.

하지만 **이 언덕 전체를 살짝 흔들어주면 공이 Local Minima를 빠져나갈 수 있다.**

그래서 공이 다시 움직여 Global Minima에 도착하고 더이상 움직이지 않으며, 흔들어줘야 하는 세기0이 되었을 때 종료하면 된다.

우리가 배운 것들과 접목시켜보자면

**Local Minima => 지역 최적해**

**Global Minima => 전역 최적해**

**언덕을 흔들어준다. => 적합도가 더 안좋아질 지라도 움직이는 것**

**언덕을 흔드는 세기 = > 확률p (이것은 T와 d(적합도값의 차이)값에 따라 변한다)**

와 대응 하는 개념으로 생각하면 이해가 쉬울 것 같다.



### 1.1.4) 모의 담금질 기법으로 풀 수 있는 문제

전역 최적해를 구하는 문제에서 모의 담금질 기법으로 해결이 가능할 수 있다.

- **TSP 여행자문제(외판원)**
- **Hill climb**
- **동전 뒤집기**
- **n차 함수에서의 최댓값 혹은 최솟값 --> 이번 과정에서 이에 대해 다루겠다.**



# 2. 모의 담금질 구현코드

## 2.1) 설계과정



1. 교수님이 작성, 설명 해주신 코드를 바탕으로

2. 3, 4 차 함수에서도 쓰일 수 있게 각각의 파라미터 값을 변경해가며 어떤 경우가 제일 좋은지 확인

   -> 교수님이 해주신거랑 거의 다를 것 없다.

   최대값이 아닌 최소값을 구하는 것이므로 f0과 f1중 f0 이 클 때로 수정

파라미터값 선정->실험을 통해 확인한다.



**(2.2 과정은 교수님의 코드와 거의 비슷합니다!.)**

**(2.3 부터 출력결과가 나옵니다.)**



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

![image-20210610233911603](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210610233911603.png)

![image-20210610235341787](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210610235341787.png)





x^4+3 x^3+2x+6 의 그래프를 나타낸 것이다.

matlab을 사용하는데 익숙하지 않아 우선은 다른 사이트를 이용해서 개형을 구했다.

x가 2.3412일 때 y가 -7.1365 가 되는데 이 점이 최소값이 된다.

![image-20210611010939369](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210611010939369.png)



![image-20210611001041373](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210611001041373.png)



위의 식 처럼 여러개의 경우를 넣어 그래프로 넣는 과정을 거쳐보니

double x = sa.solve(p, 100, 0.99, -10, 10); 이면서

확률 p를 e^(-d/t) 대신 0.0001 의 작은 수를 넣었을 때 더 정확하게 나오는 것을 볼 수 있었다.

**history의 개수가 작고, 그래프의 개형도 큰 이변이 일어나지 않거나 일어났어도 다시 제 자리를 찾아가는 것, 또한 최소값과의 오차가 가장 작은 것을 기준으로 했다.**

(경우의 수가 꽤 많고 일일이 모든 경우를 캡쳐하여 자료로 첨부하기에는 시간이 많이 소요되고 한계가 있어서 전부 첨부하지는 않았습니다.

대표적으로 그래프개형을 확인하기 위해 저런 형식으로 실행하여 그래프 개형을 시각화해보았습니다. )





# 3. 데이터모델 

## 3.1) 모델

### 3.1.1) 데이터 입력 (1독립변수->1종속변수)



요즘 비트코인 시세가 오름에 따라 그래픽 카드 가격이 많이 오르고 있는 것 같다.

그래서 이번 실습에서 비트코인 시세(독립변수) 에 대한 그래픽카드 시세(종속변수) 에 대해 다뤄보겠다.

(그래픽 카드 가격은 실제로 채굴에 많이 쓰이고, 그로 인해 가격이 많이 올랐던 RTX 3060TI를 기준으로 자료를 가져왔습니다.)

### 3.1.2) 설계과정

**설명하고자하는 그래프를  y = ax + b, 혹은 y = ax^2 + bx+ c 로 놓은 뒤**

**실제 데이터에 대입해서 각 (xn,yn) 실제값 점과 **

**내가 정한 그래프(y =ax+b) 위의 점 과의 차이 즉, cost를**

**가장 작게 하는 것을 찾고 그때의 a와 b값을 리턴하면 y= ax+b, 혹은   y = ax^2 + bx+ c 의 구하려던 함수를 구할 수 있다.**





## 3.2)  bc-gc 과의 관계 SA코드

### 3.2.1) Simulated Annealing

```java
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

        a0 = r.nextDouble()*100-50;
        b0 = r.nextDouble()*100-50;

        double f0 = p.fit(x, y, a0, b0);

        for (int i=0; i<niter; i++) {

            int kt = (int) t*50; // 이 값을 수정해서 
            for (int j = 0; j < kt; j++) {

                double a1 = r.nextDouble()*100-50;
                double b1 = r.nextDouble()*100-50;

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
```



### 3.2.2) Problem

```java
package curve_fitting;

public interface Problem {
    double fit( int[] x, int[] y,double a, double b);
    //boolean isNeighborBetter(double cost0, double cost1)
}
```



### 3.2.3) Main

```java
package curve_fitting;

public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(1000);
        // 초기온도, 온도 감쇠율

        int[] x = {2080,3400,4200,5600,6300,6400,7000}; //비트코인 가격
        int[] y = {62,65,84,146,161,180,230};           //그래픽카드 가격

        Problem p = new Problem() {
            @Override
            public double fit(int[] x, int[] y, double a, double b) {
                double f = 0; //처음엔 0으로 초기화

                for (int i = 0; i < x.length; i++) { // 길이만큼 루프를 돌고
                    double cost = (a * x[i] + b -y[i]);
                    f += cost*cost; // 그 고정된 값과 실제 차이 를 뺀 값의 제곱을 더한다.
                    //a와 가 등고선 모양으로 나와서 a와 b를 찾아감
                    
                }
                return f;
            }

        };


        sa.solve(p,100,0.99, x, y);


        System.out.println(sa.a0 + " x + (" + sa.b0+" )");

    }

}
```





교수님이 짜주신 코드에서는 x와 그 함수에 대한 값이었다면

위 비트코인-그래픽카드 가격 코드에서는 비트코인 가격과 그래픽카드가격을 각각 x,y배열에 담은 뒤

각각의 초기값과 랜덤값 (로우바운드 어퍼바운드)를 직접 설정해주고 파라미터로 x y 배열을 넣었고

갱신해줘야될 변수가 x 1개였던 것에서 개수가 늘어남에 따라 a와 b로 각각 갱신해주었다.

(**전체적인 형식은 비슷합니다.**)



## 3.3) 자료

| 비트코인(만원) | 그래픽카드(만원) |
| -------------- | ---------------- |
| 2080           | 62               |
| 3400           | 65               |
| 4200           | 84               |
| 5600           | 146              |
| 6300           | 161              |
| 6400           | 180              |
| 7000           | 230              |

![image-20210611185408981](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210611185408981.png)



y = 0.03340596x-34.36291 이 나와야 정상적으로 작동함을 알 수 있다.

(사진은 매트랩에 익숙지 않아서 인터넷 커브피팅사이트를 이용했습니다.)



## 3.4) 출력결과 

이제 값을 입력하여 출력해보면

![image-20210611190003883](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210611190003883.png)



이런식으로 완벽히 일치하지는 않지만 어느정도 값에 비슷하게 나오게 된다.

여러 결과를 거쳤을 때 sa.solve(p,100,0.99, x, y); 과 p는 e^(-d/t)의 경우로 하고 int kt = (int) t*50;

했을 때 위의 출력결과가 나왔다.

위의 자료를 바탕으로 비트코인 가격에 따른 그래픽카드가격은 

대략 y = 0.0334x-34 +-3 정도로 말할 수 있을 것 같다.



# 4. 성능비교

## 4.1) 성능

단위가 비트코인은 천만원 단위이고 그래픽카드는 십만~백만 단위여서 파라미터값을 조금만 수정해도

기울기가 많이 변하곤 했다.  그래서 같은 코드를 여러번 실행했을 때 값의 오차가 상당했는데, 

초기 온도와 감쇄율,확률 등등 각각의 파라미터값을 거듭해서 수정했을 때 유사한 값을 얻게 되었다.



확실히 이전의 직관적인 방법으로 문제를 푸는 방법

예를 들어서 최소 혹은 최대 의 값을 찾을 때 

미분을 하여 0이 나오는 값을 비교하여 얻는 방법을 그대로 코드로 구현하여 찾는 것 보다는 훨씬

코드가 복잡하고 속도도 느린 것 같다.

하지만 대수적인 풀이방법을 이미 알고있는 경우를 제외한

나머지 문제에서는 이런 모의담금질 기법이 더 유용하고 범용적으로 사용될 것 같다.





문제는 속도 인데 이것을 개선할 수 있는 방법을 연구중에 있다고한다.



# p.s.) 모의담금질 속도 개선 방법연구

모의 담금질 방법은 그 해의 수렴성이 증명되어 있는 장점이 있지만 원래의 모의 담금질 방법은 수렴 속도가 매우 느리기 때문에 복잡한 문제에 적용하기 힘들다.

대표적인 해결방법으로는  greedy 선택방법을 적용한 모의 담금질 방법이 있는 것 같다.

greedy 선택방법은 무조건 좋은 해를 선택하기 때문에, 확률적으로 좋지 않은 해를 선택할 가능성이 있는 선택방법에 비해 빠른 수렴속도를 얻을 수 있다고 한다.

 또한 greedy 방법에서는 선택 가능한 상태들의 비용함수 값의 우열관계만을 이용하여 선택하기 때문에 

비용 함수의 크기 조정에 무관하게 적용할 수 있다는 장점이 있다고 한다.



Greedy한 방법을 더 찾아보고 그 방법으로 이 문제를 적용하여 풀어보고 싶지만 시간관계상 나중에 해야겠다..
