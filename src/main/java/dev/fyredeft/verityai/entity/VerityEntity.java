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

    protected void registerGoals() {
        goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.85D));
        goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 16.0F));
    }

    @Override
    public void tick() {
        super.tick();
        companionTicks++;
        if (level.isClientSide || companionTicks % 80 != 0) return;

        PlayerEntity player = ownerUUID == null ? level.getNearestPlayer(this, 32.0D) : level.getPlayerByUUID(ownerUUID);
        if (player == null) return;

        int nights = player.getPersistentData().getInt("verityai_nights_slept");
        if (nights >= 3 && !VerityAI.STALKER_ENABLED.get()) return;
        double distance = distanceToSqr(player);

        // Friendly stage: casually catch up when the player wanders away.
        if (nights < 3) {
            if (distance > 36.0D) getNavigation().moveTo(player, 0.9D);
            return;
        }

        // After the third night, Verity keeps its distance instead of attacking.
        // It approaches only when far away and backs off if the player gets close.
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
