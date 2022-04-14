package unibo.actor22.main;

import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.common.ApplData;
import unibo.actor22.common.RadarSystemConfig;
import unibo.actor22.distrib.annot.UsingActorsWithAnnotOnPc;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.interfaces.IApplication;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@ActorLocal(name={ApplData.controllerName, ApplData.sonarName, ApplData.ledName}, 
implement= {unibo.actor22.common.ControllerActor.class,
		unibo.actor22.common.SonarActor.class,
		unibo.actor22.common.LedActor.class})

public class ControllerDevsLocals implements IApplication{
	
	public void setup( String domainConfig, String systemConfig, String commConfig )  {
	    BasicUtils.aboutThreads(getName() + " | Before setup ");
		if( domainConfig != null ) {
			DomainSystemConfig.setTheConfiguration(domainConfig);
		}
		if( systemConfig != null ) {
			RadarSystemConfig.setTheConfiguration(systemConfig);
		}
		if (commConfig != null)
		{
			CommSystemConfig.setTheConfiguration(commConfig);
		}
		if( domainConfig == null && systemConfig == null) {
			DomainSystemConfig.tracing      = false;	
			DomainSystemConfig.simulation	= false;
			DomainSystemConfig.testing		=false;
			DomainSystemConfig.ledGui		= false;
	 		CommSystemConfig.protcolType    = ProtocolType.tcp;
			CommSystemConfig.tracing        = true;
			ProtocolType protocol 		    = CommSystemConfig.protcolType;
			RadarSystemConfig.testing		=false;
			//Per il rasp
			DomainSystemConfig.DLIMIT=12;
			RadarSystemConfig.DLIMIT=12;
			
			//DomainSystemConfig.sonarDelay = 2;
			
			RadarSystemConfig.sonarObservable=false;
		}
	}
	
	
	
	protected void configure() {
		
//		new EventObserver(ApplData.observerName);
//		Qak22Context.registerAsEventObserver(ApplData.observerName, ApplData.evEndWork);
		
		Qak22Context.handleLocalActorDecl(this);
// 		Qak22Context.handleRemoteActorDecl(this);
 	}
	
	protected void execute() {
		ColorsOut.outappl("ControllerDevsOnPc | execute", ColorsOut.MAGENTA);
		Qak22Util.sendAMsg(ApplData.activateCrtl);
		System.out.println("MAIN | Messaggio inviato");
	} 
	
	public void terminate() {
		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		//In locale
		new ControllerDevsLocals().doJob(null, null, null);
		//Sul rasp
//		new ControllerDevsLocals().doJob("DomainSystemConfig.json", "RadarSystemConfig.json", "CommSystemConfig.json");
		CommUtils.aboutThreads("Before end - ");
	}


	@Override
	public String getName() {
		return this.getClass().getName();
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


}
