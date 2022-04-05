package it.unibo.radarSystem22.domain.distanceObserver;

import it.unibo.radarSystem22.domain.interfaces.IDistance;

public interface IDistanceObservable {
	public void setVal( IDistance distance);
	public void attach( IDistanceReceiver distanceReceiver );
	public void detach( IDistanceReceiver distanceReceiver );
}

