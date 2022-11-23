#version 120

uniform sampler2D texture;
uniform vec2 texelSize;
uniform float alpha = 0.2;
uniform vec3 color = vec3(1, 1, 1);

void main() {
    vec2 texCoord = gl_TexCoord[0].st;
    vec4 center = texture2D(texture, texCoord);
    gl_FragColor = vec4(color, center.a * alpha);
}