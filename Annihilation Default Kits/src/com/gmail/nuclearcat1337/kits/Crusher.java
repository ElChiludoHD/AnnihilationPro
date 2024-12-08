package com.gmail.nuclearcat1337.kits;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.Kit;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crusher extends ConfigurableKit {

    private double bonusHitChance;

    @Override
    protected void setUp() {
        // No specific setup required for this kit
    }

    @Override
    protected String getInternalName() {
        return "Crusher";
    }

    @Override
    protected ItemStack getIcon() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Override
    protected int setDefaults(ConfigurationSection section) {
        return ConfigManager.setDefaultIfNotSet(section, "BonusHitChance", 0.25); // 25% chance by default
    }

    @Override
    protected void loadKitStuff(ConfigurationSection section) {
        super.loadKitStuff(section);
        this.bonusHitChance = section.getDouble("BonusHitChance", 0.25);
    }

    @Override
    protected List<String> getDefaultDescription() {
        List<String> description = new ArrayList<>();
        description.add(ChatColor.AQUA + "You are the breaker.");
        description.add("");
        description.add(ChatColor.AQUA + "Every time you mine the enemy");
        description.add(ChatColor.AQUA + "nexus, you have a chance");
        description.add(ChatColor.AQUA + "to deal an extra point of damage.");
        return description;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNexusBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Check if the block is endstone (nexus block)
        if (block.getType() == Material.END_STONE) {
            AnniPlayer anniPlayer = AnniPlayer.getPlayer(player.getUniqueId());

            // Ensure the player has this kit
            if (anniPlayer != null && anniPlayer.getKit().equals(this)) {
                // Roll for bonus damage
                if (new Random().nextDouble() < bonusHitChance) {
                    // Assuming nexus damage is handled elsewhere, call a method to deal extra damage
                    anniPlayer.getTeam().getNexus().damage(1); // Adds 1 extra point of damage

                    // Notify the player
                    player.sendMessage(ChatColor.GREEN + "You dealt an extra point of damage to the nexus!");
                }
            }
        }
    }

    @Override
    protected Loadout getFinalLoadout() {
        return new Loadout().addItem(KitUtils.addSoulbound(new ItemStack(Material.DIAMOND_PICKAXE)));
    }

    @Override
    public void onPlayerSpawn(Player player) {
        super.onPlayerSpawn(player);
    }
}
