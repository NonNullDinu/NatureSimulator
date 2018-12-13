package ns.worldSave;

import ns.customFileFormat.File;
import ns.exceptions.LoadingException;
import ns.world.World;
import resources.In;

import java.io.InputStream;

public abstract class NSSVFile implements File {
	private final In resource;

	public NSSVFile(In resource) {
		this.resource = resource;
	}

	@Override
	public World load() throws LoadingException {
		return load(resource.asInputStream());
	}

	protected abstract World load(InputStream ins);
}