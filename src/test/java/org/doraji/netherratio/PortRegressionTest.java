package org.doraji.netherratio;

import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

class PortRegressionTest extends PluginTestBase {
    private PlayerMock player;

    @BeforeEach
    void preparePlayer() {
        nether.setEnvironment(World.Environment.NETHER);
        player = server.addPlayer();
        player.setOp(false);
        player.teleport(new Location(overworld, 800, 64, -1600));
    }

    @Test
    void ordinaryPlayerCanCalculateWithoutAdminPermission() {
        assertTrue(player.hasPermission("netherratio.calc"));
        assertFalse(player.hasPermission("netherratio.netherratio"));
        server.dispatchCommand(player, "netherratio calc 800 -1600");
        String message = player.nextMessage();
        assertNotNull(message);
        assertTrue(message.contains("100.0") && message.contains("-200.0"), message);
    }

    @Test
    void ordinaryPlayerCannotChangeRatio() {
        server.dispatchCommand(player, "netherratio set 1");
        assertEquals(8, plugin.getConfigManager().getDefaultRatio());
        assertTrue(player.nextMessage().contains("permission"));
    }

    @Test
    void nonFiniteCalculatorInputsAreRejected() {
        for (String input : new String[] {"NaN", "Infinity", "-Infinity"}) {
            server.dispatchCommand(player, "netherratio calc " + input + " 0");
            assertTrue(player.nextMessage().contains("Invalid coordinates"));
            while (player.nextMessage() != null) { /* Drain command usage messages. */ }
        }
    }

    @Test
    void cancelledPlayerPortalKeepsOriginalDestination() {
        Location destination = new Location(nether, 7, 64, 9);
        PlayerPortalEvent event = new PlayerPortalEvent(player, player.getLocation(), destination,
                PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
        event.setCancelled(true);
        server.getPluginManager().callEvent(event);
        assertTrue(event.isCancelled());
        assertEquals(destination, event.getTo());
    }

    @Test
    void cancelledEntityPortalKeepsOriginalDestination() {
        Location destination = new Location(nether, 7, 64, 9);
        EntityPortalEvent event = new EntityPortalEvent(player, player.getLocation(), destination,
                128, true, 16, PortalType.NETHER);
        event.setCancelled(true);
        server.getPluginManager().callEvent(event);
        assertEquals(destination, event.getTo());
    }
}
