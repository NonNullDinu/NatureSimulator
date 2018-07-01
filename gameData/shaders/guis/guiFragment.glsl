#version 430 core

layout(location = 0) in vec2 in_texCoords;

out vec4 frag_Color;

uniform sampler2D tex;

const vec3 transparency = vec3(1.0, 4.0 / 255.0, 214.0 / 255.0);

void main(void){
	frag_Color = texture(tex, in_texCoords);
	if(frag_Color.a < 0.5)
		discard;
	if(dot(frag_Color.xyz * 2.0 - 1.0, transparency * 2.0 - 1.0) >= 0.9999999)
		discard;
}
