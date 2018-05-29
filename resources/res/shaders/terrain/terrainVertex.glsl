#version 430 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 in_normal;
layout(location = 2) in vec3 in_final_color;

layout(location = 0) out vec4 worldPosition;
layout(location = 1) out int vID;
layout(location = 2) out vec3 out_final_color;

uniform mat4 transformationMatrix;

uniform vec4 clipPlane;

void main(void) {
	worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	vID = gl_VertexID;
	out_final_color = in_final_color;
}
