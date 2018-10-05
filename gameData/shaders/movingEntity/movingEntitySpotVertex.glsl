#version 420

layout (location = 0) in vec2 position;

out vec2 texCoord;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float y[9];

int getIndex(vec2 position){
	float x = position.x, y = position.y;
	if(x == -1.0){
		if(y == -1.0)
			return 0;
		else if(y == 0.0)
			return 1;
		else return 2;
	}
	if(x == 0.0){
		if(y == 1.0)
			return 3;
		if(y == 0.0)
			return 4;
		return 5;
	}
	if(x == 1.0){
		if(y == -1.0)
			return 6;
		if(y == 0.0)
			return 7;
		return 8;
	}
	return -1;
}

void main(void){
	vec4 worldPos = transformationMatrix * vec4(position.x, 0.0, position.y, 1.0);
	worldPos.y = y[getIndex(position)];
	gl_Position = projectionMatrix * viewMatrix * worldPos;
	texCoord = position / 2.0 + 0.5;
}
