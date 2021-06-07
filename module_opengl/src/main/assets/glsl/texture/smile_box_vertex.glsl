#version 300 es
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aColor;
layout(location = 2) in vec2 aTexCoord;

uniform float u_Time;

out vec3 ourColor;
out vec2 TexCoord;

vec2 rotate(vec2 v, float a) {
	float s = sin(a);
	float c = cos(a);
	mat2 m = mat2(c, -s, s, c);
	return m * v;
}

void main(){
    //gl_Position = vec4(aPos, 1.0);
    gl_Position = vec4(aPos.x + u_Time, -aPos.y, aPos.z, 1.0);
    ourColor = aColor;
    //TexCoord = rotate(vec2(aTexCoord.x, aTexCoord.y), u_Time);
    TexCoord = vec2(aTexCoord.x, aTexCoord.y);
}
