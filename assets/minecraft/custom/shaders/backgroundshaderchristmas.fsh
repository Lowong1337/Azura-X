#version 400 core
#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable

uniform float u_time;
uniform vec2 u_resolution;

#define pi 3.14159265359
#define pi2 6.28318530718
#define size 2e-4

float snow(vec2 uv,float scale)
{
    float w=smoothstep(20.,0.,-uv.y*(scale/10.));if(w<.1)return 0.;
    uv+=u_time/scale;uv.y+=u_time*2./scale;uv.x+=sin(uv.y+u_time*.5)/scale;
    uv*=scale;vec2 s=floor(uv),f=fract(uv),p;float k=3.,d;
    p=.5+.35*sin(11.*fract(sin((s+p+scale)*mat2(7,3,6,5))*5.))-f;d=length(p);k=min(d,k);
    k=smoothstep(0.,k,sin(f.x+f.y)*0.01);
    return k*w;
}

float lineDist(vec2 a, vec2 b, vec2 p) {

    vec2 v = a, w = b;

    float l2 = pow(distance(w, v), 2.);
    if(l2 == 0.0) return distance(p, v);

    float t = clamp(dot(p - v, w - v) / l2, 0., 1.);
    vec2 j = v + t * (w - v);

    return distance(p, j);
}

vec3 addLine(vec4 vertex1, vec4 vertex2, vec3 color, mat4 projMat, vec2 camUV) {
    vec4 p_proj1 = projMat * vertex1;
    vec2 p1 = p_proj1.xy / p_proj1.z;

    vec4 p_proj2 = projMat * vertex2;
    vec2 p2 = p_proj2.xy / p_proj2.z;

    float dist = lineDist((camUV-vec2(.5))-p1, (camUV-vec2(.5))-p2, vec2(0., 0.0));

    return color * 1. / pow(dist, 2.0001) * size;
}

vec3 renderTree(float theta, vec2 pos, vec2 siZe) {

    // standard rotation matrix around Y axis.
    mat4 projMat = mat4(
    vec4(cos(theta), 0.0, sin(theta), 0.0),
    vec4(0.0, 1.0, 0.0, 0.0),
    vec4(-sin(theta), 0.0, cos(theta), 0.0),
    vec4(0.0, 0.0, 1.0, 0.0)
    );
    vec2 uv = (gl_FragCoord.xy - vec2(u_resolution.x * 0.25, .5)) / u_resolution.y;
    uv.x += pos.x;
    uv.y += pos.y;
    vec3 imageColors = vec3(0.);
    vec3 green = vec3(0.0, 0.2, 0.);
    vec3 brown = vec3(0.5, 0.1, 0.);
    float height = siZe.y;
    float width = siZe.x;
    float trunkThickness = 0.05;

    imageColors += addLine(vec4(width * 0.0, -.3 + height * 1.0, 0., 1.), vec4(width * 0.12, -.3 + height * 0.8, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(width * 0.12, -.3 + height * 0.8, 0., 1.), vec4(width * 0.02, -.3 + height * 0.8, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(width * 0.02, -.3 + height * 0.8, 0., 1.), vec4(width * 0.24, -.3 + height * 0.5, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(width * 0.24, -.3 + height * 0.5, 0., 1.), vec4(width * 0.08, -.3 + height * 0.5, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(width * 0.08, -.3 + height * 0.5, 0., 1.), vec4(width * 0.36, -.3 + height * 0.2, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(width * 0.36, -.3 + height * 0.2, 0., 1.), vec4(width * trunkThickness, -.3 + height * 0.2, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(width * trunkThickness, -.3 + height * 0.2, 0., 1.), vec4(width * trunkThickness, -.3 + height * 0.0, 0., 1.), brown, projMat, uv);
    imageColors += addLine(vec4(-width * 0.0, -.3 + height * 1.0, 0., 1.), vec4(-width * 0.12, -.3 + height * 0.8, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(-width * 0.12, -.3 + height * 0.8, 0., 1.), vec4(-width * 0.02, -.3 + height * 0.8, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(-width * 0.02, -.3 + height * 0.8, 0., 1.), vec4(-width * 0.24, -.3 + height * 0.5, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(-width * 0.24, -.3 + height * 0.5, 0., 1.), vec4(-width * 0.08, -.3 + height * 0.5, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(-width * 0.08, -.3 + height * 0.5, 0., 1.), vec4(-width * 0.36, -.3 + height * 0.2, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(-width * 0.36, -.3 + height * 0.2, 0., 1.), vec4(-width * trunkThickness, -.3 + height * 0.2, 0., 1.), green, projMat, uv);
    imageColors += addLine(vec4(-width * trunkThickness, -.3 + height * 0.2, 0., 1.), vec4(-width * trunkThickness, -.3 + height * 0.0, 0., 1.), brown, projMat, uv);
    imageColors += addLine(vec4(width * trunkThickness, -.3 + height * 0.0, 0., 1.), vec4(-width * trunkThickness, -.3 + height * 0.0, 0., 1.), brown, projMat, uv);
    return imageColors;
}

void main(void){
    vec2 uv=(gl_FragCoord.xy*2.-u_resolution.xy)/min(u_resolution.x,u_resolution.y);
    vec3 finalColor=vec3(1, 0, 0);
    float c=smoothstep(1.,1.3,clamp(uv.y*.3+.8,0.,.75));
    c+=snow(uv,30.)*.3;
    c+=snow(uv,20.)*.5;
    c+=snow(uv,15.)*.8;
    c+=snow(uv,10.);
    c+=snow(uv,8.);
    c+=snow(uv,6.);
    c+=snow(uv,5.);
    finalColor=(vec3(c));
    finalColor += vec3(.5, .1, .1);

    gl_FragColor = vec4(finalColor,1);
    gl_FragColor += vec4(renderTree(0., vec2(0.75, 0.), vec2(.8 / 3., .7 / 3.)), 1);
    gl_FragColor += vec4(renderTree(0., vec2(-0.75, 0.), vec2(.8 / 3., .7 / 3.)), 1);
}