package patch.nsUpdateScript;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class NUSFile {
	public NUSFile(String code) {
		turnToTokens(code);
	}

	private void turnToTokens(String code) {
		String[] lines = code.split("\n");
		String codeToParseLeft = code;
		while (!codeToParseLeft.startsWith("method"))
			codeToParseLeft = codeToParseLeft.substring(1); // Skip to first method declaration
		int index = codeToParseLeft.indexOf(')') + 1;
		List<Token> tokens = new ArrayList<>();
		String tmp = codeToParseLeft.substring(0, index);
		if (MethodDeclarationToken.PATTERN.isStringOfPattern(tmp)) {
			MethodDeclarationToken methodDeclarationToken = MethodDeclarationToken.get(tmp);
			tokens.add(methodDeclarationToken);
		} else {
			index = 0;
		}
		codeToParseLeft = codeToParseLeft.substring(index);
		boolean tmpa, tmpb = false, tmpc = false;
		while ((tmpa = codeToParseLeft.startsWith("\n")) || (tmpb = codeToParseLeft.startsWith("\t")) || (tmpc = codeToParseLeft.startsWith(" "))) {
			if (tmpa)
				codeToParseLeft = codeToParseLeft.replaceFirst("\n", "");
			if (tmpb)
				codeToParseLeft = codeToParseLeft.replaceFirst("\t", "");
			if (tmpc)
				codeToParseLeft = codeToParseLeft.replaceFirst(" ", "");
		}
	}
}