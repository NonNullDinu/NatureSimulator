#version 430

in vec2 pass_textureCoords;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;
uniform float alpha;

void main(void){
	out_color = vec4(color, texture(fontAtlas, pass_textureCoords).a * alpha);
}
