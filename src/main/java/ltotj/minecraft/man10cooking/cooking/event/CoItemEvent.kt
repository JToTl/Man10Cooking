package ltotj.minecraft.man10cooking.cooking.event

import ltotj.minecraft.man10cooking.Main
import ltotj.minecraft.man10cooking.utility.ItemManager.ItemStackPlus
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

object CoItemEvent:Listener {

    @EventHandler
    fun eatCookedItem(e:PlayerItemConsumeEvent){
        if(e.isCancelled){
            return
        }
        val item=ItemStackPlus(e.item)
        val key=item.getNBTString("name",Main.plugin)
        if(Main.cookingItems.containsKey(key)) {
            e.isCancelled = true
            if (consume(e.player, e.item)) {
                Main.cookingItems[key]!!.use(e.player)
            }
            else{
                Main.plugin.logger.info("§4${e.player.name}.${key}でエラー発生")
            }
        }
    }

    private fun consume(player: Player, item:ItemStack):Boolean{
        if(player.inventory.itemInMainHand == item){
            player.inventory.setItemInMainHand(ItemStackPlus(item).setItemAmount(item.amount-1))
            return true
        }
        if(player.inventory.itemInOffHand==item){
            player.inventory.setItemInOffHand(ItemStackPlus(item).setItemAmount(item.amount-1))
            return true
        }
        return false
    }

}