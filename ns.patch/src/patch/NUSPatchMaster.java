package patch;

public class NUSPatchMaster {
	public static void main(String[] args) {
		NUSFile nusFile = new NUSFile(
				"method main()\n" +
						"{\n\tNUS_CALL rm(a.txt, b.txt, test.txt)\n}");
		nusFile.splitCode();
		nusFile.executeScript();
	}
}