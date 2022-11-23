#version 120

uniform sampler2D texture;
uniform vec2 texelSize;
uniform float alpha = 0.2;

void main() {
    vec2 texCoord = gl_TexCoord[0].st;
    vec4 center = texture2D(texture, texCoord);
    gl_FragColor = vec4(center.rgb, center.a * alpha);
}