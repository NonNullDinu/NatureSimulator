#version 430 core

in vec2 out_texCoords[11];

out vec4 frag_Color;

uniform sampler2D tex;

void main(void) {
	vec4 color = vec4(0.0);
	color += texture(tex, out_texCoords[0]) * 0.0093;
	color += texture(tex, out_texCoords[1]) * 0.028002;
	color += texture(tex, out_texCoords[2]) * 0.065984;
	color += texture(tex, out_texCoords[3]) * 0.121703;
	color += texture(tex, out_texCoords[4]) * 0.175713;
	color += texture(tex, out_texCoords[5]) * 0.198596;
	color += texture(tex, out_texCoords[6]) * 0.175713;
	color += texture(tex, out_texCoords[7]) * 0.121703;
	color += texture(tex, out_texCoords[8]) * 0.065984;
	color += texture(tex, out_texCoords[9]) * 0.028002;
	color += texture(tex, out_texCoords[10]) * 0.0093;
	frag_Color = color;
}
