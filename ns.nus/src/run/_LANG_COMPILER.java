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

import exceptions.ParsingError;
import exceptions.TokenException;
import lang.METHOD;
import statements.*;
import tokens.*;
import tree.Statements;
import variables.DATA_TYPE;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class _LANG_COMPILER {
	public static int strCode = 0;
	static int tg = 1;
	static List<VAR_> vars = new ArrayList<>();
	static List<VAR_> dataVars = new ArrayList<>();
	static int cond_code = 0;
	static int parse_index = 0;
	//TURN THE PROGRAM INTO NASM x64 ASSEMBLY AND COMPILE SAID ASSEMBLY TO MACHINE CODE
	private static String program_file_name;
	private static String target_file_name;
	private static String program;
	private static String parsed_src;
	private static Statement[] statements;
	private static String assembly;
	private static byte[] compiledAssembly; // MACHINE CODE

	public static void readProgram() {
		try (FileInputStream fin = new FileInputStream(program_file_name)) {
			program = new String(fin.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String[] spl = program.split("\n");
		parsed_src = "";
		String[] beforeDoneOnFor = new String[1 << 16];
		int d = 0;
		for (int i = 0; i < spl.length; i++) {
			while (spl[i].startsWith(" ") || spl[i].startsWith("\t"))
				spl[i] = spl[i].substring(1);
			if (spl[i].startsWith("for")) {
				String inside = spl[i].substring(spl[i].indexOf('(') + 1, spl[i].lastIndexOf(')'));
				String[] pts = inside.split(";\\s*");
				parsed_src += pts[0] + "\nD" + d + "while(" + pts[1] + ")do\n";
				beforeDoneOnFor[d] = pts[2];
				d++;
			} else if (spl[i].startsWith("done")) {
				d--;
				if (beforeDoneOnFor[d] != null) {
					parsed_src += beforeDoneOnFor[d] + "\nD" + d + spl[i] + "\n";
					beforeDoneOnFor[d] = null;
				} else parsed_src += "D" + d + spl[i] + "\n";
			} else if (spl[i].startsWith("while")) {
				parsed_src += "D" + d + spl[i] + "\n";
				d++;
			} else {
				parsed_src += spl[i] + "\n";
			}
		}
		System.out.println(parsed_src);
	}

	public static void tokenizeProgram() {
		statements = getStatements(parsed_src, 0);
	}

	public static void addNewVar(String name, String value) {
		dataVars.add(new VAR_(name, DATA_TYPE.STRING, value));
	}

	public static void makeAssembly() {
		assembly = "section .text\n\tglobal _start\n_start:\n";
		for (Statement statement : statements) {
			if (statement.type == Statement_TYPE.VAR_DECLARE) {
				vars.add(new VAR_(((VarDeclare_Statement) statement).name, ((VarDeclare_Statement) statement).type));
			} else if (statement.type == Statement_TYPE.VAR_UPDATE) {
				assembly += valueInstructions(((VarUpdate_Statement) statement).value);
				assembly += "\tmov [" + ((VarUpdate_Statement) statement).name + "], r10\n";
			} else if (statement.type == Statement_TYPE.WHILE_LOOP) {
				int a = tg++;
				assembly += "WHILE" + a + ":\n" + assemblyInstructions(((WhileLoop) statement).statements.statements);
				assembly += conditional(((WhileLoop) statement).conditionTokens);
				assembly += "\tCMP r10, 0\n\tJNE WHILE" + a + "\n";
			} else if (statement.type == Statement_TYPE.INCREMENT) {
				assembly += "\tINC WORD [" + ((VarUpdate_Statement) statement).name + "]\n";
			} else if (statement.type == Statement_TYPE.METHOD_CALL) {
				assembly += ((MethodCallStatement) statement).assembly();
			}
		}
		assembly += "\tmov eax, 1\n\tmov ebx, 0\n\tint 0x80\n";
		String asm_vars = "section .bss\n";
		for (VAR_ var : vars) {
			asm_vars += "\t" + var.name + " " + (var.type == DATA_TYPE.INT ? "RESQ" : "RESW") + " 1\n";
		}
		asm_vars += "\tINTERNAL____CACHE RESQ 65536\n";//INTERNAL____CACHE
		assembly = asm_vars + "\n\n" + assembly;
		if (Payload.allPayloads().size() != 0 || dataVars.size() != 0) {
			asm_vars = "\n\nsection .data\n";
			for (Payload p : Payload.allPayloads()) {
				asm_vars += "\t" + p.identifier + " DB ";
				for (byte b : p.payload)
					asm_vars += b + ", ";
				asm_vars = asm_vars.substring(0, asm_vars.length() - 2) + "\n";
			}
			for (VAR_ var : dataVars) {
				asm_vars += "\t" + var.name + " DB \"" + var.value + "\", 10, 0\n";
			}
			assembly += asm_vars;
		}
		System.out.println("Assembly:\n" + assembly);
	}

	public static String assemblyInstructions(Statement[] statements) {
		String asm = "";
		for (Statement statement : statements) {
			if (statement.type == Statement_TYPE.VAR_DECLARE) {
				vars.add(new VAR_(((VarDeclare_Statement) statement).name, ((VarDeclare_Statement) statement).type));
			} else if (statement.type == Statement_TYPE.VAR_UPDATE) {
				asm += valueInstructions(((VarUpdate_Statement) statement).value);
				asm += "\tmov [" + ((VarUpdate_Statement) statement).name + "], r10\n";
			} else if (statement.type == Statement_TYPE.WHILE_LOOP) {
				int a = tg++;
				asm += "WHILE" + a + ":\n" + assemblyInstructions(((WhileLoop) statement).statements.statements);
				asm += conditional(((WhileLoop) statement).conditionTokens);
				asm += "\tCMP r10, 0\n\tJNE WHILE" + a + "\n";
			} else if (statement.type == Statement_TYPE.INCREMENT) {
				asm += "\tINC WORD [" + ((Increment_Statement) statement).name + "]\n";
			} else if (statement.type == Statement_TYPE.METHOD_CALL) {
				assembly += ((MethodCallStatement) statement).assembly();
			}
		}
		return asm;
	}

	public static String conditional(Token[] conditionTokens) {
		String v = "\tmov eax, " + value(conditionTokens[0]) + "\n\tmov ebx, " + value(conditionTokens[2]) + "\n\tCMP eax, ebx\n";
		OperatorToken tkn = ((OperatorToken) conditionTokens[1]);
		cond_code++;
		if (tkn.mop == OperatorToken.Math_Operator.LOGIC_E)
			v += "\tJE COND_" + cond_code + "\n\tmov r10, 0\n\tJMP COND_" + cond_code + "END\n";
		else if (tkn.mop == OperatorToken.Math_Operator.LOGIC_NE)
			v += "\tJNE COND_" + cond_code + "\n\tmov r10, 0\n\tJMP COND_" + cond_code + "END\n";
		else if (tkn.mop == OperatorToken.Math_Operator.LOGIC_G)
			v += "\tJG COND_" + cond_code + "\n\tmov r10, 0\n\tJMP COND_" + cond_code + "END\n";
		else if (tkn.mop == OperatorToken.Math_Operator.LOGIC_GE)
			v += "\tJGE COND_" + cond_code + "\n\tmov r10, 0\n\tJMP COND_" + cond_code + "END\n";
		else if (tkn.mop == OperatorToken.Math_Operator.LOGIC_S)
			v += "\tJL COND_" + cond_code + "\n\tmov r10, 0\n\tJMP COND_" + cond_code + "END\n";
		else if (tkn.mop == OperatorToken.Math_Operator.LOGIC_SE)
			v += "\tJLE COND_" + cond_code + "\n\tmov r10, 0\n\tJMP COND_" + cond_code + "END\n";

		v += "COND_" + cond_code + ":\n\tmov r10, 1\nCOND_" + cond_code + "END:\n";
		return v;
	}

	public static String valueInstructions(Token[] valueTokens) {
		if (valueTokens.length == 1) {
			return "\tmov r10, " + value(valueTokens[0]) + "\n";
		} else if (valueTokens.length == 3) {
			return "\tmov r10, " + value(valueTokens[0]) + "\n\tmov r11, " + value(valueTokens[2]) + "\n\t" + ((OperatorToken) valueTokens[1]).asm_code("r10", "r11");
		} else {
			return "\n";
		}
	}

	public static String value(Token token) {
		if (token instanceof NumberToken)
			return Integer.toString(((NumberToken) token).v);
		else if (token instanceof IdentifierToken)
			return "[" + ((IdentifierToken) token).identifier + "]";
		else return "";
	}

	public static void compileAssembly() {
		try (FileOutputStream fout = new FileOutputStream(target_file_name)) {
			fout.write(assembly.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeMachineCodeToFile() {

	}

	public static void main(String[] args) {
		program_file_name = "update.nsu";
		target_file_name = "update.asm";
		readProgram();
		tokenizeProgram();
		makeAssembly();
		compileAssembly();
		writeMachineCodeToFile();
	}

	static Statement[] getStatements(String lines, int d) throws TokenException, ParsingError {
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
							statements.add(new Increment_Statement(name));
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

	private static Token[] tokensOfValue(String value) {
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

	public static String intToString(Token token) {
		if (token instanceof IdentifierToken) {
			String name = ((IdentifierToken) token).identifier;
			parse_index++;
			String asm = "\tmov r10, [" + name + "]\n\tmov r11, str_" + strCode + "\n\tadd r11, 64\nPARSE_" + parse_index + ":\n\tmovzx al, r10\n\tdiv 0xA\n\tmovzx r10, al\n\tadd ah, 48\n\tmov BYTE [r11], ah\n\tsub r11, 8\n\tCMP r10, 0\n\tJNE PARSE_" + parse_index + "\n";
			return asm;
		} else return "\n";
	}

	private static class VAR_ {
		private String value;
		private String name;
		private DATA_TYPE type;

		public VAR_(String name, DATA_TYPE type) {
			this.name = name;
			this.type = type;
		}

		public VAR_(String name, DATA_TYPE type, String value) {
			this.name = name;
			this.type = type;
			this.value = value;
		}
	}
}