#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec4 u_color;

void main() {
    // Sample the color from the texture at the given coordinates
    vec4 texColor = texture2D(u_texture, v_texCoords);

    // Only apply the red tint if the alpha value is greater than a threshold (e.g., alpha > 0.0)
    if (texColor.a > 0.0) {
        texColor.r = u_color.r; // Set the red component from u_color
    }

    // Set the final fragment color
    gl_FragColor = texColor;
}
