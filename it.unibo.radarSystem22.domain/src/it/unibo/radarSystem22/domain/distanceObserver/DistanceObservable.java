package it.unibo.radarSystem22.domain.distanceObserver;

import java.util.ArrayList;
import java.util.List;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.distanceObserver.IDistanceReceiver;

public class DistanceObservable implements IDistanceObservable {

	private IDistance distance;
	public static final int delta = 3;
	protected List<IDistanceReceiver> targets = new ArrayList<>();
	
	@Override
	public void setVal(IDistance distance) {
		if(Math.abs(distance.getVal()-this.distance.getVal())>delta)
		{
			System.out.println("DistanceObservable | "+distance.getVal());
			for(IDistanceReceiver dr : this.targets)
			{
				dr.update(distance);
			}
		}

	}

	@Override
	public void attach(IDistanceReceiver distanceReceiver) {
		System.out.println("DistanceObservable | aggancio un observer");
		this.targets.add(distanceReceiver);

	}

	@Override
	public void detach(IDistanceReceiver distanceReceiver) {
		this.targets.remove(distanceReceiver);
	}

}
