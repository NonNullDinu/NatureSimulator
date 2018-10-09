package patch;

@Deprecated
class NUSPatchMaster {
	public static void main(String[] args) {
		NUSFile nusFile = new NUSFile( // This is just a test
				"method main()\n{\n\tif [[ 1==1 ]] ; \n\tthen NUS_CALL rm(a.txt, b.txt, test.txt)\n\telse NUS_CALL rm(fls.txt)\n\tfi\n}");
		nusFile.splitCode();
		nusFile.executeScript();
	}
}