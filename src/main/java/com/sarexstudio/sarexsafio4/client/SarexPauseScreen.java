package com.sarexstudio.sarexsafio4.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SarexPauseScreen extends Screen {
	private static final String NS = "sarexsafio4";
	private static final ResourceLocation STUDIO = tex("sarexstudio_logo");
	private static final ResourceLocation MAIN_LOGO = tex("sarexsafio4_logo");
	private static final ResourceLocation BTN_VOLVER = tex("volver_al_juego");
	private static final ResourceLocation BTN_OPCIONES = tex("opciones");
	private static final ResourceLocation BTN_SALIR = tex("desconectarse");

	// Tamaños reales (px) de cada textura
	private static final int STUDIO_W = 973, STUDIO_H = 293;
	private static final int MAIN_W = 1281, MAIN_H = 658;
	private static final int VOLVER_W = 830, VOLVER_H = 63;
	private static final int OPC_W = 461, OPC_H = 66;
	private static final int SALIR_W = 768, SALIR_H = 66;

	private int studioX, studioY, studioW, studioH;
	private int mainX, mainY, mainW, mainH;

	public SarexPauseScreen() {
		super(Component.translatable("menu.game"));
	}

	private static ResourceLocation tex(String name) {
		return ResourceLocation.fromNamespaceAndPath(NS, "textures/gui/" + name + ".png");
	}

	@Override
	protected void init() {
		int H = this.height, W = this.width;

		// Logo del estudio (arriba)
		studioH = Math.max(20, (int) (H * 0.11));
		studioW = studioH * STUDIO_W / STUDIO_H;
		studioX = (W - studioW) / 2;
		studioY = (int) (H * 0.03);

		// Logo de Sarexsafio 4 (GRANDE)
		mainH = (int) (H * 0.40);
		mainW = mainH * MAIN_W / MAIN_H;
		if (mainW > W * 0.90) {
			mainW = (int) (W * 0.90);
			mainH = mainW * MAIN_H / MAIN_W;
		}
		mainX = (W - mainW) / 2;
		mainY = studioY + studioH + (int) (H * 0.005);

		// Botones
		int bh = Math.max(12, (int) (H * 0.065));
		int gap = (int) (H * 0.035);
		int y = mainY + mainH + (int) (H * 0.04);

		addButton(BTN_VOLVER, VOLVER_W, VOLVER_H, bh, y, "Volver al juego", this::resume);
		y += bh + gap;
		addButton(BTN_OPCIONES, OPC_W, OPC_H, bh, y, "Opciones",
				() -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options)));
		y += bh + gap;
		addButton(BTN_SALIR, SALIR_W, SALIR_H, bh, y, "Desconectarse", this::disconnect);
	}

	private void addButton(ResourceLocation t, int tw, int th, int h, int y, String label, Runnable action) {
		int w = h * tw / th;
		addRenderableWidget(new SarexImageButton((this.width - w) / 2, y, w, h, t, tw, th,
				Component.literal(label), action));
	}

	private void resume() {
		this.minecraft.setScreen(null);
		this.minecraft.mouseHandler.grabMouse();
	}

	private void disconnect() {
		Minecraft mc = this.minecraft;
		boolean local = mc.isLocalServer();
		if (mc.level != null) {
			mc.level.disconnect();
		}
		if (local) {
			mc.disconnect(new GenericMessageScreen(Component.translatable("menu.savingLevel")));
			mc.setScreen(new TitleScreen());
		} else {
			mc.disconnect();
			mc.setScreen(new JoinMultiplayerScreen(new TitleScreen()));
		}
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		super.render(g, mouseX, mouseY, partialTick); // fondo + botones

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		g.blit(STUDIO, studioX, studioY, studioW, studioH, 0f, 0f, STUDIO_W, STUDIO_H, STUDIO_W, STUDIO_H);
		g.blit(MAIN_LOGO, mainX, mainY, mainW, mainH, 0f, 0f, MAIN_W, MAIN_H, MAIN_W, MAIN_H);
		RenderSystem.disableBlend();
	}
}
