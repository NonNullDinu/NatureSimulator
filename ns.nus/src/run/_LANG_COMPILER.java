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

import lang.*;
import lang.exceptions.InvalidExpressionException;
import lang.exceptions.ParsingError;
import lang.exceptions.SyntaxError;
import lang.exceptions.TokenException;
import statements.*;
import tokens.*;
import tree.Statements;
import variables.DATA_TYPE;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class _LANG_COMPILER {
	public static int strCode = 0;
	private static final String functions_code = "print_char:\n" +
			"\tpush rax\n" +
			"\tmov ecx, eax\n" +
			"\tmov eax, 4\n" +
			"\tmov ebx, r8d\n" +
			"\tmov edx, 1\n" +
			"\tint 0x80\n" +
			"\tpop rax\n" +
			"\tret\n" +
			"printNumber:\n" +
			"\tpush rax\n" +
			"\tpush rdx\n" +
			"\txor edx,edx\n" +
			"\tdiv dword[const10]\n" +
			"\ttest eax,eax\n" +
			"\tje .l1\n" +
			"\tcall printNumber\n" +
			".l1:\n" +
			"\tlea eax,[digits+edx]\n" +
			"\tcall print_char\n" +
			"\tpop rdx\n" +
			"\tpop rax\n" +
			"\tret\n" +
			"printNewLine:\n" +
			"\tmov eax, 4\n" +
			"\tmov ebx, 1\n" +
			"\tmov ecx, new_line\n" +
			"\tmov edx, 1\n" +
			"\tint 0x80\n" +
			"\tret\n" +
			"readValue:\n" +
			"\tmov eax, 3\n" +
			"\tmov ebx, 0\n" +
			"\tmov ecx, INTERNAL____READ\n" +
			"\tmov edx, 18\n" +
			"\tint 0x80\n" +
			"\tmov ebx, eax\n" +
			"\tsub ebx, 1\n" +
			"\tmov r10, 0\n" +
			"\tmov rax, 0\n" +
			".l2:\n" +
			"\tmovzx rcx, BYTE [INTERNAL____READ + r10]\n" +
			"\tsub rcx, '0'\n" +
			"\tINC r10\n" +
			"\tmul DWORD[const10]\n" +
			"\tadd rax, rcx\n" +
			"\tCMP r10d, ebx\n" +
			"\tJL .l2\n" +
			"\tret\n" +
			"\n";
	private static final List<VAR_> vars = new ArrayList<>();
	private static final List<VAR_> dataVars = new ArrayList<>();
	private static int tg = 1;
	private static int cond_code = 0;
	private static String program_file_name;
	private static StringBuilder parsed_src;
	private static Statement[] statements;
	private static StringBuilder assembly;
	public static int fileCode = 0;
	private static String asm_source_file;
	private static final Pattern register = Pattern.compile("rax%eax%ax%al%rcx%ecx%cx%cl%rdx%edx%dx%dl%rbx%ebx%bx%bl%rsi%esi%si%sil%rdi%edi%di%dil%rsp%esp%sp%spl%rbp%ebp%bp%bpl%r8%r8d%r8w%r8b%r9%r9d%r9w%r9b%r10%r10d%r10w%r10b%r11%r11d%r11w%r11b%r12%r12d%r12w%r12b%r13%r13d%r13w%r13b%r14%r14d%r14w%r14b%r15%r15d%r15w%r15b".replaceAll("%", "|"));
	private static int internal_cache_index = 0;
	private static int cache_ptr = 0;
	public static int rec_ind = 0;
	private static Map<String, Method> methods = new HashMap<>();
	private static Map<String, String> localvars = new HashMap<>();
	private static Map<String, Boolean> registers_constant = new HashMap<>();
	private static Map<String, Integer> registers_values = new HashMap<>();
	private static Map<String, REGISTER> registerMap = new HashMap<>();
	private static String regs = "rax%eax%ax%al%rcx%ecx%cx%cl%rdx%edx%dx%dl%rbx%ebx%bx%bl%rsi%esi%si%sil%rdi%edi%di%dil%rsp%esp%sp%spl%rbp%ebp%bp%bpl%r8%r8d%r8w%r8b%r9%r9d%r9w%r9b%r10%r10d%r10w%r10b%r11%r11d%r11w%r11b%r12%r12d%r12w%r12b%r13%r13d%r13w%r13b%r14%r14d%r14w%r14b%r15%r15d%r15w%r15b".replaceAll("%", " ");
	private static List<REGISTER_ADDRESSING_SET> registerList = new ArrayList<>();
	private static int ebpoff = 0;

	public static void addNewVar(String name, String value) {
		dataVars.add(new VAR_(name, DATA_TYPE.STRING, value));
	}

	private static int locvarsize = 0;

	private static void tokenizeProgram() {
		statements = getStatements(parsed_src.toString(), 0);
	}

	public static void addNewVar(String name, byte[] value) {
		StringBuilder content = new StringBuilder();
		for (byte b : value)
			content.append(Byte.toUnsignedInt(b)).append(", ");
		dataVars.add(new VAR_(name, DATA_TYPE.STRING, content.substring(0, content.length() - 2)));
	}

	private static int instrind = 1;
	private static String jumpFalseLabel;
	private static String jumpTrueLabel;

	static {
		for (String reg : regs.split(" ")) {
			int sz;
			switch (reg) {
				case "rax":
				case "rbx":
				case "rcx":
				case "rdx":
				case "r8":
				case "r9":
				case "r10":
				case "r11":
				case "r12":
				case "r13":
				case "r14":
				case "r15":
				case "rsi":
				case "rdi":
				case "rsp":
				case "rbp":
					sz = 8;
					break;
				case "eax":
				case "ebx":
				case "ecx":
				case "edx":
				case "r8d":
				case "r9d":
				case "r10d":
				case "r11d":
				case "r12d":
				case "r13d":
				case "r14d":
				case "r15d":
				case "esi":
				case "edi":
				case "esp":
				case "ebp":
					sz = 4;
					break;
				case "ax":
				case "bx":
				case "cx":
				case "dx":
				case "r8w":
				case "r9w":
				case "r10w":
				case "r11w":
				case "r12w":
				case "r13w":
				case "r14w":
				case "r15w":
				case "si":
				case "di":
				case "sp":
				case "bp":
					sz = 2;
					break;
				case "al":
				case "bl":
				case "cl":
				case "dl":
				case "r8b":
				case "r9b":
				case "r10b":
				case "r11b":
				case "r12b":
				case "r13b":
				case "r14b":
				case "r15b":
				case "sil":
				case "dil":
				case "spl":
				case "bpl":
					sz = 1;
					break;
				default:
					sz = 0;
					break;
			}
			registerMap.put(reg, new REGISTER(sz, reg));
			registers_values.put(reg, 0);
			registers_constant.put(reg, false);
		}
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rax"), reg("eax"), reg("ax"), reg("al")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rbx"), reg("ebx"), reg("bx"), reg("bl")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rcx"), reg("ecx"), reg("cx"), reg("cl")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rdx"), reg("edx"), reg("dx"), reg("dl")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rsi"), reg("esi"), reg("si"), reg("sil")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rdi"), reg("edi"), reg("di"), reg("dil")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rsp"), reg("esp"), reg("sp"), reg("spl")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("rbp"), reg("ebp"), reg("bp"), reg("bpl")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r8"), reg("r8d"), reg("r8w"), reg("r8b")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r9"), reg("r9d"), reg("r9w"), reg("r9b")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r10"), reg("r10d"), reg("r10w"), reg("r10b")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r11"), reg("r11d"), reg("r11w"), reg("r11b")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r12"), reg("r12d"), reg("r12w"), reg("r12b")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r13"), reg("r13d"), reg("r13w"), reg("r13b")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r14"), reg("r14d"), reg("r14w"), reg("r14b")));
		registerList.add(new REGISTER_ADDRESSING_SET(reg("r15"), reg("r15d"), reg("r15w"), reg("r15b")));
	}

	public static REGISTER reg(String name) {
		return registerMap.get(name);
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
			if (spl[i].startsWith("###"))
				continue;
			if (spl[i].contains("###"))
				spl[i] = spl[i].split("###")[0];
			if (spl[i].startsWith("for")) {
				String inside = spl[i].substring(spl[i].indexOf('(') + 1, spl[i].lastIndexOf(')'));
				String[] pts = inside.split(";\\s*");
				if (pts.length != 3)
					throw new SyntaxError("Expected 3 comma-separated parts in the for at line " + (i + 1) + " but found " + pts.length);
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
	}

	private static void makeAssembly() {
		assembly = new StringBuilder(";DO NOT EDIT\n;THIS FILE IS COMPUTER GENERATED\n;AS A RESULT OF THE COMPILATION OF \"" + program_file_name + "\"\nsection .text\n" + functions_code + "\n\tglobal _start\n_start:\n");
		boolean prevdec = true;
		for (Statement statement : statements) {
			assembly.append("\t;").append(instrind++).append("\n");
			switch (statement.type) {
				case VAR_DECLARE:
					if (!prevdec)
						throw new ParsingError("Variable \"" + ((VarDeclare_Statement) statement).name + "\" can only be declared at the beginning");
					VAR_ var = new VAR_(((VarDeclare_Statement) statement).name, ((VarDeclare_Statement) statement).type);
					vars.add(var);
					IdentifierToken.identifiers.forEach((IdentifierToken id) -> {
						if (id.var == null && var.name.equals(id.identifier)) {
							id.var = var;
							id.data_type = var.type;
						}
					});
					break;
				case VAR_UPDATE:
					assembly.append(valueInstructions(((VarUpdate_Statement) statement).value));
					assembly.append("\tmov QWORD [").append(((VarUpdate_Statement) statement).name).append("], r10\n");
					break;
				case WHILE_LOOP:
					int a = tg++;
					jumpTrueLabel = ".WHILE_" + a;
					jumpFalseLabel = ".WHILE_" + a + "_END";
					assembly.append(conditional(((WhileLoop) statement).conditionTokens));
					assembly.append("\tCMP r10, 0\n\tJE .WHILE_").append(a).append("_END\n");
					assembly.append(".WHILE_").append(a).append(":\t;BACKWARD JUMP\n").append(assemblyInstructions(new Statements(((WhileLoop) statement).statements.statements), new HashMap<>()));
					assembly.append(conditional(((WhileLoop) statement).conditionTokens));
					assembly.append("\tCMP r10, 0\n\tJNE .WHILE_").append(a).append("\n");
					assembly.append(".WHILE_").append(a).append("_END:\t;FORWARD JUMP\n");
					prevdec = false;
					break;
				case INCREMENT: {
					if (((Increment_Statement) statement).dt == null) {
						DATA_TYPE trg = null;
						for (VAR_ v : vars)
							if (v.name.equals(((Increment_Statement) statement).name)) {
								trg = v.type;
								break;
							}
						if (trg == null)
							throw new ParsingError("Variable \"" + ((Increment_Statement) statement).name + "\" does not exist");
						((Increment_Statement) statement).dt = trg;
					}
					assembly.append("\tINC ").append(((Increment_Statement) statement).dt.wrdtype).append("[").append(((Increment_Statement) statement).name).append("]\n");
					prevdec = false;
					break;
				}
				case METHOD_CALL:
					assembly.append(((MethodCallStatement) statement).assembly());
					break;
				case CONDITIONAL:
					cond_code++;
					int cnd = cond_code;
					if (((Conditional) statement).onFalse != null) {
						jumpFalseLabel = ".COND_" + cnd + "_FALSE";
					} else jumpFalseLabel = ".COND_" + cnd + "_FINAL_END";
					jumpTrueLabel = ".COND_" + cnd + "_TRUE";
					assembly.append(conditional(((Conditional) statement).condition)).append("\nCMP r10, 0\n\tJE .COND_").append(cond_code).append(((Conditional) statement).onFalse != null ? "_FALSE" : "_FINAL_END").append("\n");
					assembly.append(".COND_").append(cnd).append("_TRUE:\n").append(assemblyInstructions(((Conditional) statement).onTrue, new HashMap<>()));
					if (((Conditional) statement).onFalse != null) {
						assembly.append("\n\tJMP .COND_").append(cnd).append("_FINAL_END\n").append(".COND_").append(cnd).append("_FALSE:\t;FORWARD_JUMP\n").append(assemblyInstructions(((Conditional) statement).onFalse, new HashMap<>()));
					}
					assembly.append(".COND_").append(cnd).append("_FINAL_END:\t;FORWARD JUMP\n");
					prevdec = false;
					break;
				case METHOD_RETURN:
					assembly.append("\tleave\n\tret\n\n");
					prevdec = false;
					break;
			}
			rec_ind = 0;
		}
		StringBuilder asm_vars = new StringBuilder("section .bss\n\tINTERNAL____READ RESB 19\n");
		for (VAR_ var : vars) {
			asm_vars.append("\t").append(var.name).append(" ").append(var.type.asm_type).append(" 1\n");
		}
		asm_vars.append("\tINTERNAL____CACHE RESQ 65536\n");//INTERNAL____CACHE
		assembly = new StringBuilder(asm_vars + "\n\n" + assembly.toString());
		StringBuilder mtds = new StringBuilder();
		for (Method m : methods.values()) {
			ebpoff = 8;
			locvarsize = 0;
			String code = assemblyInstructions(m.body, new HashMap<>());
			mtds.append(m.name).append(":\t;METHOD\n").append("\tpush rbp\n\tmov rbp, rsp\n\tsub rsp, ").append(locvarsize).append("\n").append(code);
		}
		assembly.append(mtds.toString());
		asm_vars = new StringBuilder("\n\nsection .rodata\n\tconst10 dd 10\n\tdigits db 48,49,50,51,52,53,54,55,56,57\n\tnew_line DB 10\n\t___end DB \"Process finished execution and returned code \"\n\t___end_len equ $-___end\n");
		for (VAR_ var : dataVars) {
			asm_vars.append("\t").append(var.name).append(" DB ").append(var.value).append("\n");
		}
		assembly.append(asm_vars);
		assembly = new StringBuilder(assembly.toString().replaceAll("\\t", ""));
	}

	private static String conditional(Token[] conditionTokens) {
		return valueInstructions(conditionTokens);
	}

	private static String assemblyInstructions(Statements statements, Map<String, VAR_> localvars) {
		StringBuilder asm = new StringBuilder();
		boolean prevdec = true;
		Map<String, VAR_> localvars_ = new HashMap<>(localvars);
		for (Statement statement : statements) {
			asm.append("\t;").append(instrind++).append("\n");
			switch (statement.type) {
				case VAR_DECLARE: {
					if (!prevdec)
						throw new ParsingError("Variable \"" + ((VarDeclare_Statement) statement).name + "\" can only be declared at the beginning");
					VAR_ var = new VAR_(((VarDeclare_Statement) statement).name, ((VarDeclare_Statement) statement).type);
					vars.add(var);
					IdentifierToken.identifiers.forEach((IdentifierToken id) -> {
						if (id.var == null && var.name.equals(id.identifier)) {
							id.var = var;
							id.data_type = var.type;
						}
					});
					localvars_.put(var.name, var);
					_LANG_COMPILER.localvars.put(var.name, "rbp - " + ebpoff);
					ebpoff += var.type.bytesize;
					locvarsize += var.type.bytesize;
					break;
				}
				case VAR_UPDATE:
					asm.append(valueInstructions(((VarUpdate_Statement) statement).value));
					String name = ((VarUpdate_Statement) statement).name;
					boolean ptr = false;
					if (name.startsWith("*")) {
						name = name.substring(1);
						ptr = true;
					}
					if (((VarUpdate_Statement) statement).dt == null) {
						DATA_TYPE trg = null;
						for (VAR_ v : vars) {
							if (v.name.equals(name)) {
								trg = v.type;
								break;
							}
						}
						if (trg == null)
							throw new ParsingError("Variable " + name + " does not exist");
						((VarUpdate_Statement) statement).dt = trg;
					}
					if (localvars_.containsKey(name))
						name = _LANG_COMPILER.localvars.get(name);
					if (ptr) {
						asm.append("\tmov r11, ").append(((VarUpdate_Statement) statement).dt.wrdtype).append(" [").append(name).append("]\n\tmov [r11], r10\n");
					} else
						asm.append("\tmov ").append(((VarUpdate_Statement) statement).dt.wrdtype).append(" [").append(name).append("], r10\n");
					break;
				case WHILE_LOOP:
					int a = tg++;
					jumpTrueLabel = ".WHILE_" + a;
					jumpFalseLabel = ".WHILE_" + a + "_END";
					asm.append(conditional(((WhileLoop) statement).conditionTokens));
					asm.append("\tCMP r10, 0\n\tJE .WHILE_").append(a).append("_END\n");
					asm.append(".WHILE_").append(a).append(":\t;BACKWARD JUMP\n").append(assemblyInstructions(new Statements(((WhileLoop) statement).statements.statements), new HashMap<>()));
					asm.append(conditional(((WhileLoop) statement).conditionTokens));
					asm.append("\tCMP r10, 0\n\tJNE .WHILE_").append(a).append("\n");
					asm.append(".WHILE_").append(a).append("_END:\t;FORWARD JUMP\n");
					prevdec = false;
					break;
				case INCREMENT: {
					if (((Increment_Statement) statement).dt == null) {
						DATA_TYPE trg = null;
						for (VAR_ v : vars)
							if (v.name.equals(((Increment_Statement) statement).name)) {
								trg = v.type;
								break;
							}
						if (trg == null)
							throw new ParsingError("Variable \"" + ((Increment_Statement) statement).name + "\" does not exist");
						((Increment_Statement) statement).dt = trg;
					}
					asm.append("\tINC ").append(((Increment_Statement) statement).dt.wrdtype).append("[").append(((Increment_Statement) statement).name).append("]\n");
					prevdec = false;
					break;
				}
				case METHOD_CALL:
					asm.append(((MethodCallStatement) statement).assembly());
					break;
				case CONDITIONAL:
					cond_code++;
					int cnd = cond_code;
					if (((Conditional) statement).onFalse != null) {
						jumpFalseLabel = ".COND_" + cnd + "_FALSE";
					} else jumpFalseLabel = ".COND_" + cnd + "_FINAL_END";
					jumpTrueLabel = ".COND_" + cnd + "_TRUE";
					asm.append(conditional(((Conditional) statement).condition)).append("\nCMP r10, 0\n\tJE .COND_").append(cond_code).append(((Conditional) statement).onFalse != null ? "_FALSE" : "_FINAL_END").append("\n");
					asm.append(".COND_").append(cnd).append("_TRUE:").append(assemblyInstructions(((Conditional) statement).onTrue, localvars_));
					if (((Conditional) statement).onFalse != null) {
						asm.append("\n\tJMP .COND_").append(cnd).append("_FINAL_END\n").append(".COND_").append(cnd).append("_FALSE:\t;FORWARD JUMP\n").append(assemblyInstructions(((Conditional) statement).onFalse, localvars_));
					}
					asm.append(".COND_").append(cnd).append("_FINAL_END:\t;FORWARD JUMP\n");
					prevdec = false;
					break;
				case METHOD_RETURN: {
					asm.append("\tleave\n\tret\n\n");
					prevdec = false;
					break;
				}
			}
			rec_ind = 0;
		}
		return asm.toString();
	}

	public static String valueInstructions(Token[] valueTokens) {
		if (rec_ind == 0) {
			internal_cache_index = 0;
			cache_ptr = 0;
		}
		int depth = rec_ind++;
		boolean constant = true;
		for (int i = 0; i < valueTokens.length && constant; i++)
			constant = !(valueTokens[i] instanceof IdentifierToken || valueTokens[i] instanceof INTERNAL____CACHE_TOKEN || (valueTokens[i] instanceof UnaryOperatorToken && ((UnaryOperatorToken) valueTokens[i]).op == UnaryOperatorToken.OP.DEREFERENCE));
		if (constant) {
			return "mov " + (depth == 0 ? "r10" : "QWORD [INTERNAL____CACHE + " + (8 * (cache_ptr = internal_cache_index++)) + "]") + ", " + Objects.requireNonNull(evaluate(valueTokens)).vi + "\n";
		}

		if (valueTokens.length == 1) {
			return "\tmov r10, " + value(valueTokens[0]) + "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * (cache_ptr = internal_cache_index++)) + "], r10\n";
		} else if (valueTokens.length == 3) {
			return "\tmov r10, " + value(valueTokens[0]) + "\n\tmov r11, " + value(valueTokens[2]) + "\n\t" + ((OperatorToken) valueTokens[1]).asm_code("r10", "r11") + "\n" + (depth == 0 ? "" : "\tmov QWORD [INTERNAL____CACHE + " + (8 * (cache_ptr = internal_cache_index++)) + "], r10\n");
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
					if (d_ != 0) throw new InvalidExpressionException(Arrays.toString(valueTokens));
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
						case LOGIC_AND:
						case LOGIC_OR:
						case LOGIC_XOR:
							Token[] tokens1 = new Token[i];
							Token[] tokens2 = new Token[valueTokens.length - i - 1];
							System.arraycopy(valueTokens, 0, tokens1, 0, i);
							System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
							int a;
							asm += valueInstructions(tokens1);
							a = 8 * cache_ptr;
							if (depth == 0) {
								if (((OperatorToken) valueTokens[i]).mop == OperatorToken.Math_Operator.LOGIC_AND) {
									asm += "\tCMP QWORD [INTERNAL____CACHE + " + a + "], 0\n\tJE " + jumpFalseLabel + "\n";
								} else if (((OperatorToken) valueTokens[i]).mop == OperatorToken.Math_Operator.LOGIC_OR) {
									asm += "\tCMP QWORD [INTERNAL____CACHE + " + a + "], 0\n\tJNE " + jumpTrueLabel + "\n";
								}
							}
							asm += valueInstructions(tokens2);
							asm += "\tmov r10, QWORD [INTERNAL____CACHE + " + a + "]\n";
							asm += "\tmov r11, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n";
							asm += ((OperatorToken) valueTokens[i]).asm_code("r10", "r11");
							if (depth != 0)
								asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "], r10\n";
							return asm;
					}
			}

			for (i = valueTokens.length - 1; i >= 0; i--) {
				if (valueTokens[i] instanceof OperatorToken)
					switch (((OperatorToken) valueTokens[i]).mop) {
						case LOGIC_E:
						case LOGIC_G:
						case LOGIC_GE:
						case LOGIC_NE:
						case LOGIC_S:
						case LOGIC_SE:
							Token[] tokens1 = new Token[i];
							Token[] tokens2 = new Token[valueTokens.length - i - 1];
							System.arraycopy(valueTokens, 0, tokens1, 0, i);
							System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
							int a;
							asm += valueInstructions(tokens1);
							a = 8 * cache_ptr;
							asm += valueInstructions(tokens2);
							asm += "\tmov r10, QWORD [INTERNAL____CACHE + " + a + "]\n";
							asm += "\tmov r11, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n";
							asm += ((OperatorToken) valueTokens[i]).asm_code("r10", "r11");
							if (depth != 0)
								asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "], r10\n";
							return asm;
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
							int a;
							asm += valueInstructions(tokens1);
							a = 8 * cache_ptr;
							asm += valueInstructions(tokens2);
							asm += "\tmov r10, QWORD [INTERNAL____CACHE + " + a + "]\n";
							asm += "\tmov r11, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n";
							asm += ((OperatorToken) valueTokens[i]).asm_code("r10", "r11");
							if (depth != 0)
								asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "], r10\n";
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
							int a;
							asm += valueInstructions(tokens1);
							a = 8 * cache_ptr;
							asm += valueInstructions(tokens2);
							asm += "\tmov r10, QWORD [INTERNAL____CACHE + " + a + "]\n";
							asm += "\tmov r11, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n";
							asm += ((OperatorToken) valueTokens[i]).asm_code("r10", "r11");
							if (depth != 0)
								asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "], r10\n";
							return asm;
					}
			}
			for (i = valueTokens.length - 1; i >= 0; i--) {
				if (valueTokens[i] instanceof OperatorToken)
					switch (((OperatorToken) valueTokens[i]).mop) {
						case SHIFT_LEFT:
						case SHIFT_RIGHT:
						case BITWISE_AND:
						case BITWISE_OR:
						case BITWISE_XOR:
							Token[] tokens1 = new Token[i];
							Token[] tokens2 = new Token[valueTokens.length - i - 1];
							System.arraycopy(valueTokens, 0, tokens1, 0, i);
							System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
							int a;
							asm += valueInstructions(tokens1);
							a = 8 * cache_ptr;
							asm += valueInstructions(tokens2);
							asm += "\tmov r10, QWORD [INTERNAL____CACHE + " + a + "]\n";
							asm += "\tmov r11, QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "]\n";
							asm += ((OperatorToken) valueTokens[i]).asm_code("r10", "r11");
							if (depth != 0)
								asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "], r10\n";
							return asm;
					}
			}
			for (i = valueTokens.length - 1; i >= 0; i--) {
				if (valueTokens[i] instanceof UnaryOperatorToken) {
					Token[] tokens2 = new Token[valueTokens.length - i - 1];
					System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
					int a;
					asm += valueInstructions(tokens2);
					if (((UnaryOperatorToken) valueTokens[i]).op == UnaryOperatorToken.OP.REFERENCE)
						if (!((IdentifierToken) valueTokens[i + 1]).local)
							UnaryOperatorToken.referenceVar = "[" + ((IdentifierToken) valueTokens[i + 1]).identifier + "]";
						else
							UnaryOperatorToken.referenceVar = value(valueTokens[i + 1]);
					asm += ((UnaryOperatorToken) valueTokens[i]).asm_code("r10");
					if (depth != 0)
						asm += "\n\tmov QWORD [INTERNAL____CACHE + " + (8 * cache_ptr) + "], r10\n";
					return asm;
				}
			}
			return "\n";
		}
	}

	private static void compileAssembly() {
		try (FileOutputStream fout = new FileOutputStream(asm_source_file)) {
			fout.write(assembly.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String value(Token token) {
		if (token instanceof NumberToken)
			return Integer.toString(((NumberToken) token).v);
		else if (token instanceof IdentifierToken) {
			IdentifierToken idf = ((IdentifierToken) token);
			if (idf.local) {
				if (idf.identifier.contains(".")) {
					String attr = idf.identifier.substring(idf.identifier.indexOf('.') + 1);
					switch (attr) {
						case "location":
							return Integer.toString(idf.stacklocation);
					}
				}
				if (idf.identifier.startsWith("arg"))
					return "[rbp + " + idf.stacklocation + "]";
				else return "[rbp - " + idf.stacklocation + "]";
			} else
				return idf.data_type.wrdtype + "[" + idf.identifier + "]";
		} else if (token instanceof INTERNAL____CACHE_TOKEN)
			return "QWORD [INTERNAL____CACHE + " + (((INTERNAL____CACHE_TOKEN) token).qwordoffset * 8) + "]";
		else if (token instanceof LogicConstantValueToken)
			return ((LogicConstantValueToken) token).v ? "1" : "0";
		else return "";
	}

	public static void main(String[] args) {
		program_file_name = "update.nsu";
		asm_source_file = "update.asm";
		readProgram();
		tokenizeProgram();
		makeAssembly();
		optimizeAssembly();
		compileAssembly();
	}

	private static void optimizeAssembly() {
		StringBuilder optimized = new StringBuilder();
		List<ASMOP> OPERATIONS = new ArrayList<>();
		boolean isCode = false;
		for (String LINE : assembly.toString().split("\n")) {
			while (LINE.startsWith(" ") || LINE.startsWith("\t"))
				LINE = LINE.substring(1);
			if (isCode) {
				if (LINE.startsWith("section ")) {
					isCode = false;
					optimized.append(LINE).append("\n");
					continue;
				}
				if (!LINE.isEmpty())
					OPERATIONS.add(operation(LINE));
			} else if (!LINE.equals("section .text"))
				optimized.append(LINE).append("\n");
			if (!isCode)
				isCode = LINE.equals("section .text");
		}
		optimized.append("section .text\n");
		for (int i = 0; i < OPERATIONS.size(); i++) {
			ASMOP asmop = OPERATIONS.get(i);
			if (asmop.isLabel) {
				for (String reg : regs.split(" ")) {
					registers_constant.put(reg, false);
				}
			} else if (asmop.arg2 != null && asmop.arg2.value_is_immediate) {
				asmop.arg2.value = cvalue(asmop.arg2.value);
			}
		}
		for (ASMOP op : OPERATIONS) {
			optimized.append(op.OP);
			if (op.arg1 != null) optimized.append(' ').append(op.arg1.value);
			if (op.arg2 != null) optimized.append(", ").append(op.arg2.value);
			optimized.append('\n');
		}

		assembly = optimized;

		{
			StringBuilder asm = new StringBuilder();
			for (String line : assembly.toString().split("\n")) {
				if (!(line.startsWith("\t") || line.contains(":") || line.startsWith("section .")))
					asm.append("\t").append(line).append("\n");
				else if (!line.matches("^\\s*$")) asm.append(line).append("\n");
			}
			assembly = asm;
		}
	}

	private static ASMOP operation(String line) {
		if (!line.startsWith("\t"))
			return new ASMOP(line, null, null);
		String opcode = line.substring(0, line.indexOf(' '));
		String argsfull = line.substring(line.indexOf(' ') + 1);
		String[] args = argsfull.split("\\s*,\\s*");
		if (args.length == 2)
			return new ASMOP(opcode, new OPERAND(args[0]), new OPERAND(args[1]));
		else if (args.length == 1)
			return new ASMOP(opcode, new OPERAND(args[0]), null);
		else return new ASMOP(opcode, null, null);
	}

	private static Statement[] getStatements(String lines, int d) throws TokenException, ParsingError {
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
							i--;
							statements.add(new WhileLoop(conditionTokens_while, new Statements(getStatements(code.toString(), d + 1))));
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
							Method method2 = methods.getOrDefault(methodName, null);
							METHOD method1 = method2 == null ? METHOD.valueOf(methodName) : METHOD.DEFINED_METHOD;

							String argfull = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
							MethodCallStatement mcs;
							if (!argfull.isEmpty()) {
								String[] args;
								if (method1 == METHOD.ASM) {
									args = argfull.split("\"\\s*,\\s*\"");
									args[0] += '"';
									for (int j = 1; j < args.length - 1; j++)
										args[j] = '"' + args[j] + '"';
									args[args.length - 1] = '"' + args[args.length - 1];
								} else {
									args = argfull.split(",");
								}
								Token[][] v = new Token[args.length][];
								for (int j = 0; j < args.length; j++)
									v[j] = tokensOfValue(args[j]);
								mcs = new MethodCallStatement(method1, v);
							} else {
								mcs = new MethodCallStatement(method1);
							}
							mcs.def_m = method2;
							statements.add(mcs);
							break;
						}
						case METHOD_DECLARE: {
							String name = split[i].substring(split[i].indexOf(' ') + 1, split[i].indexOf('(')).toUpperCase().replaceAll(" ", "_");
							methods.put(name, new Method(name, null));
							int j = ++i;
							for (; i < split.length && !split[i].equals("}"); i++)
								;
							String[] lines_arr = new String[i - j];
							if (i - j >= 0) System.arraycopy(split, j, lines_arr, 0, i - j);
							StringBuilder code = new StringBuilder();
							for (String line_ : lines_arr)
								code.append(line_).append("\n");
							methods.get(name).body = new Statements(getStatements(code.toString(), d + 1));
							i--;
							break;
						}
						case METHOD_RETURN: {
							statements.add(new ReturnStatement());
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
		String[] parts = value.split("(\\s)*([(+\\-*%/)]|&&|\\|\\||\\^|==|>=|>|<=|<|!=)(\\s)*");
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
			} else if (value.startsWith("%")) {
				tokens.add(new OperatorToken(OperatorToken.Math_Operator.MODULO));
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
			if (part.matches("^\\*.*$")) {
				tokens.add(new UnaryOperatorToken(UnaryOperatorToken.OP.DEREFERENCE));
				part = part.substring(1);
			} else if (part.matches("^&.*$")) {
				tokens.add(new UnaryOperatorToken(UnaryOperatorToken.OP.REFERENCE));
				part = part.substring(1);
			} else if (part.matches("^~.*$")) {
				tokens.add(new UnaryOperatorToken(UnaryOperatorToken.OP.BITWISE_NOT));
				part = part.substring(1);
			}
			if (part.matches("^\".*\"$")) {
				tokens.add(new StringToken(part.substring(1, part.length() - 1)));
			} else if (part.matches("^\\d+$")) {
				tokens.add(new NumberToken(Integer.parseInt(part)));
			} else if (part.matches("^(true|false)$")) {
				tokens.add(new LogicConstantValueToken(part.equals("true")));
			} else if (part.matches("^___[a-zA-Z0-9]{3}$")) {
				tokens.add(new PayloadToken(part.substring(3, 6)));
			} else if (part.matches("^[a-zA-Z_][a-zA-Z0-9_]*\\(.*\\)$")) {
				String name = part.substring(0, part.indexOf('(')).toUpperCase().replaceAll(" ", "_");
				METHOD method = METHOD.valueOf(name);
				String[] args = part.substring(part.indexOf("("), part.lastIndexOf(')')).split("\\s*,\\s*");
				Token[][] tkns = new Token[args.length][];
				for (int j = 0; j < args.length; j++)
					tkns[j] = tokensOfValue(args[j]);
				tokens.add(new MethodResultToken(method, tkns));
			} else if (part.matches("^arg\\d+\\.[a-zA-Z0-9_]+$")) {
				String[] argp = part.split("\\.");
				tokens.add(new IdentifierToken(part, true, (8 * (Integer.parseInt(argp[0].substring(3)) + 1))));
			} else if (part.matches("^arg\\d+$")) {
				tokens.add(new IdentifierToken(part, true, (8 * (Integer.parseInt(part.substring(3)) + 1))));
			} else if (part.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
				tokens.add(new IdentifierToken(part, null, null));
			}
			value = value.replaceFirst(Pattern.quote(part), "");
			while (value.matches("^\\s+.*$"))
				value = value.substring(1);
		}
		while (value.endsWith(")")) {
			value = value.substring(0, value.length() - 1);
			tokens.add(new ParenthesisClosedToken());
		}
		return tokens.toArray(new Token[0]);
	}

	public static String printIdentifier(Token token) {
		if (token instanceof IdentifierToken) {
			return "\tmov rax, [" + ((IdentifierToken) token).identifier + "]\n\tcall printNumber\n\tcall printNewLine\n";
		} else return "\n";
	}

	public static void addNewRESWVar(String name) {
		vars.add(new VAR_(name, DATA_TYPE.SHORT_INT));
	}

	public static boolean isConstant(String value) {
		if (register.matcher(value).matches() && registers_constant.get(value))
			return true;
		return value.matches("^\\d+$");
	}

	public static String cvalue(String name) {
		if (name.matches("^\\d+$"))
			return name;
		if (register.matcher(name).matches())
			return Integer.toString(registers_values.get(name));
		return null;
	}

	private static Value evaluate(Token[] valueTokens) {
		int i;
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
				Value pv = evaluate(t);
				t = new Token[valueTokens.length - i + j];
				System.arraycopy(valueTokens, 0, t, 0, j);
				System.arraycopy(valueTokens, i + 1, t, j + 1, valueTokens.length - i - 1);
				t[j] = new NumberToken(pv.vi);
				valueTokens = t;
				i = j - 1;
			}
		}

		for (i = valueTokens.length - 1; i >= 0; i--) {
			if (valueTokens[i] instanceof OperatorToken)
				switch (((OperatorToken) valueTokens[i]).mop) {
					case LOGIC_AND:
					case LOGIC_OR:
					case LOGIC_XOR:
						Token[] tokens1 = new Token[i];
						Token[] tokens2 = new Token[valueTokens.length - i - 1];
						System.arraycopy(valueTokens, 0, tokens1, 0, i);
						System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
						Value va = evaluate(tokens1);
						Value vb = evaluate(tokens2);
						return new Value(((OperatorToken) valueTokens[i]).result(va.vi, vb.vi));
				}
		}

		for (i = valueTokens.length - 1; i >= 0; i--) {
			if (valueTokens[i] instanceof OperatorToken)
				switch (((OperatorToken) valueTokens[i]).mop) {
					case LOGIC_E:
					case LOGIC_G:
					case LOGIC_GE:
					case LOGIC_NE:
					case LOGIC_S:
					case LOGIC_SE:
						Token[] tokens1 = new Token[i];
						Token[] tokens2 = new Token[valueTokens.length - i - 1];
						System.arraycopy(valueTokens, 0, tokens1, 0, i);
						System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
						Value va = evaluate(tokens1);
						Value vb = evaluate(tokens2);
						return new Value(((OperatorToken) valueTokens[i]).result(va.vi, vb.vi));
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
						Value va = evaluate(tokens1);
						Value vb = evaluate(tokens2);
						return new Value(((OperatorToken) valueTokens[i]).result(va.vi, vb.vi));
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
						Value va = evaluate(tokens1);
						Value vb = evaluate(tokens2);
						return new Value(((OperatorToken) valueTokens[i]).result(va.vi, vb.vi));
				}
		}
		for (i = valueTokens.length - 1; i >= 0; i--) {
			if (valueTokens[i] instanceof OperatorToken)
				switch (((OperatorToken) valueTokens[i]).mop) {
					case SHIFT_LEFT:
					case SHIFT_RIGHT:
					case BITWISE_AND:
					case BITWISE_OR:
					case BITWISE_XOR:
						Token[] tokens1 = new Token[i];
						Token[] tokens2 = new Token[valueTokens.length - i - 1];
						System.arraycopy(valueTokens, 0, tokens1, 0, i);
						System.arraycopy(valueTokens, i + 1, tokens2, 0, valueTokens.length - i - 1);
						Value va = evaluate(tokens1);
						Value vb = evaluate(tokens2);
						return new Value(((OperatorToken) valueTokens[i]).result(va.vi, vb.vi));
				}
		}
		return valueTokens.length == 0 ? null : new Value(valueTokens[0] instanceof NumberToken ? ((NumberToken) valueTokens[0]).v : (((LogicConstantValueToken) valueTokens[0]).v ? 1 : 0));
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

	public static class VAR_ {
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
}