package it.unibo.comm2022.enablers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.common.NaiveApplHandler;
import it.unibo.comm2022.proxy.ProxyAsClient;

public class TestEnablersFabio {

	private EnablerAsServer enabler;
	private int port = 8090;
	private ProtocolType protocol = ProtocolType.tcp;
	private ProxyAsClient proxyClient;
	
	@Before
	public void setup() {
		this.enabler = new EnablerAsServer("EnablerProva", this.port,this.protocol, new NaiveApplHandler("naiveH") );
		this.proxyClient = new ProxyAsClient("proxyCl", "localhost", ""+this.port, this.protocol);
	}
	
	@After
	public void down() {
		this.enabler.stop();
	}
	
	@Test
	public void test() {
		this.enabler.start();
		String richiesta = "richiesta da client";
		String risposta = this.proxyClient.sendRequestOnConnection(richiesta);
		assertEquals(risposta, "answerTo_"+richiesta);
	}

}
