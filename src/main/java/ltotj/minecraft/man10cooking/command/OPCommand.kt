package ltotj.minecraft.man10cooking.command

import ltotj.minecraft.man10cooking.Main
import ltotj.minecraft.man10cooking.cooking.data.cookItem.CoItemData
import ltotj.minecraft.man10cooking.utility.CommandManager.CommandArgumentType
import ltotj.minecraft.man10cooking.utility.CommandManager.CommandManager
import ltotj.minecraft.man10cooking.utility.CommandManager.CommandObject
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class OPCommand: CommandManager( Main.plugin,"mcookop",Main.pluginTitle) {

    private val giveObject=CommandObject(Main.cookingItems.keys,"料理名")
            .setFunction{
                val sender=it.first
                if(sender !is Player){
                    sender.sendMessage("${Main.pluginTitle}§4プレイヤーのみが実行可能です")
                    return@setFunction
                }
                val itemName=it.second[1]
                if(!Main.cookingItems.containsKey(itemName)){
                    sender.sendMessage("${Main.pluginTitle}§4指定したアイテムが見つかりませんでした")
                    return@setFunction
                }
                giveCoItem(sender,Main.cookingItems[itemName]!!)
            }

    private val givePObject=CommandObject(Main.cookingItems.keys,"料理名")
            .setFunction{
                val player=Bukkit.getPlayer(it.second[1])
                if(player==null){
                    it.first.sendMessage("${Main.pluginTitle}§4プレイヤーが見つかりませんでした")
                    return@setFunction
                }
                val itemName=it.second[2]
                if(!Main.cookingItems.containsKey(itemName)){
                    it.first.sendMessage("${Main.pluginTitle}§4指定したアイテムが見つかりませんでした")
                    return@setFunction
                }
                giveCoItem(player,Main.cookingItems[itemName]!!)
            }

    private val openEquipmentObject=CommandObject(Main.equipments.keys,"設備名")
            .setFunction{
                val sender=it.first
                if(sender !is Player){
                    sender.sendMessage("${Main.pluginTitle}§4プレイヤーのみが実行可能です")
                    return@setFunction
                }
                val equipmentName=it.second[2]
                if(!Main.equipments.containsKey(equipmentName)){
                    it.first.sendMessage("${Main.pluginTitle}§4指定した設備が見つかりませんでした")
                    return@setFunction
                }
                Main.equipments[equipmentName]!!.getGUI().open(sender)
            }

    init {
        addFirstArgument(
                CommandObject("test")
                        .setFunction{
                            val p=it.first as Player
                            val armorStand= p.world.spawnEntity(p.location,EntityType.ARMOR_STAND) as ArmorStand
                            armorStand.setItem(EquipmentSlot.HEAD, ItemStack(Material.DIAMOND))
                            p.location.block.type = Material.BARRIER
                        }
        )
        addFirstArgument(
                CommandObject("give")
                        .addNextArgument(
                                giveObject
                        )
                        .addNextArgument(
                                CommandObject(CommandArgumentType.ONLINE_PlAYER)
                                        .addNextArgument(
                                                givePObject
                                        )
                        )
                        .setExplanation("指定した料理を与えます")
        )
        addFirstArgument(
                CommandObject("reload")
                        .setNullable(true)
                        .addNextArgument(
                                CommandObject("item")
                                        .setFunction{
                                            Main.loadCoItem()
                                            it.first.sendMessage("${Main.pluginTitle}§aアイテムをリロードしました")
                                        }
                        )
                        .addNextArgument(
                                CommandObject("equipment")
                                        .setFunction{
                                            Main.loadEquipments()
                                            it.first.sendMessage("${Main.pluginTitle}§a設備をリロードしました")
                                        }
                        )
                        .addNextArgument(
                                CommandObject("recipe")
                                        .setFunction{
                                            Main.loadRecipes()
                                            it.first.sendMessage("${Main.pluginTitle}§aレシピをリロードしました")
                                        }
                        )
                        .addNextArgument(
                                CommandObject("config")
                                        .setFunction{
                                            Main.loadConfig()
                                            it.first.sendMessage("${Main.pluginTitle}§aコンフィグをリロードしました")
                                        }
                        )
                        .setFunction{
                            Main.loadConfig()
                            Main.loadCoItem()
                            Main.loadEquipments()
                            Main.loadRecipes()
                            it.first.sendMessage("${Main.pluginTitle}§aファイルを全てをリロードしました")
                        }
                        .setExplanation("指定種類のファイルをリロードします")
        )

        addFirstArgument(
                CommandObject("equipment")
                        .addNextArgument(
                                CommandObject("open")
                                        .addNextArgument(
                                                openEquipmentObject
                                        )
                        )
        )

    }

    fun reloadCoItemComplete(){
        giveObject.setArguments(Main.cookingItems.keys)
        givePObject.setArguments(Main.cookingItems.keys)
        openEquipmentObject.setArguments(Main.equipments.keys)
    }

    private fun giveCoItem(player:Player,coItem: CoItemData){
        player.inventory.addItem(coItem.generate())
    }
}