#version 300 es

precision mediump float;

in vec3 ourColor;
in vec2 TexCoord;

layout(location = 0) out vec4 FragColor;

// textureId sampler
uniform sampler2D u_texture;

void main()
{
    FragColor = texture(u_texture, TexCoord);
//    FragColor = textureId(u_texture, TexCoord) * vec4(ourColor, 1.0);
}