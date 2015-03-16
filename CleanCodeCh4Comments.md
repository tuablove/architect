# 4장 주석 #

프로그래밍 언어를 조율해 의도를 표현할 능력이 있다면, 주석이 전혀 필요하지 않을 것이다.

실패: 주석은 자신을 표현할 방법을 못 찾아서 할 수 없이 주석을 사용한다.

주석이 필요한 상황에 처하면, 상황을 역전해 코드로 의도를 표현할 방법이 없을까 고민해보자.


거짓말을 하는 주석은 필요없다.

그것은 코드가 오래될 수록 코드를 설명하지 못하고, 다른 이야기가 되어 버린다.

```
MockRequest request;
private final String HTTP_DATE_REGEXP =
	"[SMTWF][a-z]{2}\\,\\s[0-9]{2}\\s[JFMASOND][a-z]{2}\\s"+
	"[0-9]{4}\\s[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}\\sGMT";
private Response response;
private FitNesseContext context;
private FileResponder responder;
private Locale saveLocale;
// Example: "Tue, 02 Apr 2003 22:18:49 GMT"
```

**// Example: "Tue, 02 Apr 2003 22:18:49 GMT"** <--- private member가 추가되면서 의미를 잃어버림

주석에는 절도가 필요하며, 부정확한 주석은 아예 없는 것보다 훨씬 더 나쁘다.

독자를 오도하고, _더 이상 지킬 필요가 없는 규칙이나 지켜서는 안 되는 규칙_을 명시한다.

## 4.1 주석은 나쁜 코드를 보완하지 못한다. ##
주석이 필요해지면(=지저분한 모듈이라는 사실을 자각) 코드를 정리해야 한다.

_다른 관점에서 빠르게(지저분하게) 구현하고, 정리해가면서 코드를 완성해가는 방법과 통하는 ..._

표현력이 풍부하고 깔끔하며 주석이 거의 없는 코드가 복잡하고 어수선하며 주석이 많이 달린 코드보다 훨씬 낫다.

**난장판을 주석으로 설명하려 애쓰는 대신에 그 난장판을 깨끗이 치우는데 시간을 보내라!**

## 4.2 코드로 의도를 표현하라 ##
(여기서는 delegate pattern 을 활용한 주석 제거를 설명함)

(delegate pattern 을 활용할 때는 추상화 수준에 주의하여야 합니다.)

```
// Check to see if the employee is eligible for full benefits
if ((employee.flags & HOURLY_FLAG) && (employee.age > 65))
```
비교.
```
if (employee.isEligibleForFullBenefits())
```


## 4.3 좋은 주석 ##
### 법적인 주석 ###
```
// Copyright (C) 2003,2004,2005 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the GNU General Public License version 2 or later.
```

### 정보를 제공하는 주석 ###
추상메소드의 반환할 값을 설명하는 주석
```
// Returns an instance of the Responder being tested.
protected abstract Responder responderInstance();
```

정규표현식이 시각과 날짜를 뜻한다는 주석
```
// format matched kk:mm:ss EEE, MMM dd, yyyy
Pattern timeMatcher = Pattern.compile("\\d*:\\d*:\\d* \\w*, \\w* \\d*, \\d*");
```
^시간과 날짜를 변환하는 클래스를 만들어 코드를 옮겨주면 더 낫고 더 깔끔하겠다.

### 의도를 설명하는 주석 ###
```
public int compareTo(Object o) {
	if(o instanceof WikiPagePath) {
		WikiPagePath p = (WikiPagePath) o;
		String compressedName = StringUtil.join(names, "");
		String compressedArgumentName = StringUtil.join(p.names, "");
		return compressedName.compareTo(compressedArgumentName);
	}
	return 1; // we are greater because we are the right type.
}
```

```
public void testConcurrentAddWidgets() throws Exception {
	WidgetBuilder widgetBuilder = new WidgetBuilder(new Class[]{BoldWidget.class});
	String text = "'''bold text'''";
	ParentWidget parent = new BoldWidget(new MockWidgetRoot(), "'''bold text'''");
	AtomicBoolean failFlag = new AtomicBoolean(); failFlag.set(false);

	//This is our best attempt to get a race condition
	//by creating large number of threads.
	for (int i = 0; i < 25000; i++) {
		WidgetBuilderThread widgetBuilderThread = new WidgetBuilderThread(widgetBuilder, text, parent, failFlag);
		Thread thread = new Thread(widgetBuilderThread);
		thread.start();
	}
	assertEquals(false, failFlag.get());
}
```

### 의미를 명료하게 밝히는 주석 ###
```
public void testCompareTo() throws Exception {
	WikiPagePath a = PathParser.parse("PageA");
	WikiPagePath ab = PathParser.parse("PageA.PageB");
	WikiPagePath b = PathParser.parse("PageB");
	WikiPagePath aa = PathParser.parse("PageA.PageA");
	WikiPagePath bb = PathParser.parse("PageB.PageB");
	WikiPagePath ba = PathParser.parse("PageB.PageA");

	assertTrue(a.compareTo(a) == 0);	// a == a
	assertTrue(a.compareTo(b) != 0);	// a != b 
	assertTrue(ab.compareTo(ab) == 0);	// ab == ab 
	assertTrue(a.compareTo(b) == -1);	// a < b 
	assertTrue(aa.compareTo(ab) == -1);	// aa < ab 
	assertTrue(ba.compareTo(bb) == -1);	// ba < bb 
	assertTrue(b.compareTo(a) == 1);	// b > a 
	assertTrue(ab.compareTo(aa) == 1);	// ab > aa 
	assertTrue(bb.compareTo(ba) == 1);	// bb > ba
}
```
_이런 주석은 올바른지 검증하기 쉽지 않다._ 이것이 **의미를 명료히 밝히는 주석**이 **필요한 이유**인 동시에 **위험한 이유**이다.

### 결과를 경고하는 주석 ###
```
// Don't run unless you
// have some time to kill.
public void _testWithReallyBigFile() {
	writeLinesToFile(10000000);
	response.setBody(testFile);
	response.readyToSend(this);
	String responseString = output.toString();
	assertSubString("Content-Length: 1000000000", responseString);
	assertTrue(bytesSent > 1000000000);
}
```
아래와 같은 방식을 사용할 수도 있다. JUnit4 이상
```
@Ignore("실행이 너무 오래 걸린다")
```

```
public static SimpleDateFormat makeStandardHttpDateFormat() {
	//SimpleDateFormat is not thread safe,
	//so we need to create each instance independently.
	SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
	df.setTimeZone(TimeZone.getTimeZone("GMT"));
	return df;
}
```
정적 초기화 함수를 사용하려던 열성적인 프로그래머가 주석으로 인해 실수를 면한다.
(과연.?)

### TODO 주석 ###
```
//TODO-MdM these are not needed
// We expect this to go away when we do the checkout model protected VersionInfo makeVersion() throws Exception
{
	return null;
}
```
주기적으로 TODO 주석을 점검해 제거가능한 주석은 없애라고 권한다.
```
// TODO:[R] 5Layer 로 구성된 것으 3Layer 이하로 재구성하자. - 읽기 너무 힘들다.

// TODO:[!] 성능문제를 야기하므로 아래 테스트 코드를 배포전에는 제거해야한다.

// TODO:[W] 향후 시간될 때 수정하면 좋을 것에 대한 주석

```

### 중요성을 강조하는 주석 ###
```
String listItemContent = match.group(3).trim();
// the trim is real important. It removes the starting
// spaces that could cause the item to be recognized
// as another list.
new ListItemWidget(this, listItemContent, this.level + 1);
return buildList(text.substring(match.end()));

```


### 공개 API에서 Javadocs ###
설명이 잘 된 공개된 API는 참으로 유용하고 만족스럽다.

공개 API를 구현한다면 반드시 훌륭한 Javadocs를 작성한다.

여느 주석과 마찬가지로 Javadocs 역시 독자를 오도하거나 잘못 놓이거나, 그릇된 정보를 전달할 가능성이 존재한다.

## 4.4 나쁜 주석 ##
대다수의 주석이 나쁜 주석이며, **허술한 코드를 지탱**하거나, **엉성한 코드**를 변명하거나, **미숙한 결정**을 합리화하는 등 프로그래머가 _주절거리는 독백_에서 크게 벗어나지 못한다.

### 주절거리는 주석 ###
```
public void loadProperties() {
try {
		String propertiesPath = propertiesLocation + "/" + PROPERTIES_FILE;
		FileInputStream propertiesStream = new FileInputStream(propertiesPath);
		loadedProperties.load(propertiesStream);
	}
	catch(IOException e) {
		// No properties files means all defaults are loaded
	}
}

```
언제 초기화 되었는지에 대한 의문을 풀기 위해서 다른 코드를 뒤져보는 수 밖에는 없다.

소통

### 같은 이야기를 중복하는 주석 ###
동작 자체를 설명하거나, 메소드명과 중복되는 주석은 노이즈 일 뿐.!!!
**Listing 4-1 waitForClose**
```
// Utility method that returns when this.closed is true. Throws an exception
// if the timeout is reached.
public synchronized void waitForClose(final long timeoutMillis) throws Exception {
	if(!closed) {
		wait(timeoutMillis);
		if(!closed)
			throw new Exception("MockResponseSender could not be closed");
	}
}

```

**Listing 4-2 ContainerBase.java (Tomcat)**
```
public abstract class ContainerBase implements Container, Lifecycle, Pipeline, MBeanRegistration, Serializable {
	/**
	* The processor delay for this component. */
	protected int backgroundProcessorDelay = -1;

	/**
	* The lifecycle event support for this component. */
	protected LifecycleSupport lifecycle = new LifecycleSupport(this);

	/**
	* The container event listeners for this Container. */
	protected ArrayList listeners = new ArrayList();

	/**
	* The Loader implementation with which this Container is * associated.
	*/
	protected Loader loader = null;

	/**
	* The Logger implementation with which this Container is
	* associated.
	*/
	protected Log logger = null;

	/**
	* Associated logger name. */
	protected String logName = null;

	/**
	* The Manager implementation with which this Container is * associated.
	*/
	protected Manager manager = null;

	/**
	* The cluster with which this Container is associated. */
	protected Cluster cluster = null;

	/**
	* The human-readable name of this Container. */
	protected String name = null;

	/**
	* The parent Container to which this Container is a child. */
	protected Container parent = null;

	/**
	* The parent class loader to be configured when we install a * Loader.
	*/
	protected ClassLoader parentClassLoader = null;

	/**
	* The Pipeline object with which this Container is * associated.
	*/
	protected Pipeline pipeline = new StandardPipeline(this);

	/**
	* The Realm with which this Container is associated. */
	protected Realm realm = null;

	/**
	* The resources DirContext object with which this Container * is associated.
	*/
	protected DirContext resources = null;

```

### 오해할 여지가 있는 주석 ###
앞의 [4-1](Listing.md) 에서 나오는 코드는 this.closed 값이 변경되는 경우 호출이 보장되지 못한다.
결국 매번 겁나 기다렸다가 그때도 this.closed 가 true 인 경우에만 제대로 진행된다.

  * 자바에 대한 몇 가지 추가 이해
    1. synchronized 메소드는 synchronized(this)로 메소드 내용을 싼 것과 같다.
    1. sync블록 내의 wait() 메소드는 sync블록에서 Lock으로 사용한 오브젝트에 대한 권한을 내려놓고 기다란다.
    1. lockObject.notify() 메소드가 호출되면, 권한을 내려놓고 기다리는 다른 쓰레드를 깨워준다.
    1. lockObject.notifyAll() 메소드가 호출되면, 권한을 내려놓고 기다리는 다른 쓰레드를 모두 깨워준다.

다시 말해서 위의 경우에도 notify를 딱 맞게 적용했다면, 저자의 말과 다르게 정상동작할 수 도 있다.

### 의무적으로 다는 주석 ###
**Listing 4-3**
```
/** *
 * @param title The title of the CD
 * @param author The author of the CD
 * @param tracks The number of tracks on the CD
 * @param durationInMinutes The duration of the CD in minutes */
public void addCD(String title, String author, int tracks, int durationInMinutes) {
	CD cd = new CD();
	cd.title = title;
	cd.author = author;
	cd.tracks = tracks;
	cd.duration = duration;
	cdList.add(cd);
}

```

### 이력을 기록하는 주석 ###
```
/*
 * Changes (from 11-Oct-2001)
 * --------------------------
 * 11-Oct-2001 : Re-organised the class and moved it to new package 
 *               com.jrefinery.date (DG);
 * 05-Nov-2001 : Added a getDescription() method, and eliminated NotableDate 
 *               class (DG);
 * 12-Nov-2001 : IBD requires setDescription() method, now that NotableDate 
 *               class is gone (DG); Changed getPreviousDayOfWeek(), 
 *               getFollowingDayOfWeek() and getNearestDayOfWeek() to correct 
 *               bugs (DG);
 * 05-Dec-2001 : Fixed bug in SpreadsheetDate class (DG);
 * 29-May-2002 : Moved the month constants into a separate interface 
 *               (MonthConstants) (DG);
 * 27-Aug-2002 : Fixed bug in addMonths() method, thanks to N???levka Petr (DG); 
 * 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Implemented Serializable (DG);
 * 29-May-2003 : Fixed bug in addMonths method (DG);
 * 04-Sep-2003 : Implemented Comparable. Updated the isInRange javadocs (DG);
 * 05-Jan-2005 : Fixed bug in addYears() method (1096282) (DG);
 */

```
이제는 SCM(git, svn, tfs 등) 에서 log, blame 등으로 제공해주므로 필요 없는 주석이다.


### 있으나 마나 한 주석 ###
```
/**
 * Default constructor.
 */
 protected AnnualDateRule() {
 }
```
그렇단 말이지? 다음 어떤가?
```
/** The day of the month. */
private int dayOfMonth;
```
이번에는 전형적인 중복을 보여준다.
```
/**
 * Returns the day of the month.
 *
 * @return the day of the month.
 */
public int getDayOfMonth() {
	return dayOfMonth;
}
```
위와 같은 주석은 지나친 참견이라 개발자가 주석을 무시하는 습관에 빠진다.

_Listing 4-4_ 에서 **첫 번째 주석은 적절**해 보인다. 하지만 _두 번째 주석은 전혀 쓸모 없다_.

**Listing 4-4 startSending**
```
private void startSending() {
	try {
		doSending();
	}
	catch(SocketException e) {
		// normal. someone stopped the request.
	}
	catch(Exception e) {
		try {
			response.add(ErrorResponder.makeExceptionString(e));
			response.closeAll();
		}
		catch(Exception e1) {
			//Give me a break!
		}
	}
}

```

아래 리팩토링 결과인 _Listing 4-5_ 를 살펴보자
**Listing 4-5 startSending (refactored)**
```
private void startSending() {
	try {
		doSending();
	}
	catch(SocketException e) {
		// normal. someone stopped the request.
	}
	catch(Exception e) {
		addExceptionAndCloseResponse(e);
	}
}
private void addExceptionAndCloseResponse(Exception e) {
	try {
		response.add(ErrorResponder.makeExceptionString(e));
		response.closeAll();
	}
	catch(Exception e1) {
	}
}

```


### 무서운 잡음 ###
Javadocs 의 잡음
```
/** The name. */
private String name;

/** The version. */
private String version;

/** The licenceName. */
private String licenceName;

/** The version. */
private String info;

```


### 함수나 변수로 표현할 수 있다면 주석을 달지 마라 ###
```
// does the module from the global list <mod> depend on the
// subsystem we are part of?
if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem()))
```

아래와 같이 개선해 보았다.
주석이 필요 없어졌다.
```
ArrayList moduleDependees = smodule.getDependSubsystems();
String ourSubSystem = subSysMod.getSubSystem();
if (moduleDependees.contains(ourSubSystem))
```


### 위치를 표시하는 주석 ###
```
// Actions //////////////////////////////////
```
아주 간혹 필요한 곳이 있으나 반드시 필요한 경우에 아주 드믈게 사용하는 편이 좋다.

```
#pragma mark - xxx
#pragma mark yyy
#pragma mark - constructor & life cycle
#pragma mark - property accessor & memory management
```


### 닫는 괄호에 다는 주석 ###
**Listing 4-6 wc.java**
```
	public class wc {
		public static void main(String[] args) {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String line;
			int lineCount = 0;
			int charCount = 0;
			int wordCount = 0;
			try {
				while ((line = in.readLine()) != null) {
					lineCount++;
					charCount += line.length();
					String words[] = line.split("\\W");
					wordCount += words.length;
				} //while
				System.out.println("wordCount = " + wordCount);
				System.out.println("lineCount = " + lineCount);
				System.out.println("charCount = " + charCount);
			} // try
			catch (IOException e) {
				System.err.println("Error:" + e.getMessage());
			} //catch
		} //main
	}

```
닫는 괄호에 주석은 작고 캡슐화된 함수에는 잡음일 뿐이다. 구조를 변경해라.

(비중을 제대로 이해하기 어려운 코드를 유지보수 할 때 어디에 한 줄을 어디에 넣어야 하는 지 고민에 빠질 수 있다.)

(모든 프로그래머가 당신 만큼 뛰어나지 않다. 그들이 이해할 수 있는 수준의 코드도 필요하다.)


### 공로를 돌리거나 저자를 표시하는 주석 ###
```
/* Added by Rick */
```
이미 SCM의 blame 으로 남기 때문에 중복 주석이다.


### 주석으로 처리한 코드 ###
```
	InputStreamResponse response = new InputStreamResponse();
	response.setBody(formatter.getResultStream(), formatter.getByteCount());
//	InputStream resultsStream = formatter.getResultStream();
//	StreamReader reader = new StreamReader(resultsStream);
//	response.setContent(reader.read(formatter.getByteCount()));
```
이런 코드는 사용되지 않지만, 다른 이는 코드 삭제를 주저하게 된다.

질 나쁜 와인병 바닥에 앙금이 쌓이듯 쓸모 없는 코드가 점점 쌓여간다.

**Apach commons에서 가져온 안 좋은 예**
```
this.bytePos = writeBytes(pngIdBytes, 0);
//hdrPos = bytePos;
writeHeader();
writeResolution();
//dataPos = bytePos;
if (writeImageData()) {
	writeEnd();
	this.pngBytes = resizeByteArray(this.pngBytes, this.maxPos);
}
else {
	this.pngBytes = null;
}
return this.pngBytes;

```

(이런 경우에 "TODO:[!] 다음 릴리즈에서 삭제" 로 남겨 둡니다.)

(이것을 만들어낸 프로그래머는 이번 릴리즈의 안정성을 위해서 남겨두지 않았을까요.?)


### HTML 주석 ###
저자는 도구가 책임져야한다고 주장하며, 이런 주석을 협오한다.

(필요악이지만, 꼭 필요한 순간엔 써줘야 하지 않을까.?)
```
/**
 * Task to run fit tests.
 * This task runs fitnesse tests and publishes the results.
 * <p/>
 * <pre>
 * Usage:
 * &lt;taskdef name=&quot;execute-fitnesse-tests&quot;
 *		classname=&quot;fitnesse.ant.ExecuteFitnesseTestsTask&quot;
 *		classpathref=&quot;classpath&quot; /&gt;
 * OR
 * &lt;taskdef classpathref=&quot;classpath&quot;
 * 		resource=&quot;tasks.properties&quot; /&gt;
 * <p/>
 * &lt;execute-fitnesse-tests
 *		suitepage=&quot;FitNesse.SuiteAcceptanceTests&quot;
 *		fitnesseport=&quot;8082&quot;
 * 		resultsdir=&quot;${results.dir}&quot;
 * 		resultshtmlpage=&quot;fit-results.html&quot;
 * 		classpathref=&quot;classpath&quot; /&gt;
 * </pre>
 */

```


### 전역 정보 ###
영어에서는 NonLocal Information 으로 현재 코드범위에 포함되지 않는 주석을 의미

이런 주석은 마땅히 클랙스 묶음(package, namespace) 쪽 주석으로 옮겨야 할 것 같다.
```
/**
 * Port on which fitnesse would run. Defaults to <b>8082</b>. *
 * @param fitnessePort
 */
public void setFitnessePort(int fitnessePort) {
	this.fitnessePort = fitnessePort;
}

```


### 너무 많은 정보 ###
Base64 Encoding/decoding 을 테스트하는 곳에서 가져온 주석으로

Base64에 대한 설명을 주르륵..!!
```
/*
	RFC 2045 - Multipurpose Internet Mail Extensions (MIME)
	Part One: Format of Internet Message Bodies
	section 6.8. Base64 Content-Transfer-Encoding
	The encoding process represents 24-bit groups of input bits as output
	strings of 4 encoded characters. Proceeding from left to right, a
	24-bit input group is formed by concatenating 3 8-bit input groups.
	These 24 bits are then treated as 4 concatenated 6-bit groups, each
	of which is translated into a single digit in the base64 alphabet.
	When encoding a bit stream via the base64 encoding, the bit stream
	must be presumed to be ordered with the most-significant-bit first.
	That is, the first bit in the stream will be the high-order bit in
	the first 8-bit byte, and the eighth bit will be the low-order bit in
	the first 8-bit byte, and so on.
*/
```


### 모호한 관계 ###
Apache commons 에서 가져온 주석으로 주석이 스스로 설명을 필요로 하므로 안타깝기 그지 없다.
```
/*
 * start with an array that is big enough to hold all the pixels
 * (plus filter bytes), and an extra 200 bytes for header info
 */
this.pngBytes = new byte[((this.width + 1) * this.height * 3) + 200];

```


### 함수 헤더 ###
짧은 함수는 긴 설명이 필요 업다.

짧고 한 가지만 수행하며 이름을 잘 붙인 함수가 주석으로 헤더를 추가한 함수보다 훨씬 낫다.

(단위 함수 하나만 보면 그렇지만, 읽는 프로그래머 입장에서는) <br>
(100개 컴포넌트(함수)를 이해해야하는 것과 3개의 복잡한 컴포넌트를 이해하는 것) <br>
(어떤 것이 읽기 쉬울까요.?) <br>
(짧고 한 가지만 수행하는 함수를 주로 사용한다면, <i>이름을 잘 붙이는</i> 것 외에도 <b>추상화 수준(Weighting)</b>에 대한 전략이 필요하다.) <br>
(Weighting 을 논하지 않고는 대응이 어렵다.)<br>
<br>
<br>
<h3>비공개 코드에서 Javadocs</h3>
공개 API는 Javadocs가 유용하지만 공개하지 않을 코드라면 Javadocs는 쓸모 없다.<br>
<br>
시스템 내부에 속하는 클래스와 함수에 Javadocs 를 생성할 필요는 없다.<br>
<br>
유용하지 않을 뿐만 아니라 Javadocs 주석이 요구하는 형식으로 인해 코드만 보기 싫고 산만해질 뿐이다.<br>
<br>
(html에 익숙한 개발자라면, 기획내용/ 개념설계/ 구현설계 이것을 코드에 담을 때 javadocs를 사용할 수 있다.) <br>
(주석은 다른 이가 보기 편하게 다는 것도 중요하지만, 스스로에게 필요한 정보를 잘 정리해 놓는 것도 의미 있지 않을까.?)<br>
<br>
<h3>예제</h3>
<b>Listing 4-7 GeneratePrimes.java</b>
<pre><code>/**<br>
 * This class Generates prime numbers up to a user specified<br>
 * maximum. The algorithm used is the Sieve of Eratosthenes.<br>
 * &lt;p&gt;<br>
 * Eratosthenes of Cyrene, b. c. 276 BC, Cyrene, Libya --<br>
 * d. c. 194, Alexandria. The first man to calculate the<br>
 * circumference of the Earth. Also known for working on<br>
 * calendars with leap years and ran the library at Alexandria.<br>
 * &lt;p&gt;<br>
 * The algorithm is quite simple. Given an array of integers<br>
 * starting at 2. Cross out all multiples of 2. Find the next<br>
 * uncrossed integer, and cross out all of its multiples.<br>
 * Repeat untilyou have passed the square root of the maximum<br>
 * value. *<br>
 * @author Alphonse<br>
 * @version 13 Feb 2002 atp */<br>
import java.util.*;<br>
public class GeneratePrimes {<br>
	/**<br>
	 * @param maxValue is the generation limit. */<br>
	public static int[] generatePrimes(int maxValue) {<br>
		if (maxValue &gt;= 2) // the only valid case<br>
		{<br>
			// declarations<br>
			int s = maxValue + 1; // size of array<br>
			boolean[] f = new boolean[s];<br>
			int i;<br>
			// initialize array to true.<br>
			for (i = 0; i &lt; s; i++)<br>
				f[i] = true;<br>
<br>
			// get rid of known non-primes<br>
			f[0] = f[1] = false;<br>
<br>
			// sieve<br>
			int j;<br>
			for (i = 2; i &lt; Math.sqrt(s) + 1; i++) {<br>
				if (f[i]) // if i is uncrossed, cross its multiples.<br>
				{<br>
					for (j = 2 * i; j &lt; s; j += i)<br>
						f[j] = false; // multiple is not prime<br>
				}<br>
			}<br>
<br>
			// how many primes are there?<br>
			int count = 0;<br>
			for (i = 0; i &lt; s; i++)<br>
			{<br>
				if (f[i])<br>
					count++; // bump count.<br>
			}<br>
<br>
			int[] primes = new int[count];<br>
<br>
			// move the primes into the result<br>
			for (i = 0, j = 0; i &lt; s; i++)<br>
			{<br>
				if (f[i])	// if prime<br>
					primes[j++] = i;<br>
			}<br>
<br>
			return primes; // return the primes<br>
		}<br>
		else // maxValue &lt; 2<br>
			return new int[0];	// return null array if bad input.<br>
		}<br>
	}<br>
}<br>
<br>
</code></pre>


<b>Listing 4-8 PrimeGenerator.java (refactored)</b>
<pre><code>/**<br>
 * This class Generates prime numbers up to a user specified<br>
 * maximum. The algorithm used is the Sieve of Eratosthenes.<br>
 * Given an array of integers starting at 2:<br>
 * Find the first uncrossed integer, and cross out all its<br>
 * multiples. Repeat until there are no more multiples * in the array.<br>
 */<br>
public class PrimeGenerator {<br>
	private static boolean[] crossedOut; private static int[] result;<br>
	public static int[] generatePrimes(int maxValue)<br>
	{<br>
<br>
		if (maxValue &lt; 2)<br>
			return new int[0];<br>
		else {<br>
			uncrossIntegersUpTo(maxValue);<br>
			crossOutMultiples();<br>
			putUncrossedIntegersIntoResult();<br>
			return result;<br>
		}<br>
	}<br>
<br>
	private static void uncrossIntegersUpTo(int maxValue)<br>
	{<br>
		crossedOut = new boolean[maxValue + 1];<br>
		for (int i = 2; i &lt; crossedOut.length; i++)<br>
			crossedOut[i] = false;<br>
	}<br>
<br>
	private static void crossOutMultiples()<br>
	{<br>
		int limit = determineIterationLimit();<br>
		for (int i = 2; i &lt;= limit; i++)<br>
			if (notCrossed(i))<br>
				crossOutMultiplesOf(i);<br>
	}<br>
<br>
	private static int determineIterationLimit()<br>
	{<br>
		// Every multiple in the array has a prime factor that<br>
		// is less than or equal to the root of the array size,<br>
		// so we don't have to cross out multiples of numbers<br>
		// larger than that root.<br>
		double iterationLimit = Math.sqrt(crossedOut.length);<br>
		return (int) iterationLimit;<br>
	}<br>
<br>
	private static void crossOutMultiplesOf(int i)<br>
	{<br>
		for (int multiple = 2*i;<br>
			 multiple &lt; crossedOut.length;<br>
			 multiple += i)<br>
		  crossedOut[multiple] = true;<br>
	}<br>
<br>
	private static boolean notCrossed(int i)<br>
	{<br>
		return crossedOut[i] == false;<br>
	}<br>
<br>
	private static void putUncrossedIntegersIntoResult()<br>
	{<br>
		result = new int[numberOfUncrossedIntegers()];<br>
		for (int j = 0, i = 2; i &lt; crossedOut.length; i++)<br>
			if (notCrossed(i))<br>
				result[j++] = i;<br>
	}<br>
<br>
	private static int numberOfUncrossedIntegers()<br>
	{<br>
		int count = 0;<br>
		for (int i = 2; i &lt; crossedOut.length; i++)<br>
			if (notCrossed(i))<br>
				count++;<br>
<br>
		return count;<br>
	}<br>
}<br>
<br>
</code></pre>



<b>요약</b>
<ol><li>좋은 주석<br>
<ul><li>동작 자체를 기술하는 것 보다<br>
<blockquote>의도/ 목표/ 경고/ 전략/ 방향 등을 표현하는 주석<br>
</blockquote></li></ul></li><li>나쁜 주석<br>
<ul><li>주석은 그 자체로 나쁘다.<br>
</li><li>(하지만, "3장 함수 - 추상화 수준"에 비추어 보면, 좋은 주석일 수 있다.)