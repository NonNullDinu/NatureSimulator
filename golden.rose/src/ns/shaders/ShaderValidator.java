package ns.shaders;

import ns.utils.GU;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.util.ArrayList;
import java.util.List;

class ShaderValidator {
	private List<String> vs_outputs;

	private List<String> fs_inputs;

	private boolean hasGeometryShader = false;

	private List<String> geom_inputs;
	private List<String> geom_outputs;

	private String error;
	private List<Integer> vs_layout_defined_outputs;
	private List<Integer> fs_layout_defined_inputs;

	public boolean validate(Shader... shaderStages) {
		for (int i = 0; i < shaderStages.length; i++)
			if (!validate(i == 0 ? "vertex_shader" : (i == 1 ? "fragment_shader" : "geometry_shader"), shaderStages[i]))
				return false;
		return figure_bindings();
	}

	private boolean validate(String type, Shader shaderStage) {
		String src = shaderStage.getSource();
		if (type.equals("vertex_shader") && shaderStage.type == GL20.GL_VERTEX_SHADER) {
			List<String> vs_inputs = new ArrayList<>();
			vs_outputs = new ArrayList<>();
			vs_layout_defined_outputs = new ArrayList<>();
			String[] srclines = src.split("\n");
			for (String s : srclines) {
				if (s.startsWith("in"))
					vs_inputs.add(s.substring(3, s.length() - 1));
				if (s.contains("out ")) {
					if (s.startsWith("out "))
						vs_outputs.add(s.substring(4, s.length() - 1));
					else if (s.startsWith("layout") && s.substring(6, s.lastIndexOf(' ')).contains("out")) {
						int loc = Integer.parseInt(s.substring(s.indexOf('=') + 1, s.indexOf(')')).replaceAll(" ", ""));
						vs_layout_defined_outputs.add(loc);
					}
				}
			}
			return true;
		} else if (type.equals("fragment_shader") && shaderStage.type == GL20.GL_FRAGMENT_SHADER) {
			fs_inputs = new ArrayList<>();
			List<String> fs_outputs = new ArrayList<>();
			fs_layout_defined_inputs = new ArrayList<>();
			String[] srclines = src.split("\n");
			for (String s : srclines) {
				if (s.contains("in")) {
					if (s.startsWith("in"))
						fs_inputs.add(s.substring(3, s.length() - 1));
					else if (s.startsWith("layout") && s.substring(6, s.lastIndexOf(' ')).contains("in")) {
						int loc = Integer.parseInt(s.substring(s.indexOf('=') + 1, s.indexOf(')')).replaceAll(" ", ""));
						fs_layout_defined_inputs.add(loc);
					}
				}
				if (s.startsWith("out"))
					fs_outputs.add(s.substring(4, s.length() - 1));
			}
			return true;
		} else if (type.equals("geometry_shader") && shaderStage.type == GL32.GL_GEOMETRY_SHADER) {
			hasGeometryShader = true;
			geom_inputs = new ArrayList<>();
			geom_outputs = new ArrayList<>();
			String[] srclines = src.split("\n");
			for (String s : srclines) {
				if (s.startsWith("in"))
					geom_inputs.add(s.substring(3, s.length() - 1));
				if (s.startsWith("out"))
					geom_outputs.add(s.substring(4, s.length() - 1));
			}
			return true;
		} else {
			error = "shader type not matching(" + type + " " + GU.getGL20Type(shaderStage.type) + ")";
			return false;
		}
	}

	private boolean figure_bindings() {
		for (int i = 0; i < vs_outputs.size(); i++) {
			String vs_output = vs_outputs.get(i);
			if (hasGeometryShader && geom_inputs.contains(vs_output)) {
				geom_inputs.remove(vs_output);
				vs_outputs.remove(i);
				i--;
			} else if (fs_inputs.contains(vs_output)) {
				fs_inputs.remove(vs_output);
				vs_outputs.remove(i);
				i--;
			} else {
				error = "Could not find vertex shader output " + vs_output + "'s counterpart in " + (hasGeometryShader ? "other pipeline stages" : "the fragment shader");
				return false;
			}
		}
		for (int i = 0; i < vs_layout_defined_outputs.size(); i++) {
			int loc = vs_layout_defined_outputs.get(i);
			if (fs_layout_defined_inputs.contains(loc)) {
				vs_layout_defined_outputs.remove(i);
				fs_layout_defined_inputs.remove((Object) loc);
				i--;
			} else {
				error =
						"Could not find vertex shader output at location " + loc + "'s counterpart in " + (hasGeometryShader ? "other pipeline stages" : "the fragment shader");
				return false;
			}
		}
		if (hasGeometryShader)
			for (int i = 0; i < geom_outputs.size(); i++) {
				String geom_output = geom_outputs.get(i);
				if (fs_inputs.contains(geom_output)) {
					fs_inputs.remove(geom_output);
					geom_outputs.remove(i);
					i--;
				} else {
					error = "Could not find geometry shader output " + geom_output + "'s counterpart in the fragment shader";
					return false;
				}
			}

		return vs_layout_defined_outputs.size() == 0 && fs_layout_defined_inputs.size() == 0;
	}

	public String getError() {
		return error;
	}
}