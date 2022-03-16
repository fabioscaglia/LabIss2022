package it.unibo.radarSystem22.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.mock.LedMock;

public class TestLedMock {

	@Before
	public void up()
	{
		System.out.println("UP");
	}
	
	@After
	public void down()
	{
		System.out.println("DOWN");
	}
	
	
	@Test
	public void testLedMockOn()
	{
		System.out.println("Test");
		
		ILed led = new LedMock();
		assertTrue(led.getState()==false); //led spento
		
		led.turnOn();
		assertTrue(led.getState()==true); //led acceso
		
		
	}
	
	@Test
	public void testLedMockOff()
	{
		System.out.println("Test");
		
		ILed led = new LedMock();
		assertTrue(led.getState()==false); //led spento
		
		led.turnOn();
		assertTrue(led.getState()==true); //led acceso
		
		led.turnOff();
		assertTrue(led.getState()==false); //led spento
	}
	
	
	
}
