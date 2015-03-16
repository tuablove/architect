# 8장 경계 #

시스템에 들어가는 모든 소프트웨어를 직접 개발하는 경우는 드물다.

패키지를 사용하고, 오픈소스를 이용하고, 혹은 다른 팀이 제공하는 컴포넌트를 사용한다.

이 외부 코드를 우리 코드에 깔끔하게 통합해야 한다.

## 8.1 외부 코드 사용하기 ##

패키지 제공자는 적용성을 최대한 넓히려고 애쓴다

사용자는 자신의 요구사항에 집중하는 인터페이스를 바란다.

이로 인해 시스템 경계는 문제가 생길 소지가 많다.


**목록 6-1** java.util.

Map 이 제공하는 인터페이스

• clear() void – Map

• containsKey(Object key) boolean – Map

• containsValue(Object value) boolean – Map

• entrySet() Set – Map

• equals(Object o) boolean – Map

• get(Object key) Object – Map

• getClass() Class<? extends Object> – Object

• hashCode() int – Map

• isEmpty() boolean – Map

• keySet() Set – Map

• notify() void – Object

• notifyAll() void – Object

• put(Object key, Object value) Object – Map

• putAll(Map t) void – Map

• remove(Object key) Object – Map

• size() int – Map

• toString() String – Object

• values() Collection – Map

• wait() void – Object

• wait(long timeout) void – Object

• wait(long timeout, int nanos) void – Object

다양한 인터페이스 제공은 예기치 못한 위험을 가지고 있다.

예를 들어

1. clear() 메소드를 제공함으로써 누구나 내용을 지울수 있다.

2. Map은 객체 유형을 제한하지 않으므로 누구나 객체 유형을 추가할 수 있다.

```
Map sensors = new HashMap();

Sensor s = (Sensor)sensors.get(sensorId );
```

위의 코드는 의도가 분명히 드러나지 않는다.

다음과 같이 제네릭을 사용하면 코드 가독성이 크게 높아진다.

```
Map<Sensor> sensors = new HashMap<Sensor>();
...
Sensor s = sensors.get(sensorId );
```

하지만 위 방법도 `Map<Sensor>`가 사용자에게 필요하지 않은 기능까지 제공한다"

`Map<Sensor>` 인스턴스를 여기저기로 넘길시 수정할 코드가 많아진다.

```
public class Sensors {
	private Map sensors = new HashMap();

	public Sensor getById(String id) {
		return (Sensor) sensors.get(id);
	}
	// snip
}
```

Sensors 사용자는 제네릭 사용 여부에 신경쓸 필요가 없다.

Map은 Sensors class 안으로 숨긴다. (Map 인터페이스가 변하더라도 나머지 프로그램에 영향을 미치지 않는다.)

Sensors class 는 프로그램에 필요한 인터페이스만 제공한다.(코드는 이해하기 쉬우며 오용하기 어렵다.)


Map과 같은 경계 인터페이스를 이용할 때는 이를 이용하는 클래스나 클래스 밖으로 노출되지 않도록 한다.

Map 인스턴스를 공개 API의 인수로 넘기거나 반환 값으로 사용하지 않는다.

## 8.2 경계 살피고 익히기 ##

외부 패키지 테스트는 우리 자신을 위해 우리가 사용할 코드를 테스트 하는 편이 바람직하다.

외부코드를 익히기는 어렵다.

외부 코드를 통합하기도 어렵다.

=> 간단한 테스트 케이스를 작성해 외부 코드를 익혀야 한다. 이를 학습테스트라 부른다.

학습 테스트는 프로그램에서 사용하려는 방식대로 외부 API 를 호출한다.

통제된 환경에서 API 를 제대로 이해했는지 확인하는 것이다.

## 8.3 log4j 익히기 ##

log4j 학습 테스트

```
@Test
public void testLogCreate() {
	Logger logger = Logger.getLogger("MyLogger");
	logger.info("hello");
}
```

Appender 라는 뭔가가 필요하다는 오류 발생

```
@Test
public void testLogAddAppender() {
	Logger logger = Logger.getLogger("MyLogger");
	ConsoleAppender appender = new ConsoleAppender();
	logger.addAppender(appender);
	logger.info("hello");
}
```

Appender에 출력 스트림이 없다는 사실을 발견

```
@Test
public void testLogAddAppender() {
	Logger logger = Logger.getLogger("MyLogger");
	logger.removeAllAppenders();
	logger.addAppender(new ConsoleAppender(new PatternLayout("%p %t %m%n"),
			ConsoleAppender.SYSTEM_OUT));
	logger.info("hello");
}
```

ConsoleAppender.SYSTEM\_OUT 인수를 제거 했더니 문제가 없다.

PatternLayout 인수를 제거 했더니 또 다시 출력 스트림이 없다는 오류가 생긴다.

ConsoleAppender 생성자는 "설정되지 않은" 상태이다.

이와 같이 log4j가 돌아가는 방식을 이해 한다.

```
public class LogTest {
	private Logger logger;

	@Before
	public void initialize() {
		logger = Logger.getLogger("logger");
		logger.removeAllAppenders();
		Logger.getRootLogger().removeAllAppenders();
	}

	@Test
	public void basicLogger() {
		BasicConfigurator.configure();
		logger.info("basicLogger");
	}

	@Test
	public void addAppenderWithStream() {
		logger.addAppender(new ConsoleAppender(new PatternLayout("%p %t %m%n"),
				ConsoleAppender.SYSTEM_OUT));
		logger.info("addAppenderWithStream");
	}

	@Test
	public void addAppenderWithoutStream() {
		logger.addAppender(new ConsoleAppender(
						new PatternLayout("%p %t %m%n")));
		logger.info("addAppenderWithoutStream");
	}
}
```

위와 같은 학습테스트를 한후 독자적인 로거 클레스로 캡슐화 한다.

## 8.4 학습 테스트는 공짜 이상이다 ##

학습 테스트는

필요한 지식만 확보하는 손쉬운 방법이다.

패키지의 새버전이 나온다면 학습 테스트를 돌려 차이가 있는지 확인한다.

학습테스트를 이용한 학습이 필요하든 그렇지 않든, 실제 코드와 동일한 바식으로 인터페이스를 사용하는 테스트 케이스가 필요하다.

## 8.5 아직 존재하지 않는 코드를 사용하기 ##

아는 코드와 모르는 코드를 분리하는 경계가 있다.

우리 지식이 경계를 넘어 미치지 못하는 코드 영역이 있다.

더 이상 내다보지 않기로 한다.

## 8.6 클린 경계 ##

통제하지 못하는 코드를 사용할 때는 너무 많은 투ㅏ를 하거나 향후 변경 비용이 커지지 않도록 주의하여야 한다.

경계에 위치하는 코드는 깔끔하게 분리한다.

기대치를 정의하는 테스트 케이스도 작성한다.

통제가 불가능한 외부 패키지에 의존하는 대신 통제가 가능한 우리 코드에 의존하는 편이 낫다.