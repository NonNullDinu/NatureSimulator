#version 430 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 in_normal;
layout(location = 2) in vec3 in_final_color;

layout(location = 0) flat out vec3 out_color;
layout(location = 1) out float out_visibility;

uniform mat4 transformationMatrix;

uniform vec4 clipPlane;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

#Struct_Lib.Light
uniform Light light;

#Struct_Lib.FogValues
uniform FogValues fogValues;

#define COLOR_CHANGE_SPEED 0.001

layout(std430, binding = 0) buffer colors
{
	float cls[];
};

void calculateDiffuse(in vec3 normal, out vec3 diffuse) {
	float dot1 = dot(normalize(-light.direction), normal);
	dot1 = max(dot1, 0.0);
	diffuse = (light.color * light.bias.x) + (dot1 * light.color * light.bias.y);
}

void calculateColor(inout vec3 color, in vec3 normal) {
	vec3 diffuse;
	calculateDiffuse(normal, diffuse);
	color = color * diffuse;
}

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	vec4 posRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * posRelativeToCamera;
	int vertId = gl_VertexID;
	vec3 color = mix(
			vec3(cls[vertId * 3], cls[vertId * 3 + 1], cls[vertId * 3 + 2]),
			in_final_color, COLOR_CHANGE_SPEED);
	out_color = color;
	cls[vertId * 3] = color.x;
	cls[vertId * 3 + 1] = color.y;
	cls[vertId * 3 + 2] = color.z;

	calculateColor(out_color, in_normal);

	float distance = length(posRelativeToCamera.xyz);
	out_visibility = exp(
			-pow((distance * fogValues.density), fogValues.gradient));
	out_visibility = clamp(out_visibility, 0.0, 1.0);
}
