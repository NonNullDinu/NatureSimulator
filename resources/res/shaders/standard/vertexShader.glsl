#version 430 core

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec3 in_normal;
layout(location = 2) in vec2 in_texCoords;
layout(location = 3) in vec3 color;
layout(location = 4) in vec3 colorsIndicators;
layout(location = 5) in vec4 in_materialData;

layout(location = 0) out vec4 out_cc;
layout(location = 1) out vec2 out_texCoords;
layout(location = 2) out float out_visibility;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

#Struct_Lib.Light

uniform Light light;
uniform vec4 clipPlane;

uniform vec3 customColors[3];

#Struct_Lib.FogValues

uniform FogValues fogValues;

uniform float time;

const float windLength = 20.0;
const float amplitude = 0.8;

const float PI = 3.1415926535897932384626433832795;

vec3 calculateDiffuse(vec3 normal) {
	float dot1 = max(dot(-normalize(light.direction), normal), 0.0);
	return (light.color * light.bias.x) + (dot1 * light.color * light.bias.y);
}

vec3 customColor(vec3 normal) {
	vec3 f;
	if (colorsIndicators.z == 1)
		f = vec3(1, 0, 0);
	else if (colorsIndicators.z == 2)
		f = vec3(0, 1, 0);
	else if (colorsIndicators.z == 3)
		f = vec3(1, 1, 0);
	else if (colorsIndicators.z == 4)
		f = vec3(0, 0, 1);
	else if (colorsIndicators.z == 5)
		f = vec3(1, 0, 1);
	else if (colorsIndicators.z == 6)
		f = vec3(0, 1, 1);
	else if (colorsIndicators.z == 7)
		f = vec3(1, 1, 1);
	int indc = int(colorsIndicators.x - 1);
	return calculateDiffuse(normal)
			* (indc < 0 ? color : (customColors[indc] + colorsIndicators.y * f));
}

float generateOffset(float x, float z, float val1, float val2) {
	float radiansX = ((mod(x + z * x * val1, windLength) / windLength)
			+ time * mod(x * 0.8 + z, 1.5)) * 2.0 * PI;
	float radiansZ = ((mod(val2 * (z * x + x * z), windLength) / windLength)
			+ time * 2.0 * mod(x, 2.0)) * 2.0 * PI;
	return amplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
}

vec3 applyDistortion(vec3 vertex, vec3 normal) {
	float xDistortion = generateOffset(vertex.x, vertex.y, 0.2, 0.1);
	float yDistortion = generateOffset(vertex.x, vertex.z, 0.1, 0.3);
	float zDistortion = generateOffset(vertex.x, vertex.y, 0.15, 0.2);
	return vertex
			+ mix(normal, vec3(xDistortion, yDistortion, zDistortion), 0.4)
					* in_materialData.x;
}

vec4 calculatePosition(vec3 normal) {
	vec4 pos = vec4(in_position, 1.0);
//	pos += time * vec4(in_normal, 0.0) * in_materialData.x;
	pos = transformationMatrix * pos;
	vec3 p = applyDistortion(pos.xyz, normal);
	pos.xyz = p;
	return pos;
}

void main(void) {
	vec3 normal = normalize(in_normal);
	normal = (transformationMatrix * vec4(normal, 0.0)).xyz;

	vec4 worldPosition = calculatePosition(normal);
	vec4 posRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * posRelativeToCamera;
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	out_texCoords = in_texCoords;
	out_cc = vec4(customColor(normal), (colorsIndicators.z == -1 ? 0 : 1));
	float distance = length(posRelativeToCamera.xyz);
	out_visibility = exp(
			-pow((distance * fogValues.density), fogValues.gradient));
	out_visibility = clamp(out_visibility, 0.0, 1.0);
}
