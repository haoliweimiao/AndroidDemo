#version 300 es
layout (location = 0) in vec4 vPosition;

uniform float uMoveXPos;
uniform float uMoveYPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
     vec4 cache =  vec4(vPosition.x + uMoveXPos, vPosition.y + uMoveYPos, vPosition.z, vPosition.w);
     gl_Position = projection * view * model * cache;
     gl_PointSize = 10.0;
}