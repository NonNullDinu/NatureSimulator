package ns.openglObjects;

public interface IOpenGLObject {
	IOpenGLObject create();

	void delete();

	int getID();

	boolean isCreated();
}