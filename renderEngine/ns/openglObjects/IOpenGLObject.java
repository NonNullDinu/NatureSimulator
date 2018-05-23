package ns.openglObjects;

public interface IOpenGLObject {
	public abstract IOpenGLObject create();

	public abstract void delete();

	public abstract int getID();
}