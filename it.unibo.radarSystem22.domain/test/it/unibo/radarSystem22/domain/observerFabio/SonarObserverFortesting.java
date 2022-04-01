package it.unibo.radarSystem22.domain.observerFabio;

import static org.junit.Assert.assertTrue;

import java.util.Observable;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;



@SuppressWarnings("deprecation")
class SonarObserverFortesting implements IDistanceReceiver{
	private String name;
	private boolean oneShot = false;
	private int v0          = -1;
	private int delta       =  1;
	private ISonarObservable sonar;
	
	public SonarObserverFortesting(String name, ISonarObservable sonar, boolean oneShot) {
		this.name    = name;
		this.sonar   = sonar;
		this.oneShot = oneShot;
		this.sonar.attach(this);
	}
	
	@Override
	public void update(Observable source, Object data) {
		 ColorsOut.out( name + " | update data=" + data ); //+ " from " + source	
		 System.out.println( name + " | update data=" + data ); 
		 update( (IDistance)data );
	}

	
	@Override
	public void update(IDistance value) {
		int dist = value.getVal();
		if(oneShot) {
			 System.out.println( name + "| oneShot value=" + dist);  
			 assertTrue( dist==DomainSystemConfig.testingDistance);	
		 }else {
			 if( v0 == -1 ) {	//set the first value observed
				v0 = dist;
				System.out.println( name + "| v0=" + v0);
			 }else {
				System.out.println( name + "| value=" + value);  
 				int vexpectedMin = v0-delta;
				int vexpectedMax = v0+delta;
				assertTrue(  dist <= vexpectedMax && dist >= vexpectedMin );
				v0 = dist;
				//if( v0 == 30 && name.equals("observer1")) sonar.detach(this);
			 }
		 }
		
	}
	
}

