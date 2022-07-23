package ltotj.minecraft.man10cooking.cooking

import ltotj.minecraft.man10cooking.cooking.equipment.Steamer
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object EquipmentEvent:Listener {

    @EventHandler
    fun test(e:PlayerInteractEvent){
        if((e.clickedBlock?:return).type== Material.BARRIER&&(e.hand?:return)==(EquipmentSlot.HAND)){
            val entities=e.clickedBlock!!.location.getNearbyLivingEntities(0.5,0.5)
            for(entity in entities){
                if(entity is ArmorStand){
                    Steamer(entity)
                            .open(e.player)
                    break
                }
            }
        }
    }

}