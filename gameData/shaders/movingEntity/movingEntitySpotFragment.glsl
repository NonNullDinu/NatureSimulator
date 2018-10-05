#version 420

in vec2 texCoord;

out vec4 out_Color;

uniform sampler2D tex;

void main(void){
	out_Color = texture(tex, texCoord);
	if(out_Color.a < 0.5)
		discard;
}
