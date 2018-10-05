#version 430 core

const float PI = 3.1415926535897932384626433832795;

const float waveLength = 20.0;
const float waveAmplitude = 0.8;
const float specularReflectivity = 0.8;
const float shineDamper = 20.0;

layout(location = 0) in vec2 in_position;
layout(location = 1) in vec4 in_indicators;

layout(location = 0) out vec4 pass_clipSpaceGrid;
layout(location = 1) out vec4 pass_clipSpaceReal;
layout(location = 2) flat out vec3 pass_normal;
layout(location = 3) out vec3 pass_toCameraVector;
layout(location = 4) flat out vec3 pass_specular;
layout(location = 5) flat out vec3 pass_diffuse;
layout(location = 6) out float out_visibility;

uniform vec3 cameraPos;
uniform float waveTime;
uniform float one;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

#Struct_Lib.Light

uniform Light sun;
uniform Light moon;

#Struct_Lib.FogValues

uniform FogValues fogValues;

vec3 calcSpecularLighting(vec3 toCamVector, vec3 toLightVector, vec3 normal, vec3 toLightVector2) {
	vec3 reflectedLightDirection = reflect(-toLightVector, normal);
	float specularFactor = dot(reflectedLightDirection, toCamVector);
	specularFactor = max(specularFactor, 0.0);
	specularFactor = pow(specularFactor, shineDamper);
	vec3 reflectedLightDirection2 = reflect(-toLightVector2, normal);
	float specularFactor2 = dot(reflectedLightDirection2, toCamVector);
	specularFactor2 = max(specularFactor2, 0.0);
	specularFactor2 = pow(specularFactor2, shineDamper * 4.0);
	return specularFactor * specularReflectivity * sun.color + specularFactor2 * specularReflectivity * moon.color;
}

vec3 calculateDiffuseLighting(vec3 toLightVector, vec3 normal, vec3 toLightVector2) {
	float brightness = max(dot(toLightVector, normal), 0.0);
	float brightness2 = max(dot(toLightVector2, normal), 0.0);
	return (sun.color * sun.bias.x)
			+ (brightness * sun.color * sun.bias.y)
			+ (moon.color * moon.bias.x)
			+ (brightness2 * moon.color * moon.bias.y);
}

vec3 calcNormal(vec3 vertex0, vec3 vertex1, vec3 vertex2) {
	vec3 tangent = vertex1 - vertex0;
	vec3 bitangent = vertex2 - vertex0;
	return normalize(cross(tangent, bitangent));
}

float generateOffset(float x, float z, float val1, float val2) {
	float radiansX = ((mod(x + z * x * val1, waveLength) / waveLength)
			+ waveTime * mod(x * 0.8 + z, 1.5)) * 2.0 * PI;
	float radiansZ = ((mod(val2 * (z * x + x * z), waveLength) / waveLength)
			+ waveTime * 2.0 * mod(x, 2.0)) * 2.0 * PI;
	return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
}

//vec3 applyDistortion(vec3 vertex) {
//	float xDistortion = generateOffset(vertex.x, vertex.z, 0.2, 0.1);
//	float yDistortion = generateOffset(vertex.x, vertex.z, 0.1, 0.3);
//	float zDistortion = generateOffset(vertex.x, vertex.z, 0.15, 0.2);
//	return vertex + vec3(xDistortion, yDistortion, zDistortion);
//}

vec3 applyDistortion(vec3 vertex) {
	float xDistortion = generateOffset(vertex.x, vertex.z, 0.2, 0.1);
	float yDistortion = generateOffset(vertex.x, vertex.z, 0.1, 0.3);
	float zDistortion = generateOffset(vertex.x, vertex.z, 0.15, 0.2);
	xDistortion = 5.0 * xDistortion;
	yDistortion = yDistortion;
	zDistortion = 5.0 * zDistortion;
	return vertex + vec3(xDistortion, yDistortion, zDistortion);
}

void main(void) {
	vec3 currentVertex = vec3(in_position.x, 0.0, in_position.y);
	vec3 vertex1 = currentVertex
			+ vec3(in_indicators.x * one, 0.0, in_indicators.y * one);
	vec3 vertex2 = currentVertex
			+ vec3(in_indicators.z * one, 0.0, in_indicators.w * one);

	pass_clipSpaceGrid = projectionMatrix * viewMatrix
			* vec4(currentVertex, 1.0);

	currentVertex = applyDistortion(currentVertex);
	vertex1 = applyDistortion(vertex1);
	vertex2 = applyDistortion(vertex2);

	pass_normal = calcNormal(currentVertex, vertex1, vertex2);

	vec4 posRelativeToCamera = viewMatrix * vec4(currentVertex, 1.0);

	pass_clipSpaceReal = projectionMatrix * posRelativeToCamera;
	gl_Position = pass_clipSpaceReal;

	pass_toCameraVector = normalize(cameraPos - currentVertex);

	vec3 toLightVector = -normalize(sun.direction);
	vec3 toLightVector2 = -normalize(moon.direction);
	pass_specular = calcSpecularLighting(pass_toCameraVector, toLightVector,
			pass_normal, toLightVector2);
	pass_diffuse = calculateDiffuseLighting(toLightVector, pass_normal, toLightVector2);

	float distance = length(posRelativeToCamera.xyz);
	out_visibility = exp(
			-pow((distance * fogValues.density), fogValues.gradient));
	out_visibility = clamp(out_visibility, 0.0, 1.0);
}
