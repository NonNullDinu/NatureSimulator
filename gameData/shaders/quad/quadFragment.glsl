#version 430 core

in vec2 texCoord;

out vec4 frag_Color;

uniform vec3 color;
uniform int config;
uniform sampler2D tex;
uniform float multFactor;

void main(void) {
	if (config == 1)
		frag_Color = vec4(color, 1.0);
	else {
		frag_Color = texture(tex, texCoord);
		if (config == 3)
			frag_Color = mix(vec4(color, 1.0), frag_Color, frag_Color.a);
		if (config == 4 && frag_Color.a < 0.1)
			discard;
	}
	frag_Color.xyz *= multFactor;
}
