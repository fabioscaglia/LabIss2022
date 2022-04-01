package it.unibo.radarSystem22.domain.observerFabio;

import it.unibo.radarSystem22.domain.interfaces.ISonar;

public interface ISonarObservable extends ISonar {
	
	public void attach( IDistanceReceiver distanceReceiver );
	public void detach( IDistanceReceiver distanceReceiver );

}
