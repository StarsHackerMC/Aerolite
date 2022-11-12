package net.ccbluex.liquidbounce.launch.uninfo

import me.stars.utils.Renderer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.LiquidBouncePlus.ColorMixer
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.GuiMainMenuLLL
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiYesNoCallback
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color

/*
 * Author: Stars
 * Packet you SB
 * Ethereal v1.0.0 on top
 */

class GuiUpdateLog : GuiScreen(), GuiYesNoCallback {

    private val logoFile = ResourceLocation("aerolite/main/m.png")
    var text = ArrayList<String>()

    override fun initGui() {
        this.buttonList.add(GuiButton(0, this.width / 2 - 50, 460, 100, 20, "Back"))
        text.add("[+] Better GuiMainMenu")
        text.add("[+] Better GuiMainMenu")
        text.add("[+] Criticals NewPacket")
        text.add("[*] Fix KillAura Kick")
        text.add("[*] Fix Font")
        text.add("[*] Try fix arraylist")
    }

    override fun drawScreen(p_drawScreen_1_: Int, p_drawScreen_2_: Int, p_drawScreen_3_: Float) {
        val sr = ScaledResolution(mc)
      //  drawBackground(0)
        mc.textureManager.bindTexture(ResourceLocation("aerolite/main/game.png"))
        drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, width, height, width.toFloat(), height.toFloat())
        GL11.glPushMatrix()
        val creditInfo = "Aerolite ${LiquidBounce.CLIENT_REAL_VERSION}"
        Fonts.font35.drawStringWithShadow(creditInfo, 2F, height - 12F, -1)
        GlStateManager.disableAlpha()
        RenderUtils.drawImage(logoFile, sr.scaledWidth / 2 - 35, 30, 70,70)
        GlStateManager.enableAlpha()
        RenderUtils.drawRoundedCornerRect(sr.scaledWidth / 2 - 300f, 110f, sr.scaledWidth / 2 + 300f, 430f, 5f, Color(255,255,255,200).rgb)
        Renderer.addSmoothLine(2.5f)
        RenderUtils.drawRect(sr.scaledWidth_double / 2 - 300.0, 130.0, sr.scaledWidth_double / 2 + 300.0, 132.5, ColorMixer.getMixedColor(3000, 3).rgb)
        // Text
        val fontLeft = sr.scaledWidth / 2 - 297f
        var startY = 130f
        Fonts.gs40.drawCenteredString("Update Log (v3.1.0)", width / 2f, 115f, Color(0,0,0,200).rgb)
        // End
        Renderer.removeSmoothLine()
        GL11.glPopMatrix()
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_)
        text.forEach {
            startY += 10f
            Fonts.gs35.drawString(it, fontLeft, startY, Color(0,0,0,175).rgb)
        }
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 ->  {
                mc.displayGuiScreen(GuiMainMenuLLL())
            }
        }
    }
}