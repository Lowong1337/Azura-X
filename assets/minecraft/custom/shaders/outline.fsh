#version 120

uniform sampler2D texture;
uniform vec2 texelSize;
uniform float lineWidth = 1.0, alpha = 0.2, radius = 20;
uniform vec3 color = vec3(1.);

float gauss(float x, float sigma) {
    return .4 * exp(-.4 * x * x / (sigma * sigma)) / sigma;
}

void main() {
    vec2 texCoord = gl_TexCoord[0].st;
    float lWidth = max(1.0, lineWidth);
    vec4 center = texture2D(texture, texCoord),
    left = texture2D(texture, texCoord - vec2(texelSize.x * lWidth, 0.)),
    right = texture2D(texture, texCoord + vec2(texelSize.x * lWidth, 0.)),
    up = texture2D(texture, texCoord - vec2(0., texelSize.y * lWidth)),
    down = texture2D(texture, texCoord + vec2(0., texelSize.y * lWidth));
    vec3 col = (left.rgb + right.rgb + up.rgb + down.rgb) * color;
    float alph = clamp((center.a - left.a) + (center.a - right.a) + (center.a - up.a) + (center.a - down.a), 0.0, 2.0) * center.a;
    float a = alph;
    for (float i = -radius; i <= radius; i++) {
        vec4 tex0 = texture2D(texture, gl_TexCoord[0].st + i * texelSize * vec2(1.0, 0.0)),
        tex1 = texture2D(texture, gl_TexCoord[0].st + i * texelSize * vec2(0.0, 1.0));
        a += tex0.a * gauss(i, radius / 2) / 2;
        a += tex1.a * gauss(i, radius / 2) / 2;
    }
    a -= center.a;
    gl_FragColor = vec4(color, a * alpha);
}