#version 430 core

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

layout(location = 0) in vec4 worldPosition[];
layout(location = 1) in int vID[];
layout(location = 2) in vec3 final_color[];

layout(location = 0) flat out vec3 out_color;
layout(location = 1) out float out_visibility;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

#Struct_Lib.Light
uniform Light light;

#Struct_Lib.FogValues
uniform FogValues fogValues;

const float COLOR_CHANGE_SPEED = 0.001;

layout(std430, binding = 0) buffer colors
{
	float cls[];
};

vec3 calculateDiffuse(vec3 normal) {
	float dot1 = dot(normalize(-light.direction), normal);
	dot1 = max(dot1, 0.0);
	return (light.color * light.bias.x) + (dot1 * light.color * light.bias.y);
}

vec3 calculateColor(vec3 color, vec3 normal) {
	return color * calculateDiffuse(normal);
}

void main(void) {
	for (int i = 0; i < 3; i++) {
		vec4 posRelativeToCamera = viewMatrix * worldPosition[i];
		gl_Position = projectionMatrix * posRelativeToCamera;
		int vertId = vID[i];
		vec3 color = mix(
				vec3(cls[vertId * 3], cls[vertId * 3 + 1],
						cls[vertId * 3 + 2]), final_color[i],
				COLOR_CHANGE_SPEED);
		out_color = color;
		cls[vertId * 3] = color.x;
		cls[vertId * 3 + 1] = color.y;
		cls[vertId * 3 + 2] = color.z;
		vec3 normal = normalize(cross(worldPosition[1].xyz - worldPosition[0].xyz, worldPosition[2].xyz - worldPosition[0].xyz));

		out_color = calculateColor(out_color, normal);

		float distance = length(posRelativeToCamera.xyz);
		out_visibility = exp(
				-pow((distance * fogValues.density), fogValues.gradient));
		out_visibility = clamp(out_visibility, 0.0, 1.0);
		EmitVertex();
	}
	EndPrimitive();
}
