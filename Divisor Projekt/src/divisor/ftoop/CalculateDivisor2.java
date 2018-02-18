package divisor.ftoop;

import java.awt.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Das folgende Programm soll aus einem vorgegebene Interval von Long-Zahlen die
 * Zahl zurückgeben, die die meisten Divisoren hat. Sollte es mehrere solche
 * Zahlen geben, so soll die kleinste dieser Zahlen ausgegeben werden.
 * 
 * Die Berechnung soll in n Threads stattfinden, die via Executor Framework
 * gesteuert werden, und sich das Problem aufteilen - jeder Thread soll eine
 * Teilmenge des Problems lösen. Verwenden Sie bitte einen FixedThreadPool und
 * implementieren Sie die Worker als Callable.
 * 
 * @author ble
 * 
 */
public class CalculateDivisor2 {

	long von, bis;
	int threadCount;

	/**
	 * @param von
	 *            untere Intervallgrenze
	 * @param bis
	 *            obere Intervallgrenze
	 * @param threadCount
	 *            Abzahl der Threads, auf die das Problem aufgeteilt werden soll
	 */
	public CalculateDivisor2(long von, long bis, int threadCount) {
		this.von = von;
		this.bis = bis;
		this.threadCount = threadCount;

	}

	/**
	 * Berechnungsroutine
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public String calculate() throws InterruptedException, ExecutionException {
    
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		
		ArrayList<Future<String>> list = new ArrayList<>();
		
		long intervallSize = (this.bis - this.von) / this.threadCount;
		long bis = this.von + intervallSize;
		
		for (int i = 0; i < this.threadCount; i++) {
			Callable<String> worker = new Calculation2(von,bis);
			Future<String> primBlock = executor.submit(worker);
			list.add(primBlock);
			von = bis + 1;
			bis = von + intervallSize;
		}	
		
		StringBuilder result = new StringBuilder();
		for (Future<String> future : list)
		{
			result.append(future.get());
		}
		executor.shutdown();
		
		
		
		return result.substring(0, result.length() -1);
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		if (args.length != 3) {
			System.out
					.println("Usage: CalculateDivisor <intervalStart> <intervalEnd> <threadCount>");
			System.exit(1);
		}
		long von = Long.parseLong(args[0]);
		long bis = Long.parseLong(args[1]);
		int threads = Integer.parseInt(args[2]);

		CalculateDivisor2 cp = new CalculateDivisor2(von, bis, threads);
		System.out.println("Ergebnis: Primzahlen von " + von + "bis" + bis +" !");
		System.out.println(cp.calculate());
	}

}

/**
 * Hält das Ergebnis einer Berechnung
 * 
 * @author bele
 * 
 * 
 */

class Calculation2 implements Callable<String> {
	
private long start;
private long ende;

Calculation2 (long start, long ende){
	this.start = start;
	this.ende = ende;
}

@Override
public String call() {
	
	StringBuilder primeBlock = new StringBuilder();
	
	for (long i = start; i < ende; i++) {
		if (isPrime(i)) {
			primeBlock.append(i).append(",");
		}
	}
	return primeBlock.toString();
}

synchronized private boolean isPrime(long n)
{
  //test, ob n durch 2 teilbar ist.
  if (n % 2 == 0)
  {
    return false;
  }

  //wenn nicht werden alle ungeraden Teiler ueberprueft.
  for (long i = 3; i * i <= n; i += 2)
  {
    if (n % i == 0)
    {
      return false;
    }
  }
  return true;
}
}
