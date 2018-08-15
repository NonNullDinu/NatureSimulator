package patch;

import java.util.*;

public class NUSFile {
	private static final Map<String, MethodToken> LANGUAGE_METHODS = new HashMap<>();
	private Map<String, MethodToken> script_methods;
	private String code;

	static {
		NUSFile fl = new NUSFile();
		LANGUAGE_METHODS.put("rm", fl.new MethodToken("rm", (Token[] a) -> {
			for (Token token : a) {
				System.out.println(((MethodArgToken) token).literal);
			}
			System.out.println("Test remove");
		}));
	}

	public NUSFile(String code) {
		this.code = code;
		script_methods = new HashMap<>();
	}

	private NUSFile() {
	}

	public void splitCode() {
		turnToTokens(code);
	}

	private List<Token> turnToTokens(String code) {
		List<Token> tokens = new ArrayList<>();
		int indexOfCurrentMethodBegin = code.indexOf("method");
		int indexOfCurrentMethodEnd = code.indexOf("}");
		String currentMethodFull = code.substring(indexOfCurrentMethodBegin, indexOfCurrentMethodEnd + 1);
		String[] currentMethodLines = currentMethodFull.split("\n");
		MethodDeclarationToken declarationToken = new MethodDeclarationToken(currentMethodLines[0]);
		MethodBodyToken bodyToken = new MethodBodyToken(currentMethodFull.substring(currentMethodLines[0].length() + 1));
		MethodToken currentMethod = new MethodToken(declarationToken, bodyToken);
		tokens.add(currentMethod);
		script_methods.put(currentMethod.name, currentMethod);
		while (true) {
			indexOfCurrentMethodBegin = code.indexOf("method", indexOfCurrentMethodBegin + 1);
			if (indexOfCurrentMethodBegin == -1)
				break;
			indexOfCurrentMethodEnd = code.indexOf("}", indexOfCurrentMethodEnd + 1);
			currentMethodFull = code.substring(indexOfCurrentMethodBegin, indexOfCurrentMethodEnd + 1);
			currentMethodLines = currentMethodFull.split("\n");
			declarationToken = new MethodDeclarationToken(currentMethodLines[0]);
			bodyToken = new MethodBodyToken(currentMethodFull.substring(currentMethodLines[0].length() + 1));
			currentMethod = new MethodToken(declarationToken, bodyToken);
			tokens.add(currentMethod);
			script_methods.put(currentMethod.name, currentMethod);
		}
		return tokens;
	}

	private abstract class Token {
	}

	private interface ExecutableToken {
		void execute(Token[] args);
	}

	private class MethodDeclarationToken extends Token {
		private String name;

		public MethodDeclarationToken(String declarationLine) {
			name = declarationLine.substring(7/* remove "method " */, declarationLine.length() - 2);
		}
	}

	private class MethodBodyToken extends Token implements ExecutableToken {
		private List<Token> tokens;
		private String code;

		public MethodBodyToken(String code) {
			tokens = turnToTokensMethodBody(code);
			this.code = code;
		}

		public void execute(Token[] args) {
			for (Token token : tokens) {
				if (token instanceof ExecutableToken)
					((ExecutableToken) token).execute(args);
			}
		}
	}

	private class MethodToken extends Token implements ExecutableToken {
		private Action action;
		private String name;
		private MethodDeclarationToken declarationToken;
		private MethodBodyToken bodyToken;

		public MethodToken(MethodDeclarationToken declarationToken, MethodBodyToken bodyToken) {
			this.declarationToken = declarationToken;
			this.bodyToken = bodyToken;
			this.name = declarationToken.name;
		}

		public MethodToken(String name, Action action) {
			this.action = action;
			this.name = name;
		}

		public void execute(Token[] args) {
			if (action != null)
				action.execute(args);
			else
				bodyToken.execute(args);
		}
	}

	private class MethodArgToken extends Token {
		private int id;
		private String literal;

		public MethodArgToken(int id, String literal) {
			this.id = id;
			this.literal = literal;
		}
	}

	private class MethodCallToken extends Token implements ExecutableToken {
		private MethodToken calledMethod;
		private MethodArgToken[] args;

		public MethodCallToken(MethodToken calledMethod, MethodArgToken[] args) {
			this.calledMethod = calledMethod;
			this.args = args;
		}

		@Override
		public void execute(Token[] args) {
			Token[] arg = Arrays.copyOf(args, args.length);
			for (int i = 0; i < this.args.length; i++) {
				MethodArgToken mthdArg = this.args[i];
				if (mthdArg.literal.startsWith("$")) {
					arg[i] = args[Integer.parseInt(mthdArg.literal.substring(1))];
				} else {
					arg[i] = mthdArg;
				}
			}
			calledMethod.execute(arg);
		}
	}

	private class IfStatementToken extends Token implements ExecutableToken {
		private Condition condition;
		private ConditionResultToken condition_true_token;
		private ConditionResultToken condition_false_token;

		public IfStatementToken(ConditionToken conditionToken, ConditionResultToken condition_true_token, ConditionResultToken condition_false_token) {
			this.condition = conditionToken.turnToCondition();
			this.condition_false_token = condition_false_token;
			this.condition_true_token = condition_true_token;
		}

		@Override
		public void execute(Token[] args) {
			if (condition.check(args))
				condition_true_token.execute(args);
			else
				condition_false_token.execute(args);
		}
	}

	private class ConditionToken extends Token {
		private String literal;

		public ConditionToken(String literal) {
			this.literal = literal;
		}

		public Condition turnToCondition() {
			return null;
		}
	}


	private class ConditionResultToken extends Token implements ExecutableToken {
		private List<MethodCallToken> calls;

		public ConditionResultToken(List<MethodCallToken> calls) {
			this.calls = calls;
		}

		@Override
		public void execute(Token[] args) {
			for (MethodCallToken call : calls)
				call.execute(args);
		}
	}

	private interface Condition {
		boolean check(Token[] args);
	}

	private List<Token> turnToTokensMethodBody(String code) {
		List<Token> tokens = new ArrayList<>();
		String[] lines = code.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("\t"))
				line = line.replaceAll("\t", "");
			if (isMethodCall(line)) {
				tokens.add(getMethodCallToken(line));
			}
		}
		return tokens;
	}

	private MethodCallToken getMethodCallToken(String line) {
		String callType = line.substring(0, 8);
		String methodName = line.substring(9, line.indexOf('('));
		Map<String, MethodToken> map = (callType.equals("SCR_CALL") ? script_methods : (callType.equals("NUS_CALL") ? LANGUAGE_METHODS : null));
		if (map == null)
			throw new WrongMethodTypeException(line);
		MethodToken method = map.get(methodName);
		if (method == null)
			throw new MethodNotFoundException(methodName + " in " + line);
		String[] methodArgs = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')')).split(",");
		MethodArgToken[] argTokens = new MethodArgToken[methodArgs.length];
		for (int i = 0; i < methodArgs.length; i++) {
			String arg = methodArgs[i];
			while (arg.startsWith(" ") || arg.startsWith("\t"))
				arg = arg.substring(1);
			while (arg.endsWith(" ") || arg.endsWith("\t"))
				arg = arg.substring(0, arg.length() - 1);
			argTokens[i] = new MethodArgToken(i, arg);
		}
		return new MethodCallToken(method, argTokens);
	}

	public boolean isMethodCall(String code) {
		return code.startsWith("NUS_CALL ");
	}

	public void executeScript() {
		script_methods.get("main").execute(null);
	}

	private interface Action {
		void execute(Token[] args);
	}

	private class MethodNotFoundException extends RuntimeException {
		public MethodNotFoundException(String s) {
			super(s);
		}
	}

	private class WrongMethodTypeException extends RuntimeException {
		public WrongMethodTypeException(String line) {
			super(line);
		}
	}
}