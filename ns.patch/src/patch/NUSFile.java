package patch;

import java.util.*;

@Deprecated
public class NUSFile {
	private static final Map<String, MethodToken> LANGUAGE_METHODS = new HashMap<>();
	private Map<String, MethodToken> script_methods;
	private String code;
	private static final char FORMAT_SPACE_OR_TAB_OR_NOTHING = '`';

	private static final Map<String, PatternToken> patterns = new HashMap<>();

	static {
		NUSFile fl = new NUSFile();
		LANGUAGE_METHODS.put("rm", fl.new MethodToken("rm", (Token[] a) -> {
			for (Token token : a) {
				System.out.println(((MethodArgToken) token).literal);
			}
			System.out.println("Test remove");
		}));
		// TODO Add language methods here
		PatternToken ifPattern = fl.new PatternToken("if [[ * ]]" + FORMAT_SPACE_OR_TAB_OR_NOTHING + ";" + FORMAT_SPACE_OR_TAB_OR_NOTHING);
		patterns.put("LANG_IF", ifPattern);
		patterns.put("LANG_METHOD_CALL", fl.new PatternToken("*_CALL *(*)"));
		// TODO Add language patterns here
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

	private void turnToTokens(String code) {
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
	}

	private abstract class Token {
	}

	private interface ExecutableToken {
		void execute(Token[] args);
	}

	private class MethodDeclarationToken extends Token {
		private final String name;

		MethodDeclarationToken(String declarationLine) {
			name = declarationLine.substring(7/* remove "method " */, declarationLine.length() - 2);
		}
	}

	private class MethodBodyToken extends Token implements ExecutableToken {
		private final List<Token> tokens;
		private String code;

		MethodBodyToken(String code) {
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
		private final String name;
		private MethodDeclarationToken declarationToken;
		private MethodBodyToken bodyToken;

		MethodToken(MethodDeclarationToken declarationToken, MethodBodyToken bodyToken) {
			this.declarationToken = declarationToken;
			this.bodyToken = bodyToken;
			this.name = declarationToken.name;
		}

		MethodToken(String name, Action action) {
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
		private final String literal;

		MethodArgToken(int id, String literal) {
			this.id = id;
			this.literal = literal;
		}
	}

	private class MethodCallToken extends Token implements ExecutableToken {
		private final MethodToken calledMethod;
		private final MethodArgToken[] args;

		MethodCallToken(MethodToken calledMethod, MethodArgToken[] args) {
			this.calledMethod = calledMethod;
			this.args = args;
		}

		@Override
		public void execute(Token[] args) {
			if (args != null) {
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
			} else calledMethod.execute(this.args);
		}
	}

	private class IfStatementToken extends Token implements ExecutableToken {
		private final Condition condition;
		private final ConditionResultToken condition_true_token;
		private final ConditionResultToken condition_false_token;

		IfStatementToken(ConditionToken conditionToken, ConditionResultToken condition_true_token, ConditionResultToken condition_false_token) {
			this.condition = conditionToken.turnToCondition();
			this.condition_false_token = condition_false_token;
			this.condition_true_token = condition_true_token;
		}

		@Override
		public void execute(Token[] args) {
			if (condition.check(args))
				condition_true_token.execute(args);
			else if (condition_false_token != null)
				condition_false_token.execute(args);
		}
	}

	private class ConditionToken extends Token {
		private final String literal;

		ConditionToken(String literal) {
			this.literal = literal;
		}

		Condition turnToCondition() {
			if (literal.contains("==")) {
				String[] literalParts = literal.split("==");
				String leftLiteral = literalParts[0];
				if (leftLiteral.contains(" "))
					leftLiteral = leftLiteral.replaceAll(" ", "");
				String rightLiteral = literalParts[1];
				if (rightLiteral.contains(" "))
					rightLiteral = rightLiteral.replaceAll(" ", "");
				final String fleftliteral = leftLiteral;
				final String frightliteral = rightLiteral;
				return (Token[] tokens) -> {
					String a = fleftliteral;
					String b = frightliteral;
					if (tokens != null)
						for (int i = tokens.length - 1; i >= 0; i--) {
							a = a.replaceAll("$" + i, ((MethodArgToken) tokens[i]).literal);
							b = b.replaceAll("$" + i, ((MethodArgToken) tokens[i]).literal);
						}
					return a.equals(b);
				};
			}
			return null;
		}
	}


	private class ConditionResultToken extends Token implements ExecutableToken {
		private final List<ExecutableToken> calls;

		ConditionResultToken(List<ExecutableToken> calls) {
			this.calls = calls;
		}

		@Override
		public void execute(Token[] args) {
			for (ExecutableToken call : calls)
				call.execute(args);
		}
	}

	private interface Condition {
		boolean check(Token[] args);
	}

	private int parseIndexIfStatementReached;

	private List<Token> turnToTokensMethodBody(String code) {
		List<Token> tokens = new ArrayList<>();
		String[] lines = code.split("\n");
		for (int i = 1; i < lines.length - 1; i++) {
			String line = lines[i];
			if (line.startsWith("\t"))
				line = line.replaceAll("\t", "");
			if (isMethodCall(line)) {
				tokens.add(getMethodCallToken(line));
			} else if (isIfStatement(line)) {
				tokens.add(getIfStatementToken(i, line, lines));
				i = parseIndexIfStatementReached;
			}
		}
		return tokens;
	}

	private IfStatementToken getIfStatementToken(int i, String line, String[] lines) {
		String condition = line.substring(6, line.lastIndexOf(']') - 2);
		ConditionToken conditionToken = new ConditionToken(condition);
		i++;
		line = lines[i];
		line = line.replaceAll("\t", "");
		if (!line.startsWith("then ")) throw new IfSyntaxError("Expected to find \"then\" on line:" + line);
		List<ExecutableToken> statements = new ArrayList<>();
		List<ExecutableToken> statementsFalse = null;
		for (; !lines[i].endsWith("fi"); i++) {
			line = lines[i];
			if (line.startsWith("\t"))
				line = line.replaceAll("\t", "");
			if (line.startsWith("then "))
				line = line.replaceFirst("then ", "");
			if (line.startsWith("else")) {
				statementsFalse = new ArrayList<>();
				line = line.replaceFirst("else ", "");
			}
			if (isMethodCall(line)) {
				Objects.requireNonNullElse(statementsFalse, statements).add(getMethodCallToken(line));
			} else if (isIfStatement(line)) {
				Objects.requireNonNullElse(statementsFalse, statements).add(getIfStatementToken(i, line, lines));
				i = parseIndexIfStatementReached;
			}
		}
		ConditionResultToken result_true = new ConditionResultToken(statements);
		ConditionResultToken result_false = (statementsFalse != null ? new ConditionResultToken(statementsFalse) : null);
		parseIndexIfStatementReached = i;
		return new IfStatementToken(conditionToken, result_true, result_false);
	}

	private class PatternToken {
		private final String format;

		PatternToken(String format) {
			this.format = format;
		}

		boolean isStringOfPattern(String string) {
			char[] str = string.toCharArray();
			char[] pattern = format.toCharArray();
			int add = 0;
			for (int i = 0; i < pattern.length; i++) {
				if (pattern[i] == '*') {
					if (i == pattern.length - 2)
						return str[str.length - 1] == pattern[i + 1];
					else if (i == pattern.length - 1)
						return true;
					else {
						if (pattern[i + 2] != '*' && pattern[i + 2] != FORMAT_SPACE_OR_TAB_OR_NOTHING) {
							for (; !(pattern[i + 1] == str[i + add] && pattern[i + 2] == str[i + add + 1]); add++) {
								if (i + add + 1 == str.length - 1) {/*Reached the end but still not found the next chars in pattern*/
									return false;
								}
							}
							add--;
						} else {
							for (; pattern[i + 1] != str[i + add]; add++) {
								if (i + add == str.length - 1) {/*Reached the end but still not found the next char in pattern*/
									return false;
								}
							}
							add--;
						}
					}
				} else if (pattern[i] == FORMAT_SPACE_OR_TAB_OR_NOTHING) {
					if (i + add == str.length) {
						return true;
					}
					if (!(str[i + add] == ' ' || str[i + add] == '\t')) {
						add--;
					}
				} else {
					if (pattern[i] != str[i + add]) {
						return false;
					}
				}
			}
			return true;
		}
	}

	private boolean isIfStatement(String line) {
		return patterns.get("LANG_IF").isStringOfPattern(line);
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

	private boolean isMethodCall(String code) {
		return patterns.get("LANG_METHOD_CALL").isStringOfPattern(code);
//		return code.startsWith("NUS_CALL ") || code.startsWith("SCR_CALL ");
	}

	public void executeScript() {
		script_methods.get("main").execute(null);
	}

	private interface Action {
		void execute(Token[] args);
	}

	private class MethodNotFoundException extends RuntimeException {
		MethodNotFoundException(String s) {
			super(s);
		}
	}

	private class WrongMethodTypeException extends RuntimeException {
		WrongMethodTypeException(String line) {
			super(line);
		}
	}

	private class IfSyntaxError extends RuntimeException {
		IfSyntaxError(String s) {
			super(s);
		}
	}
}