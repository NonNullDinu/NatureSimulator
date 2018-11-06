#version 430 core

//const vec3 waterColour = vec3(0.604, 0.867, 0.851);
//const vec3 waterColour = vec3(0.6, 0.9, 0.9);
const vec3 waterColour = vec3(0.8, 1.0, 1.0);
const float fresnelReflective = 2.0;
const float minBlueness = 0.2;
const float maxBlueness = 0.7;
const float murkyDepth = 14;

out vec4 frag_Color;

layout(location = 0) in vec4 pass_clipSpaceGrid;
layout(location = 1) in vec4 pass_clipSpaceReal;
layout(location = 2) flat in vec3 pass_normal;
layout(location = 3) in vec3 pass_toCameraVector;
layout(location = 4) in vec3 pass_specular;
layout(location = 5) flat in vec3 pass_diffuse;
layout(location = 6) in float in_visibility;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
//uniform sampler2D reflectionBlured;
//uniform sampler2D refractionBlured;
uniform vec2 nearFarPlanes;
uniform vec3 skyColor;

float toLinearDepth(float depth) {
    return (2.0 * nearFarPlanes.x * nearFarPlanes.y / (nearFarPlanes.y + nearFarPlanes.x - (2.0 * depth - 1.0) *
    (nearFarPlanes.y - nearFarPlanes.x)));
}

vec3 applyMurkiness(vec3 refractColour, float waterDepth) {
	float murkyFactor = clamp(waterDepth / murkyDepth, 0.0, 1.0);
	float murkiness = minBlueness + murkyFactor * (maxBlueness - minBlueness);
	return mix(refractColour, waterColour, murkiness);
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
	texCoordsGrid = mix(texCoordsReal, texCoordsGrid, 0.4);

	vec2 refractionTexCoords = texCoordsGrid;
	vec2 reflectionTexCoords = vec2(texCoordsGrid.x, 1.0 - texCoordsGrid.y);
	float waterDepth = calculateWaterDepth(texCoordsReal);

	vec3 refractColour = texture(refractionTexture, refractionTexCoords).rgb;
	vec3 reflectColour = texture(reflectionTexture, reflectionTexCoords).rgb;
//	vec3 reflectBluredColour = texture(reflectionBlured, reflectionTexCoords).rgb;
//	vec3 refractBluredColour = texture(refractionBlured, refractionTexCoords).rgb;
//	reflectColour = mix(reflectColour, reflectBluredColour, length(pass_toCameraVector) / 1000.0);
//	refractColour = mix(refractColour, refractBluredColour, length(pass_toCameraVector) / 1000.0);

	//apply some blueness
	refractColour = applyMurkiness(refractColour, waterDepth);
	reflectColour = mix(reflectColour, waterColour, minBlueness);

	vec3 finalColour = mix(reflectColour, refractColour, calculateFresnel());
	finalColour = finalColour * pass_diffuse + pass_specular;

	frag_Color = vec4(finalColour, 1.0);

	frag_Color = mix(vec4(skyColor, 1.0), frag_Color, in_visibility);
}