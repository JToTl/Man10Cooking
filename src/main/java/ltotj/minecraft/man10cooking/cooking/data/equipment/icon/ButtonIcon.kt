package ltotj.minecraft.man10cooking.cooking.data.equipment.icon

import org.bukkit.Material

class ButtonIcon(iconData:Map<String,*>):IconItem(iconData) {

    init {
        setEvent { guiItem, inventoryClickEvent ->
            inventoryClickEvent.isCancelled=true
            guiItem.addLore("クリックした")
            guiItem.gui().renderItem(inventoryClickEvent.slot)
        }
    }

}