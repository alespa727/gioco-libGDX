#version 120

precision mediump float;

uniform sampler2D u_texture;       // La texture dell'oggetto
uniform sampler2D u_noiseTexture;  // La texture di rumore
uniform float u_dissolveThreshold; // Soglia di dissolvenza (da 0 a 1)
uniform vec4 u_dissolveColor;     // Colore del bordo dissolvente

varying vec2 v_texCoord;          // Coordinate della texture

void main() {
    // Ottieni il colore dell'oggetto
    vec4 color = texture2D(u_texture, v_texCoord);

    // Ottieni il valore di rumore per il pixel corrente
    float noiseValue = texture2D(u_noiseTexture, v_texCoord).r;

    // Se il valore di rumore è maggiore della soglia, disegna il pixel
    if (noiseValue > u_dissolveThreshold) {
        gl_FragColor = color; // Mantieni il colore originale
    } else {
        // Se il valore di rumore è inferiore alla soglia, il pixel è trasparente
        gl_FragColor = vec4(u_dissolveColor.rgb, 0.0); // Colore del bordo
    }
}
