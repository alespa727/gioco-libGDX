#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform sampler2D u_texture;

// Centro della luce in [0,1]
uniform vec2 u_lightPos;

// Raggio in [0,1], relativo alla larghezza
uniform float u_lightRadius;

// Colore della luce
uniform vec4 u_lightColor;

// Risoluzione della texture o dello schermo
uniform vec2 u_resolution;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);

    // Compensa il rapporto d'aspetto
    vec2 scaledCoord = vec2(
    (v_texCoord.x - u_lightPos.x) * (u_resolution.x / u_resolution.y),
    v_texCoord.y - u_lightPos.y
    );

    float dist = length(scaledCoord)*0.5f;

    // Raggio in coordinate normalizzate, rispetto alla larghezza
    float intensity = 0.1 - smoothstep(0.05, u_lightRadius, dist);

    vec4 finalColor = texColor + u_lightColor * intensity;

    gl_FragColor = vec4(finalColor.rgb, texColor.a);
}
