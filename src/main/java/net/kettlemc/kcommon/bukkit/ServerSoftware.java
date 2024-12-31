package net.kettlemc.kcommon.bukkit;

import net.kettlemc.kcommon.java.ClassUtil;

public enum ServerSoftware {

    KETTING("org.kettingpowered.ketting.config.KettingConfig", "Ketting"),
    MAGMA_1_18("org.magmafoundation.magma.helpers.EnumJ17Helper", "Magma"),
    MAGMA_1_12("org.magmafoundation.magma.Magma", "Magma"),
    CATSERVER("catserver.server.CatServer", "CatServer"),
    CRUCIBLE( "io.github.crucible.Crucible", "Crucible"),
    URANIUM(null, "Uranium"),
    THERMOS("thermos.Thermos", "Thermos"),
    CAULDRON(null, "Cauldron"),

    NEO_FORGE("net.neoforged.neoforge.common.NeoForge", "NeoForge"),
    FORGE("net.minecraftforge.common.MinecraftForge", "Forge"),

    SPORT_PAPER("org.github.paperspigot.SharedConfig", "SportPaper"),
    NACHO("me.elier.nachospigot.config.NachoConfig", "Nacho", "NachoSpigot"),
    TACO("net.techcable.tacospigot.TacoSpigotConfig", "Taco", "TacoSpigot"),
    PURPUR("org.purpurmc.purpur.PurpurConfig", "Purpur", "PurpurMC"),
    PAPER("io.papermc.paper.configuration.Configuration", "Paper"),
    PAPER_LEGACY("com.destroystokyo.paper", "Paper (Legacy)"),
    PAPER_SPIGOT("org.github.paperspigot.PaperSpigotConfig", "PaperSpigot"),
    SPIGOT("org.spigotmc.SpigotConfig", "Spigot"),
    BUKKIT("org.bukkit.Bukkit", "CraftBukkit", "Bukkit"),

    UNKNOWN(null, "Unknown");

    private static ServerSoftware currentSoftware = null;
    private final String[] names;
    private final String classIdentifier;

    /**
     * @param identifierClass Class that identifies this platform
     * @param names    Names the platform could be known as
     */
    ServerSoftware(String identifierClass, String... names) {
        this.names = names;
        this.classIdentifier = identifierClass == null ? "" : identifierClass;
    }

    /**
     * @return Software the server is running on or the next highest one on the fork chain
     **/
    static ServerSoftware getServerSoftware() {

        // Don't check every time, it's not going to change
        if (currentSoftware == null) {
            // Order is important due to fork chain
            for (ServerSoftware software : values()) {
                if (ClassUtil.isClassPresent(software.getIdentifierClass())) {
                    currentSoftware = software;
                    break;
                }
            }
        }
        return currentSoftware;
    }

    /**
     * @return Name of the software
     **/
    public String getName() {
        return this.names[0];
    }

    /**
     * @return Names of the software
     **/
    public String[] getNames() {
        return this.names;
    }

    /**
     * @return Name of the identifier class
     **/
    public String getIdentifierClass() {
        return this.classIdentifier;
    }

}
