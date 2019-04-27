/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package run;

import lang.METHOD;
import lang.exceptions.ParsingError;
import lang.exceptions.TokenException;
import statements.*;
import tokens.*;
import tree.Statements;
import variables.DATA_TYPE;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ScriptInterpreter {
	public static void main(String[] args) {
		String src;
		try (FileInputStream fin = new FileInputStream(args.length == 0 ? "update.nsu" : args[0])) {
			src = new String(fin.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.exit(new ScriptInterpreter().run(src));
	}

	int run(String source) {
		Statement[] statments;
		String[] spl = source.split("\n");
		String src = "";
		String[] beforeDoneOnFor = new String[1 << 16];
		int d = 0;
		for (int i = 0; i < spl.length; i++) {
			while (spl[i].startsWith(" ") || spl[i].startsWith("\t"))
				spl[i] = spl[i].substring(1);
			if (spl[i].startsWith("for")) {
				String inside = spl[i].substring(spl[i].indexOf('(') + 1, spl[i].lastIndexOf(')'));
				String[] pts = inside.split(";\\s*");
				src += pts[0] + "\nD" + d + "while(" + pts[1] + ")do\n";
				beforeDoneOnFor[d] = pts[2];
				d++;
			} else if (spl[i].startsWith("done")) {
				d--;
				if (beforeDoneOnFor[d] != null) {
					src += beforeDoneOnFor[d] + "\nD" + d + spl[i] + "\n";
					beforeDoneOnFor[d] = null;
				} else src += "D" + d + spl[i] + "\n";
			} else if (spl[i].startsWith("while")) {
				src += "D" + d + spl[i] + "\n";
				d++;
			} else {
				src += spl[i] + "\n";
			}
		}
		System.out.print(src);
		try {
			statments = getStatements(src, 0);
		} catch (TokenException e) {
			e.printStackTrace();
			return 1;
		}
		Statements tree = new Statements(statments);
		System.out.print("Running...\n\n");
		tree.run(new HashMap<>());
		System.out.print("\n\nExecution finished");
		return 0;
	}

	Statement[] getStatements(String lines, int d) throws TokenException, ParsingError {
		String[] split = lines.split("\n");
		List<Statement> statements = new ArrayList<>();
		int i;
		for (i = 0; i < split.length; i++) {
			String line = split[i];
			if (line.isEmpty())
				continue;
			if (line.equals("PAYLOAD:"))
				break;
			for (Statement_TYPE type : Statement_TYPE.values())
				if (line.matches(type.pattern)) {
					switch (type) {
						case VAR_DECLARE: {
							String type_ = line.substring(0, line.indexOf(' '));
							DATA_TYPE type__ = null;
							for (DATA_TYPE data_type : DATA_TYPE.values()) {
								if (type_.matches(data_type.pattern)) {
									type__ = data_type;
									break;
								}
							}
							String l = line.substring(line.indexOf(' ') + 1);
							int ind = l.indexOf('=');
							String name = l.substring(0, ind == -1 ? l.length() : ind);
							while (name.startsWith(" ") || name.startsWith("\t"))
								name = name.substring(1);
							while (name.endsWith(" ") || name.endsWith("\t"))
								name = name.substring(0, name.length() - 1);
							statements.add(new VarDeclare_Statement(name, type__));
							if (line.contains("=")) {
								String value = line.substring(line.indexOf('=') + 1);
								Token[] valueTokens = tokensOfValue(value);
								statements.add(new VarUpdate_Statement(name, valueTokens));
							}
							break;
						}
						case VAR_UPDATE: {
							String l = line;
							String name = l.substring(0, l.indexOf('='));
							while (name.startsWith(" ") || name.startsWith("\t"))
								name = name.substring(1);
							while (name.endsWith(" ") || name.endsWith("\t"))
								name = name.substring(0, name.length() - 1);
							String value = line.substring(line.indexOf('=') + 1);
							Token[] valueTokens = tokensOfValue(value);
							statements.add(new VarUpdate_Statement(name, valueTokens));
							break;
						}
						case CONDITIONAL: {
							Token[] conditionTokens = tokensOfValue(line.substring(line.indexOf('(') + 1, line.lastIndexOf(')')));
							Statements onTrue = null, onFalse = null;
							for (i++; i < split.length && !split[i].equals("fi"); i++) {
								if (split[i].equals("then")) {
									int j = ++i;
									for (; i < split.length && !split[i].equals("else") && !split[i].equals("fi"); i++)
										;
									String[] lines_arr = new String[i - j];
									if (i - j >= 0) System.arraycopy(split, j, lines_arr, 0, i - j);
									String code = "";
									for (String line_ : lines_arr)
										code += line_ + "\n";
									onTrue = new Statements(getStatements(code, d + 1));
									i--;
								}
								if (split[i].equals("else")) {
									int j = ++i;
									for (; i < split.length && !split[i].equals("fi"); i++) ;
									String[] lines_arr = new String[i - j];
									if (i - j >= 0) System.arraycopy(split, j, lines_arr, 0, i - j);
									String code = "";
									for (String line_ : lines_arr)
										code += line_ + "\n";
									onFalse = new Statements(getStatements(code, d + 1));
									i--;
								}
							}
							statements.add(new Conditional(conditionTokens, onTrue, onFalse));
							break;
						}
						case WHILE_LOOP: {
							Token[] conditionTokens_while = tokensOfValue(line.substring(line.indexOf('(') + 1, line.lastIndexOf(')')));
							int j = ++i;
							for (; i < split.length && !split[i].equals("D" + (d) + "done"); i++)
								;
							String[] lines_arr = new String[i - j];
							if (i - j >= 0) System.arraycopy(split, j, lines_arr, 0, i - j);
							String code = "";
							for (String line_ : lines_arr)
								code += line_ + "\n";
							Statements runstatements = new Statements(getStatements(code, d + 1));
							i--;
							statements.add(new WhileLoop(conditionTokens_while, runstatements));
							break;
						}
						case DELETE_VAR: {
							String name = line.split(" ")[1];
							statements.add(new DeleteVariableStatement(name));
							break;
						}
						case INCREMENT: {
							int ind = line.indexOf('+');
							String name = line.substring(0, ind != -1 ? ind : line.indexOf('-'));
							statements.add(new VarUpdate_Statement(name, new Token[]{
									new IdentifierToken(name),
									new OperatorToken(ind != -1 ? OperatorToken.Math_Operator.ADD : OperatorToken.Math_Operator.SUBTRACT),
									new NumberToken(1)
							}));
							break;
						}
						case METHOD_CALL: {
							String methodName = line.substring(0, line.indexOf('(')).toUpperCase().replaceAll(" ", "_");
							METHOD method = METHOD.valueOf(methodName);
							String argfull = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
							if (!argfull.isEmpty()) {
								String[] args = argfull.split(",");
								Token[][] v = new Token[args.length][];
								for (int j = 0; j < args.length; j++)
									v[j] = tokensOfValue(args[j]);
								statements.add(new MethodCallStatement(method, v));
							} else
								statements.add(new MethodCallStatement(method));
							break;
						}
					}
					break;
				}
		}
		int begin = lines.indexOf("PAYLOAD:") + 9;
		if (begin != 8) {
			byte[] data = lines.substring(begin).getBytes();
			i = 0;
			while (i < data.length) {
				String name = new String(new byte[]{data[i], data[i + 1], data[i + 2]});
				i += 4;
				String cs = "";
				while (i < data.length && data[i] != (byte) ' ')
					cs += (char) data[i++];
				if (i == data.length)
					throw new Error("Unexpected end of payload");
				int c = Integer.parseInt(cs);
				int j;
				byte[] buf = new byte[c];
				for (j = ++i; i < j + c; i++) {
					buf[i - j] = data[i];
				}
				i++;
				new Payload(name, buf);
			}
		}

		return statements.toArray(new Statement[statements.size()]);
	}

	private Token[] tokensOfValue(String value) {
		while (value.startsWith(" ") || value.startsWith("\t"))
			value = value.substring(1);
		while (value.endsWith(" ") || value.endsWith("\t"))
			value = value.substring(0, value.length() - 1);
		List<Token> tokens = new ArrayList<>();
		String[] parts = value.split("(\\s)*([(+\\-*/)]|&&|\\|\\||==|>=|>|<|<=|!=)(\\s)*");
		for (int i = 0; i < parts.length; i++) {
			int l = 0;
			if (value.startsWith("&&")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_AND));
				l = 2;
			} else if (value.startsWith("||")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_OR));
				l = 2;
			} else if (value.startsWith("^")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_XOR));
				l = 1;
			} else if (value.startsWith("+")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.ADD));
				l = 1;
			} else if (value.startsWith("-")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.SUBTRACT));
				l = 1;
			} else if (value.startsWith("*")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.MULTIPLY));
				l = 1;
			} else if (value.startsWith("/")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.DIVIDE));
				l = 1;
			} else if (value.startsWith("==")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_E));
				l = 2;
			} else if (value.startsWith(">=")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_GE));
				l = 2;
			} else if (value.startsWith(">")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_G));
				l = 1;
			} else if (value.startsWith("<=")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_SE));
				l = 2;
			} else if (value.startsWith("<")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_S));
				l = 1;
			} else if (value.startsWith("!=")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.LOGIC_NE));
				l = 2;
			} else if (value.startsWith("(")) {
				tokens.add(new ParenthesisOpenedToken());
				l = 1;
			} else if (value.startsWith(")")) {
				tokens.add(new ParenthesisClosedToken());
				l = 1;
			}
			value = value.substring(l);
			while (value.matches("^\\s+.*$"))
				value = value.substring(1);
			if (parts[i].matches("^\".*\"$")) {
				tokens.add(new StringToken(parts[i].substring(1, parts[i].length() - 1)));
				System.out.println("String token: " + parts[i]);
			} else if (parts[i].matches("^\\d+$")) {
				tokens.add(new NumberToken(Integer.parseInt(parts[i])));
			} else if (parts[i].matches("^(true|false)$")) {
				tokens.add(new LogicConstantValueToken(parts[i].equals("true")));
			} else if (parts[i].matches("^___[a-zA-Z0-9]{3}$")) {
				tokens.add(new PayloadToken(parts[i].substring(3, 6)));
			} else if (parts[i].matches("^[a-zA-Z_][a-zA-Z0-9_]*\\(.*\\)$")) {
				String name = parts[i].substring(0, parts[i].indexOf('(')).toUpperCase().replaceAll(" ", "_");
				METHOD method = METHOD.valueOf(name);
				String[] args = parts[i].substring(parts[i].indexOf("("), parts[i].lastIndexOf(')')).split(",(\\s)?");
				Token[][] tkns = new Token[args.length][];
				for (int j = 0; j < args.length; j++)
					tkns[j] = tokensOfValue(args[j]);
				tokens.add(new MethodResultToken(method, tkns));
			} else if (parts[i].matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
				tokens.add(new IdentifierToken(parts[i]));
			}
			value = value.replaceFirst(Pattern.quote(parts[i]), "");
			while (value.matches("^\\s+.*$"))
				value = value.substring(1);
		}
		return tokens.toArray(new Token[tokens.size()]);
	}
}