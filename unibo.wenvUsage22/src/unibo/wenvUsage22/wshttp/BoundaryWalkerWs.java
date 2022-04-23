package unibo.wenvUsage22.wshttp;

import java.util.Observable;

import org.json.JSONObject;

import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.common.ApplData;

public class BoundaryWalkerWs implements IObserver{

	private Interaction2021 conn;
	private boolean obstacle = false;
	private int counter;
	
	@Override
	public void update(Observable o, Object data) {
		ColorsOut.out("ClientUsingWs update/2 receives:" + data);
		JSONObject d = new JSONObject(""+data);
		if(d.has("collision") /*&& d.getBoolean("collision")==true*/)
		{
			this.obstacle=true;
			this.counter++;
			ColorsOut.outappl("BoundaryWalkWs update/2 collision=" + d.getBoolean("collision"), ColorsOut.MAGENTA);
		}
		try {
			this.doJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BoundaryWalkerWs() {
		conn = WsConnection.create("localhost:8091" );
		((WsConnection)conn).addObserver(this);
		this.counter=0;
	}
	
	@Override
	public void update(String value) {
		ColorsOut.out("ClientUsingWs update/1 receives:" + value);
		if(value.contains("collision"))
		{
			this.obstacle=true;
			this.counter++;
			ColorsOut.outappl("BoundaryWalkWs update/1 collision relevated", ColorsOut.MAGENTA);
		}
		try {
			this.doJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception   {
		CommUtils.aboutThreads("Before start - ");
 		new BoundaryWalkerHttp().doJob();
		CommUtils.aboutThreads("At end - ");
	}

	protected void doJob() throws Exception {
		if(counter<4)
		{
			if(!obstacle)
			{
				conn.forward( ApplData.moveForward(500) ); 
			}
			else
			{
				obstacle = false;
		  		conn.forward( ApplData.turnLeft(300) );
			}
			
		}
		else
		{
			conn.close();
		}
	}
	
}
