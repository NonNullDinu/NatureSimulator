package patch.nsUpdateScript;

import java.util.List;

public class Method {
	private Code code;
	final String name;
	private List<Token> actions;

	public Method(String name, List<Token> actions) {
		this.name = name;
		this.actions = actions;
	}

	public Method(String name, Code code) {
		this.name = name;
		this.code = code;
	}

	public void execute(String[] args) {
		if (code != null)
			code.execute(args);
		else
			for (Token token : actions)
				if (token instanceof ExecutableToken) {
					((ExecutableToken) token).execute(args);
				}
	}
}