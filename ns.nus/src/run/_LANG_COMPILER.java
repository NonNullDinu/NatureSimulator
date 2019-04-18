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
import lang.exceptions.InvalidExpressionException;
import statements.*;
import tokens.*;
import tree.Statements;
import variables.DATA_TYPE;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class _LANG_COMPILER {
	public static int strCode = 0;
	private static final String functions_code = "print_char:\n\tpush rax\n\tmov ecx, eax\n\tmov eax, 4\n\tmov ebx, r8d\n\tmov edx, 1\n\tint 0x80\n\tpop rax\n\tret\n\n" +
			"printNumber:\n\tpush rax\n\tpush rdx\n\txor edx,edx\n\tdiv dword[const10]\n\ttest eax,eax\n\tje .l1\n\tcall printNumber\n.l1:\n\tlea eax,[digits+edx]\n\tcall print_char\n\tpop rdx\n\tpop rax\n\tret\n\n" +
			"printNewLine:\n\tmov eax, 4\n\tmov ebx, 1\n\tmov ecx, new_line\n\tmov edx, 1\n\tint 0x80\n\tret\n\n";
	private static final List<VAR_> vars = new ArrayList<>();
	private static final List<VAR_> dataVars = new ArrayList<>();
	private static int tg = 1;
	private static int cond_code = 0;
	private static String program_file_name;
	private static StringBuilder parsed_src;
	private static Statement[] statements;
	private static StringBuilder assembly;
	private static byte[] compiledAssembly; // MACHINE CODE
	public static int fileCode = 0;
	private static String asm_source_file;
	private static int rec_ind = 0;
	private static int internal_cache_index = 0;
	private static int cache_ptr = 0;

	public static void addNewVar(String name, String value) {
		dataVars.add(new VAR_(name, DATA_TYPE.STRING, value));
	}

	private static void readProgram() {
		String program;
		try (FileInputStream fin = new FileInputStream(program_file_name)) {
			program = new String(fin.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String[] spl = program.split("\n");
		parsed_src = new StringBuilder();
		String[] beforeDoneOnFor = new String[1 << 16];
		int d = 0;
		for (int i = 0; i < spl.length; i++) {
			while (spl[i].startsWith(" ") || spl[i].startsWith("\t"))
				spl[i] = spl[i].substring(1);
			if (spl[i].startsWith("for")) {
				String inside = spl[i].substring(spl[i].indexOf('(') + 1, spl[i].lastIndexOf(')'));
				String[] pts = inside.split(";\\s*");
				parsed_src.append(pts[0]).append("\nD").append(d).append("while(").append(pts[1]).append(")do\n");
				beforeDoneOnFor[d] = pts[2];
				d++;
			} else if (spl[i].startsWith("done")) {
				d--;
				if (beforeDoneOnFor[d] != null) {
					parsed_src.append(beforeDoneOnFor[d]).append("\nD").append(d).append(spl[i]).append("\n");
					beforeDoneOnFor[d] = null;
				} else parsed_src.append("D").append(d).append(spl[i]).append("\n");
			} else if (spl[i].startsWith("while")) {
				parsed_src.append("D").append(d).append(spl[i]).append("\n");
				d++;
			} else {
				parsed_src.append(spl[i]).append("\n");
			}
		}
		System.out.println(parsed_src.toString());
	}

	private static void tokenizeProgram() {
		statements = getStatements(parsed_src.toString(), 0);
	}

	public static void addNewVar(String name, byte[] value) {
		StringBuilder content = new StringBuilder();
		for (byte b : value)
			content.append(Byte.toUnsignedInt(b)).append(", ");
		dataVars.add(new VAR_(name, DATA_TYPE.STRING, content.substring(0, content.length() - 2)));
	}

	private static void makeAssembly() {
		assembly = new StringBuilder(";DO NOT EDIT\n;THIS FILE IS COMPUTER GENERATED\n;AS A RESULT OF THE COMPILATION OF \"" + program_file_name + "\"\nsection .text\n" + functions_code + "\n\tglobal _start\n_start:\n");
		int i = 1;
		for (Statement statement : statements) {
			assembly.append("\t;" + i + "\n");
			if (statement.type == Statement_TYPE.VAR_DECLARE) {
				vars.add(new VAR_(((VarDeclare_Statement) statement).name, ((VarDeclare_Statement) statement).type));
			} else if (statement.type == Statement_TYPE.VAR_UPDATE) {
				assembly.append(valueInstructions(((VarUpdate_Statement) statement).value));
				assembly.append("\tmov QWORD [").append(((VarUpdate_Statement) statement).name).append("], r10\n");
			} else if (statement.type == Statement_TYPE.WHILE_LOOP) {
				int a = tg++;
				assembly.append("WHILE").append(a).append(":\n").append(assemblyInstructions(new Statements(((WhileLoop) statement).statements.statements)));
				assembly.append(conditional(((WhileLoop) statement).conditionTokens));
				assembly.append("\tCMP r10, 0\n\tJNE WHILE").append(a).append("\n");
			} else if (statement.type == Statement_TYPE.INCREMENT) {
				assembly.append("\tINC QWORD [").append(((VarUpdate_Statement) statement).name).append("]\n");
			} else if (statement.type == Statement_TYPE.METHOD_CALL) {
				assembly.append(((MethodCallStatement) statement).assembly());
			} else if (statement.type == Statement_TYPE.CONDITIONAL) {
				assembly.append(conditional(((Conditional) statement).condition)).append("\nCMP r10, 0\n\tJE COND_" + cond_code + "_FALSE\nCOND_" + cond_code + "_TRUE:\n\t");
				int cnd = cond_code;
				assembly.append(assemblyInstructions(((Conditional) statement).onTrue)).append("\n\tJMP COND_" + cnd + "_FINAL_END\nCOND_" + cnd + "_FALSE:\n");
				if (((Conditional) statement).onFalse != null) {
					assembly.append(assemblyInstructions(((Conditional) statement).onFalse));
				}
				assembly.append("COND_" + cnd + "_FINAL_END:\n");
			}
			rec_ind = 0;
			i++;
		}
		StringBuilder asm_vars = new StringBuilder("section .bss\n");
		for (VAR_ var : vars) {
			asm_vars.append("\t").append(var.name).append(" ").append(var.type == DATA_TYPE.INT ? "RESQ" : "RESW").append(" 1\n");
		}
		asm_vars.append("\tINTERNAL____CACHE RESQ 65536\n");//INTERNAL____CACHE
		assembly = new StringBuilder(asm_vars + "\n\n" + assembly.toString());
		asm_vars = new StringBuilder("\n\nsection .data\n\tconst10 dd 10\n\tdigits db 48,49,50,51,52,53,54,55,56,57\n\tnew_line DB 10\n");
//		for (Payload p : Payload.allPayloads()) {
//			asm_vars.append("\t").append(p.identifier).append(" DB ");
//			for (byte b : p.payload)
//				asm_vars.append(b).append(", ");
//			asm_vars = new StringBuilder(asm_vars.substring(0, asm_vars.length() - 2) + "\n");
//		}
		for (VAR_ var : dataVars) {
			asm_vars.append("\t").append(var.name).append(" DB ").append(var.value).append("\n");
		}
		assembly.append(asm_vars);
		System.out.println("Assembly:\n" + assembly.toString());
	}

	private static String assemblyInstructions(Statements statements) {
		StringBuilder asm = new StringBuilder();
		int i = 1;
		System.out.println(statements.statements.length);
		for (Statement statement : statements) {
			asm.append("\t;" + i + "\n");
			System.out.println(statement.type);
			if (statement.type == Statement_TYPE.VAR_DECLARE) {
				vars.add(new VAR_(((VarDeclare_Statement) statement).name, ((VarDeclare_Statement) statement).type));
			} else if (statement.type == Statement_TYPE.VAR_UPDATE) {
				asm.append(valueInstructions(((VarUpdate_Statement) statement).value));
				asm.append("\tmov QWORD [").append(((VarUpdate_Statement) statement).name).append("], r10\n");
			} else if (statement.type == Statement_TYPE.WHILE_LOOP) {
				int a = tg++;
				asm.append("WHILE").append(a).append(":\n").append(assemblyInstructions(new Statements(((WhileLoop) statement).statements.statements)));
				asm.append(conditional(((WhileLoop) statement).conditionTokens));
				asm.append("\tCMP r10, 0\n\tJNE WHILE").append(a).append("\n");
			} else if (statement.type == Statement_TYPE.INCREMENT) {
				asm.append("\tINC QWORD [").append(((Increment_Statement) statement).name).append("]\n");
			} else if (statement.type == Statement_TYPE.METHOD_CALL) {
				asm.append(((MethodCallStatement) statement).assembly());
				System.out.println("CALL " + asm.toString());
			}
			rec_ind = 0;
			i++;
		}
		return asm.toString();
	}

	private static String conditional(Token[] conditionTokens) {
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
		if (rec_ind == 0) {
			internal_cache_index = 0;
			cache_ptr = 0;
		}
		rec_ind++;
		if (valueTokens.length == 1) {
			return "\tmov r10, " + value(valueTokens[0]) + "\n\tmov QWORD[INTERNAL____CACHE + " + (8 * (cache_ptr = internal_cache_index++)) + "], r10\n";
		} else if (valueTokens.length == 3) {
			return "\tmov r10, " + value(valueTokens[0]) + "\n\tmov r11, " + value(valueTokens[2]) + "\n\t" + ((OperatorToken) valueTokens[1]).asm_code("r10", "r11") + "\n\tmov QWORD[INTERNAL____CACHE + " + (8 * (cache_ptr = internal_cache_index++)) + "], r10\n";
		} else {
			int i;
			int cnt = 0;
			for (i = 0; i < valueTokens.length; i++) {
				if (valueTokens[i] instanceof ParenthesisOpenedToken)
					cnt++;
				else if (valueTokens[i] instanceof ParenthesisClosedToken) {
					cnt--;
					if (cnt < 0)
						break;
				}
			}
			if (cnt != 0)
				throw new InvalidExpressionException(Arrays.deepToString(valueTokens));
			String asm = "";
			for (i = valueTokens.length - 1; i >= 0; i--) {
				if (valueTokens[i] instanceof ParenthesisClosedToken) {
					int d_ = 1, j;
					for (j = i - 1; j >= 0; j--) {
						if (valueTokens[j] instanceof ParenthesisOpenedToken) {
							--d_;
							if (d_ == 0)
								break;
						} else if (valueTokens[j] instanceof ParenthesisClosedToken)
							d_++;
					}
					Token[] t = new Token[i - j - 1];
					System.arraycopy(valueTokens, j + 1, t, 0, i - j - 1);
					asm += "\t" + valueInstructions(t) + "\n\t";
					t = new Token[valueTokens.length - i + j];
					System.arraycopy(valueTokens, 0, t, 0, j);
					System.arraycopy(valueTokens, i + 1, t, j + 1, valueTokens.length - i - 1);
					t[j] = new INTERNAL____CACHE_TOKEN(cache_ptr);
					valueTokens = t;
					i = j - 1;
				}
			}
			for (i = valueTokens.length - 1; i >= 0; i--) {
				if (valueTokens[i] instanceof OperatorToken)
					switch (((OperatorToken) valueTokens[i]).mop) {
						case SUBTRACT:
						case ADD:
							Token[] tokens1 = new Token[i];
							Token[] tokens2 = new Token[valueTokens.length - i - 1];
							System.arraycopy(valueTokens, 0, tokens1, 0, i);
							System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
							int a, b;
							asm += valueInstructions(tokens1) + "\tmov r10, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n\tmov QWORD [INTERNAL____CACHE + " + (8 * (a = internal_cache_index++)) + "], r10\n";
							asm += valueInstructions(tokens2) + "\tmov r10, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n\tmov QWORD [INTERNAL____CACHE + " + (8 * (b = internal_cache_index++)) + "], r10\n";
							asm += "\tmov r12, QWORD [INTERNAL____CACHE + " + (8 * a) + "]\n";
							asm += "\tmov r13, QWORD [INTERNAL____CACHE + " + (8 * b) + "]\n";
							asm += ((OperatorToken) valueTokens[i]).asm_code("r12", "r13");
							asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * (cache_ptr = internal_cache_index++)) + "], r12\n\tmov r10, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n";
							return asm;
					}
			}
			for (i = valueTokens.length - 1; i >= 0; i--) {
				if (valueTokens[i] instanceof OperatorToken)
					switch (((OperatorToken) valueTokens[i]).mop) {
						case DIVIDE:
						case MULTIPLY:
							Token[] tokens1 = new Token[i];
							Token[] tokens2 = new Token[valueTokens.length - i - 1];
							System.arraycopy(valueTokens, 0, tokens1, 0, i);
							System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
							int a, b;
							asm += valueInstructions(tokens1) + "\tmov r10, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n\tmov QWORD [INTERNAL____CACHE + " + (8 * (a = internal_cache_index++)) + "], r10\n";
							asm += valueInstructions(tokens2) + "\tmov r10, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n\tmov QWORD [INTERNAL____CACHE + " + (8 * (b = internal_cache_index++)) + "], r10\n";
							asm += "\tmov r12, QWORD [INTERNAL____CACHE + " + (8 * a) + "]\n";
							asm += "\tmov r13, QWORD [INTERNAL____CACHE + " + (8 * b) + "]\n";
							asm += ((OperatorToken) valueTokens[i]).asm_code("r12", "r13");
							asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * (cache_ptr = internal_cache_index++)) + "], r12\n\tmov r10, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n";
							return asm;
					}
			}
			return "\n";
		}
	}

	private static String value(Token token) {
		if (token instanceof NumberToken)
			return Integer.toString(((NumberToken) token).v);
		else if (token instanceof IdentifierToken)
			return "[" + ((IdentifierToken) token).identifier + "]";
		else if (token instanceof INTERNAL____CACHE_TOKEN)
			return "[INTERNAL____CACHE + " + (((INTERNAL____CACHE_TOKEN) token).qwordoffset * 8) + "]";
		else return "";
	}

	private static void compileAssembly() {
		try (FileOutputStream fout = new FileOutputStream(asm_source_file)) {
			fout.write(assembly.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		program_file_name = "update.nsu";
		asm_source_file = "update.asm";
		readProgram();
		tokenizeProgram();
		makeAssembly();
		compileAssembly();
	}

	private static Statement[] getStatements(String lines, int d) throws TokenException, ParsingError {
		String[] split = lines.split("\n");
		List<Statement> statements = new ArrayList<>();
		int i;
		System.out.println("LINE SPLIT LENGTH: " + split.length);
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
							String name = line.substring(0, line.indexOf('='));
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
									StringBuilder code = new StringBuilder();
									for (String line_ : lines_arr)
										code.append(line_).append("\n");
									System.out.println("CONDITIONAL CODE: " + code.toString());
									onTrue = new Statements(getStatements(code.toString(), d + 1));
									i--;
								}
								if (split[i].equals("else")) {
									int j = ++i;
									for (; i < split.length && !split[i].equals("fi"); i++) ;
									String[] lines_arr = new String[i - j];
									if (i - j >= 0) System.arraycopy(split, j, lines_arr, 0, i - j);
									StringBuilder code = new StringBuilder();
									for (String line_ : lines_arr)
										code.append(line_).append("\n");
									onFalse = new Statements(getStatements(code.toString(), d + 1));
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
							StringBuilder code = new StringBuilder();
							for (String line_ : lines_arr)
								code.append(line_).append("\n");
							Statements runstatements = new Statements(getStatements(code.toString(), d + 1));
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
							System.out.println("CALL TO METHOD " + method.name());
							String argfull = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
							System.out.println("WITH ARGS : " + argfull);
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
				StringBuilder cs = new StringBuilder();
				while (i < data.length && data[i] != (byte) ' ')
					cs.append((char) data[i++]);
				if (i == data.length)
					throw new Error("Unexpected end of payload");
				int c = Integer.parseInt(cs.toString());
				int j;
				byte[] buf = new byte[c];
				for (j = ++i; i < j + c; i++) {
					buf[i - j] = data[i];
				}
				i++;
				new Payload(name, buf);
			}
		}

		return statements.toArray(new Statement[0]);
	}

	private static Token[] tokensOfValue(String value) {
		while (value.startsWith(" ") || value.startsWith("\t"))
			value = value.substring(1);
		while (value.endsWith(" ") || value.endsWith("\t"))
			value = value.substring(0, value.length() - 1);
		if (value.startsWith("\"") && value.endsWith("\""))
			return new Token[]{new StringToken(value)};
		List<Token> tokens = new ArrayList<>();
		String[] parts = value.split("(\\s)*([(+\\-*/)]|&&|\\|\\||==|>=|>|<=|<|!=)(\\s)*");
		for (String part : parts) {
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
			if (part.matches("^\".*\"$")) {
				tokens.add(new StringToken(part.substring(1, part.length() - 1)));
				System.out.println("String token: " + part);
			} else if (part.matches("^\\d+$")) {
				tokens.add(new NumberToken(Integer.parseInt(part)));
			} else if (part.matches("^(true|false)$")) {
				tokens.add(new LogicConstantValueToken(part.equals("true")));
			} else if (part.matches("^___[a-zA-Z0-9]{3}$")) {
				tokens.add(new PayloadToken(part.substring(3, 6)));
			} else if (part.matches("^[a-zA-Z_][a-zA-Z0-9_]*\\(.*\\)$")) {
				String name = part.substring(0, part.indexOf('(')).toUpperCase().replaceAll(" ", "_");
				METHOD method = METHOD.valueOf(name);
				String[] args = part.substring(part.indexOf("("), part.lastIndexOf(')')).split(",(\\s)?");
				Token[][] tkns = new Token[args.length][];
				for (int j = 0; j < args.length; j++)
					tkns[j] = tokensOfValue(args[j]);
				tokens.add(new MethodResultToken(method, tkns));
			} else if (part.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
				tokens.add(new IdentifierToken(part));
			}
			value = value.replaceFirst(Pattern.quote(part), "");
			while (value.matches("^\\s+.*$"))
				value = value.substring(1);
		}
		return tokens.toArray(new Token[0]);
	}

	public static String printIdentifier(Token token) {
		if (token instanceof IdentifierToken) {
			return "\tmov eax, [" + ((IdentifierToken) token).identifier + "]\n\tcall printNumber\n\tcall printNewLine\n";
		} else return "\n";
	}

	public static void addNewRESWVar(String name) {
		vars.add(new VAR_(name, DATA_TYPE.SHORT_INT));
	}

	private static class VAR_ {
		private String value;
		private final String name;
		private final DATA_TYPE type;

		VAR_(String name, DATA_TYPE type) {
			this.name = name;
			this.type = type;
		}

		VAR_(String name, DATA_TYPE type, String value) {
			this.name = name;
			this.type = type;
			this.value = value;
		}
	}

	private static class INTERNAL____CACHE_TOKEN extends Token {
		int qwordoffset;

		INTERNAL____CACHE_TOKEN(int qwordoffset) {
			this.qwordoffset = qwordoffset;
		}

		@Override
		public String toString() {
			return "ICT(" + qwordoffset + ")";
		}
	}
}