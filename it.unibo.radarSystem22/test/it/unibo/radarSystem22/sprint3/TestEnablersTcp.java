package it.unibo.radarSystem22.sprint3;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.enablers.EnablerAsServer;
import it.unibo.comm2022.utils.BasicUtils;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.RadarSystemConfig;
import it.unibo.radarSystem22.sprint2a.proxy.LedProxyAsClient;
import it.unibo.radarSystem22.sprint2a.proxy.SonarProxyAsClient;
import it.unibo.radarSystem22.sprint3.handlers.LedApplHandler;
import it.unibo.radarSystem22.sprint3.handlers.SonarApplHandler;

public class TestEnablersTcp {

	private ISonar sonar;
	private ILed led;
	private ISonar sonarProxy;
	private ILed ledProxy;
	private EnablerAsServer sonarServer;
	private EnablerAsServer ledServer;
	private ProtocolType protocol;
	
	
	@Before
	public void setup() {
		DomainSystemConfig.simulation=true;
		DomainSystemConfig.ledGui=true;
		DomainSystemConfig.DLIMIT=70;
		DomainSystemConfig.sonarDelay=100;
		RadarSystemConfig.DLIMIT=70;
		RadarSystemConfig.ledPort=8081;
		RadarSystemConfig.sonarPort=8082;
		this.protocol=ProtocolType.tcp;
		
		this.sonar = DeviceFactory.createSonar();
		this.led = DeviceFactory.createLed();
		
		this.sonarServer = new EnablerAsServer("sonarServer", RadarSystemConfig.sonarPort, this.protocol,
				new SonarApplHandler("sonarHandler", this.sonar));
		this.ledServer = new EnablerAsServer("ledServer", RadarSystemConfig.ledPort, this.protocol,
				new LedApplHandler("ledHandler", this.led));
		
		this.sonarProxy = new SonarProxyAsClient("sonarProxy", "localhost", ""+RadarSystemConfig.sonarPort, this.protocol);
		
		this.ledProxy = new LedProxyAsClient("ledProxy", "localhost", ""+RadarSystemConfig.ledPort, this.protocol);
	}
	
	@After
	public void down() {
		this.ledServer.stop();
		this.sonarServer.stop();
	}
	
	@Test
	public void test() {
		this.sonarServer.start();
		this.ledServer.start();
		
		this.sonarProxy.activate();
		
		for(int i=0; i<30; i++)
		{
			int distance = this.sonarProxy.getDistance().getVal();
			ColorsOut.out("i=" + i +" distance: "+distance);
			BasicUtils.delay(DomainSystemConfig.sonarDelay);
			if(distance<RadarSystemConfig.DLIMIT)
			{
				this.ledProxy.turnOn();
				boolean ledState = this.ledProxy.getState();
				assertTrue(ledState);
			}
			else
			{
				this.ledProxy.turnOff();
				boolean ledState = this.ledProxy.getState();
				assertFalse(ledState);
			}
		}
		
		
	}

}
