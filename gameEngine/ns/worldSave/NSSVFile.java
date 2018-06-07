package ns.worldSave;

import java.io.InputStream;

import ns.customFileFormat.File;
import ns.exceptions.LoadingException;
import ns.world.World;
import res.Resource;

public abstract class NSSVFile implements File {
	private Resource resource;
	
	public NSSVFile(Resource resource) {
		this.resource = resource;
	}

	@Override
	public World load() throws LoadingException{
		return load(resource.asInputStream());
	}
	
	protected abstract World load(InputStream ins);
}