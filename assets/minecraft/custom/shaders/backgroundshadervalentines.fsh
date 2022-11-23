#version 400 core
#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable

#define NUM_OCTAVES 16

uniform float u_time;
uniform vec2 u_resolution;


float random(vec2 pos) {
    return fract(sin(dot(pos.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 pos) {
    vec2 i = floor(pos);
    vec2 f = fract(pos);
    float a = random(i + vec2(0.0, 0.0));
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float fbm(vec2 pos) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    mat2 rot = mat2(cos(0.5), sin(0.5), -sin(0.5), cos(0.5));
    for (int i=0; i<NUM_OCTAVES; i++) {
        v += a * noise(pos);
        pos = rot * pos * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

    // Author:
    // Title:

    #define time u_time
    #define resolution u_resolution

    #define POINT_COUNT 8

vec2 points[POINT_COUNT];
const float speed = -0.5;
const float len = 0.25;
float intensity = 1.3;
float radius = 0.008;

//https://www.shadertoy.com/view/MlKcDD
//Signed distance to a quadratic bezier
float sdBezier(vec2 pos, vec2 A, vec2 B, vec2 C){
    vec2 a = B - A;
    vec2 b = A - 2.0*B + C;
    vec2 c = a * 2.0;
    vec2 d = A - pos;

    float kk = 1.0 / dot(b,b);
    float kx = kk * dot(a,b);
    float ky = kk * (2.0*dot(a,a)+dot(d,b)) / 3.0;
    float kz = kk * dot(d,a);

    float res = 0.0;

    float p = ky - kx*kx;
    float p3 = p*p*p;
    float q = kx*(2.0*kx*kx - 3.0*ky) + kz;
    float h = q*q + 4.0*p3;

    if(h >= 0.0){
        h = sqrt(h);
        vec2 x = (vec2(h, -h) - q) / 2.0;
        vec2 uv = sign(x)*pow(abs(x), vec2(1.0/3.0));
        float t = uv.x + uv.y - kx;
        t = clamp( t, 0.0, 1.0 );

        // 1 root
        vec2 qos = d + (c + b*t)*t;
        res = length(qos);
    }else{
        float z = sqrt(-p);
        float v = acos( q/(p*z*2.0) ) / 3.0;
        float m = cos(v);
        float n = sin(v)*1.732050808;
        vec3 t = vec3(m + m, -n - m, n - m) * z - kx;
        t = clamp( t, 0.0, 1.0 );

        // 3 roots
        vec2 qos = d + (c + b*t.x)*t.x;
        float dis = dot(qos,qos);

        res = dis;

        qos = d + (c + b*t.y)*t.y;
        dis = dot(qos,qos);
        res = min(res,dis);

        qos = d + (c + b*t.z)*t.z;
        dis = dot(qos,qos);
        res = min(res,dis);

        res = sqrt( res );
    }

    return res;
}


//http://mathworld.wolfram.com/HeartCurve.html
vec2 getHeartPosition(float t){
    return vec2(16.0 * sin(t) * sin(t) * sin(t),
    -(13.0 * cos(t) - 5.0 * cos(2.0*t)
    - 2.0 * cos(3.0*t) - cos(4.0*t)));
}

//https://www.shadertoy.com/view/3s3GDn
float getGlow(float dist, float radius, float intensity){
    return pow(radius/dist, intensity);
}

float getSegment(float t, vec2 pos, float offset, float scale){
    for(int i = 0; i < POINT_COUNT; i++){
        points[i] = getHeartPosition(offset + float(i)*len + fract(speed * t) * 6.28);
    }

    vec2 c = (points[0] + points[1]) / 2.0;
    vec2 c_prev;
    float dist = 10000.0;

    for(int i = 0; i < POINT_COUNT-1; i++){
        //https://tinyurl.com/y2htbwkm
        c_prev = c;
        c = (points[i] + points[i+1]) / 2.0;
        dist = min(dist, sdBezier(pos, scale * c_prev, scale * points[i], scale * c));
    }
    return max(0.0, dist);
}

void main2(){
    vec2 uv = gl_FragCoord.xy/resolution.xy;
    float widthHeightRatio = resolution.x/resolution.y;
    vec2 centre = vec2(0.5, 0.5);
    vec2 pos = centre - uv;
    pos.y /= widthHeightRatio;
    //Shift upwards to centre heart
    pos.y += 0.02;
    float scale = 0.000015 * resolution.y;

    float t = time;

    //Get first segment
    float dist = getSegment(t, pos, 0.0, scale);
    float glow = getGlow(dist, radius, intensity);

    vec3 col = vec3(0.0);

    //White core
    col += 10.0*vec3(smoothstep(0.003, 0.001, dist));
    //Pink glow
    col += glow * vec3(1.0,0.05,0.3);

    //Get second segment
    dist = getSegment(t, pos, 3.4, scale);
    glow = getGlow(dist, radius, intensity);



    dist = getSegment(t, pos, 1.0, scale);
    glow = getGlow(dist, radius, intensity);


    //White core
    col += 10.0*vec3(smoothstep(0.003, 0.001, dist));
    //Pink glow
    col += glow * vec3(1.0,0.05,0.3);

    //Get second segment
    dist = getSegment(t, pos, 3.4, scale);
    glow = getGlow(dist, radius, intensity);

    //White core
    col += 10.0*vec3(smoothstep(0.003, 0.001, dist));
    //Blue glow
    col += glow * vec3(0.1,0.4,1.0);

    //Tone mapping
    col = 1.0 - exp(-col);

    //Gamma
    col = pow(col, vec3(0.4545));

    //Output to screen
    gl_FragColor += vec4(col,0.5);
}


void main(void) {
    vec2 p = (gl_FragCoord.xy * 2.0 - u_resolution.xy) / min(u_resolution.x, u_resolution.y);

    float t = 0.0, d;

    float u_time2 = 3.0 * u_time / 2.0;

    vec2 q = vec2(0.0);
    q.x = fbm(p + 0.00 * u_time2);
    q.y = fbm(p + vec2(1.0));
    vec2 r = vec2(0.0);
    r.x = fbm(p + 1.0 * q + vec2(1.7, 9.2) + 0.15 * u_time2);
    r.y = fbm(p + 1.0 * q + vec2(8.3, 2.8) + 0.126 * u_time2);
    float f = fbm(p + r);
    vec3 color = mix(
    vec3(0.102, 0.4588, 0.8667),
    vec3(0.6667, 0.3686, 0.5765),
    clamp((f * f) * 4.0, 0.0, 1.0)
    );

    color = mix(
    color,
    vec3(0.8314, 0.3451, 0.0667),
    clamp(length(q), 0.0, 1.0)
    );


    color = mix(
    color,
    vec3(0.1, -0.5, 0.1),
    clamp(length(r.x), 0.0, 1.0)
    );

    color = (f *f * f + 0.6 * f * f + 0.5 * f) * color;

    gl_FragColor = vec4(color, 1.0);
    main2();
}
