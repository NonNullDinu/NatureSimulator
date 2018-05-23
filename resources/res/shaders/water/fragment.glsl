#version 430 core

const vec3 waterColour = vec3(0.604, 0.867, 0.851);
const float fresnelReflective = 2.5;
const float edgeSoftness = 1;
const float minBlueness = 0.2;
const float maxBlueness = 0.6;
const float murkyDepth = 14;

out vec4 frag_Color;

layout(location = 0) in vec4 pass_clipSpaceGrid;
layout(location = 1) in vec4 pass_clipSpaceReal;
layout(location = 2) flat in vec3 pass_normal;
layout(location = 3) in vec3 pass_toCameraVector;
layout(location = 4) in vec3 pass_specular;
layout(location = 5) in vec3 pass_diffuse;
layout(location = 6) in float in_visibility;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform vec2 nearFarPlanes;
uniform vec3 skyColor;

vec3 applyMurkiness(vec3 refractColour, float waterDepth) {
	float murkyFactor = clamp(waterDepth / murkyDepth, 0.0, 1.0);
	float murkiness = minBlueness + murkyFactor * (maxBlueness - minBlueness);
	return mix(refractColour, waterColour, murkiness);
}

float toLinearDepth(float zDepth) {
	float near = nearFarPlanes.x;
	float far = nearFarPlanes.y;
	return 2.0 * near * far / (far + near - (2.0 * zDepth - 1.0) * (far - near));
}

float calculateWaterDepth(vec2 texCoords) {
	float depth = texture(depthTexture, texCoords).r;
	float floorDistance = toLinearDepth(depth);
	depth = gl_FragCoord.z;
	float waterDistance = toLinearDepth(depth);
	return floorDistance - waterDistance;
}

float calculateFresnel() {
	vec3 viewVector = normalize(pass_toCameraVector);
	vec3 normal = mix(normalize(pass_normal), vec3(0.0, 1.0, 0.0), 0.9);
	float refractiveFactor = dot(viewVector, normal);
	refractiveFactor = pow(refractiveFactor, fresnelReflective);
	return clamp(refractiveFactor, 0.0, 1.0);
}

vec2 clipSpaceToTexCoords(vec4 clipSpace) {
	vec2 ndc = (clipSpace.xy / clipSpace.w);
	vec2 texCoords = ndc / 2.0 + 0.5;
	return clamp(texCoords, 0.002, 0.998);
}

void main(void) {

	vec2 texCoordsReal = clipSpaceToTexCoords(pass_clipSpaceReal);
	vec2 texCoordsGrid = clipSpaceToTexCoords(pass_clipSpaceGrid);

	vec2 refractionTexCoords = texCoordsGrid;
	vec2 reflectionTexCoords = vec2(texCoordsGrid.x, 1.0 - texCoordsGrid.y);
	float waterDepth = calculateWaterDepth(texCoordsReal);

	vec3 refractColour = texture(refractionTexture, refractionTexCoords).rgb;
	vec3 reflectColour = texture(reflectionTexture, reflectionTexCoords).rgb;

	//apply some blueness
	refractColour = applyMurkiness(refractColour, waterDepth);
	reflectColour = mix(reflectColour, waterColour, minBlueness);

	vec3 finalColour = mix(reflectColour, refractColour, calculateFresnel());
	finalColour = finalColour * pass_diffuse + pass_specular;

	frag_Color = vec4(finalColour, 1.0);

	frag_Color = mix(vec4(skyColor, 1.0), frag_Color, in_visibility);

	//apply soft edges
//	out_colour.a = clamp(waterDepth / edgeSoftness, 0.0, 1.0);

}
