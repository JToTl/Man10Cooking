package ltotj.minecraft.man10cooking.cooking.data.equipment

import ltotj.minecraft.man10cooking.cooking.data.recipe.PhaseData
import ltotj.minecraft.man10cooking.cooking.data.recipe.RecipeData
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.ArmorStand

class EquipmentData(name: String, con: YamlConfiguration,val key:ArmorStand) : BaseEquipment(name, con) {

    var recipe:RecipeData?=null
    var phase:PhaseData?=null
    val rating=3



    fun loadData(){

    }

}