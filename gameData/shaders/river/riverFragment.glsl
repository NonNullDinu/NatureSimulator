#version 430 core

in float out_visibility;

out vec4 frag_Color;

uniform vec3 color;
uniform vec3 skyColor;

void main(void){
	frag_Color = mix(vec4(skyColor, 0.8), vec4(color, 0.8), out_visibility);
}
