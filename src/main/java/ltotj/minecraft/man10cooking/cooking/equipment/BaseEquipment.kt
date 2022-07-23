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

abstract class BaseEquipment(displayName:String,private val innerName:String,row:Int,key: ArmorStand,private val availableSlot:Array<Int>): AnimationGUI(Main.plugin,row,displayName ) {

    private val keyItem= GUIItem(key.getItem(EquipmentSlot.HEAD))
    private val timeFromPreviousToCurrent=(Date().time/1000L)-keyItem.getNBTLong("LastTime",Main.plugin)
    private val elapsedTime= HashMap<Int,Int>()

    init {
        fillBlank(GUIItem(Material.WHITE_STAINED_GLASS_PANE,1)
                .setDisplay("")
                .setEvent { _, inventoryClickEvent ->
                    inventoryClickEvent.isCancelled=true
                })


        setClickEvent { _, inventoryClickEvent ->
            if((!inventoryClickEvent.isRightClick&&!inventoryClickEvent.isLeftClick)||inventoryClickEvent.isShiftClick){
                inventoryClickEvent.isCancelled=true
            }
        }

        setClickEvent { _, inventoryClickEvent ->
            if(inventoryClickEvent.slot !in availableSlot)return@setClickEvent
            if((inventoryClickEvent.clickedInventory?:return@setClickEvent).type== InventoryType.PLAYER)return@setClickEvent
            val item=inventoryClickEvent.currentItem?:return@setClickEvent
            if(!fit(item)){
                inventoryClickEvent.isCancelled
            }
            elapsedTime[inventoryClickEvent.slot]=0
        }
        for(slot in availableSlot) {
            val item = GUIItem(CookingFunc.itemFromBase64(keyItem.getNBTString("${innerName}${slot}", Main.plugin))
                    ?: continue)
            val time = if (keyItem.getNBTInt("Slot${slot}ElapsedTime", Main.plugin) < 0) 0 else keyItem.getNBTInt("Slot${slot}ElapsedTime", Main.plugin)
            elapsedTime[slot]=time+timeFromPreviousToCurrent.toInt()
        }
        setForcedCloseEvent{ _, inventoryCloseEvent ->
            if(invPlayerList.isEmpty()) {
                for (slot in availableSlot) {
                    keyItem.setNBTInt("Slot${slot}ElapsedTime",elapsedTime[slot]?:0,Main.plugin)
                    val item=getInvItem(slot)
                    val itemData = CookingFunc.itemToBase64(getInvItem(slot))
                    if(itemData==null){
                        keyItem.setNBTString("${innerName}Slot${slot}","",Main.plugin)
                        continue
                    }
                    if(itemData.toByteArray(Charsets.UTF_8).size<32766) {
                        keyItem.setNBTString("${innerName}Slot${slot}",itemData,Main.plugin)
                    }
                    else{
                        inventoryCloseEvent.player.world.dropItem(inventoryCloseEvent.player.location,item)
                    }
                }
                keyItem.setNBTLong("LastTime", Date().time/1000L,Main.plugin)
                key.setItem(EquipmentSlot.HEAD,keyItem)
            }
        }


    }

    private fun fit(item: ItemStack):Boolean{

        return true
    }

    fun fillBlank(item:GUIItem){
        for(slot in 0 until inv.size){
            if(!availableSlot.contains(slot)){
                setItem(slot,item)
            }
        }
    }




}