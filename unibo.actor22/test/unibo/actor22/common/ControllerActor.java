package unibo.actor22.common;

import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

public class ControllerActor extends QakActor22 {

	private int numIter;
	private IRadarDisplay radar;
	
	public ControllerActor(String name) {
		super(name);
		this.radar = DeviceFactory.createRadarGui();
		this.numIter=0;
	}

	@Override
	protected void handleMsg(IApplMessage msg) {
//		System.out.println("Controller: "+msg.toString());
		if( msg.isEvent() )        elabEvent(msg);
		else if( msg.isDispatch()) elabCmd(msg) ;	
		else if( msg.isReply() )   elabReply(msg) ;	
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
//		System.out.println(getName()  + " | elabCmd=" + msgCmd + " obs=" + RadarSystemConfig.sonarObservable);
		ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd + " obs=" + RadarSystemConfig.sonarObservable, ColorsOut.BLUE);
		if( msgCmd.equals(ApplData.cmdActivate)  ) {
 				sendMsg( ApplData.activateSonar);
// 				System.out.println("CONTROLLER:-> attivo il sonar");
				doControllerWork();
 		}		
	}
	
	protected void elabReply(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabReply=" + msg, ColorsOut.GREEN);
		//if( msg.msgId().equals(ApplData.reqDistance ))
		String dStr = msg.msgContent();
		int d = Integer.parseInt(dStr);
		//Radar
		radar.update(dStr, "60");
		//LedUse case
		if( d <  RadarSystemConfig.DLIMIT ) {
			forward(ApplData.turnOnLed); 
			//forward(ApplData.deactivateSonar);
		}
		else {
			forward(ApplData.turnOffLed); 
		}
		doControllerWork();
	}
	

	protected void elabEvent(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabEvent=" + msg, ColorsOut.GREEN);
		if( msg.isEvent()  ) {  //defensive
			String dstr = msg.msgContent();
			int d       = Integer.parseInt(dstr);
			radar.update(dstr, "60");
			if( d <  DomainSystemConfig.DLIMIT ) {
				forward(ApplData.turnOnLed); 		
				//forward(ApplData.deactivateSonar);
			}
			else {
				forward(ApplData.turnOffLed); 
				System.out.println(getName()+" |shutting down led");
			}
		}
	}
	
	private void terminate()
	{
		forward(ApplData.turnOffLed);
		forward(ApplData.deactivateSonar);
	}

    protected void doControllerWork() {
    	if(numIter<30)
    	{
    		CommUtils.aboutThreads(getName()  + " |  Before doControllerWork " + RadarSystemConfig.sonarObservable );
        	if( ! RadarSystemConfig.sonarObservable) {
        		CommUtils.delay(300);
        		request( ApplData.askDistance );
        	}
        	this.numIter++;
    	}
    	else{
    		this.terminate();
    	}
	}
	
}
