#version 300 es
precision mediump float;

layout(location = 0) out vec4 FragColor;

in vec3 ourColor;
in vec2 TexCoord;

// textureId samplers
uniform sampler2D uTexture1;
uniform sampler2D uTexture2;

void main(){
    // linearly interpolate between both textures (80% container, 20% awesomeface)
        FragColor = mix(texture(uTexture1, TexCoord), texture(uTexture2, TexCoord), 0.2);
//    FragColor = textureId(uTexture1, TexCoord)*0.8 + textureId(uTexture2, TexCoord)*0.2;
}