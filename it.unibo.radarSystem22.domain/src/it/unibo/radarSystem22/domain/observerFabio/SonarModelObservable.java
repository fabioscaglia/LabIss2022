package it.unibo.radarSystem22.domain.observerFabio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcrete;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarModelObservable implements ISonarObservable {

	protected  IDistance curVal = new Distance(90);
	protected boolean stopped   = true;
	protected List<IDistanceReceiver> targets = new ArrayList<>();
	
	 	
	public static ISonar create() {
		if( DomainSystemConfig.simulation )  return createSonarMock();
		else  return createSonarConcrete();	
	}

	public static ISonar createSonarMock() {
		ColorsOut.out("createSonarMock", ColorsOut.BLUE);
		return new SonarMock();
	}	
	public static ISonar createSonarConcrete() {
		ColorsOut.out("createSonarConcrete", ColorsOut.BLUE);
		return new SonarConcrete();
	}	
	
	protected SonarModelObservable() {//hidden costructor, to force setup
		ColorsOut.out("SonarObserver | calling (specialized) sonarSetUp ", ColorsOut.BLUE );
		sonarSetUp();   
	}
	
	protected void updateDistance( int d ) {
		//System.out.println("SonarModelObservable | "+d);
		if (d!=curVal.getVal())
		{
			System.out.println("SonarModelObservable | "+d);
			
			for(IDistanceReceiver dr : this.targets)
			{
				dr.update(new Distance(d));
			}
			
		}
		curVal = new Distance( d );
		ColorsOut.out("SonarModel | updateDistance "+ d, ColorsOut.BLUE);
	}	

	protected abstract void sonarSetUp() ;
 	protected abstract void sonarProduce() ;

	@Override
	public boolean isActive() {
		//ColorsOut.out("SonarModel | isActive "+ (! stopped), ColorsOut.GREEN);
		return ! stopped;
	}
	
	@Override
	public IDistance getDistance() {
		return curVal;
	}
	
	@Override
	public void activate() {
		curVal = new Distance( 90 );
 		ColorsOut.out("SonarModelObserver | activate" );
		stopped = false;
		//System.out.println("SonarModelObserver | Lista: "+targets.size());
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					//Colors.out("SonarModel | call produce", Colors.GREEN);
					sonarProduce(  );
					BasicUtils.delay(DomainSystemConfig.sonarDelay);
				}
				ColorsOut.out("SonarModelObserver | ENDS" );
		    }
		}.start();
	}
 	
	@Override
	public void deactivate() {
		ColorsOut.out("SonarModelObserver | deactivate", ColorsOut.BgYellow );
		stopped = true;
	}

	@Override
	public void attach(IDistanceReceiver distanceReceiver) {
		System.out.println("SonarModelObserver | aggancio un observer");
		
		this.targets.add(distanceReceiver);
	

	}

	@Override
	public void detach(IDistanceReceiver distanceReceiver) {
		
		this.targets.remove(distanceReceiver);
		
	}

}
