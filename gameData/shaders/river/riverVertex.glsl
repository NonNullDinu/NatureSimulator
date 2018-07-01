#version 430 core

layout(location = 0) in vec3 in_position1;
layout(location = 1) in vec3 in_position2;
layout(location = 2) in vec3 in_position3;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform int loc;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix
			* vec4(
					(loc == 0) ?
							(in_position1) :
							((loc == 1) ? in_position2 : in_position3), 1.0);
}
