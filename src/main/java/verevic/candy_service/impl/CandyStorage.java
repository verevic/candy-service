package verevic.candy_service.impl;

import verevic.candy_service.ICandy;

/**
 * A storage for candies to-be-eaten
 * 
 * @author verevic
 */
public interface CandyStorage {
	void add(ICandy candy);
	ICandy getNext(ICandy prev) throws InterruptedException;
}
