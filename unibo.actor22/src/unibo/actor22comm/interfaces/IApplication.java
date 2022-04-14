package unibo.actor22comm.interfaces;

public interface IApplication {
	public void doJob( String domainConfig, String systemConfig, String commConfig );
	public String getName();
}
