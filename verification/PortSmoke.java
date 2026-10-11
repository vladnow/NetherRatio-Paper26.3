package verification;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.doraji.netherratio.NetherRatio;

/** Synthetic event integration checks on real Paper; not a client gameplay test. */
public class PortSmoke extends JavaPlugin {
    private int checks;
    private NetherRatio subject;
    private World overworld;
    private World nether;
    private Entity entity;

    @Override
    public void onEnable() {
        getServer().getScheduler().runTask(this, () -> {
            try {
                subject = (NetherRatio) getServer().getPluginManager().getPlugin("NetherRatio");
                check(subject != null && subject.isEnabled(), "Plugin enabled");
                overworld = getServer().getWorlds().stream()
                        .filter(w -> w.getEnvironment() == World.Environment.NORMAL).findFirst().orElseThrow();
                nether = getServer().getWorlds().stream()
                        .filter(w -> w.getEnvironment() == World.Environment.NETHER).findFirst().orElseThrow();
                subject.getConfig().set("world-pairs", null);
                subject.getConfig().set("coordinate-bounds.enabled", false);
                subject.getConfig().set("world-pairs." + overworld.getName(), nether.getName());
                subject.saveConfig();
                subject.getConfigManager().reload();
                entity = overworld.spawnEntity(new Location(overworld, 0.5, 5, 0.5), EntityType.PIG);

                verify(8, 800, -1600, 100, -200);
                verify(1, 800, -1600, 800, -1600);
                verify(16, -1600, 800, -100, 50);
                subject.getConfig().set("world-pairs." + overworld.getName(), null);
                String path = "world-pairs." + overworld.getName() + ".";
                subject.getConfig().set(path + "nether", nether.getName());
                subject.getConfig().set(path + "ratio", 8);
                subject.getConfig().set(path + "offset-x", 1000);
                subject.getConfig().set(path + "offset-z", -500);
                subject.saveConfig();
                subject.getConfigManager().reload();
                Location forward = travel(overworld, 800, -1600, nether, PortalType.NETHER, false).getTo();
                check(close(forward.getX(), 1100) && close(forward.getZ(), -700), "Offsets forward");
                Location back = travel(nether, 1100, -700, overworld, PortalType.NETHER, false).getTo();
                check(close(back.getX(), 800) && close(back.getZ(), -1600), "Offsets reverse");

                EntityPortalEvent cancelled = travel(overworld, 800, 0, nether, PortalType.NETHER, true);
                check(cancelled.isCancelled() && close(cancelled.getTo().getX(), 7), "Cancelled event preserved");
                check(close(travel(overworld, 800, 0, nether, PortalType.ENDER, false).getTo().getX(), 7), "End portal unchanged");
                check(close(travel(overworld, 800, 0, nether, PortalType.END_GATEWAY, false).getTo().getX(), 7), "End gateway unchanged");

                subject.getConfig().set("coordinate-bounds.enabled", true);
                subject.getConfig().set("coordinate-bounds.min-x", -100);
                subject.getConfig().set("coordinate-bounds.max-x", 100);
                subject.getConfig().set("coordinate-bounds.min-z", -100);
                subject.getConfig().set("coordinate-bounds.max-z", 100);
                subject.saveConfig();
                subject.getConfigManager().reload();
                Location bounded = travel(overworld, 800, -1600, nether, PortalType.NETHER, false).getTo();
                check(close(bounded.getX(), 100) && close(bounded.getZ(), -100), "Configured bounds clamp");
                check(bounded.getY() < nether.getMinHeight() + nether.getLogicalHeight(), "Destination Y within logical height");

                getServer().dispatchCommand(getServer().getConsoleSender(), "netherratio list");
                getServer().dispatchCommand(getServer().getConsoleSender(), "netherratio set 1 " + overworld.getName());
                check(close(subject.getConfigManager().getRatioForWorld(overworld.getName()), 1), "Set world command");
                getServer().dispatchCommand(getServer().getConsoleSender(), "netherratio reload");
                check(close(subject.getConfigManager().getRatioForWorld(overworld.getName()), 1), "Reload persists ratio");
                getLogger().info("NETHERRATIO_SMOKE_PASS checks=" + checks + " server=" + getServer().getVersion());
            } catch (Throwable failure) {
                getLogger().log(java.util.logging.Level.SEVERE, "NETHERRATIO_SMOKE_FAIL", failure);
            } finally {
                if (entity != null) entity.remove();
                getServer().shutdown();
            }
        });
    }

    private void verify(double ratio, double x, double z, double expectedX, double expectedZ) {
        subject.getConfigManager().setDefaultRatio(ratio);
        Location to = travel(overworld, x, z, nether, PortalType.NETHER, false).getTo();
        check(to.getWorld() == nether && close(to.getX(), expectedX) && close(to.getZ(), expectedZ), "Forward ratio " + ratio);
        Location back = travel(nether, expectedX, expectedZ, overworld, PortalType.NETHER, false).getTo();
        check(back.getWorld() == overworld && close(back.getX(), x) && close(back.getZ(), z), "Reverse ratio " + ratio);
    }

    private EntityPortalEvent travel(World source, double x, double z, World destination, PortalType type, boolean cancelled) {
        EntityPortalEvent event = new EntityPortalEvent(entity, new Location(source, x, 200, z),
                new Location(destination, 7, 64, 9), 128, true, 16, type);
        event.setCancelled(cancelled);
        getServer().getPluginManager().callEvent(event);
        return event;
    }

    private boolean close(double a, double b) { return Math.abs(a - b) < 0.0001; }
    private void check(boolean pass, String label) {
        if (!pass) throw new AssertionError(label);
        checks++;
        getLogger().info("PASS " + label);
    }
}
