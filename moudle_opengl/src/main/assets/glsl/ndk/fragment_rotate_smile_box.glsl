#version 300 es
precision mediump float;
layout(location = 0) out vec4 FragColor;
in vec2 TexCoord;
uniform sampler2D uTexture1;
uniform sampler2D uTexture2;
void main(){
    FragColor = mix(texture(uTexture1, TexCoord), texture(uTexture2, TexCoord), 0.2);
}