package ltotj.minecraft.man10cooking.cooking.data.equipment.icon

import ltotj.minecraft.man10cooking.utility.GUIManager.GUIItem
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class ButtonIcon(iconData:Map<String,*>):IconItem(iconData) {

    override fun onClick(guiItem: GUIItem, inventoryClickEvent: InventoryClickEvent) {
        inventoryClickEvent.isCancelled=true
        guiItem.addLore("クリックした")
        guiItem.gui().renderItem(inventoryClickEvent.slot)
    }

}