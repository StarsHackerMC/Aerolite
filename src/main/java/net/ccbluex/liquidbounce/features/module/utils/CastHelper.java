package net.ccbluex.liquidbounce.features.module.utils;

import java.util.ArrayList;
import java.util.List;

import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class CastHelper {

    private final List<EntityType> casts = new ArrayList<>();

    public static EntityType isInstanceof(Entity entity, EntityType... types) {
        for (EntityType type : types) {
            if (entity == Minecraft.getMinecraft().thePlayer) {
                if (type == EntityType.SELF)
                    return type;
            }
            if (type == EntityType.VILLAGERS && entity instanceof EntityVillager) {
                return type;
            }
            if (type == EntityType.FRIENDS && entity instanceof EntityPlayer) {
                if (EntityUtils.INSTANCE.isFriend(entity.getName())) {
                    return type;
                }
            }
            if (type == EntityType.PLAYERS && entity instanceof EntityPlayer
                    && entity != Minecraft.getMinecraft().thePlayer && !EntityUtils.INSTANCE.isFriend(entity.getName())) {
                return type;
            }
            if (type == EntityType.MOBS && entity instanceof EntityMob) {
                return type;
            }
            if (type == EntityType.ANIMALS && (entity instanceof EntityAnimal || entity instanceof EntityGolem)) {
                return type;
            }
        }
        return null;
    }

    public CastHelper apply(EntityType type) {
        this.casts.add(type);
        return this;
    }

    public EntityType[] build() {
        return this.casts.toArray(new EntityType[0]);
    }

    public enum EntityType {
        PLAYERS, MOBS, ANIMALS, VILLAGERS, FRIENDS, SELF;
    }
}
