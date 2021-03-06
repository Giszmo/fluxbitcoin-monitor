package de.leowandersleb.bitcoinsw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tools {
	public static void getRatesAsync(ResultReceiver receiver) {
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		new GetKrakenRateTask(receiver).executeOnExecutor(threadPool);
		new GetBitstampRateTask(receiver).executeOnExecutor(threadPool);
		new GetBitTRexRateTask(receiver).executeOnExecutor(threadPool);
		new GetBitfinexRateTask(receiver).executeOnExecutor(threadPool);
	}
}
