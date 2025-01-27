package net.ccbluex.liquidbounce.features.module.modules.world
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.init.Blocks
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper
import net.minecraft.util.MovingObjectPosition

@ModuleInfo("SpeedMine", ModuleCategory.WORLD)
class SpeedMine : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Hypixel", "Packet", "CustomPacket", "Instant", "Theresa"),"Hypixel")
    private val breakSpeedValue = FloatValue("HypixelSpeed", 1.2F, 1F, 1.5F).displayable { modeValue.get().equals("Hypixel") }
    private val speedValue3 = FloatValue("TheresaSpeed", 1.6f, 0.1f, 3.0f).displayable { modeValue.get().equals("TheresaSpeed") }
    private val packetAdd = FloatValue("CustomPacketMP", 0.1f, 0.01f, 0.2f).displayable { modeValue.get().equals("CustomPacket") }
    private var bzs = false
    private var bzx = 0.0F
    var blockPos: BlockPos? = null
    private var facing: EnumFacing? = null
    override val tag: String
        get() = modeValue.get()
    @EventTarget
    fun onPacket(event: PacketEvent) {
        if(modeValue.get() == "Hypixel" || modeValue.get() == "Theresa") {
            if (event.packet is C07PacketPlayerDigging && !mc.playerController.extendedReach()
                && mc.playerController != null) {
                val c07PacketPlayerDigging = event.packet
                if (c07PacketPlayerDigging.status == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    bzs = true
                    blockPos = c07PacketPlayerDigging.position
                    facing = c07PacketPlayerDigging.facing
                    bzx = 0.0f
                } else if (c07PacketPlayerDigging.status == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK
                    || c07PacketPlayerDigging.status == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                    bzs = false
                    blockPos = null
                    facing = null
                }
            }
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        when(modeValue.get()) {
            "Packet" -> {
                if(mc.playerController.curBlockDamageMP in 0.1F..0.19F)
                    mc.playerController.curBlockDamageMP += 0.1F
                if(mc.playerController.curBlockDamageMP in 0.4F..0.49F)
                    mc.playerController.curBlockDamageMP += 0.1F
                if(mc.playerController.curBlockDamageMP in 0.8F..0.89F)
                    mc.playerController.curBlockDamageMP += 0.9F
            }
            "CustomPacket" -> {
                if(mc.playerController.curBlockDamageMP in 0.1F..0.19F)
                    mc.playerController.curBlockDamageMP = mc.playerController.curBlockDamageMP + packetAdd.get()
                if(mc.playerController.curBlockDamageMP in 0.4F..0.49F)
                    mc.playerController.curBlockDamageMP = mc.playerController.curBlockDamageMP + packetAdd.get()
                if(mc.playerController.curBlockDamageMP >= 0.8F)
                    mc.playerController.curBlockDamageMP = mc.playerController.curBlockDamageMP + packetAdd.get()
            }
            "Theresa" -> {
                if (mc.playerController.extendedReach()) {
                    mc.playerController.blockHitDelay = 0
                } else if (bzs) {
                    val block = mc.theWorld.getBlockState(blockPos).block
                    bzx += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) * speedValue3.get()
                    if (bzx >= 1.0f) {
                        mc.theWorld.setBlockState(blockPos, Blocks.air.defaultState, 11)
                        mc.thePlayer.sendQueue.networkManager.sendPacket(
                            C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                blockPos, facing
                            )
                        )
                        bzx = 0.0f
                        bzs = false
                    }
                }
            }
            "Instant" -> {
                if (mc.playerController.curBlockDamageMP > 0 &&
                    mc.gameSettings.keyBindAttack.pressed && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.objectMouseOver.blockPos != null
                ) {
                    mc.netHandler.addToSendQueue(
                        C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                            mc.objectMouseOver.blockPos, mc.objectMouseOver.sideHit
                        )
                    )
                }
            }
            "Hypixel" -> {
                if (mc.playerController.extendedReach()) {
                    mc.playerController.blockHitDelay = 0
                } else if (bzs) {
                    val block = mc.theWorld.getBlockState(blockPos).block
                    bzx += (block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos).toDouble() * breakSpeedValue.get()).toFloat()
                    if (bzx >= 1.0F) {
                        mc.theWorld.setBlockState(blockPos, Blocks.air.defaultState, 11)
                        mc.netHandler.networkManager.sendPacket(C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, facing))
                        bzx = 0.0F
                        bzs = false
                    }
                }
            }
        }
    }
}