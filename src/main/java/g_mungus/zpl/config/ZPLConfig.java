package g_mungus.zpl.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ZPLConfig {

    private static ForgeConfigSpec.ConfigValue<Double> thrusterStrength;
    private static final double thrusterStrengthDefault = 512d;

    private static ForgeConfigSpec.ConfigValue<Double> gyroscopeStrength;
    private static final double gyroscopeStrengthDefault = 128d;

    private static ForgeConfigSpec.ConfigValue<Double> thrusterDrag;
    private static final double thrusterDragDefault = 2400d;

    private static ForgeConfigSpec.ConfigValue<Double> advGyroMaxSpin;
    private static final double advGyroMaxSpinDefault = 256d;

    private static ForgeConfigSpec.ConfigValue<Double> advGyroStabilizeSpeed;
    private static final double advGyroStabilizeSpeedDefault = 256d;

    private static ForgeConfigSpec.ConfigValue<Double> advGyroProportionalGain;
    private static final double advGyroProportionalGainDefault = 64d;

    private static ForgeConfigSpec.ConfigValue<Double> advGyroMaxTorqueFactor;
    private static final double advGyroMaxTorqueFactorDefault = 1200d;

    public static double getThrusterStrength() {
        double result = thrusterStrengthDefault;
        try {
            result = thrusterStrength.get();
        } catch (Exception ignored) { }
        return result * 1000;
    }

    public static double getThrusterDrag() {
        double result = thrusterDragDefault;
        try {
            result = thrusterDrag.get();
        } catch (Exception ignored) { }
        return result;
    }

    public static double getGyroStrength() {
        double result = gyroscopeStrengthDefault;
        try {
            result = gyroscopeStrength.get();
        } catch (Exception ignored) { }
        return result * 500;
    }


    public static double getAdvGyroMaxSpin() {
        try { return advGyroMaxSpin.get(); } catch (Exception ignored) { }
        return advGyroMaxSpinDefault;
    }

    public static double getAdvGyroStabilizeSpeed() {
        try { return advGyroStabilizeSpeed.get(); } catch (Exception ignored) { }
        return advGyroStabilizeSpeedDefault;
    }

    public static double getAdvGyroProportionalGain() {
        try { return advGyroProportionalGain.get(); } catch (Exception ignored) { }
        return advGyroProportionalGainDefault;
    }

    public static double getAdvGyroMaxTorqueFactor() {
        try { return advGyroMaxTorqueFactor.get(); } catch (Exception ignored) { }
        return advGyroMaxTorqueFactorDefault;
    }

    public static final ForgeConfigSpec CONFIG_SPEC = buildConfig();

    private static ForgeConfigSpec buildConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        thrusterStrength = builder.define("ThrusterBaseStrength", thrusterStrengthDefault);
        gyroscopeStrength = builder.define("GyroscopeBaseStrength", gyroscopeStrengthDefault);
        thrusterDrag = builder.define("ThrusterDrag", thrusterDragDefault);
        advGyroMaxSpin = builder.define("AdvGyroMaxSpin", advGyroMaxSpinDefault);
        advGyroStabilizeSpeed = builder.define("AdvGyroStabilizeSpeed", advGyroStabilizeSpeedDefault);
        advGyroProportionalGain = builder.define("AdvGyroProportionalGain", advGyroProportionalGainDefault);
        advGyroMaxTorqueFactor = builder.define("AdvGyroMaxTorqueFactor", advGyroMaxTorqueFactorDefault);
        return builder.build();
    }
}
