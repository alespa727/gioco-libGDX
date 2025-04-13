#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform vec3 u_tint;

varying vec2 v_texCoords;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
    color.rgb *= u_tint;
    gl_FragColor = color;
}
