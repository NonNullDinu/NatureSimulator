package ns.parallelComputing;

import ns.openglObjects.Query;

public class CreateQueryRequest extends Request {

	private final Query query;

	public CreateQueryRequest(Query query) {
		this.query = query;
	}

	@Override
	public void execute() {
		query.create();
	}
}