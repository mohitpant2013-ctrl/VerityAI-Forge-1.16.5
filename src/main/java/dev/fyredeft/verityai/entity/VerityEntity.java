package dev.fyredeft.verityai.entity;

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
        goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 12.0F));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        companionTicks++;
        if (!level.isClientSide && companionTicks % 100 == 0) {
            PlayerEntity player = level.getNearestPlayer(this, 16.0D);
            if (player != null && distanceToSqr(player) > 36.0D) {
                getNavigation().moveTo(player, 0.9D);
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
