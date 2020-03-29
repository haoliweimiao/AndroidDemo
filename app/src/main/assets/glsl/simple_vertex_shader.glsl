attribute vec4 a_Position;
attribute vec4 a_Color;
uniform float a_Size;

varying vec4 v_Color;

void main(){
    v_Color = a_Color;

    gl_Position = a_Position;
    gl_PointSize = a_Size;
}