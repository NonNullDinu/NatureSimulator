#version 430 core

layout(location = 0) in vec3 in_position;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(in_position, 1.0);
	gl_Position = projectionMatrix * worldPosition;
}
