# 13 동시성 #



## 13.1 동시성이 필요한 이유 ##

### 미신과 오해 ###
|_**Concurrency always improves performance.**_|
|:---------------------------------------------|
|Concurrency can sometimes improve performance, but only when there is a lot of wait time that can be shared between multiple threads or multiple processors. Neither situation is trivial.|
|  |
|_**Design does not change when writing concurrent programs.**_|
|In fact, the design of a concurrent algorithm can be remarkably different from the design of a single-threaded system. The decoupling of what from when usually has a huge effect on the structure of the system.|
|  |
|_**Understanding concurrency issues is not important when working with a container such as a Web or EJB container.**_|
|In fact, you’d better know just what your container is doing and how to guard against the issues of concurrent update and deadlock described later in this chapter. <br />Here are a few more balanced sound bites regarding writing concurrent software:|
|  |
|_**Concurrency incurs some overhead, both in performance as well as writing additional code.**_|
|  |
|_**Correct concurrency is complex, even for simple problems.**_|
|  |
|_**Concurrency bugs aren’t usually repeatable, so they are often ignored as one-offs instead of the true defects they are.**_|
|  |
|_**Concurrency often requires a fundamental change in design strategy.**_|


## 13.2 난관 ##
```
public class X {
    private int lastIdUsed;
    public int getNextId() {
        return ++lastIdUsed;
    }
}
```
|Thread one|Thread two|lastIdUsed|Description|
|:---------|:---------|:---------|:----------|
|43|44|44|Thread One이 먼저 처리된 이후에 Thread Two가 처리된 경우|
|44|43|44|Thread Two가 먼저 처리된 이후에 Thread One이 처리된 경우|
|43|43|43|lastIdUsed의 값이 변경되기전에 Thread One, Two가 동시에 값을 읽어간 경우(가능성 높음)|

|++lastIdUsed|읽기|1더하기|
|:-----------|:-----|:---------|

|lastIdUsed|42|->| **43** |->| **43** |
|:---------|:-|:-|:-------|:-|:-------|
|스레드1| _읽기(42)_ |더하기(43)| _쓰기_ |- |- |
|스레드2|- | _읽기(42)_ |- |더하기(43)| _쓰기_ |


```
public class X {
    private volatile int lastIdUsed;    // java.util.concurrent.atomic.AtomicLong lastIdUsed;

    public int getNextId() {
        synchronized(this) {
            return ++lastIdUsed;         // return lastIdUsed.getAnddIncrement();
        }
    }
}
```
[Volatile\_variable](http://en.wikipedia.org/wiki/Volatile_variable#In_Java)로 변경하고,monitor Lock 을 걸어서 해결.
AtomicLong 을 사용해서 해결하는 방법도 있음.

```
public class StopThread {
    private static boolean stopRequested;

    public static void main(String[] args)
            throws InterruptedException {
        Thread backgroundThread = new Thread(new Runnable() {
            public void run() {
                int i = 0;
                while (!stopRequested)
                    i++;
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
```
위 코드는 Runtime 에 따라서 무한루프에 빠진다.

## 13.3 동시성 방어 원칙 ##
  * 동신성 코드는 독자적인 개발, 변경, 조율 주기가 있다.
  * 동시성 코드에는 독자적인 난관이 있다. 다른 코드에서 겪는 난관과 다르며 훨씬 어렵다.
  * 잘못 구현한 동시성 코드는 별의 별 방식으로 실패한다. 주변에 있는 다른 코드가 발목을 잡지 않더라도 동시성 하나만으로도 충분히 어렵다.

```
권장사항: 동시성 코드는 다른 코드와 분리하라.
```

### 단일 책임 원칙(SRP, Single Responsibility Principle) ###

### 추론: 자료 범위를 제한하라 ###

### 추론: 자료 사본을 사용하라 ###

### 추론: 스레드는 가능한 독립적으로 구현하라 ###

## 13.4 라이브러리를 이해하라 ##

### 스레드 환경에 안전한 집합 컬렉션 ###

## 13.5 실행 모델을 이해하라 ##

### 생산자-소비자(Producer-Consumer) [wikipedia](http://en.wikipedia.org/wiki/Producer-consumer) ###

### 읽기-쓰기(Readers-Writer) [wikipedia](http://en.wikipedia.org/wiki/Readers-writers_problem) ###

### 식사하는 철학자들(Dining Philosophers) [wikipedia](http://en.wikipedia.org/wiki/Dining_philosophers_problem) ###

## 13.6 동기화하는 메소드 사이에 의존성을 이해하라 ##

## 13.7 동기화하는 부분을 작게 만들어라 ##

## 13.8 올바른 종료 코드는 구현하기 어렵다 ##

## 13.9 스레드 코드 테스트하기 ##

### 말이 안 되는 실패는 잠정적인 스레드 문제로 취급하라 ###

### 다중 스레드를 골하지 않은 순차 코드부터 제대로 돌게 만들자 ###

### 다중 스레드를 쓰는 코드 부분을 쉽게 다양한 환경에 끼워 넣을 수 있게 스레드를 구현하라 ###

### 다중 스레드를 쓰는 코드 부분을 상황에 맞게 조정할 수 있게 작성하라 ###

### 프로세서 수보다 많은 스레드를 돌려보라 ###

### 다른 플랫폼에서 돌려보라 ###

### 코드에 보조 코드를 넣어서 돌려라. 강제로 실패를 일으키게 해보라 ###

### 직접 구현하기 ###

### 자동화 ###

## 13.10 결론 ##
|difficult|다중 스레드 코드는 올바로 구현하기 어렵다. - 공유자료의 무결성이 망가지면 대응하기 어렵죠.|
|:--------|:-------------------------------------------------------------------------------------------------------------------------------|
|SRP|**SRP**(Single Responsibility Principle)를 준수하고, 동시성이 필요한 코드는 작게 만들어야 테스트 용이하죠.|
|이해|라이브러리와 기본 알고리즘을 이해하고,|
|LockedSection|보호할 코드 영역을 찾아내는 방법과 특정 코드 영역을 잠그는 방법을 이해하고, 잠긴 영역에서는 다른 잠긴 영역을 호출하지 않아야하고, <br>다시 말해 공유하는 개체 수와 범위를 최대한 줄이고, 공유 상태의 관리 책임을 시스템 밖에 넘기지 않아야죠.<br>
<tr><td>Test</td><td><b>TDD 3대규칙</b>(테스트코드 먼저 만들기, 테스트코드 컴파일되게 만들기, 테스트에 성공하는 최소의 실제코드 작성)을 잘 따라서 <br>테스트 용의성을 확보하고, <br />많은 플랫폼-다양한 구성으로 반복해서 계속 테스트 해야죠.</td></tr>
<tr><td>instrument</td><td>시간을 들여 보조코드(yield, sleep 등)를 삽입하여 실행 순서를 뒤엉키게 하고, 최대한 오랫동안 돌려봐야죠.</td></tr>
<tr><td>Clean approach</td><td> 제대로 돌아갈 가능성이 올라가죠, drastically</td></tr></tbody></table>

<br />

---


<br /><br /><br />
# Examples on Effective Java Second Edition #
## 용어 ##
|상호배타|상태전이|안전출판|
|:-----------|:-----------|:-----------|
|효율적인 가변객체|
|활동실패|안전실패|
|교착상태|개방호출|

|CountDownLatch|Semaphore|CyclicBarrier|Exchanger|
|:-------------|:--------|:------------|:--------|
|비논리 기상|

|불변|무조건적 스레드 안전|조건적 스레드 안전|스레드 안전하지 않음|스레드 적대|
|:-----|:----------------------------|:-------------------------|:----------------------------|:---------------|

|서비스 거부|
|:---------------|

|늦 초기화 홀더 클래스|이중-검사|단일-검사|racy single-check idiom|
|:-----------------------------|:------------|:------------|:----------------------|

|분주-대기|
|:------------|


결론적으로 **가변 데이터의 사용은 단일 스레드로 제한하자** - "Single Thread Confinement"<br>
<b>효율적인 가변객체</b> 정적인 객체의 참조를 공유하는 메소드만 동기화하여 해결할 수 있고,<br>
이 <b>효율적인 가변객체</b>를 다른 스레드로 전달하는 것을 <b>안전출판</b>이라 한다.<br>
<br>
여러 스레드가 가변 데이터를 공유할 때, 그 데이터를 읽거나 쓰는 각 스레드에서는 반드시 동기화를 해야한다.<br>
<table><thead><th>상호배타</th><th>상태전이</th><th>활동실패</th><th>안전실패</th><th>안전출판</th></thead><tbody></tbody></table>

<pre><code>// Broken! - How long would you expect this program to run?<br>
public class StopThread {<br>
    private static boolean stopRequested;<br>
    public static void main(String[] args)<br>
            throws InterruptedException {<br>
        Thread backgroundThread = new Thread(new Runnable() {<br>
            public void run() {<br>
                int i = 0;<br>
                while (!stopRequested)<br>
                    i++;<br>
            }<br>
        });<br>
        backgroundThread.start();<br>
<br>
        TimeUnit.SECONDS.sleep(1);<br>
        stopRequested = true;<br>
    }<br>
}<br>
</code></pre>

<pre><code>while (!done)<br>
    i++;<br>
</code></pre>
into this code:<br>
<pre><code>    if (!done)<br>
        while (true)<br>
            i++;<br>
</code></pre>

<pre><code>// Properly synchronized cooperative thread termination<br>
public class StopThread {<br>
    private static boolean stopRequested;<br>
    private static synchronized void requestStop() {<br>
        stopRequested = true;<br>
    }<br>
    private static synchronized boolean stopRequested() {<br>
        return stopRequested;<br>
    }<br>
<br>
    public static void main(String[] args)<br>
            throws InterruptedException {<br>
        Thread backgroundThread = new Thread(new Runnable() {<br>
            public void run() {<br>
                int i = 0;<br>
                while (!stopRequested())<br>
                    i++;<br>
            }<br>
        });<br>
        backgroundThread.start();<br>
<br>
        TimeUnit.SECONDS.sleep(1);<br>
        requestStop();<br>
    }<br>
}<br>
</code></pre>

<pre><code>// Cooperative thread termination with a volatile field<br>
public class StopThread {<br>
    private static volatile boolean stopRequested;<br>
<br>
    public static void main(String[] args)<br>
            throws InterruptedException {<br>
        Thread backgroundThread = new Thread(new Runnable() {<br>
            public void run() {<br>
                int i = 0;<br>
                while (!stopRequested)<br>
                    i++;<br>
            }<br>
        });<br>
        backgroundThread.start();<br>
<br>
        TimeUnit.SECONDS.sleep(1);<br>
        stopRequested = true;<br>
    }<br>
}<br>
</code></pre>

<pre><code>// Broken - requires synchronization!<br>
private static volatile int nextSerialNumber = 0;<br>
<br>
public static int generateSerialNumber() {<br>
    return nextSerialNumber++;<br>
}<br>
</code></pre>
<pre><code>private static final AtomicLong nextSerialNum = new AtomicLong();<br>
<br>
public static long generateSerialNumber() {<br>
    return nextSerialNum.getAndIncrement();<br>
}<br>
</code></pre>

<table><thead><th><i><b>활동실패</b>, <b>안전실패</b>가 생기지 않도록 하려면,</i></th></thead><tbody>
<tr><td>  </td></tr>
<tr><td>동기화 된 메소드나 블록 안에서 절대로 클라이언트에 제어권을 넘기면 안된다.</td></tr>
<tr><td>동기화 영역 안에서 외계인메소드를 절대 호출하지 말자.</td></tr>
<tr><td>동기화된 영역 안에서 해야할 일의 양을 제한해야 한다.</td></tr>
<tr><td>가변 클래스를 설계할 때는 자체적으로 동기화해야 하는지 잘 생각하자.</td></tr>
<tr><td>동기화를 지나치게 하지 않는 것이 더 중요하다.</td></tr>
<tr><td>내부적 동기화 결정 사항을 명확하게 문서화 하자.</td></tr></tbody></table>

<pre><code>// Broken - invokes alien method from synchronized block!<br>
public class ObservableSet&lt;E&gt; extends ForwardingSet&lt;E&gt; {<br>
    public ObservableSet(Set&lt;E&gt; set) { super(set); }<br>
<br>
    private final List&lt;SetObserver&lt;E&gt;&gt; observers =<br>
        new ArrayList&lt;SetObserver&lt;E&gt;&gt;();<br>
<br>
    public void addObserver(SetObserver&lt;E&gt; observer) {<br>
        synchronized(observers) {<br>
            observers.add(observer);<br>
        }<br>
    }<br>
    public boolean removeObserver(SetObserver&lt;E&gt; observer) {<br>
        synchronized(observers) {<br>
            return observers.remove(observer);<br>
        }<br>
    }<br>
    private void notifyElementAdded(E element) {<br>
        synchronized(observers) {<br>
            for (SetObserver&lt;E&gt; observer : observers)<br>
        }<br>
    }<br>
<br>
    @Override public boolean add(E element) {<br>
        boolean added = super.add(element);<br>
        if (added)<br>
            notifyElementAdded(element);<br>
        return added;<br>
    }<br>
<br>
    @Override public boolean addAll(Collection&lt;? extends E&gt; c) {<br>
        boolean result = false;<br>
        for (E element : c)<br>
        result |= add(element); //calls notifyElementAdded<br>
        return result;<br>
    }<br>
}<br>
</code></pre>

<pre><code> public interface SetObserver&lt;E&gt; {<br>
    // Invoked when an element is added to the observable set<br>
    void added(ObservableSet&lt;E&gt; set, E element);<br>
}<br>
</code></pre>
<pre><code>public static void main(String[] args) {<br>
    ObservableSet&lt;Integer&gt; set =<br>
        new ObservableSet&lt;Integer&gt;(new HashSet&lt;Integer&gt;());<br>
<br>
        set.addObserver(new SetObserver&lt;Integer&gt;() {<br>
            public void added(ObservableSet&lt;Integer&gt; s, Integer e) {<br>
                System.out.println(e);<br>
            }<br>
        });<br>
<br>
        for (int i = 0; i &lt; 100; i++)<br>
            set.add(i);<br>
}<br>
</code></pre>
<pre><code>set.addObserver(new SetObserver&lt;Integer&gt;() {<br>
    public void added(ObservableSet&lt;Integer&gt; s, Integer e) {<br>
        System.out.println(e);<br>
        if (e == 23) s.removeObserver(this);<br>
    }<br>
});<br>
</code></pre>
<pre><code>// Observer that uses a background thread needlessly<br>
set.addObserver(new SetObserver&lt;Integer&gt;() {<br>
    public void added(final ObservableSet&lt;Integer&gt; s, Integer e) {<br>
        System.out.println(e);<br>
        if (e == 23) {<br>
            ExecutorService executor =<br>
                Executors.newSingleThreadExecutor();<br>
            final SetObserver&lt;Integer&gt; observer = this;<br>
            try {<br>
                executor.submit(new Runnable() {<br>
                    public void run() {<br>
                        s.removeObserver(observer);<br>
                    }<br>
                }).get();<br>
            } catch (ExecutionException ex) {<br>
                throw new AssertionError(ex.getCause());<br>
            } catch (InterruptedException ex) {<br>
                throw new AssertionError(ex.getCause());<br>
            } finally {<br>
                executor.shutdown();<br>
            }<br>
        }<br>
    }<br>
});<br>
</code></pre>
<pre><code>// Alien method moved outside of synchronized block - open calls<br>
private void notifyElementAdded(E element) {<br>
    List&lt;SetObserver&lt;E&gt;&gt; snapshot = null;<br>
    synchronized(observers) {<br>
        snapshot = new ArrayList&lt;SetObserver&lt;E&gt;&gt;(observers);<br>
    }<br>
    for (SetObserver&lt;E&gt; observer : snapshot)<br>
        observer.added(this, element);<br>
}<br>
</code></pre>
<pre><code>// Thread-safe observable set with CopyOnWriteArrayList<br>
private final List&lt;SetObserver&lt;E&gt;&gt; observers =<br>
    new CopyOnWriteArrayList&lt;SetObserver&lt;E&gt;&gt;();<br>
public void addObserver(SetObserver&lt;E&gt; observer) {<br>
    observers.add(observer);<br>
}<br>
public boolean removeObserver(SetObserver&lt;E&gt; observer) {<br>
    return observers.remove(observer);<br>
}<br>
private void notifyElementAdded(E element) {<br>
    for (SetObserver&lt;E&gt; observer : observers)<br>
        observer.added(this, element);<br>
}<br>
</code></pre>

1.5 부터 java.util.concurrent 패키지의 Executor Framework을 제공함.<br>
execute(), shutdown(), invokeAny(), invokeAll(), awaitTermination, ExecutorCompletionService()<br>
<br>
ThreadPoolExecutor, newCachedThreadPool, newFixedThread<br>
<br>
Runnable, Callable(반환값 있음)<br>
ScheduledThreadPoolExecutor 로 java.util.Timer 대체(다중쓰레드지원, uchecked예외 복구)<br>
<br>
<pre><code>ExecutorService executor = Executors.newSingleThreadExecutor();<br>
</code></pre>
<pre><code>executor.execute(runnable);<br>
</code></pre>
<pre><code>executor.shutdown();<br>
</code></pre>


"CountDownLatch", "Semaphore", "CyclicBarrier", "Exchanger"<br>
<pre><code>// Concurrent canonicalizing map atop ConcurrentMap - not optimal<br>
private static final ConcurrentMap&lt;String, String&gt; map =<br>
    new ConcurrentHashMap&lt;String, String&gt;();<br>
<br>
public static String intern(String s) {<br>
    String previousValue = map.putIfAbsent(s, s);<br>
    return previousValue == null ? s : previousValue;<br>
}<br>
</code></pre>

<pre><code>// Concurrent canonicalizing map atop ConcurrentMap - faster!<br>
public static String intern(String s) {<br>
    String result = map.get(s);<br>
    if (result == null) {<br>
        result = map.putIfAbsent(s, s);<br>
        if (result == null)<br>
            result = s;<br>
    }<br>
    return result;<br>
}<br>
</code></pre>

<pre><code>// Simple framework for timing concurrent execution<br>
public static long time(Executor executor, int concurrency,<br>
        final Runnable action) throws InterruptedException {<br>
    final CountDownLatch ready = new CountDownLatch(concurrency);<br>
    final CountDownLatch start = new CountDownLatch(1);<br>
    final CountDownLatch done = new CountDownLatch(concurrency);<br>
    for (int i = 0; i &lt; concurrency; i++) {<br>
        executor.execute(new Runnable() {<br>
            public void run() {<br>
                ready.countDown(); // Tell timer we're ready<br>
                try {<br>
                    start.await(); // Wait till peers are ready<br>
                    action.run();<br>
                } catch (InterruptedException e) {<br>
                    Thread.currentThread().interrupt();<br>
                } finally {<br>
                    done.countDown();  // Tell timer we're done<br>
                }<br>
            }<br>
        });<br>
    }<br>
    ready.await(); // Wait for all workers to be ready<br>
    long startNanos = System.nanoTime();<br>
    start.countDown(); // And they're off!<br>
    done.await(); // Wait for all workers to finish<br>
    return System.nanoTime() - startNanos;<br>
}<br>
</code></pre>

<pre><code>//Always use the wait loop idiom to invoke the wait method; never invoke it outside of a loop.<br>
// The standard idiom for using the wait method<br>
synchronized (obj) {<br>
    while (&lt;condition does not hold&gt;)<br>
        obj.wait(); // (Releases lock, and reacquires on wakeup)<br>
<br>
    ... // Perform action appropriate to condition<br>
}<br>
</code></pre>
<table><thead><th> <b>임의로 스레드가 깨어날 수 있는 이유</b> </th></thead><tbody>
<tr><td>  </td></tr>
<tr><td>대기하던 스레드가 잠자던 중에 다른 스레드가 보호되던 상태를 변경했을 가능성이 있을 경우</td></tr>
<tr><td>조건이 만족되지 않았을 때 다른 스레드가 실수나 악의로 notify를 호출했을 경우 - public object lock</td></tr>
<tr><td>대기중이 스레드를 깨우는 스레드가 notifyAll 을 호출하여 깨운 경우</td></tr>
<tr><td>대기중인 스레드가 저절로 깨어나는 경우 "비논리 기상"</td></tr></tbody></table>


<table><thead><th> <b>스레드 안전을 문서화 하자</b> </th></thead><tbody>
<tr><td>  </td></tr>
<tr><td> <b>불변</b> </td><td>안 바뀜. 동기화 필요 없음</td></tr>
<tr><td> <b>무조건적 스레드 안전</b> </td><td>외부 동기화 없이 사용할 만 하다.</td></tr>
<tr><td> <b>조건적 스레드 안전</b> </td><td>안전한 동시적 사용을 위해 일부 메소드의 외부 동기화가 필요하다. <br>(Collections.synchWrappedCollection)</td></tr>
<tr><td> <b>스레드 안전하지 않음</b> </td><td>동시적으로 사용하려면, 외부 동기화가 필요하다. 범용 Collection</td></tr>
<tr><td> <b>스레드 적대</b> </td><td>외부동기화를 하더라도 동시적 사용에 안전하지 않다.</td></tr></tbody></table>

<table><thead><th> <b>스레드 안전 속성을 명확하게 문서화 해야한다.</b> </th></thead><tbody>
<tr><td>  </td></tr>
<tr><td>synchronized는 javaDoc에 기록되지 않으므로 명시적으로 문서화가 필요하다.</td></tr>
<tr><td><b>조건적 스레드 안전</b>의 락이 필요한 경우와 어떤 락이 필요한지 기술해야한다.</td></tr>
<tr><td><b>무조건적 스레드 안전</b> 객체를 구현할 경우에 'private lock object' 이디엄을 참조하자.</td></tr></tbody></table>

<pre><code>// Collections.synchronizedMap 동기화<br>
Map&lt;K, V&gt; m = Collections.synchronizedMap(new HashMap&lt;K, V&gt;());<br>
    ...<br>
Set&lt;K&gt; s = m.keySet();  // Needn't be in synchronized block<br>
    ...<br>
synchronized(m) {  // Synchronizing on m, not s!<br>
    for (K key : s)<br>
    key.f();<br>
}<br>
</code></pre>
<pre><code>// Private lock object idiom - thwarts denial-of-service attack<br>
private final Object lock = new Object();<br>
<br>
public void foo() {<br>
    synchronized(lock) {<br>
        ...<br>
    }<br>
}<br>
</code></pre>

<table><thead><th> <b>Lazy Initialize</b> </th><th>필요하지 않으면 하지 말자</th></thead><tbody>
<pre><code>// Normal initialization of an instance field<br>
private final FieldType field = computeFieldValue();<br>
</code></pre>
<pre><code>// Lazy initialization of instance field - synchronized accessor<br>
private FieldType field;<br>
<br>
synchronized FieldType getField() {<br>
    if (field == null)<br>
        field = computeFieldValue();<br>
    return field;<br>
}<br>
</code></pre>
<pre><code>// Lazy initialization holder class idiom for static fields<br>
private static class FieldHolder {<br>
    static final FieldType field = computeFieldValue();<br>
}<br>
static FieldType getField() { return FieldHolder.field; }<br>
</code></pre>
<pre><code>// Double-check idiom for lazy initialization of instance fields<br>
private volatile FieldType field;<br>
FieldType getField() {<br>
    FieldType result = field;<br>
    if (result == null) {  // First check (no locking)<br>
        synchronized(this) {<br>
            result = field;<br>
            if (result == null)  // Second check (with locking)<br>
                field = result = computeFieldValue();<br>
            }<br>
        }<br>
    return result;<br>
}<br>
</code></pre>
<pre><code>// Single-check idiom - can cause repeated initialization!<br>
private volatile FieldType field;<br>
private FieldType getField() {<br>
    FieldType result = field;<br>
    if (result == null)<br>
        field = result = computeFieldValue();<br>
    return result;<br>
}<br>
</code></pre></tbody></table>

<pre><code>// Awful CountDownLatch implementation - busy-waits incessantly!<br>
public class SlowCountDownLatch {<br>
    private int count;<br>
    public SlowCountDownLatch(int count) {<br>
        if (count &lt; 0)<br>
            throw new IllegalArgumentException(count + " &lt; 0");<br>
        this.count = count;<br>
    }<br>
<br>
    public void await() {<br>
        while (true) {<br>
            synchronized(this) {<br>
                if (count == 0) return;<br>
            }<br>
        }<br>
    }<br>
<br>
    public synchronized void countDown() {<br>
        if (count != 0)<br>
            count--;<br>
    }<br>
}<br>
</code></pre>
<table><thead><th> <b>"분주-대기"상태에 스레드가 빠지면 안된다. - 무한 루프 체크</b> </th></thead><tbody>
<tr><td>  </td></tr>
<tr><td>(예제) while(true) synchronized 블록에 count==0 만 있음. <br><font color='white'>CPU 하나를 집어먹으며, 시스템 성능을 낮춘다</font>.</td></tr></tbody></table>

<table><thead><th> <b>Thread 조작</b> </th></thead><tbody>
<tr><td>  </td></tr>
<tr><td>Thread.yield 메소드로 '문제를 해결'하려고 하면 안되며,</td></tr>
<tr><td>성능평가도 할 수 없다. (JVM마다 yield 동작 다를 수 있으며...)</td></tr>
<tr><td>스레드 우선순위로 동작을 제어하는 것은 미친 짓이다.</td></tr>
<tr><td>동시적으로 실행 가능한 스레드 개수를 줄이기 위해 애플리케이션을 재구성하는 것이 더 좋은 조치이다.</td></tr>