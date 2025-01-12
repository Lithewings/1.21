package com.equilibrium.config;


import com.equilibrium.MITEequilibrium;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class CommonConfig {
    private final static String fileConfigName = "crafttime";

    public final static String item_crafting_time = "item_crafting_time";

    public final static String item_crafttable_level = "item_crafttable_level";
    public final static String item_furnace_level = "item_furnace_level";

    public final static String fuel_item_level = "fuel_item_level";
    public final static String item_cooktime = "item_cooktime";


    public final static HashMap<String, Float> craftItemTimeMap = new HashMap<>();

    public final static HashMap<String, Integer> itemCrafttableLevelMap = new HashMap<>();
    public final static HashMap<String, Integer> itemFurnaceLevelMap = new HashMap<>();

    public final static HashMap<String, Integer> fuelItemLevelMap = new HashMap<>();
    public final static HashMap<String, Integer> itemCooktimeMap = new HashMap<>();



    public CommonConfig(){
    }

    //序列化配置文件:写入配置文件
    public JsonObject serialize(){
        JsonObject root = new JsonObject();//父类
        JsonObject entry = new JsonObject();//子条目
        JsonObject entry_items_crafting_time = new JsonObject();//子条目
        JsonObject entry_item_crafttable_level = new JsonObject();//子条目
        JsonObject entry_item_furnace_levell = new JsonObject();//子条目
        JsonObject entry_fuel_item_level = new JsonObject();//子条目
        JsonObject entry_item_cooktime = new JsonObject();//子条目

        entry.addProperty("desc0:", "若发现配置不生效，则是模组设定好的内容不可更改，如：铁锭的工作台等级为3");
        //物品合成时间========================================================================
        entry.addProperty("desc1:", "添加单个材料物品的合成所需时间,例如： " +
                "{\n" +
                "   \"minecraft:stick\": 20.0,\n" +
                "   \"minecraft:coal\": 20.0\n" +
                "}");
        entry.add(item_crafting_time,entry_items_crafting_time);
        craftItemTimeMap.forEach(entry_items_crafting_time::addProperty);

        //物品工作台等级========================================================================
        entry.addProperty("desc2:", "添加单个材料物品的所需工作台等级,FLINT_CRAFTING_TABLE = 1, COPPER_CRAFTING_TABLE = 2, IRON_CRAFTING_TABLE = 3, DIAMOND_CRAFTING_TABLE = 4, NETHERITE_CRAFTING_TABLE = 5" +
                "例如： " +
                "{\n" +
                "   \"minecraft:stick\": 1,\n" +
                "   \"minecraft:coal\": 2\n" +
                "}");
        entry.add(item_crafttable_level,entry_item_crafttable_level);
        itemCrafttableLevelMap.forEach(entry_item_crafttable_level::addProperty);

        //物品熔炼所需熔炉等级========================================================================
        entry.addProperty("desc3:", "添加物品熔炼所需熔炉等级,CLAY_FURNACE = 0, FURNACE = 1, BLAST_FURNACE = 1,OBSIDIAN_FURNACE = 2, NETHERRACK_FURNACE = 3" +
                "例如： " +
                "{\n" +
                "   \"minecraft:raw_copper\": 1,\n" +
                "   \"minecraft:raw_iron\": 2\n" +
                "}");
        entry.add(item_furnace_level,entry_item_furnace_levell);
        itemFurnaceLevelMap.forEach(entry_item_furnace_levell::addProperty);

        //燃料的等级========================================================================
        entry.addProperty("desc4:", "添加燃料的等级,CLAY_FURNACE = 0, FURNACE = 1, BLAST_FURNACE = 1,OBSIDIAN_FURNACE = 2, NETHERRACK_FURNACE = 3" +
                "例如： " +
                "{\n" +
                "   \"minecraft:stick\": 1,\n" +
                "   \"minecraft:coal\": 2\n" +
                "}");
        entry.add(fuel_item_level,entry_fuel_item_level);
        fuelItemLevelMap.forEach(entry_fuel_item_level::addProperty);

        //物品在熔炉中烧炼所需时间========================================================================
        entry.addProperty("desc5:", "添加物品在熔炉中烧炼所需时间,minecraft:iron_ore为200,minecraft:deepslate_iron_ore为802" +
                "例如： " +
                "{\n" +
                "   \"minecraft:iron_ore\": 200,\n" +
                "   \"minecraft:deepslate_iron_ore\": 802\n" +
                "}");
        entry.add(item_cooktime,entry_item_cooktime);
        itemCooktimeMap.forEach(entry_item_cooktime::addProperty);

        root.add(fileConfigName, entry);//创建父类条目名称，并把子条目添加进去
        return root;
    }

    //反序列化：获取配置文件内容
    public void deserialize(JsonObject data) {
        if (data == null) {
            MITEequilibrium.LOGGER.error("Config file was empty!");
        } else {
            try {
                craftItemLevelsMapGet(data);
                itemCrafttableLevelMapGet(data);
                itemFurnaceLevelMapGet(data);
                fuelItemLevelMapGet(data);
                itemCooktimeMapGet(data);

            } catch (Exception var3) {
                MITEequilibrium.LOGGER.error("Could not parse config file", var3);
            }

        }
    }

    //获取配置文件中：物品的工作台等级
    public void craftItemLevelsMapGet(JsonObject data){
        JsonObject object =data.get(fileConfigName).getAsJsonObject();
        JsonObject items_crafting_table = object.getAsJsonObject(item_crafting_time);//子目录
        craftItemTimeMap.clear();
        items_crafting_table.entrySet().forEach(i -> {
            String item = i.getKey();
            float value = i.getValue().getAsFloat();
            craftItemTimeMap.put(item,value);
        });
    }

    //获取配置文件中：单个材料物品的合成所需时间
    public void itemCrafttableLevelMapGet(JsonObject data){
        JsonObject object =data.get(fileConfigName).getAsJsonObject();
        JsonObject items_crafting_table = object.getAsJsonObject(item_crafttable_level);//子目录
        itemCrafttableLevelMap.clear();
        items_crafting_table.entrySet().forEach(i -> {
            String item = i.getKey();
            int value = i.getValue().getAsInt();
            itemCrafttableLevelMap.put(item,value);
        });
    }

    //获取配置文件中：物品熔炼所需熔炉等级
    public void itemFurnaceLevelMapGet(JsonObject data){
        JsonObject object =data.get(fileConfigName).getAsJsonObject();
        JsonObject items_crafting_table = object.getAsJsonObject(item_furnace_level);//子目录
        itemFurnaceLevelMap.clear();
        items_crafting_table.entrySet().forEach(i -> {
            String item = i.getKey();
            int value = i.getValue().getAsInt();
            itemFurnaceLevelMap.put(item,value);
        });
    }

    //获取配置文件中：物品熔炼所需熔炉等级
    public void fuelItemLevelMapGet(JsonObject data){
        JsonObject object =data.get(fileConfigName).getAsJsonObject();
        JsonObject items_crafting_table = object.getAsJsonObject(fuel_item_level);//子目录
        fuelItemLevelMap.clear();
        items_crafting_table.entrySet().forEach(i -> {
            String item = i.getKey();
            int value = i.getValue().getAsInt();
            fuelItemLevelMap.put(item,value);
        });
    }

    //获取配置文件中：物品熔炼所需熔炉等级
    public void itemCooktimeMapGet(JsonObject data){
        JsonObject object =data.get(fileConfigName).getAsJsonObject();
        JsonObject items_crafting_table = object.getAsJsonObject(item_cooktime);//子目录
        itemCooktimeMap.clear();
        items_crafting_table.entrySet().forEach(i -> {
            String item = i.getKey();
            int value = i.getValue().getAsInt();
            itemCooktimeMap.put(item,value);
        });
    }
}
