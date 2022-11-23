varying vec4 vColor;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    vColor = vec4(1.0, 1.0, 1.0, 1.0);
}