package ltotj.minecraft.man10cooking.cooking.data.equipment.icon

import ltotj.minecraft.man10cooking.utility.GUIManager.GUIItem
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class IconItem:GUIItem{

    lateinit var iconType: IconType

    constructor(material:Material,amount:Int) : super(material, amount){

    }

    constructor(item:ItemStack):super(item){

    }

    constructor(iconData:Map<String,*>):super(Material.valueOf(iconData["material"] as String),1){

        iconType= IconType.valueOf(iconData["type"] as String)
        iconData["name"]?.let { setDisplay(it as String) }
        iconData["lore"]?.let {
            for(str in it as List<String>){
                addLore(str)
            }
        }
    }
//
//    open fun onClick(e: InventoryClickEvent){
//
//    }

}