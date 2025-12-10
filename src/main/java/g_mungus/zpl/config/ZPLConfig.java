package g_mungus.zpl.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ZPLConfig {

    private static ForgeConfigSpec.ConfigValue<Double> thrusterStrength;
    private static final double thrusterStrengthDefault = 800_000d;

    private static ForgeConfigSpec.ConfigValue<Double> gyroscopeStrength;
    private static final double gyroscopeStrengthDefault = 64_000d;

    public static double getThrusterStrength() {
        double result = thrusterStrengthDefault;
        try {
            result = thrusterStrength.get();
        } catch (Exception ignored) { }
        return result;
    }

    public static double getGyroStrength() {
        double result = gyroscopeStrengthDefault;
        try {
            result = gyroscopeStrength.get();
        } catch (Exception ignored) { }
        return result;
    }


    public static final ForgeConfigSpec CONFIG_SPEC = buildConfig();

    private static ForgeConfigSpec buildConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        thrusterStrength = builder.define("ThrusterBaseStrength", thrusterStrengthDefault);
        gyroscopeStrength = builder.define("GyroscopeBaseStrength", gyroscopeStrengthDefault);
        return builder.build();
    }
}
