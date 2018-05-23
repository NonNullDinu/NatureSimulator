package ns.parallelComputing;

public abstract class Request {
	protected String request;
	protected Object[] args;

	public Request(String request, Object[] args) {
		this.request = request;
	}
	
	public abstract void execute();
}