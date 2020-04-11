#version 300 es
//out vec4 FragColor;
layout(location = 0) out vec4 outColor;

in vec3 ourColor;
in vec2 TexCoord;

// texture sampler
uniform sampler2D u_texture;

void main()
{
    outColor = texture(u_texture, TexCoord);
}