package ltotj.minecraft.man10cooking.cooking.data.equipment

import ltotj.minecraft.man10cooking.Main
import ltotj.minecraft.man10cooking.cooking.data.equipment.icon.*
import ltotj.minecraft.man10morefish.utilities.GUIManager.menu.AnimationGUI
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.util.*

open class BaseEquipment(val name:String, con:YamlConfiguration)  {

    val icons= HashMap<String, IconItem?>()
    private val slots= HashMap<Int,String>()
    val displayName=con.getString("${name}.name")?:"未設定"
    var row=0



    init {

        val equipment=con.getConfigurationSection(name)!!

        for(iconData in equipment.get("icons") as List<Map<String,*>>){
            addIcon(iconData)
        }

        val slotList=equipment.getStringList("slots")
        for((row,slotIcons) in slotList.withIndex()){
            for(i in 0 until 9){
                slots[9*row+i]=slotIcons.substring(i,i+1)
            }
        }
        this.row=equipment.getStringList("slots").size

        Main.equipments[name]=this
    }

    private fun addIcon(data:Map<String,*>){
        when(IconType.valueOf((data["type"] as String))){
            IconType.NoEffect->{
                icons[data["icon"] as String]= NoEffectIcon(data)
            }
            IconType.Button->{
                icons[data["icon"] as String]= ButtonIcon(data)
            }
            IconType.Pocket->{
                icons[data["icon"] as String]=null
            }
            IconType.Switch->{
                icons[data["icon"] as String]= SwitchIcon(data,this)
            }
        }
    }

    fun getGUI(): AnimationGUI {
        val gui= AnimationGUI(Main.plugin,row,displayName)
        for(slot in slots.keys){
            gui.setItem(slot,icons[slots[slot]]?:continue)
        }
        return gui
    }

}