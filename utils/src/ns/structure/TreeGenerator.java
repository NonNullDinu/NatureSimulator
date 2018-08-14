package ns.structure;

import java.io.File;

public class TreeGenerator {
	public static void main(String[] args) {
		File f = new File("../gameData");
		String s = "<root_tree>\n";
		for (File fl : f.listFiles())
			s = add(fl, s);
		s += "</root_tree>";
		System.out.println(s);
	}

	private static String add(File f, String s) {
		if (f.isDirectory()) {
			s += "<folder name=\"" + f.getName() + "\">";
			for (File fl : f.listFiles())
				s = add(fl, s);
			s += "</folder>";
		} else {
			System.out.println(f.getPath());
			int p = f.getName().indexOf('.');
			s += "<file name=\"" + f.getName().substring(0, p) + "\" extension=\"" + f.getName().substring(p) + "\"/>";
		}
		return s;
	}
}
