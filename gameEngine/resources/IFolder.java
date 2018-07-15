package resources;

public interface IFolder {
	public Resource _getResourceAt(String location);

	public Resource _getResourceAt(String location, boolean version);
}