package verevic.candy_service.storage;

import org.springframework.stereotype.Component;

import verevic.candy_service.ICandy;
import verevic.candy_service.impl.CandyStorage;

/**
 * Create individual list of candies for each flavour.
 * Smth like Map<Integer, Collection<ICandy>>
 * 
 * Potential issue: we loose initial order of candies.
 * What we gain: don't need to scan a potentially lengthy queue of candies...
 * 
 * Additional benefit: if we don't care about order of candies with different flavours,
 * getNext could retrieve candy with the same flavour.
 * 
 * @author verevic
 *
 */
@Component
public class ByFlavourCandyStorage implements CandyStorage {

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
