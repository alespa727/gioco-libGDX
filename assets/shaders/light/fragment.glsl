#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform vec2 u_lightPos;       // centro della luce [0,1]
uniform float u_lightIntensity;
uniform float u_lightRadius;   // raggio [0,1] rispetto alla larghezza
uniform vec4 u_lightColor;     // colore luce
uniform vec2 u_resolution;     // dimensione in pixel

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);

    // Correggi per rapporto d'aspetto
    vec2 scaledCoord = vec2(
    (v_texCoord.x - u_lightPos.x) * (u_resolution.x / u_resolution.y),
    v_texCoord.y - u_lightPos.y
    );

    float dist = length(scaledCoord);

    // Intensità della luce, da 1 (centro) a 0 (fuori raggio)
    float intensity = (1 - smoothstep(0.0, u_lightRadius, dist))*u_lightIntensity;


    // Applica la luce solo dove c'è opacità
    vec4 lightEffect = u_lightColor * intensity * texColor.a;

    // Somma il colore con l'effetto luce
    vec4 finalColor = texColor + lightEffect;

    // Clamp del colore finale per evitare sovrasaturazioni
    finalColor = clamp(finalColor, 0.0, 1.0);

    gl_FragColor = vec4(finalColor.rgb, texColor.a);
    //gl_FragColor = vec4(vec3(intensity), 1.0); // scala di grigi
}
