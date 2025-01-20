package com.equilibrium.persistent_state;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import static com.equilibrium.MITEequilibrium.MOD_ID;

public class StateSaverAndLoader extends PersistentState {


    public boolean savedValue=false;

    //写入一个关键字为"savedValue"的int变量
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        ;
        nbt.putBoolean("savedValue", savedValue);
        return nbt;
    }

    //若使用这个函数,则读取一个关键字为savedValue的变量,这个变量被初始化进StateSaverAndLoader对象中
    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader stateSaverAndLoader = new StateSaverAndLoader();
        stateSaverAndLoader.savedValue = tag.getBoolean("savedValue");
        return stateSaverAndLoader;
    }

    private static Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new, // 使用 lambda 创建实例
            (nbt, lookup) -> {
                StateSaverAndLoader state = new StateSaverAndLoader();
                state.savedValue = nbt.getBoolean("savedValue");
                return state;
            },
            null
    );

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        // (注：如需在任意维度生效，请使用 'World.OVERWORLD' ，不要使用 'World.END' 或 'World.NETHER')
        //拿到从服务器的所有nbt数据
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();


        //检索savedValue,若有,加载,若没有,创建
        // 当第一次调用了方法 'getOrCreate' 后，它会创建新的 'StateSaverAndLoader' 并将其存储于  'PersistentStateManager' 中。
        //  'getOrCreate' 的后续调用将本地的 'StateSaverAndLoader' NBT 传递给 'StateSaverAndLoader::createFromNbt'。
        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, MOD_ID);
        // 若状态未标记为脏(dirty)，当 Minecraft 关闭时， 'writeNbt' 不会被调用，相应地，没有数据会被保存。
        // 从技术上讲，只有在事实上发生数据变更时才应当将状态标记为脏(dirty)。
        // 但大多数开发者和模组作者会对他们的数据未能保存而感到困惑，所以不妨直接使用 'markDirty' 。
        // 另外，这只将对应的布尔值设定为 TRUE，代价是文件写入磁盘时模组的状态不会有任何改变。(这种情况非常少见)
        state.markDirty();

        return state;
    }








}
