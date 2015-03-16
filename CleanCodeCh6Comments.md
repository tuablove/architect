# 6장 객체와 자료 구조 #

외부에서 변수를 직접 접근하지 않기 위해 private  으로 정의를 한다.

그렇다면 왜 많은 프로그래머들이 get 함수와 set 함수로 변수를 외부에 노출 시키는 것일까?

## 6.1 자료추상화 ##

**목록 6-1**
```
public class Point {
   public double x;
   public double y;
}
```

**목록 6-2**
```
public interface Point {
   double getX();
   double getY();
   void setCartesian(double x, double y); double getR();
   double getTheta();
   void setPolar(double r, double theta); 
}
```

목록 6-1은 각 값을 개별적으로 읽고 설정한다. 또한 구현을 노출한다.

목록 6-2는 점이 직교좌표계를 사용하는지 극좌표계를 사용하는지 알수가 없다 => 추상적이다.

**목록 6-3**
```
public interface Vehicle {
   double getFuelTankCapacityInGallons(); 
   double getGallonsOfGasoline();
}
```

**목록 6-4**
```
public interface Vehicle {
   double getPercentFuelRemaining();
}
```

목록 6-3의 두 함수는 변수를 단순히 읽어서 반환하는 기능을 한다.

목록 6-4는 백분율이라는 추상적인 개념을 반환 하므로 그 함수의 내부가 드러나지 않는다. => 추상적이다.

조회/설정 함수만으로 추상화가 이뤄지지 않는다.

개발자는 객체가 포함하는 자료를 표현할 좋은 방법을 고민해야 한다.

## 6.2 자료/객체 비대칭 ##


객체는 자료를 숨긴채 자료를 다루는 함수만을 공개한다.

자료구조는 함수를 제공하지 않고 자료를 그대로 공개한다.

두 개념은 정반대다.

**목록 6-5**

```
public class Square { 
   public Point topLeft; 
   public double side;
}
public class Rectangle { 
   public Point topLeft; 
   public double height; public double width;
}
public class Circle { 
   public Point center; 
   public double radius;
}
public class Geometry {
   public final double PI = 3.141592653589793;
   public double area(Object shape) throws NoSuchShapeException {
      if (shape instanceof Square) { 
          Square s = (Square)shape; return s.side * s.side;
      }
      else if (shape instanceof Rectangle) { 
          Rectangle r = (Rectangle)shape; return r.height * r.width;
      }
      else if (shape instanceof Circle) {
         Circle c = (Circle)shape;
         return PI * c.radius * c.radius; 
      }
      throw new NoSuchShapeException(); }
}
```

**목록 6-6**

```
public class Square implements Shape { 
   private Point topLeft;
   private double side;
   public double area() { 
      return side*side;
   } 
}

public class Rectangle implements Shape { 
   private Point topLeft;
   private double height;
   private double width;
   public double area() { 
      return height * width;
   } 
}

public class Circle implements Shape { 
   private Point center;
   private double radius;
   public final double PI = 3.141592653589793;
   public double area() {
      return PI * radius * radius;
   } 
}
```

목록 6-5에 함수를 추가하고 싶다면 도형클레스는 영향을 받지 않는다.

하지만 새로운 도형을 추가한다면 Geometry 클레스에 속한 함수 모두를 수정하여야 한다.

목록 6-6에 새로운 도형을 추가한다면 기존 함수는 아무런 영향을 받지 않는다.

하지만 새로운 함수를 추가한다면 모든 도형에 새 함수를 추가하여야 한다.

자료구조를 사용하는 절차적 코드는 기존 자료 구조를 변경하지 않으면서 새 함수를 추가하기 쉽다.

객체 지향 코드는 기존 함수를 변경하지 않으면서 새 클레스를 추가하기 쉽다.

두가지는 서로 반대다.

모든 것이 객체라는 믿음은 잘못된 생각이다.

상황에 맞는 기법을 사용 하여야 한다.

## 6.3 디미터 법칙 ##

디미터 법칙 : 모듈은 자신이 조작하는 객체의 속사정을 몰라야 한다.

"클레스 C의 메소드 f 는 다음 객체의 메소드만 호출해야 한다"

```
class A {
	public void f2() {}
}

class B {
	public void f3() {}
}

class D {
	public void f4() {}
}

class C {
	B m_value;
	public void f(A object) {
		D d = new D();
	}
}

```

함수 f 가 호출 할수 있는 객체 :

클레스 C

f가 생성한 객체 (클레스 D)

f 인수로 넘어온 객체 (클레스 A)

C 인스턴스 변수에 저장된 객체 (클레스 B)

하지만 위 객체에서 허용된 메소드가 반환하는 객체의 메소드는 호출하면 안된다.
( 함수 f2, f3, f4 )

다음 코드는 디미터 법칙을 어기는 듯 보인다.

```
final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath()
```

### 기차충돌 ###

위와 같은 코드를 기차충돌 이라고 부른다.

일반적으로 조잡하다 여겨지는 방식으로 피하는 편이 좋다.

다음과 같이 고치는 것이 좋다.

```
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```

위 코드는 디미터 법칙을 위반할까?

위 코드를 포함하는 함수는

ctxt 객체가 Options 을 포함하며, Options가 ScratchDir 을 포함하며, ScratchDir이

AbsolutePath 를 포함한다는 사실을 안다.

ctxt, Options, ScratchDir 이 자료구조 : 디미터 법칙을 위반하지 않음


ctxt, Options, ScratchDir 이 객체인 경우

```
class Context {
	private Options A;
	public Options getOptions(){
		return this.A;
	}
}

class File {
	private String C;
	public String getAbsolutePath() {
		return this.C;
	}
}
class Options {
	private File B;
	public File getScratchDir() {
		return this.B;
	}
}
```

내부 구조를 드러내기 때문에 디미터 법칙을 위반

```
final String outputDir = ctxt.options.scratchDir.absolutePath;
```

위와 같은 코드는 디미터 법칙을 거론할 필요가 없음

```
class Context {
	public Options options;
}

class Options {
	public File scratchDir;
}

class File {
	public String absolutePath;
}
```

자료구조는 함수 없이 공개 변수만 포함

객체는 사적함수와 공개함수를 포함 하는것이 좋음

하지만 그렇지 못한 경우가 생긴다

(단순 자료 구조에 조회 함수와 설정함수를 정의하라 요구하는 프레임워크와 표준 )

### 잡종 구조 ###

잡종 구조 : 절반은 객체 절반은 자료구조인 구조

새로운 함수와 새로운 자료구조 모두 추가하기 어려움


### 구조체 감추기 ###

```
class Context {
	public String getAbsolutePathOfScratchDirectoryOption() {
		
	}
}

public class test {
	public static void main(String[] args)
	{
		Context ctxt = new Context();
		ctxt.getScratchDirectoryOption().getAbsolutePath();
	}
}

```

객체에 공개해야 하는 메소드가 너무 많아진다.

```

class Options {
	public String scratchDir;
	public String getAbsolutePath() {
		return this.scratchDir;
	}
}

class Context {
	Options A;
	public Options getScratchDirectoryOption() {
		return this.A;
	}
}
public class test {
	public static void main(String[] args)
	{
		Context ctxt = new Context();
		ctxt.getScratchDirectoryOption();
	}
}
```

임시 디렉토리의 절대 경로가 필요한 이유?

```
String outFile = outputDir + "/" + className.replace('.', '/') + ".class";
FileOutputStream fout = new FileOutputStream(outFile);
BufferedOutputStream bos = new BufferedOutputStream(fout);
```

임시 파일을 생성하기 위함

```
BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName);
```



## 6.4 자료 전달 객체 ##

자료 구조체 : 공개 변수만 있고 함수가 없는 클레스

자료 구조체를 때로는 DTO(Data Transfer Object) 라고 한다.

DTO 는 DB통신 및 소켓에서 받은 메시지의 구문을 분석할때 유리


```
public class Address {
	private String street;
	private String streetExtra;
	private String city;
	private String state;
	private String zip;

	public Address(String street, String streetExtra, String city,
			String state, String zip) {
		this.street = street;
		this.streetExtra = streetExtra;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public String getStreetExtra() {
		return streetExtra;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}
}
```

위 코드는 private 변수를 조회/설정 함수로 조작한다.

어떤 이익도 제공하지 못한다.


```

class Ranch
{
        public $name;
        public $level;
        public $exp;
        public $energy;

        function __construct3($global, $watch, $row)
        {
                $row 에 디비에서 얻은값을 넣음
        }

        function gainExp(){}
        function useEnergy($global, $watch, $dbo, $value) {}
}


```

```

class Ranch
{
        public $name;
        public $level;
        public $exp;
        public $energy;
}

class RanchSystem 
{
        function gainExp(){}
        function useEnergy($global, $watch, $dbo, $value) {}
}

```
### 활성 레코드 ###

활성 레코드는 DTO의 특수한 형태

사적 변수에 조회/설정 함수가 있는 자료 구조지만 save 나 find 같은 탐색 함수도 제공

활성 레코드는 자료구조로 취급하고 비즈니스 규칙을 담으면서 내부 자료를 숨기는

객체는 따로 생성한다.

## 6.5 결론 ##

각각의 상황에 맞게 자료구조와 객체를 선택하여 사용할 수 있도록 한다.