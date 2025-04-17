#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;  // Coordinate della texture
uniform sampler2D u_texture;  // La texture da applicare (la scena)

void main() {
    // Prendi il colore dalla texture
    vec4 color = texture2D(u_texture, v_texCoords);

    // Normalizza le coordinate per ottenere il centro come (0.5, 0.5)
    vec2 position = v_texCoords - vec2(0.5);
    float dist = length(position);  // Calcola la distanza dal centro della texture

    // Imposta l'effetto vignetta usando smoothstep per creare una transizione morbida
    float vignette = smoothstep(0.1, 2.0, dist);  // Modifica questi valori per regolare l'intensità

    // Misceliamo il colore originale con il nero (0.0) in base alla distanza dal centro
    vec3 darkened = mix(color.rgb, vec3(0.0), vignette);

    // Ritorna il colore finale (con la componente alpha originale)
    gl_FragColor = vec4(darkened, color.a);
}
