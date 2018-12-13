package resources;

import ns.utils.GU;

import java.io.File;
import java.nio.file.Paths;

class _File extends File {
	private static final long serialVersionUID = 8237978443107895929L;
	public final String locGiven;

	public _File(String loc) {
		super((Paths.get(loc).isAbsolute() ? "" : GU.path) + loc);
		locGiven = loc;
	}
}
