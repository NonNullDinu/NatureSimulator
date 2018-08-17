package patch.nsUpdateScript;

import java.util.List;

public class Method {
	protected String name;
	protected List<Token> actions;

	public void execute(String[] args) {
		for (Token token : actions)
			if (token instanceof ExecutableToken) {
				((ExecutableToken) token).execute(args);
			}
	}
}