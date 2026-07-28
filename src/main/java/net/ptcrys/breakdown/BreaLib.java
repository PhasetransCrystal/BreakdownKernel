package net.ptcrys.breakdown;

import net.ptcrys.breakdown.utils.FormattingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class BreaLib {

    public static final String Core_ID = "breakdown";
    public static final String NAME = "瓦解";
    public static final Logger LOGGER = BreaLib.getLogger("Kernal");
    @Getter
    static ModContainer modContainer;
    @Getter
    static IEventBus modEventBus;
    private static final Identifier TEMPLATE_LOCATION = Identifier.fromNamespaceAndPath(BreaLib.Core_ID, "");

    public static Logger getLogger(String subModule) {
        return getLogger(subModule, null);
    }

    public static Logger getLogger(String subModule, @Nullable String suffix) {
        return LogManager.getLogger("Breakdown." + subModule + (suffix == null ? "" : ":" + suffix));
    }

    public static Identifier id(String path) {
        if (Strings.isBlank(path)) {
            return TEMPLATE_LOCATION;
        }
        return TEMPLATE_LOCATION.withPath(FormattingUtil.toLowerCaseUnder(path));
    }

    public static String appendIdString(String id) {
        return id.indexOf(':') == -1 ? (BreaLib.Core_ID + ":" + id) : id;
    }

    public static Identifier appendId(String id) {
        String[] strings = new String[] { BreaLib.Core_ID, id };
        int i = id.indexOf(':');
        if (i >= 0) {
            strings[1] = id.substring(i + 1);
            if (i >= 1) {
                strings[0] = id.substring(0, i);
            }
        }
        return Identifier.fromNamespaceAndPath(strings[0], strings[1]);
    }

    public static boolean isProd() {
        return FMLEnvironment.isProduction();
    }

    public static boolean isDev() {
        return !isProd();
    }

    public static boolean isDataGen() {
        return DatagenModLoader.isRunningDataGen();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isClientSide() {
        return FMLEnvironment.getDist().isClient();
    }

    public static boolean isClientThread() {
        return isClientSide() && Minecraft.getInstance().isSameThread();
    }

    public static boolean canGetServerLevel() {
        if (isClientSide()) {
            return Minecraft.getInstance().level != null;
        }
        var server = getMinecraftServer();
        return server != null &&
                !(server.isStopped() || server.isShutdown() || !server.isRunning() || server.isCurrentlySaving());
    }

    public static MinecraftServer getMinecraftServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    public static String platformName() {
        return "NeoForge";
    }

    public static boolean isForge() {
        return true;
    }

    public static Path getGamePath() {
        return FMLLoader.getCurrent().getGameDir();
    }

    public static boolean isRemote() {
        if (isClientSide()) {
            return Minecraft.getInstance().isSameThread();
        }
        return false;
    }
}
