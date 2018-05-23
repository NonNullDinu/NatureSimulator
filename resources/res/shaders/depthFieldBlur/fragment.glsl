#version 430 core

layout(location = 0) in vec2 in_texCoords;

out vec4 frag_Color;

uniform sampler2D colorTexture;
uniform sampler2D depthTexture;
uniform sampler2D bluredTexture;

uniform vec2 nearFarPlanes;

float toLinearDepth(float zDepth) {
	float near = nearFarPlanes.x;
	float far = nearFarPlanes.y;
	return 2.0 * near * far / (far + near - (2.0 * zDepth - 1.0) * (far - near));
}

vec4 blurColor() {
	vec4 color = texture(bluredTexture, in_texCoords);
	return color;
}

void main(void) {
	vec4 color = texture(colorTexture, in_texCoords);
	float depth = toLinearDepth(texture(depthTexture, in_texCoords).r);
	vec4 trueColor = color;
	if (depth > 0.1) {
		trueColor = blurColor();
	}
	frag_Color = trueColor;
//	frag_Color = vec4(texture(depthTexture, in_texCoords).r);
}
