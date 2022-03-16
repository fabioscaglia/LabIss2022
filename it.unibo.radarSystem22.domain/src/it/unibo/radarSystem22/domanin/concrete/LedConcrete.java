package it.unibo.radarSystem22.domanin.concrete;

import java.io.IOException;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.models.LedModel;

public class LedConcrete extends LedModel implements ILed {

	private Runtime rt  = Runtime.getRuntime(); //per accedere al sistema operativo sottostante
	
	@Override
	protected void ledActivate(boolean val) 
	{
		try
		{
			if( val ) 
			{
				rt.exec( "sudo bash led25GpioTurnOn.sh" );
			}
			else
			{
				rt.exec( "sudo bash led25GpioTurnOff.sh" );
			}
		} 
		catch (IOException e) 
		{
			System.out.println("LedConcrete | ERROR " +  e.getMessage());
		}
	}
}
