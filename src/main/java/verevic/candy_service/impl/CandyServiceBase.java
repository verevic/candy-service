package verevic.candy_service.impl;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import verevic.candy_service.ICandy;
import verevic.candy_service.ICandyEater;

/**
 * Сервис пожирания конфет, требует реализации
 */
public class CandyServiceBase {
	private final static Logger log = LoggerFactory.getLogger(CandyServiceBase.class);

	private final CandyStorage storage;

	private final ExecutorService executor;
	private final AtomicInteger threadCount = new AtomicInteger(0);

	/**
	 * Сервис получает при инициализации массив доступных пожирателей конфет
	 * 
	 * @param candyEaters
	 */
	private CandyServiceBase(CandyStorage storage, ICandyEater[] candyEaters) {
		this.storage = storage;
		executor = Executors.newFixedThreadPool(candyEaters.length, r ->
			new Thread(r, "CandyServiceWorker-" + threadCount.incrementAndGet()));

		for (ICandyEater eater : candyEaters) {
			executor.submit(() -> {
				ICandy candy = null;
				for (;;) {
					try {
						candy = storage.getNext(candy);
						try {
							if (candy != null)
								eater.eat(candy);
						} catch (Exception e) {
							log.error("Eater has chocked on candy " + Objects.toString(candy), e);
						}
					} catch (InterruptedException e) {
						return;
					}
				}
			});
		}
	}

//	@PostConstruct
//	public void start() {
//		log.info("Starting service");
//		log.info("Service successfully started");
//	}

	@PreDestroy
	public void stop() {
		log.info("Stopping service");
		executor.shutdown();
		try {
			if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
				log.info("Service successfully stopped");
			} else {
				log.warn("Service termination took too long");
			}
		} catch (InterruptedException ignored) {
			log.warn("Service termination has been interrupted");
		}
	}
	
	/**
	 * 
	 * Добавить конфету на съедение
	 * 
	 * @param candy
	 */
	public void addCandy(ICandy candy) {
		storage.add(candy);
	}

	@Component
	public static class Builder {
		private final CandyStorage storage;

		// Will have to choose which implementation we're using
		@Autowired
		public Builder(CandyStorage storage) {
			this.storage = storage;
		}

		public CandyServiceBase build(ICandyEater[] candyEaters) {
			return new CandyServiceBase(storage, candyEaters);
		}
	}
}
