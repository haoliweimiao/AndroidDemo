//#version 300 es
//layout (location = 0) in vec3 aPos;
//layout (location = 1) in vec3 aColor;
//layout (location = 2) in vec2 aTexCoord;
//
//out vec3 ourColor;
//out vec2 TexCoord;
//
//void main()
//{
//    gl_Position = vec4(aPos, 1.0);
//    ourColor = aColor;
////    TexCoord = vec2(aTexCoord.x, aTexCoord.y);
//    TexCoord = aTexCoord;
//}

#version 300 es
//layout (location = 0) in vec4 vPosition;
//layout (location = 1) in vec2 aTextureCoord;

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aTexCoord;

//输出纹理坐标(s,t)
out vec2 vTexCoord;
void main() {
    gl_Position  = vec4(aPos, 1.0);
    gl_PointSize = 10.0;
    vTexCoord = aTexCoord;
}
