package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.core.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.ritual.data.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.data.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.tile.base.TileBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileImperfectRitualStone extends TileBase implements IImperfectRitualStone {
    // IImperfectRitualStone

    @Override
    public boolean performRitual(World world, BlockPos pos, ImperfectRitual imperfectRitual, EntityPlayer player) {
        if (imperfectRitual != null && ImperfectRitualRegistry.ritualEnabled(imperfectRitual)) {
            if (!PlayerHelper.isFakePlayer(player) && !world.isRemote) {
                NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, imperfectRitual.getActivationCost());
                if (imperfectRitual.onActivate(this, player)) {
                    if (imperfectRitual.isLightshow())
                        getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), getPos().getX(), getPos().getY() + 2, getPos().getZ(), true));
                    return true;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public World getRitualWorld() {
        return getWorld();
    }

    @Override
    public BlockPos getRitualPos() {
        return getPos();
    }
}
