package it.unibo.radarSystem22.domain.distanceObserver;

import java.util.Observer;

import it.unibo.radarSystem22.domain.interfaces.IDistance;

public interface IDistanceReceiver extends Observer {
	public void update( IDistance value );
}
