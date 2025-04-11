#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_time; // Passato dal codice Java

void main() {
    // Scorre la texture verso destra nel tempo
    vec2 scrolledCoords = vec2(v_texCoords.x + u_time, v_texCoords.y);
    vec4 texColor = texture2D(u_texture, scrolledCoords);
    gl_FragColor = texColor * v_color;
}
