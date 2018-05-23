package ns.components;

public abstract interface IComponent {
	public static final int BIOME_SPREAD = Blueprint.BIOME_SPREAD;

	public static IComponent create(int id) {
		switch (id) {
		case BIOME_SPREAD:
			return new BiomeSpreadComponent();
		default:
			return null;
		}
	}
}