#version 120

uniform sampler2D texture;
uniform vec2 texelSize;
uniform float lineWidth = 1.0, alpha = 0.2, radius = 10;
uniform vec3 color = vec3(1.);

float gauss(float x, float sigma) {
    return .4 * exp(-.5 * x * x / (sigma * sigma)) / sigma;
}

void main() {
    vec2 texCoord = gl_TexCoord[0].st;
    vec4 center = texture2D(texture, texCoord);
    float lWidth = max(1.0, lineWidth);
    vec4 left = texture2D(texture, texCoord - vec2(texelSize.x * lWidth, 0.0));
    vec4 right = texture2D(texture, texCoord + vec2(texelSize.x * lWidth, 0.0));
    vec4 up = texture2D(texture, texCoord - vec2(0.0, texelSize.y * lWidth));
    vec4 down = texture2D(texture, texCoord + vec2(0.0, texelSize.y * lWidth));
    float leftDiff  = abs(center.a - left.a);
    float rightDiff = abs(center.a - right.a);
    float upDiff    = abs(center.a - up.a);
    float downDiff  = abs(center.a - down.a);
    float total = clamp(leftDiff + rightDiff + upDiff + downDiff, 0.0, 1.0) * center.a;
    vec3 outColor = left.rgb + right.rgb + up.rgb + down.rgb;
    gl_FragColor = vec4(outColor * total * color, total * alpha);

}