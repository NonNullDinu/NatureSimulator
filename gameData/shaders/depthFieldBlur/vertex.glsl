#version 430 core

layout (location = 0) in vec2 in_position;

layout (location = 0) out vec2 out_texCoords;

void main(void){
	gl_Position = vec4(in_position, 0.0, 1.0);
	out_texCoords = in_position / 2.0 + 0.5;
}
