package ns.shaders;

public final class UniformLocator {
	private ShaderProgram program;

	public UniformLocator(ShaderProgram program) {
		this.program = program;
	}
	
	public UniformFloat locateUniformFloat(String name) {
		return (UniformFloat) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}
	
	public UniformVec2 locateUniformVec2(String name) {
		return (UniformVec2) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}
	
	public UniformVec3 locateUniformVec3(String name) {
		return (UniformVec3) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}
	
	public UniformVec4 locateUniformVec4(String name) {
		return (UniformVec4) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}
	
	public UniformMat4 locateUniformMat4(String name) {
		return (UniformMat4) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}
	
	public UniformInt locateUniformInt(String name) {
		return (UniformInt) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}
	
	public UniformBool locateUniformBool(String name) {
		return (UniformBool) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}

	public UniformSampler2D locateUniformSampler2D(String name) {
		return (UniformSampler2D) UniformVar.createVar(program.variableType(name), program.getLocation(name));
	}
	
	public UniformFloat locateUniformFloat(String name, boolean typeCheck) {
		return (UniformFloat) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_FLOAT), program.getLocation(name));
	}
	
	public UniformVec2 locateUniformVec2(String name, boolean typeCheck) {
		return (UniformVec2) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_VEC2), program.getLocation(name));
	}
	
	public UniformVec3 locateUniformVec3(String name, boolean typeCheck) {
		return (UniformVec3) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_VEC3), program.getLocation(name));
	}
	
	public UniformVec4 locateUniformVec4(String name, boolean typeCheck) {
		return (UniformVec4) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_VEC4), program.getLocation(name));
	}
	
	public UniformMat4 locateUniformMat4(String name, boolean typeCheck) {
		return (UniformMat4) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_MAT4), program.getLocation(name));
	}
	
	public UniformInt locateUniformInt(String name, boolean typeCheck) {
		return (UniformInt) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_INT), program.getLocation(name));
	}
	
	public UniformBool locateUniformBool(String name, boolean typeCheck) {
		return (UniformBool) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_BOOL), program.getLocation(name));
	}

	public UniformSampler2D locateUniformSampler2D(String name, boolean typeCheck) {
		return (UniformSampler2D) UniformVar.createVar((typeCheck ? program.variableType(name) : UniformVar.TYPE_SAMPLER_2D), program.getLocation(name));
	}
}
