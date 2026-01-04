void flw_materialFragment() {
    float time = 64000.0 * fract(flw_ticks + flw_partialTick);
    vec2 uv = flw_vertexTexCoord;

    float alpha = 1.0 - uv.x;

    flw_fragColor = vec4(
        alpha * alpha * 0.5,
        max(1.0 - alpha, alpha * alpha * 0.5),
        1.0,
        alpha * (0.75 + sin(time + uv.x * -14.0 + (uv.y * 8.0 * 3.1459 + time / 100.0)) / 4.0)
    );
}
