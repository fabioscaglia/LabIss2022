package unibo.actor22.common;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

public class RadarDisplayActor extends QakActor22 {

	private IRadarDisplay radarDisplay;
	
	public RadarDisplayActor(String name) {
		super(name);
		this.radarDisplay = DeviceFactory.createRadarGui();
	}
	
	

	@Override
	protected void handleMsg(IApplMessage msg) {
		CommUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.out( getName()  + " | doJob " + msg, ColorsOut.CYAN);
		if( msg.isRequest() ) elabRequest(msg);
		else elabCmd(msg);
	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
 		switch( msgCmd ) {
			case ApplData.cmdUpdate  : {
					//TODO
					//Sta roba può andare?????
					String payload = msg.msgContent();
					Struct payloadAsTerm = (Struct) Term.createTerm(payload);
					radarDisplay.update(payloadAsTerm.getArg(0).toString(), payloadAsTerm.getArg(1).toString());
				break;
			}
			default: ColorsOut.outerr(getName()  + " | unknown " + msgCmd);
		}
	}
 
	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		//ColorsOut.out( getName()  + " | elabRequest " + msgCmd, ColorsOut.CYAN);
		switch( msgReq ) {
			case ApplData.reqGetCurDistance  :{
				int curDistance = this.radarDisplay.getCurDistance();
				IApplMessage reply = MsgUtil.buildReply(getName(), ApplData.reqGetCurDistance, ""+curDistance, msg.msgSender());
				ColorsOut.out( getName()  + " | reply= " + reply, ColorsOut.CYAN);
 				sendReply(msg, reply );				
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgReq);
		}
	}

}
