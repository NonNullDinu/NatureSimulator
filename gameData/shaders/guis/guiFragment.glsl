#version 430 core

layout(location = 0) in vec2 in_texCoords;

out vec4 frag_Color;

uniform sampler2D tex;

void main(void){
	frag_Color = texture(tex, in_texCoords);
	if(frag_Color.a < 0.5)
		discard;
	if(dot(normalize(frag_Color.xyz * 2.0 - 1.0), normalize(vec3(1.0, 0.016, 0.839) * 2.0 - 1.0)) >= 0.99999)
		discard;
}
