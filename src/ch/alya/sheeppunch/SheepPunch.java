package ch.alya.sheeppunch;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Brings back punching sheep for wool (Alpha to Beta 1.6.6).
 *
 * Before shears were added in Beta 1.7, hitting a sheep made it drop wool and
 * left it sheared. This plugin brings that back with a single wool per sheep,
 * so shears (2 to 4 wool) stay the better way to get wool.
 */
public class SheepPunch extends JavaPlugin {

    private static final Logger LOG = Logger.getLogger("Minecraft");

    // block and item ids (Beta 1.7.3)
    private static final int WOOL = 35;
    private static final int SHEARS = 359;

    private final Random random = new Random();

    // ================================================================== lifecycle

    public void onEnable() {
        // Monitor: protection plugins have already decided, the hit will happen
        getServer().getPluginManager().registerEvent(
                Event.Type.ENTITY_DAMAGE, new Entities(),
                Event.Priority.Monitor, this);
        LOG.info("[SheepPunch] v" + getDescription().getVersion()
                + " enabled. Punching a sheep drops 1 wool.");
    }

    public void onDisable() {
        LOG.info("[SheepPunch] disabled.");
    }

    // ================================================================== wool

    /**
     * Shears the sheep and drops one wool in its colour.
     *
     * Same drop as vanilla shearing in EntitySheep.a(EntityHuman), just one item
     * instead of 2 to 4: spawned 1 block above the sheep's feet with a pickup
     * delay of 10 ticks (World.dropItem does both), plus the small extra push
     * vanilla gives every wool item.
     */
    private void dropWool(Sheep sheep) {
        sheep.setSheared(true);

        DyeColor color = sheep.getColor();
        short data = (color == null) ? 0 : color.getData();

        Location loc = sheep.getLocation().add(0.0D, 1.0D, 0.0D);
        Item item = sheep.getWorld().dropItem(loc, new ItemStack(WOOL, 1, data));

        Vector v = item.getVelocity();
        v.setX(v.getX() + (random.nextFloat() - random.nextFloat()) * 0.1F);
        v.setY(v.getY() + random.nextFloat() * 0.05F);
        v.setZ(v.getZ() + (random.nextFloat() - random.nextFloat()) * 0.1F);
        item.setVelocity(v);
    }

    // ================================================================== listeners

    private class Entities extends EntityListener {
        /**
         * A player's melee hit fires this event before the damage is applied
         * (EntityHuman.d(Entity)). Vanilla ignores the hit when the event is
         * cancelled or the damage is 0, so those are skipped here too.
         */
        public void onEntityDamage(EntityDamageEvent e) {
            if (e.isCancelled() || e.getDamage() <= 0) return;
            if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
            if (!(e instanceof EntityDamageByEntityEvent)) return;
            if (!(e.getEntity() instanceof Sheep)) return;

            Entity damager = ((EntityDamageByEntityEvent) e).getDamager();
            if (!(damager instanceof Player)) return;

            Sheep sheep = (Sheep) e.getEntity();
            // dying sheep can still be hit during the death animation, but
            // already dropped their wool on death
            if (sheep.isSheared() || sheep.isDead() || sheep.getHealth() <= 0) return;

            Player p = (Player) damager;

            // left-click with shears: no punch wool, right-click shears for 2 to 4
            ItemStack hand = p.getItemInHand();
            if (hand != null && hand.getTypeId() == SHEARS) return;

            if (!p.hasPermission("sheeppunch.use")) return;

            dropWool(sheep);
        }
    }
}
