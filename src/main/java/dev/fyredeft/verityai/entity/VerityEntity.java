package dev.fyredeft.verityai.entity;

import dev.fyredeft.verityai.VerityAI;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import java.util.UUID;
import net.minecraft.world.World;

/**
 * Personal companion with a calm first stage and a distant, non-hostile
 * watcher stage. The custom name is synced by vanilla entity metadata and
 * also tells the client renderer which model silhouette to display.
 */
public class VerityEntity extends CreatureEntity {
    private int companionTicks;
    private UUID ownerUUID;

    public VerityEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        setPersistenceRequired();
        setCustomName(new StringTextComponent("Verity"));
    }

    public void setOwnerUUID(UUID owner) { this.ownerUUID = owner; }
    public UUID getOwnerUUID() { return ownerUUID; }

    public boolean isStalkerStage() {
        return getCustomName() != null && "The Watcher".equals(getCustomName().getString());
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        if (ownerUUID != null) tag.putUUID("VerityOwner", ownerUUID);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("VerityOwner")) ownerUUID = tag.getUUID("VerityOwner");
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.85D));
        goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 16.0F));
    }

    @Override
    public void tick() {
        super.tick();
        companionTicks++;
        if (level.isClientSide || companionTicks % 40 != 0) return;

        PlayerEntity player = ownerUUID == null
                ? level.getNearestPlayer(this, 32.0D)
                : level.getPlayerByUUID(ownerUUID);
        if (player == null || !player.isAlive()) return;

        int nights = player.getPersistentData().getInt("verityai_nights_slept");
        boolean stalking = nights >= 3 && VerityAI.STALKER_ENABLED.get();
        setCustomName(new StringTextComponent(stalking ? "The Watcher" : "Verity"));

        double distance = distanceToSqr(player);

        // Survival awareness: when the owner is hurt or low on health,
        // Verity stops idling and moves close enough to be noticed.
        boolean ownerNeedsHelp = player.hurtTime > 0 || player.getHealth() <= 7.0F;
        if (!stalking && ownerNeedsHelp) {
            if (distance > 16.0D) getNavigation().moveTo(player, 1.05D);
            else {
                getNavigation().stop();
                getLookControl().setLookAt(player, 35.0F, 35.0F);
            }
            return;
        }

        // Friendly stage: follow reliably without crowding the player.
        if (!stalking) {
            if (distance > 36.0D) getNavigation().moveTo(player, 0.95D);
            else if (distance < 9.0D) getNavigation().stop();
            return;
        }

        // Watcher stage: keep a deliberate distance and never attack.
        if (distance > 225.0D) {
            getNavigation().moveTo(player, 0.72D);
        } else if (distance < 64.0D) {
            double dx = getX() - player.getX();
            double dz = getZ() - player.getZ();
            double length = Math.max(0.001D, Math.sqrt(dx * dx + dz * dz));
            double retreatX = getX() + (dx / length) * 7.0D;
            double retreatZ = getZ() + (dz / length) * 7.0D;
            getNavigation().moveTo(retreatX, getY(), retreatZ, 0.85D);
        } else {
            getNavigation().stop();
            getLookControl().setLookAt(player, 30.0F, 30.0F);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
