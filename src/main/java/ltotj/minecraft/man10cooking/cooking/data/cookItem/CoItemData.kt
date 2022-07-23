package ltotj.minecraft.man10cooking.cooking.data.cookItem

import ltotj.minecraft.man10cooking.Main
import ltotj.minecraft.man10cooking.utility.ItemManager.ItemStackPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.math.min
import kotlin.random.Random

class CoItemData(val innerName:String) {

    var displayName:String?=null
    var lore: MutableList<String>?=null
    lateinit var material:Material
    var customModelData=0
    var foodLevel=0
    var saturation=0F
    private val commands= ArrayList<String>()
    private val randomCommands=ArrayList<Pair<Int,ArrayList<String>>>()
    private var weightSum=0
    private val potionEffects=ArrayList<PotionEffect>()
    var overSaturation=false

    @JvmName("setDisplayName1")
    fun setDisplayName(name:String?): CoItemData {
        displayName=name
        return this
    }

    @JvmName("setMaterial1")
    fun setMaterial(material:Material): CoItemData {
        this.material=material
        return this
    }

    @JvmName("setLore1")
    fun setLore(lore:MutableList<String>?): CoItemData {
        this.lore=lore
        return this
    }

    fun setCMD(cmd:Int): CoItemData {
        customModelData=cmd
        return this
    }

    @JvmName("setCommands1")
    fun setCommands(commands:MutableList<String>?): CoItemData {
        this.commands.addAll(commands?:return this)
        return this
    }

    fun setRandomCommands(list:MutableList<*>?): CoItemData {
        if(list==null)return this
        for(section in list){
            section as Map<*, *>
            randomCommands.add(Pair(section["weight"] as Int,section["commands"] as ArrayList<String>))
            weightSum+=section["weight"] as Int
        }
        return this
    }

    @JvmName("setFoodLevel1")
    fun setFoodLevel(level:Int): CoItemData {
        foodLevel=level
        return this
    }

    @JvmName("setSaturation1")
    fun setSaturation(saturation:Float): CoItemData {
        this.saturation=saturation
        return this
    }

    @JvmName("setOverSaturation1")
    fun setOverSaturation(boolean: Boolean): CoItemData {
        overSaturation=boolean
        return this
    }

    fun addPotionEffects(type: PotionEffectType,duration:Int,ambient:Int,particle:Boolean): CoItemData {
        potionEffects.add(PotionEffect(type,duration,ambient, particle))
        return this
    }

    fun generate():ItemStackPlus{
        val item= ItemStackPlus(material,1)
                .setNBTString("name",innerName, Main.plugin)
        if(displayName!=null){
            item.setDisplay(displayName!!)
        }
        if(lore!=null){
            item.setItemLore(lore!!)
        }
        if (customModelData!=0){
            item.setCustomModelData(customModelData)
        }
        return item
    }

    fun use(player:Player){
        player.foodLevel+=foodLevel
        player.saturation+=if(overSaturation )saturation else min(saturation,player.foodLevel-player.saturation)
        for(potion in potionEffects){
            player.addPotionEffect(potion)
        }
        for(command in commands){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command
                    .replace("<player>",player.name)
                    .replace("<world>",player.world.name))
        }
        if(randomCommands.isNotEmpty()){
            var random= Random.nextInt(weightSum)
            for(list in randomCommands){
                random-=list.first
                if(random<0){
                    for(command in list.second){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command
                                .replace("<player>",player.name)
                                .replace("<world>",player.world.name))
                    }
                    break
                }
            }
        }
    }


}