package verevic.candy_service.storage;

import org.springframework.stereotype.Component;

import verevic.candy_service.ICandy;
import verevic.candy_service.impl.CandyStorage;

/**
 * Implementation for candies which tend to go "boxed" - a bunch of candies of the same flavour at once
 * Sore them as a list of:
 * - flavour
 * - list of candies with the flavour
 * 
 * @author verevic
 *
 */
@Component
public class BoxedCandyStorage implements CandyStorage {

	@Override
	public void add(ICandy candy) {
		// TODO Auto-generated method stub

	}

	@Override
	public ICandy getNext(ICandy prev) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

}
