#version 430 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 in_normal;
layout(location = 2) in vec3 final_color;

layout(location = 0) flat out vec3 out_color;
layout(location = 1) out float out_visibility;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;

#Struct_Lib.Light

uniform Light light;
uniform vec4 clipPlane;

#Struct_Lib.FogValues

uniform FogValues fogValues;

layout(std430, binding = 0) buffer colors
{
	float cls[];
};

vec3 calculateDiffuse() {
	float dot1 = dot(normalize(-light.direction), in_normal);
	dot1 = max(dot1, 0.0);
	return (light.color * light.bias.x) + (dot1 * light.color * light.bias.y);
}

vec3 calculateColor(vec3 color) {
	return color * calculateDiffuse();
}

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 posRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * posRelativeToCamera;
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	vec3 color = mix(
			vec3(cls[gl_VertexID * 3], cls[gl_VertexID * 3 + 1],
					cls[gl_VertexID * 3 + 2]), final_color, 0.0015);
	out_color = color;
	cls[gl_VertexID * 3] = color.x;
	cls[gl_VertexID * 3 + 1] = color.y;
	cls[gl_VertexID * 3 + 2] = color.z;

	out_color = calculateColor(out_color);

	float distance = length(posRelativeToCamera.xyz);
	out_visibility = exp(
			-pow((distance * fogValues.density), fogValues.gradient));
	out_visibility = clamp(out_visibility, 0.0, 1.0);
}
