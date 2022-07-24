package ltotj.minecraft.man10cooking.cooking.data.equipment.icon

import ltotj.minecraft.man10cooking.utility.GUIManager.GUIItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import kotlin.properties.Delegates

abstract class IconItem:GUIItem{

    init {
        setEvent { guiItem, inventoryClickEvent ->
            onClick(guiItem,inventoryClickEvent)
            playSound(inventoryClickEvent)
        }
    }

    lateinit var iconType: IconType
    var sound: String?=null
    var sound_volume=1F
    var sound_pitch=1F

    constructor(material:Material,amount:Int) : super(material, amount){

    }

    constructor(item:ItemStack):super(item){

    }

    constructor(iconData:Map<String,*>):super(Material.valueOf(iconData["material"] as String),1){

        iconType= IconType.valueOf(iconData["type"] as String)
        iconData["name"]?.let { setDisplay((it as String).replace("&","§")) }
        iconData["lore"]?.let {
            for(str in it as List<String>){
                addLore(str)
            }
        }
        iconData["sound"]?.let {
            val soundData=(it as Map<String,*>)
            sound=soundData["sound"] as String
            sound_volume=(soundData["volume"] as Int).toFloat()
            sound_pitch=(soundData["pitch"] as Int).toFloat()
        }
    }

    open fun playSound(inventoryClickEvent:InventoryClickEvent){
        if(sound==null){
            return
        }
        val player=inventoryClickEvent.whoClicked as Player
        player.playSound(player.location,sound!!,sound_volume,sound_pitch)
    }

    open fun onClick(guiItem: GUIItem,inventoryClickEvent: InventoryClickEvent){

    }

}