#version 300 es
precision highp float;
uniform vec2 u_resolution;
uniform float u_time;
uniform vec3 u_color_1;
uniform vec3 u_color_2;

float rand(vec2 n) {
    return fract(cos(dot(n, vec2(2.9898, 20.1414))) * 5.5453);
}

float noise(vec2 n) {
    const vec2 d = vec2(0.0, 1.0);
    vec2 b = floor(n), f = smoothstep(vec2(0.0), vec2(1.0), fract(n));
    return mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y);
}

float fbm(vec2 n){
    float total=0.,amplitude=1.5;
    for(int i=0;i<18;i++){
        total+=noise(n)*amplitude;
        n+=n;
        amplitude*=.45;
    }
    return total;
}
void main(void){
    vec3 c1=u_color_1 * vec3(0.3, 0.3, 0.3);
    vec3 c2=(u_color_1 + u_color_1) * vec3(0.6, 0.6, 0.6);
    vec3 c3=u_color_1 * vec3(0.3, 0.3, 0.3);
    vec3 c4=u_color_2 * vec3(0.6, 0.6, 0.6);
    vec3 c5=u_color_2 * vec3(0.6, 0.6, 0.6);
    vec3 c6=u_color_1 * vec3(0.3, 0.3, 0.3);
    vec2 p=gl_FragCoord.xy*5./u_resolution.xx;
    float q=fbm(p-u_time*.05);
    vec2 r=vec2(fbm(p+q+u_time*0.1-p.x-p.y),fbm(p+q-u_time*0.1));
    vec3 c=mix(c1,c2,fbm(p+r))+mix(c3,c4,r.x)-mix(c5,c6,r.y);
    float grad=gl_FragCoord.y/u_resolution.y;
    gl_FragColor=vec4(c*cos(1.0*gl_FragCoord.y/u_resolution.y),1.5);
    gl_FragColor.xyz*=1.15-grad;
}