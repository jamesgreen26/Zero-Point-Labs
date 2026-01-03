void flw_transformBoundingSphere(in FlwInstance instance, inout vec3 center, inout float radius) {
    vec3 pos = (instance.pose * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
    radius += length((instance.pose * vec4(1.0, 1.0, 1.0, 1.0)).xyz);
    center += pos;
}