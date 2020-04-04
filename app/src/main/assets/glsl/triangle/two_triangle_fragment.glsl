#version 300 es
precision mediump float;
out vec4 fragColor;

struct FragmentColor {
    vec4 color;
};

uniform FragmentColor f_Color;
void main() {
    fragColor = f_Color.color;
}