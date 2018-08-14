package resources;

import ns.utils.GU;

import java.io.File;

public class _File extends File {
	public _File(String loc) {
		super(GU.path + loc);
	}
}
