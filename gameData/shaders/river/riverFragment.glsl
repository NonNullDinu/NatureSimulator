#version 430 core

out vec4 frag_Color;

uniform vec3 color;

void main(void){
	frag_Color = vec4(color, 0.8);
}
