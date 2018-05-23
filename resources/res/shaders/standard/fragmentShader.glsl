#version 430 core

layout(location = 0) in vec4 in_cc;
layout(location = 1) in vec2 in_texCoords;
layout(location = 2) in float in_visibility;

out vec4 frag_Color;

uniform sampler2D tex;
uniform vec3 skyColor;

void main(void)
{
	frag_Color = (in_cc.w == 0 ? texture(tex, in_texCoords) : vec4(in_cc.xyz, 1.0));
	frag_Color = mix(vec4(skyColor, 1.0), frag_Color, in_visibility);
}
