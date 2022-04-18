package unibo.actor22.RSActor22Distrib;

import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.*;
import unibo.actor22.common.ApplData;
import unibo.actor22.common.RadarSystemConfig;
import unibo.actor22.main.ControllerDevsLocals;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.interfaces.IApplication;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@ActorLocal(name={ApplData.controllerName}, 
			implement= {unibo.actor22.common.ControllerActor.class})
@ActorRemote(name= {ApplData.ledName, ApplData.sonarName},
			host=    {ApplData.raspAddr,ApplData.raspAddr}, 
			port=    { ""+ApplData.ctxPort, ""+ApplData.ctxPort}, 
			protocol={ "TCP" , "TCP" })
public class ControllerRadarOnPC implements IApplication{

	public static void main(String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new ControllerRadarOnPC().doJob("DomainSystemConfig.json", "RadarSystemConfig.json", "CommSystemConfig.json");
		CommUtils.aboutThreads("Before end - ");

	}
	
	@Override
	public void doJob(String domainConfig, String systemConfig, String commConfig) {
		ColorsOut.outappl("ControllerDevsOnPc | Start", ColorsOut.BLUE);
		setup(domainConfig, systemConfig, commConfig);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		//terminate();
		
	}


	private void setup(String domainConfig, String systemConfig, String commConfig) {
		BasicUtils.aboutThreads(getName() + " | Before setup ");
		if( domainConfig != null ) {
			DomainSystemConfig.setTheConfiguration(domainConfig);
			System.out.println("LED GUI: "+DomainSystemConfig.ledGui);
		}
		else
		{
			DomainSystemConfig.tracing      = false;	
			DomainSystemConfig.simulation	= false;
			DomainSystemConfig.testing		=false;
			DomainSystemConfig.ledGui		= false;
		}
		if( systemConfig != null ) {
			//System.out.println("SONO QUI");
			//RadarSystemConfig.setTheConfiguration(systemConfig);
		}
		else {
			RadarSystemConfig.testing		=false;
			RadarSystemConfig.sonarObservable=false;
		}
		if (commConfig != null)
		{
			CommSystemConfig.setTheConfiguration(commConfig);
		}
		else
		{
			CommSystemConfig.protcolType    = ProtocolType.tcp;
			CommSystemConfig.tracing        = true;
		}
		if( domainConfig == null && systemConfig == null && commConfig==null) {
			DomainSystemConfig.tracing      = false;	
			DomainSystemConfig.simulation	= true;
			DomainSystemConfig.testing		=false;
			DomainSystemConfig.ledGui		= true;
	 		CommSystemConfig.protcolType    = ProtocolType.tcp;
			CommSystemConfig.tracing        = true;
			ProtocolType protocol 		    = CommSystemConfig.protcolType;
			RadarSystemConfig.testing		=false;
			//Per il rasp
//			DomainSystemConfig.DLIMIT=12;
//			RadarSystemConfig.DLIMIT=12;			
			//DomainSystemConfig.sonarDelay = 2;
			RadarSystemConfig.sonarObservable=false;
		}
	}
	
	private void configure() {
		Qak22Context.handleLocalActorDecl(this);
		Qak22Context.handleRemoteActorDecl(this);
		if(RadarSystemConfig.sonarObservable)
		{
			//Sto lavorando con il sonar observable. L'osservatore dei valori prodotti diventa il controller
			Qak22Context.registerAsEventObserver(ApplData.controllerName, ApplData.evDistance);
		}
		
	}



	private void execute() {
		ColorsOut.outappl("ControllerDevsOnPc | execute", ColorsOut.MAGENTA);
		Qak22Util.sendAMsg(ApplData.activateCrtl);
		System.out.println("MAIN | Messaggio inviato");
		
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}
}
