package it.unibo.radarSystem22.domain.observerFabio;

import java.util.Observable;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

public class DistanceReceiver implements IDistanceReceiver {

	private String nome;
	public DistanceReceiver(String nome)
	{
		this.nome= nome;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		ColorsOut.out(nome+" distanza nuova "+((IDistance)arg).getVal(), ColorsOut.ANSI_PURPLE);

	}


	@Override
	public void update(IDistance value) {
		ColorsOut.out(nome+" distanza nuova "+value.getVal(), ColorsOut.ANSI_PURPLE);
		
	}

}
