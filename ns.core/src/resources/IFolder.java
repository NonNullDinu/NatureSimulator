package resources;

public interface IFolder {
	In _getResourceAt(String location);

	In _getResourceAt(String location, boolean version);
}