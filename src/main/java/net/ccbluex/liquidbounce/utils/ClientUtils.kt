/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.utils

import com.google.gson.JsonObject
import net.ccbluex.liquidbounce.Aerolite
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.utils.Metrics.SimplePie
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.minecraft.util.IChatComponent

import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.Display
import oshi.SystemInfo
import java.io.File
import java.util.*

object ClientUtils : MinecraftInstance() {
    private val logger = LogManager.getLogger("Aerolite")
    val osType: EnumOSType

    /**
     * the hardware id used to identify in bStats
     */
    private val hardwareUuid: UUID

    init {
        val os = System.getProperty("os.name").lowercase()
        osType = if (os.contains("win")) {
            EnumOSType.WINDOWS
        } else if (os.contains("mac")) {
            EnumOSType.MACOS
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            EnumOSType.LINUX
        } else {
            EnumOSType.UNKNOWN
        }

        hardwareUuid = try {
            val systemInfo = SystemInfo()
            val hardware = systemInfo.hardware
            val processors = hardware.processors
            val memory = hardware.memory

            val vendor = systemInfo.operatingSystem.manufacturer
            val processorSerialNumber = processors.joinToString("-") { it.identifier }
            //val processorModel = processors.joinToString("-") { it.model }

            UUID.nameUUIDFromBytes(("$vendor, " +
                    "$processorSerialNumber, " +
                    //"$processorModel, " +
                    "${memory.total}, " +
                    "${hardware.processors.size}").toByteArray())
        } catch (e: Throwable) {
            e.printStackTrace()
            UUID.randomUUID()
        }

        logInfo("Your hardware UUID is $hardwareUuid")
    }

    fun mouseWithinBounds(mouseX: Int, mouseY: Int, x: Float, y: Float, x2: Float, y2: Float) = mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2

    fun buildMetrics() {
        // delete files generated by old metrics
        val bsUuidFile = File("BS_UUID")
        if (bsUuidFile.exists())
            bsUuidFile.delete()

        // build metrics
        val metrics = Metrics(LiquidBounce.CLIENT_NAME, 11076, LiquidBounce.CLIENT_REAL_VERSION, hardwareUuid.toString(), true)

        metrics.addCustomChart(SimplePie("config_name") {
            LiquidBounce.configManager.nowConfig
        })
        metrics.addCustomChart(SimplePie("server_address") {
            ServerUtils.getRemoteIp()
        })
    }

    fun logInfo(msg: String) {
        logger.info(msg)
    }

    fun logWarn(msg: String) {
        logger.warn(msg)
    }

    fun logError(msg: String) {
        logger.error(msg)
    }

    fun logError(msg: String, t: Throwable) {
        logger.error(msg, t)
    }

    fun logDebug(msg: String) {
        logger.debug(msg)
    }

    
    fun setLoadingTitle() {
        Display.setTitle("${LiquidBounce.CLIENT_NAME}正在注入核心,请稍后!")
    }

    
    fun setClientTitle() {
        Display.setTitle(Aerolite.title + Aerolite.titles[RandomUtils.nextInt(0, Aerolite.titles.size)])
    }

    
    fun displayAlert(message: String) {
        displayChatMessage("§8[" + LiquidBounce.COLORED_NAME + "§8] §f" + message)
    }

    fun customAlert(message: String) {
        displayChatMessage(message)
    }

    fun displayChatMessage(message: String) {
        if (mc.thePlayer == null) {
            logger.info("(MCChat)$message")
            return
        }
        val jsonObject = JsonObject()
        jsonObject.addProperty("text", message)
        mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()))
    }

    enum class EnumOSType(val friendlyName: String) {
        WINDOWS("win"), LINUX("linux"), MACOS("mac"), UNKNOWN("unk");
    }
}