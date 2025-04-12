#version 120

attribute vec4 a_position;    // Posizione dei vertici
attribute vec2 a_texCoord;    // Coordinate della texture

uniform mat4 u_projTrans;     // Matrice di trasformazione (proiezione + vista)

varying vec2 v_texCoord;      // Passa le coordinate della texture al fragment shader

void main() {
    gl_Position = u_projTrans * a_position;
    v_texCoord = a_texCoord;
}
