package it.unibo.radarSystem22.domain.models;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.mock.*;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.domanin.concrete.LedConcrete;

public abstract class LedModel implements ILed {

	private boolean state=false;
	
	public static ILed create() {
		ILed led ; 
		if( DomainSystemConfig.simulation ) led = createLedMock();
		else led = createLedConcrete();
		return led;
	}
	
	public static ILed createLedMock() {
		if( DomainSystemConfig.ledGui ) return LedMockWithGui.createLed();
		else return new LedMock();
		
	}
	public static ILed createLedConcrete() {
		ColorsOut.out("createLedConcrete", ColorsOut.BLUE);
		return new LedConcrete();
	}
	
	
	protected abstract void ledActivate( boolean val);

	protected void setState( boolean val ) {
		this.state = val;
	    ledActivate( this.state );
	}
	
	@Override
	public void turnOn() {
		setState(true);

	}

	@Override
	public void turnOff() {
		setState(false);

	}

	@Override
	public boolean getState() {
		return(this.state);
	}

}
