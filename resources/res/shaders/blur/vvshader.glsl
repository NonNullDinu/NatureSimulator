#version 430 core

layout(location = 0) in vec2 in_position;

out vec2 out_texCoords[11];

uniform vec2 size;

void main(void) {
	gl_Position = vec4(in_position, 0.0, 1.0);
	vec2 ctexCoords = in_position / 2.0 + 0.5;
	float ps = 1.0 / size.y;
	for (int i = -5; i <= 5; i++) {
		out_texCoords[i + 5] = ctexCoords + vec2(0.0, i * ps);
	}
}
