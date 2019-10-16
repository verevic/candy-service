package verevic.candy_service.storage;

import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import verevic.candy_service.ICandy;
import verevic.candy_service.impl.CandyStorage;

/**
 * Хранилище для конфет, появляющихся в случайном поряке (врассыпную)
 * 
 * Keep all candies in the common queue.
 * 
 * @author verevic
 */
@Component
public class RandomCandyStorage implements CandyStorage {
	private final Lock lock = new ReentrantLock();
	private final Condition candyAdded = lock.newCondition();

	private final List<ICandy> candies = new LinkedList<>();

	// Could be changed to EnumSet if we have a enum
	// Would be nice to know how many types we have in advance
	private final BitSet typesInProgress = new BitSet();

	@Override
	public void add(ICandy candy) {
		lock.lock();
		try {
			candies.add(candy);
			candyAdded.signal();
		} finally {
			lock.unlock();
		}
	}

	private void setFlavour(ICandy candy, boolean set) {
		if (candy != null) {
			int flavour = candy.getCandyFlavour();
			typesInProgress.set(flavour, set);
		}		
	}

	private ICandy scanExisting() {
		for (Iterator<ICandy> it = candies.iterator(); it.hasNext(); ) {
			ICandy candy = it.next();
			int flavour = candy.getCandyFlavour();
			if (!typesInProgress.get(flavour)) {
				it.remove();
				return candy;
			}
		}

		return null;
	}

	@Override
	public ICandy getNext(ICandy prev) throws InterruptedException {
		lock.lock();
		try {
			setFlavour(prev, false);
			for (;;) {
				ICandy next = scanExisting();
				if (next != null) {
					setFlavour(next, true);
					return next;
				}
				candyAdded.await();
			}
		} finally {
			lock.unlock();
		}
	}
}
