package patch;

import patch.nsUpdateScript.LANG_DEF;
import patch.nsUpdateScript.Method;

import java.util.HashMap;
import java.util.Map;

public class LineByLineNUS {
	private String code;
	private Map<String, String> variables;

	public static void main(String[] args) {
		LineByLineNUS nus = new LineByLineNUS(
				"VAR_SET ab=nl\n" +
						"VAR_SET ca=n\n" +
						"CONDITION \"#ca\"l==\"#ab\"\n" +
						"ON_CONDITION NUS_CALL rm([a,b,c])\n" +
						"ON_NOT_CONDITION NUS_CALL rm([a,d])");
		nus.run();
	}

	public LineByLineNUS(String code) {
		this.code = code;
		this.variables = new HashMap<>();
	}

	public void run() {
		boolean condition = false;
		for (String line : code.split("\n")) {
			if (line.startsWith("CONDITION "))
				condition = evaluateCondition(line.substring(10));
			else if (line.startsWith("ON_CONDITION ")) {
				if (condition)
					execute(line.substring(13));
			} else if (line.startsWith("ON_NOT_CONDITION ")) {
				if (!condition)
					execute(line.substring(17));
			} else if (line.startsWith("VAR_SET ")) {
				int tmpint;
				String variableName = line.substring(8, (tmpint = line.indexOf('=')));
				String value = line.substring(tmpint + 1);
				for (String varName : variables.keySet())
					value = value.replaceAll("\"#" + varName + "\"", variables.get(varName));
				System.out.println(variableName + ":" + value);
				variables.put(variableName, value);
			} else
				execute(line);
		}
	}

	public void execute(String line) {
		if (line.startsWith("NUS_CALL ")) {
			String methodCalledName = line.substring(9, line.indexOf('('));
			Method methodToCall = LANG_DEF.NUS_METHODS.get(methodCalledName);
			String[] methodArgs = line.substring(line.indexOf("([") + 2, line.lastIndexOf("])")).split(",");
			for (int i = 0; i < methodArgs.length; i++) {
				while (methodArgs[i].startsWith(" ") || methodArgs[i].startsWith("\t"))
					methodArgs[i] = methodArgs[i].substring(1);
				while (methodArgs[i].endsWith(" ") || methodArgs[i].endsWith("\t"))
					methodArgs[i] = methodArgs[i].substring(0, methodArgs[i].length() - 1);
			}
			methodToCall.execute(methodArgs);
		} else if (!line.isEmpty()) {
			System.err.println("Command not found: " + line);
		}
	}

	public boolean evaluateCondition(String condition) {
		if (condition.contains("==")) {
			String[] parts = condition.split("==");
			for (String varName : variables.keySet()) {
				String varValue = variables.get(varName);
				parts[0] = parts[0].replaceAll("\"#" + varName + "\"", varValue);
				parts[1] = parts[1].replaceAll("\"#" + varName + "\"", varValue);
			}
			System.out.println(parts[0] + ":" + parts[1]);
			return parts[0].equals(parts[1]);
		} else return false;
	}
}
