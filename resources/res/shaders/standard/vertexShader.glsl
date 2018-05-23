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

vec4 calculatePosition() {
	vec4 pos = vec4(in_position, 1.0);
	pos += time * vec4(in_normal, 0.0) * in_materialData.x;
	return pos;
}

void main(void) {
	vec4 worldPosition = transformationMatrix * calculatePosition();
	vec4 posRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * posRelativeToCamera;
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	out_texCoords = in_texCoords;
	vec3 normal = normalize(in_normal);
	normal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	out_cc = vec4(customColor(normal), (colorsIndicators.z == -1 ? 0 : 1));
	float distance = length(posRelativeToCamera.xyz);
	out_visibility = exp(
			-pow((distance * fogValues.density), fogValues.gradient));
	out_visibility = clamp(out_visibility, 0.0, 1.0);
}
