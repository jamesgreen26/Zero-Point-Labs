in vec2 texCoord0;

out vec4 frag_color;

uniform float GameTime;

void main() {
    float time = GameTime * 64000;
    float alpha = 1 - texCoord0.x;
    frag_color = vec4(alpha * alpha * 0.5, max(1 - alpha, alpha * alpha * 0.5), 1, alpha * (0.75f + sin(time + (texCoord0.x * -14) + (texCoord0.y * 8 * 3.1459f + time / 100)) / 4));
}