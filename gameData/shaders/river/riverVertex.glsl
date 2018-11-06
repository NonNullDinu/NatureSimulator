#version 430 core

layout(location = 0) in vec3 in_position1;
layout(location = 1) in vec3 in_position2;
layout(location = 2) in vec3 in_position3;

out float out_visibility;
out float y;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform int loc;

#Struct_Lib.FogValues

uniform FogValues fogValues;

void main(void) {
	vec4 posRelToCamera = viewMatrix * vec4((loc == 0) ? (in_position1) : ((loc == 1) ? in_position2 : in_position3), 1.0);
	gl_Position = projectionMatrix * posRelToCamera;
	y = ((loc == 0) ? (in_position1) : ((loc == 1) ? in_position2 : in_position3)).y;

	float distance = length(posRelToCamera.xyz);
	out_visibility = exp(
			-pow((distance * fogValues.density), fogValues.gradient));
	out_visibility = clamp(out_visibility, 0.0, 1.0);
}
