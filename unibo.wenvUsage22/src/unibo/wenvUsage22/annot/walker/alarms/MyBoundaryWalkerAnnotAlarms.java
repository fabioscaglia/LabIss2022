package unibo.wenvUsage22.annot.walker.alarms;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.annot.walker.GuardContinueWork;
import unibo.wenvUsage22.annot.walker.GuardEndOfWork;
import unibo.wenvUsage22.annot.walker.WsConnWEnvObserver;
import unibo.wenvUsage22.common.ApplData;
import unibo.wenvUsage22.common.VRobotMoves;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
public class MyBoundaryWalkerAnnotAlarms extends QakActor22FsmAnnot{

	private Interaction2021 conn;	
 	private int ncorner  = 0;
	
	public MyBoundaryWalkerAnnotAlarms(String name) {
		super(name);
		Qak22Context.registerAsEventObserver(ApplData.robotName,SystemData.fireEventId);
		Qak22Context.registerAsEventObserver(ApplData.robotName,SystemData.endAlarmId);
	}
	
	@State( name = "robotStart", initial=true)
	@Transition( state="robotMoving", msgId=SystemData.endMoveOkId)
	@Transition( state="wallDetected", msgId=SystemData.endMoveKoId)
	@Transition( state="alarm", msgId=SystemData.fireEventId)
	protected void robotStart(IApplMessage msg) {
		outInfo(""+msg+" connecting... ");
		conn = WsConnection.create("localhost:8091");
		outInfo("connected "+conn);	
   		((WsConnection)conn).addObserver( new WsConnWEnvObserver(getName()) );
  		VRobotMoves.step(getName(),conn);
	}
	
	@State(name="robotMoving")
	@Transition( state="robotMoving", msgId=SystemData.endMoveOkId)
	@Transition( state="wallDetected", msgId=SystemData.endMoveKoId)
	@Transition( state="alarm", msgId=SystemData.fireEventId)
	protected void robotMoving(IApplMessage msg) {
//		outInfo(""+msg);
		VRobotMoves.step(getName(), conn);
	}
	
	@State(name="wallDetected")
	@Transition( state="robotMoving", msgId=SystemData.endMoveOkId, guard=GuardContinueWork.class)
	@Transition( state="alarm", msgId=SystemData.fireEventId)
	@Transition( state="endState", msgId=SystemData.endMoveOkId, guard=GuardEndOfWork.class)
	protected void wallDetected(IApplMessage msg) {
		ncorner++;
		outInfo("ncorner="+ncorner+" "+msg);
		VRobotMoves.turnLeft(getName(), conn);
		GuardContinueWork.setValue(ncorner);
		GuardEndOfWork.setValue(ncorner);
		
	}
	
	@State(name="alarm")
	@Transition( state="endAlarm", msgId=SystemData.endAlarmId)
	protected void alarm(IApplMessage msg) {
		outInfo("<---ALARM----> "+msg);
//		ColorsOut.outappl("ALARM", ColorsOut.ANSI_RED_BACKGROUND);
	}
	
	@State(name="endAlarm")
	@Transition(state="robotMoving", msgId=SystemData.endMoveOkId)
	@Transition(state="wallDetected", msgId=SystemData.endMoveKoId)
	protected void endAlarm(IApplMessage msg)
	{
		outInfo("<---FINE ALLARME --->"+msg);
//		if(msg.msgContent().equals("turnLeft"))
//		{
//			ncorner++;
//		}
//		VRobotMoves.step(getName(), conn);
	}
	
	@State(name="endState")
	protected void endState(IApplMessage msg) {
		outInfo(""+msg);
		System.out.println("FINE");
		System.exit(0);
	}
	

}
