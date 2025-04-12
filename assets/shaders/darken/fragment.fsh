#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
    color.rgb *= (1.0 - 0.34);
    gl_FragColor = color;
}
