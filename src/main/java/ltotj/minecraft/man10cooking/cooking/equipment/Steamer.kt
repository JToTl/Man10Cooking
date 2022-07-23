package ltotj.minecraft.man10cooking.cooking.equipment

import ltotj.minecraft.man10cooking.Main
import ltotj.minecraft.man10cooking.cooking.CookingFunc
import ltotj.minecraft.man10cooking.utility.GUIManager.GUIItem
import ltotj.minecraft.man10morefish.utilities.GUIManager.menu.AnimationGUI
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

class Steamer(key:ArmorStand): AnimationGUI(Main.plugin,6,"蒸し器") {

    private val elapsedTime= arrayOf(0,0,0,0,0,0,0,0,0)

    init {

        val keyItem= GUIItem(key.getItem(EquipmentSlot.HEAD))
        val timeFromPreviousToCurrent=(Date().time/1000L)-keyItem.getNBTLong("LastTime",Main.plugin)
        val guiItem=GUIItem(Material.WHITE_STAINED_GLASS_PANE,1)
                .setDisplay("")
                .setEvent { _, inventoryClickEvent ->
                    inventoryClickEvent.isCancelled=true
                }
        fillItem(guiItem)
        removeItems(arrayOf(12,13,14,21,22,23,30,31,32))

        setClickEvent { _, inventoryClickEvent ->
            if((!inventoryClickEvent.isRightClick&&!inventoryClickEvent.isLeftClick)||inventoryClickEvent.isShiftClick){
                inventoryClickEvent.isCancelled=true
            }
        }

        setClickEvent { _, inventoryClickEvent ->
            if(inventoryClickEvent.slot !in arrayOf(12,13,14,21,22,23,30,31,32))return@setClickEvent
            if((inventoryClickEvent.clickedInventory?:return@setClickEvent).type==InventoryType.PLAYER)return@setClickEvent
            val item=inventoryClickEvent.currentItem?:return@setClickEvent
            if(!canBeSteamed(item)){
                inventoryClickEvent.isCancelled
            }
            elapsedTime[((inventoryClickEvent.slot-3)%9)+3*(-1+(inventoryClickEvent.slot-3)/9)]=0
        }
        for(slot in 0..8) {
            val item = GUIItem(CookingFunc.itemFromBase64(keyItem.getNBTString("SteamerSlot${slot + 1}", Main.plugin))
                    ?: continue)
            val time = if (keyItem.getNBTInt("Slot${slot}ElapsedTime", Main.plugin) < 0) 0 else keyItem.getNBTInt("Slot${slot}ElapsedTime", Main.plugin)
            println(time)
            elapsedTime[slot]=time+timeFromPreviousToCurrent.toInt()
            if(time+timeFromPreviousToCurrent>20){
                setItem(12 + (slot % 3) + 9 * (slot / 3),GUIItem(Material.DIAMOND,1))
            }
            else {
                setItem(12 + (slot % 3) + 9 * (slot / 3), item)
            }
        }
        setForcedCloseEvent{ _, inventoryCloseEvent ->
            if(invPlayerList.isEmpty()) {
                for (slot in 0..8) {
                    keyItem.setNBTInt("Slot${slot}ElapsedTime",elapsedTime[slot],Main.plugin)
                    val item=getInvItem(12+(slot%3)+9*(slot/3))
                    val itemData =CookingFunc.itemToBase64(getInvItem(12+(slot%3)+9*(slot/3)))
                    if(itemData==null){
                        keyItem.setNBTString("SteamerSlot${slot+1}","",Main.plugin)
                        continue
                    }
                    if(itemData.toByteArray(Charsets.UTF_8).size<32766) {
                        keyItem.setNBTString("SteamerSlot${slot+1}",itemData,Main.plugin)
                    }
                    else{
                        inventoryCloseEvent.player.world.dropItem(inventoryCloseEvent.player.location,item)
                    }
                }
                keyItem.setNBTLong("LastTime",Date().time/1000L,Main.plugin)
                key.setItem(EquipmentSlot.HEAD,keyItem)
            }
        }


    }

    private fun canBeSteamed(item:ItemStack):Boolean{

        return true
    }

}