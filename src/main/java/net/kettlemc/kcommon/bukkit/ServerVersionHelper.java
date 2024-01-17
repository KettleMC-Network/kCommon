package net.kettlemc.kcommon.bukkit;

import net.kettlemc.kcommon.java.ClassUtil;
import org.bukkit.Bukkit;

import java.util.regex.Pattern;

public class ServerVersionHelper {

    private static final String FORGE_CLASS = "net.minecraftforge.common.MinecraftForge";
    private static final Pattern MC_VERSION_PATTERN = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+", Pattern.CASE_INSENSITIVE);

    private static final boolean forgeSupport = ClassUtil.isClassPresent(FORGE_CLASS);
    private final static MinecraftVersion version = getVersion(Bukkit.getVersion());
    private final static ServerSoftware serverSoftware = ServerSoftware.getServerSoftware();

    // This server is running Paper version git-Paper-1618 (MC: 1.12.2) (Implementing API version 1.12.2-R0.1-SNAPSHOT)
    // Current: git-Purpur-2042 (MC: 1.20.1)
    // This server is running Crucible | https://github.com/CrucibleMC/Crucible | 1.7.10-R0.1-SNAPSHOT | 1.7.10-staging-3aba0ea3 (MC: 1.7.10)

    private static MinecraftVersion getVersion(String versionString) {
        String version = versionString
                .substring(versionString.indexOf("(MC: ") + 5)
                .substring(0, versionString.indexOf(")"));

        if (MC_VERSION_PATTERN.matcher(version).matches()) {
            return MinecraftVersion.fromString(version);
        } else {
            return MinecraftVersion.UNKNOWN;
        }
    }

    public static boolean hasForgeSupport() {
        return forgeSupport;
    }

    public static MinecraftVersion getVersion() {
        return version;
    }

    public static ServerSoftware getServerSoftware() {
        return serverSoftware;
    }

}