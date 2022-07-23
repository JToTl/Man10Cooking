package ltotj.minecraft.man10cooking.cooking.data.equipment.icon

import org.bukkit.Material

class NoEffectIcon(iconData:Map<String,*>):IconItem(iconData) {

    init {

       setEvent { _, inventoryClickEvent ->
           inventoryClickEvent.isCancelled=true
       }

    }

}