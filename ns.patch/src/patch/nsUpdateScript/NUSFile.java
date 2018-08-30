package patch.nsUpdateScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated
public class NUSFile {
	private List<Token> tokens;
	private Map<String, Method> methods;

	public static void main(String[] args) {
		NUSFile fl = new NUSFile(
				"method main() {\n" +
						"\tNUS_CALL rm([a, b, c])\n" +
						"}");
		fl.callMain();
	}

	public NUSFile(String code) {
		turnToTokens(code);
	}

	private void turnToTokens(String code) {
		String codeToParseLeft = code;
		while (!codeToParseLeft.startsWith("method"))
			codeToParseLeft = codeToParseLeft.substring(1); // Skip to first method declaration
		tokens = getTokens(codeToParseLeft);
	}

	private int idx = 0;
	private int removed = 0;

	private List<Token> getTokens(String codeToParseLeft) {
		List<Token> tokens = new ArrayList<>();
		String code = codeToParseLeft;
		while (!code.isEmpty()) {
			tokens.add(getNextToken(code));
			code = code.substring(idx + removed);
		}
		return tokens;
	}

	private Token getNextToken(String code) {
		idx = removed = 0;
		while (code.startsWith(" ") || code.startsWith("\t") || code.startsWith("\n")) {
			code = code.substring(1);
			removed++;
		}
		for (int i = 0; i < LANG_DEF.LANG_PATTERNS.length; i++) {
			if (LANG_DEF.LANG_PATTERNS[i].isStringOfPattern(code)) {
				switch (i) {
					case LANG_DEF.METHOD_DECLARATION:
						return new MethodDeclarationToken(code.substring(code.indexOf(' ') + 1, (idx = (code.indexOf('(') + 2)) - 2));
					case LANG_DEF.METHOD_CALL:
						String argumentsString = code.substring(code.indexOf("([") + 2, (idx = code.indexOf("])") + 2) - 2);
						String[] args = argumentsString.split(",");
						MethodCallArgumentToken[] tokens = new MethodCallArgumentToken[args.length];
						for (int j = 0; j < args.length; j++) {
							while (args[j].startsWith(" ") || args[j].startsWith("\t"))
								args[j] = args[j].substring(1);
							while (args[j].endsWith(" ") || args[j].endsWith("\t"))
								args[j] = args[j].substring(0, args[j].length() - 1);
							tokens[j] = new MethodCallArgumentToken(args[j]);
						}
						String type = code.substring(0, 8);
						Method methodToCall = null;
						String methodName = code.substring(code.indexOf(' ') + 1, code.indexOf("(["));
						if (type.equals("NUS_CALL")) {
							methodToCall = LANG_DEF.NUS_METHODS.get(methodName);
						} else if (type.equals("SCR_CALL")) {
							methodToCall = methods.get(methodName);
						}
						return new MethodCallToken(methodToCall, new MethodCallArgumentsToken(tokens));
				}
			}
		}
		return null;
	}

	public void callMain() {
		for (Method m : methods.values()) {
			if (m.name.equals("main")) {
				m.execute(null);
				break;
			}
		}
	}
}