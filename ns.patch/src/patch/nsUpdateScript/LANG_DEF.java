package patch.nsUpdateScript;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LANG_DEF {
	public static final char FORMAT_SPACE_OR_TAB_OR_NOTHING = '`';
	public static final Map<String, Method> NUS_METHODS = new HashMap<>();

	static final int METHOD_DECLARATION = 0;
	static final int METHOD_CALL = 1;
	protected static final int CONDITION_DECLARATION = 2;
	static final Pattern[] LANG_PATTERNS = {
			pattern("method *()"),
			pattern("*_CALL *(*)"),
			pattern("CONDITION(*)"),
	};

	static {
		NUS_METHODS.put("rm", new Method("rm", (String[] args) -> {
			for (String arg : args)
				System.out.println(arg);
		}));
		NUS_METHODS.put("runtime_cmd_exec", new Method("cmd_exec", (String[] args) -> {
			try {
				Runtime.getRuntime().exec(args);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));
	}

	private static Pattern pattern(String pattern) {
		return new Pattern(pattern);
	}

	public static MethodDeclarationToken breakToTokensMDT(String string) {
		String name = string.substring(8, string.indexOf('('));
		return new MethodDeclarationToken(name);
	}
}