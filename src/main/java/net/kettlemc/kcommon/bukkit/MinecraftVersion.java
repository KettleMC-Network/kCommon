package net.kettlemc.kcommon.bukkit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents each Minecraft protocol version.
 */
public enum MinecraftVersion {
    UNKNOWN(-1, "Unknown"),
    LEGACY(-2, "Legacy"),

    MINECRAFT_1_7_2(4, "1.7.2"),
    MINECRAFT_1_7_3(4, "1.7.3"),
    MINECRAFT_1_7_4(4, "1.7.4"),
    MINECRAFT_1_7_5(4, "1.7.5"),
    MINECRAFT_1_7_6(5, "1.7.6"),
    MINECRAFT_1_7_7(5, "1.7.7"),
    MINECRAFT_1_7_8(5, "1.7.8"),
    MINECRAFT_1_7_9(5, "1.7.9"),
    MINECRAFT_1_7_10(5, "1.7.10"),
    MINECRAFT_1_8(47, "1.8"),
    MINECRAFT_1_8_1(47, "1.8.1"),
    MINECRAFT_1_8_2(47, "1.8.2"),
    MINECRAFT_1_8_3(47, "1.8.3"),
    MINECRAFT_1_8_4(47, "1.8.4"),
    MINECRAFT_1_8_5(47, "1.8.5"),
    MINECRAFT_1_8_6(47, "1.8.6"),
    MINECRAFT_1_8_7(47, "1.8.7"),
    MINECRAFT_1_8_8(47, "1.8.8"),
    MINECRAFT_1_8_9(47, "1.8.9"),
    MINECRAFT_1_9(107, "1.9"),
    MINECRAFT_1_9_1(108, "1.9.1"),
    MINECRAFT_1_9_2(109, "1.9.2"),
    MINECRAFT_1_9_3(110, "1.9.3"),
    MINECRAFT_1_9_4(110, "1.9.4"),
    MINECRAFT_1_10(210, "1.10"),
    MINECRAFT_1_10_1(210, "1.10.1"),
    MINECRAFT_1_10_2(210, "1.10.2"),
    MINECRAFT_1_11(315, "1.11"),
    MINECRAFT_1_11_1(316, "1.11.1"),
    MINECRAFT_1_11_2(316, "1.11.2"),
    MINECRAFT_1_12(335, "1.12"),
    MINECRAFT_1_12_1(338, "1.12.1"),
    MINECRAFT_1_12_2(340, "1.12.2"),
    MINECRAFT_1_13(393, "1.13"),
    MINECRAFT_1_13_1(401, "1.13.1"),
    MINECRAFT_1_13_2(404, "1.13.2"),
    MINECRAFT_1_14(477, "1.14"),
    MINECRAFT_1_14_1(480, "1.14.1"),
    MINECRAFT_1_14_2(485, "1.14.2"),
    MINECRAFT_1_14_3(490, "1.14.3"),
    MINECRAFT_1_14_4(498, "1.14.4"),
    MINECRAFT_1_15(573, "1.15"),
    MINECRAFT_1_15_1(575, "1.15.1"),
    MINECRAFT_1_15_2(578, "1.15.2"),
    MINECRAFT_1_16(735, "1.16"),
    MINECRAFT_1_16_1(736, "1.16.1"),
    MINECRAFT_1_16_2(751, "1.16.2"),
    MINECRAFT_1_16_3(753, "1.16.3"),
    MINECRAFT_1_16_4(754, "1.16.4"),
    MINECRAFT_1_16_5(754, "1.16.5"),
    MINECRAFT_1_17(755, "1.17"),
    MINECRAFT_1_17_1(756, "1.17.1"),
    MINECRAFT_1_18(757, "1.18"),
    MINECRAFT_1_18_1(757, "1.18.1"),
    MINECRAFT_1_18_2(758, "1.18.2"),
    MINECRAFT_1_19(759, "1.19"),
    MINECRAFT_1_19_1(760, "1.19.1"),
    MINECRAFT_1_19_2(760, "1.19.2"),
    MINECRAFT_1_19_3(761, "1.19.3"),
    MINECRAFT_1_19_4(762, "1.19.4"),
    MINECRAFT_1_20(763, "1.20"),
    MINECRAFT_1_20_1(763, "1.20.1"),
    MINECRAFT_1_20_2(764, "1.20.2"),
    MINECRAFT_1_20_3(765, "1.20.3"),
    MINECRAFT_1_20_4(765, "1.20.4");

    /**
     * Represents the lowest supported version.
     */
    public static final MinecraftVersion MINIMUM_VERSION = MINECRAFT_1_7_2;

    /**
     * Represents the highest supported version.
     */
    public static final MinecraftVersion MAXIMUM_VERSION = values()[values().length - 1];

    /**
     * Maps protocol versions to their {@link MinecraftVersion} constants.
     */
    private static final Map<String, MinecraftVersion> NAME_TO_PROTOCOL_CONSTANT = new HashMap<>();

    static {
        // Add all versions to the map
        for (MinecraftVersion version : values()) {
            NAME_TO_PROTOCOL_CONSTANT.put(version.getName(), version);
        }
    }

    private final int protocol;
    private final String name;

    MinecraftVersion(int protocol, String name) {
        this.protocol = protocol;
        this.name = name;
    }

    /**
     * Gets the {@link MinecraftVersion} for the given protocol.
     *
     * @param protocol the protocol as an int
     * @return the protocol version
     */
    public static List<MinecraftVersion> getVersions(int protocol) {
        return Arrays.stream(values()).filter(version -> version.getProtocol() == protocol).collect(Collectors.toList());
    }

    /**
     * Gets the latest {@link MinecraftVersion} for the given protocol.
     *
     * @param protocol the protocol as an int
     * @return the latest protocol version
     */
    public static MinecraftVersion getLatestVersion(int protocol) {
        return getVersions(protocol).get(getVersions(protocol).size() - 1);
    }

    /**
     * Gets all {@link MinecraftVersion}s that have the same protocol as the given version.
     *
     * @param version the version
     * @return the versions
     */
    public static List<MinecraftVersion> getSameAs(MinecraftVersion version) {
        return getVersions(version.getProtocol());
    }

    /**
     * Gets the {@link MinecraftVersion} for the given version name.
     *
     * @param version the version name in the format "XY.XY.XY", e.g. "1.8.8"
     * @return the protocol version
     */
    public static MinecraftVersion fromString(String version) {
        return NAME_TO_PROTOCOL_CONSTANT.getOrDefault(version, UNKNOWN);
    }

    /**
     * Returns the protocol as an int.
     *
     * @return the protocol version
     */
    public int getProtocol() {
        return protocol;
    }

    /**
     * Returns the user-friendly name for this protocol.
     *
     * @return the protocol name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }

}