package unibo.actor22.main;

import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.common.ApplData;
import unibo.actor22.common.RadarSystemConfig;
import unibo.actor22.distrib.annot.UsingActorsWithAnnotOnPc;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.interfaces.IApplication;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@ActorLocal(name={ApplData.controllerName, ApplData.sonarName, ApplData.ledName}, 
implement= {unibo.actor22.common.ControllerActor.class,
		unibo.actor22.common.SonarActor.class,
		unibo.actor22.common.LedActor.class})

public class ControllerDevsLocals implements IApplication{
	
	public static final IApplMessage turnOnLed    = Qak22Util.buildDispatch("main", "cmd", ApplData.comdLedon,   ApplData.ledName);
	public static final IApplMessage turnOffLed   = Qak22Util.buildDispatch("main", "cmd", ApplData.comdLedoff,  ApplData.ledName);
	private EnablerContextForActors ctx;
	public void setup( String domainConfig, String systemConfig, String commConfig )  {
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
	
	
	
	protected void configure() {
		
//		new EventObserver(ApplData.observerName);
//		Qak22Context.registerAsEventObserver(ApplData.observerName, ApplData.evEndWork);
		ctx = new EnablerContextForActors( "ctx",ApplData.ctxPort,ApplData.protocol);
		Qak22Context.handleLocalActorDecl(this);
// 		Qak22Context.handleRemoteActorDecl(this);
 	}
	
	protected void execute() {
		ctx.activate();
		ColorsOut.outappl("ControllerDevsOnPc | execute", ColorsOut.MAGENTA);
		Qak22Util.sendAMsg(ApplData.activateCrtl);
//		Qak22Util.sendAMsg(turnOnLed);
		System.out.println("MAIN | Messaggio inviato");
	} 
	
	public void terminate() {
		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		//In locale
//		new ControllerDevsLocals().doJob(null, null, null);
		//Sul rasp
		new ControllerDevsLocals().doJob("DomainSystemConfig.json", "RadarSystemConfig.json", "CommSystemConfig.json");
//		new ControllerDevsLocals().doJob(null, "RadarSystemConfig.json", "CommSystemConfig.json");
		CommUtils.aboutThreads("Before end - ");
	}


	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void doJob(String domainConfig, String systemConfig, String commConfig) {
		ColorsOut.outappl("ControllerDevsOnPc | Start", ColorsOut.BLUE);
		ColorsOut.outappl("ControllerDevsOnPc | domain="+domainConfig+" system="+systemConfig+" comm="+ commConfig, ColorsOut.BLUE);
		setup(domainConfig, systemConfig, commConfig);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		//terminate();
	}


}
