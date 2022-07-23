package ltotj.minecraft.man10cooking

import ltotj.minecraft.man10cooking.command.OPCommand
import ltotj.minecraft.man10cooking.cooking.EquipmentEvent
import ltotj.minecraft.man10cooking.cooking.data.cookItem.CoItemData
import ltotj.minecraft.man10cooking.cooking.data.equipment.EquipmentData
import ltotj.minecraft.man10cooking.cooking.event.CoItemEvent
import ltotj.minecraft.man10cooking.utility.ConfigManager.ConfigManager
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffectType
import java.io.File
import java.util.*

class Main : JavaPlugin() {


    companion object{
        lateinit var plugin: JavaPlugin
        const val pluginTitle="Man10Cooking"
        val cookingItems=HashMap<String, CoItemData>()
        val equipments=HashMap<String,EquipmentData>()
        lateinit var con:FileConfiguration
        lateinit var equipmentsFile: File
        lateinit var recipesFile: File
        lateinit var coItemsFile: File
        var opCommand: OPCommand?=null

        fun loadConfig(){
            plugin.saveDefaultConfig()
            plugin.reloadConfig()
            con=plugin.config

            opCommand?.reloadCoItemComplete()
        }

        fun loadEquipments(){
            equipments.clear()
            equipmentsFile=File("${plugin.dataFolder.absolutePath}${File.separator}equipments")
            equipmentsFile.mkdir()
            if(con.getBoolean("load-default-equipments")){
                ConfigManager(plugin,"Sample_Equipments","equipments")
                        .saveDefConfig()
            }
            val fileList= equipmentsFile.listFiles()?:return
            for(file in fileList){
                val configFile=YamlConfiguration.loadConfiguration(file)
                for(key in configFile.getKeys(false)){
                    EquipmentData(key,configFile)
                }
            }
            opCommand?.reloadCoItemComplete()
        }

        fun loadRecipes(){
            recipesFile=File("${plugin.dataFolder.absolutePath}${File.separator}recipes")
            recipesFile.mkdir()
            if(con.getBoolean("load-default-recipes")){
                ConfigManager(plugin,"Sample_Recipes","recipes")
                        .saveDefConfig()
            }
        }

        fun loadCoItem(){
            cookingItems.clear()
            coItemsFile=File("${plugin.dataFolder.absolutePath}${File.separator}items")
            coItemsFile.mkdir()
            if(con.getBoolean("load-default-equipments")){
                ConfigManager(plugin,"Sample_Items","items")
                        .saveDefConfig()
            }
            val fileList=coItemsFile.listFiles()?:return
            for(file in fileList){
                val configFile=YamlConfiguration.loadConfiguration(file)

                for(key in configFile.getKeys(false)){
                    val item= CoItemData(key)
                            .setMaterial(Material.getMaterial(configFile.getString("${key}.type")?:"")?:Material.BARRIER)
                            .setDisplayName(configFile.getString("${key}.name"))
                            .setLore(configFile.getStringList("${key}.lore"))
                            .setCMD(configFile.getInt("${key}.cmd"))
                            .setFoodLevel(configFile.getInt("${key}.foodLevel"))
                            .setSaturation(configFile.getDouble("${key}.saturation").toFloat())
                            .setOverSaturation(configFile.getBoolean("${key}.over_saturation"))
                            .setCommands(configFile.getStringList("${key}.commands"))
                            .setRandomCommands(configFile.getList("${key}.random-commands"))
                    configFile.getConfigurationSection("${key}.potion")?.getKeys(false)?.forEach { potionKey ->
                        item.addPotionEffects( PotionEffectType.getByName(potionKey)!!,
                                configFile.getInt("${key}.potion.${potionKey}.duration"),
                                configFile.getInt("${key}.potion.${potionKey}.ambient"),
                                configFile.getBoolean("${key}.potion.${potionKey}.hide-particle"))
                    }
                    cookingItems[key]=item
                }
            }

            opCommand?.reloadCoItemComplete()
        }
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin=this
        loadConfig()
        loadEquipments()
        loadCoItem()
        loadRecipes()
        plugin.server.pluginManager.registerEvents(EquipmentEvent,this)
        plugin.server.pluginManager.registerEvents(CoItemEvent,this)
        opCommand=OPCommand()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


}