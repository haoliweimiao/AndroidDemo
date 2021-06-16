#version 300 es
precision mediump float;

layout(location = 0) out vec4 FragColor;

in vec3 ourColor;
in vec2 TexCoord;

uniform sampler2D uTexture;

void main(){
    FragColor = texture(uTexture, TexCoord);
}