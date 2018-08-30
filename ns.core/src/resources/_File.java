package resources;

import ns.utils.GU;

import java.io.File;
import java.nio.file.Paths;

public class _File extends File {
	public final String locGiven;

	public _File(String loc) {
		super((Paths.get(loc).isAbsolute() ? "" : GU.path) + loc);
		locGiven = loc;
	}
}
