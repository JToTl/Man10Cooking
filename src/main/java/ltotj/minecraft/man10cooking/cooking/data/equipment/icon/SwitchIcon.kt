package ltotj.minecraft.man10cooking.cooking.data.equipment.icon

import ltotj.minecraft.man10cooking.cooking.data.equipment.BaseEquipment
import ltotj.minecraft.man10cooking.utility.GUIManager.GUIItem
import org.bukkit.event.inventory.InventoryClickEvent

class SwitchIcon(private val iconData:Map<String,*>,private val baseEquipment: BaseEquipment):IconItem(iconData) {

    override fun onClick(guiItem: GUIItem, inventoryClickEvent: InventoryClickEvent) {
        inventoryClickEvent.isCancelled=true
        gui().changeItem(baseEquipment.icons[iconData["switch-icon"]]!!,this)
    }

}