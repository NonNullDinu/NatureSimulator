#version 430 core

layout(location = 0) flat in vec3 in_color;
layout(location = 1) in float in_visibility;

out vec4 frag_Color;

uniform vec3 skyColor;

void main(void){
	frag_Color = vec4(in_color, 1.0);
	frag_Color = mix(vec4(skyColor, 1.0), frag_Color, in_visibility);
}
