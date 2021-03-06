package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeArrowProtect;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatTrackerArrowProtect extends StatTracker {
    public static HashMap<LivingArmour, Double> changeMap = new HashMap<LivingArmour, Double>();
    public static int[] damageRequired = new int[]{30, 200, 400, 800, 1500, 2500, 3500, 5000, 7000, 15000};
    public int totalDamage = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.arrowProtect";
    }

    @Override
    public void resetTracker() {
        this.totalDamage = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalDamage = tag.getInteger(BloodMagic.MODID + ".tracker.arrowProtect");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.arrowProtect", totalDamage);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour)) {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0) {
                totalDamage += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0d);

                this.markDirty();

                return true;
            }
        }

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour)) {
            changeMap.remove(livingArmour);
        }
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades() {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++) {
            if (totalDamage >= damageRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeArrowProtect(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalDamage, damageRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.arrowProtect");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeArrowProtect) {
            int level = upgrade.getUpgradeLevel();
            if (level < damageRequired.length) {
                totalDamage = Math.max(totalDamage, damageRequired[level]);
                this.markDirty();
            }
        }
    }

    public static void incrementCounter(LivingArmour armour, double damage) {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + damage : damage);
    }
}
