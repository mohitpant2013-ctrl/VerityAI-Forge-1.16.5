package dev.fyredeft.verityai.entity;

import dev.fyredeft.verityai.VerityAI;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class VerityEntity extends CreatureEntity {
    private int companionTicks;

    public VerityEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        setPersistenceRequired();
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.85D));
        goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 16.0F));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        companionTicks++;
        if (level.isClientSide || companionTicks % 80 != 0) return;

        PlayerEntity player = level.getNearestPlayer(this, 32.0D);
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
