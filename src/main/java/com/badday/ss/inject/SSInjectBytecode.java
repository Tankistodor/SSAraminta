package com.badday.ss.inject;

import java.util.HashMap;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;

public class SSInjectBytecode implements IClassTransformer {

	HashMap<String, ObfuscationEntry> nodemap = new HashMap();
	private boolean deobfuscated = true;
	private boolean optifinePresent;
	private boolean isServer;
	private boolean playerApiActive;
	private DefaultArtifactVersion mcVersion;
	private static int operationCount = 0;
	private static int injectionCount = 0;

	public SSInjectBytecode() {

		this.mcVersion = new DefaultArtifactVersion((String) cpw.mods.fml.relauncher.FMLInjectionData.data()[4]);
		try {
			this.deobfuscated = (Launch.classLoader.getClassBytes("net.minecraft.world.World") != null);
			this.optifinePresent = (Launch.classLoader.getClassBytes("CustomColorizer") != null);
			this.playerApiActive = (Launch.classLoader.getClassBytes("api.player.forge.PlayerAPITransformer") != null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mcVersionMatches("[1.7.10]")) {
			this.nodemap.put("PlayerMP", new ObfuscationEntry("net/minecraft/entity/player/EntityPlayerMP", "mw"));
			this.nodemap.put("worldClass", new ObfuscationEntry("net/minecraft/world/World", "ahb"));
			this.nodemap.put("confManagerClass", new ObfuscationEntry("net/minecraft/server/management/ServerConfigurationManager", "oi"));
			this.nodemap.put("gameProfileClass", new ObfuscationEntry("com/mojang/authlib/GameProfile"));
			this.nodemap.put("itemInWorldManagerClass", new ObfuscationEntry("net/minecraft/server/management/ItemInWorldManager", "mx"));
			this.nodemap.put("playerControllerClass", new ObfuscationEntry("net/minecraft/client/multiplayer/PlayerControllerMP", "bje"));
			this.nodemap.put("playerClient", new ObfuscationEntry("net/minecraft/client/entity/EntityClientPlayerMP", "bjk"));
			this.nodemap.put("statFileWriterClass", new ObfuscationEntry("net/minecraft/stats/StatFileWriter", "pq"));
			this.nodemap.put("netHandlerPlayClientClass", new ObfuscationEntry("net/minecraft/client/network/NetHandlerPlayClient", "bjb"));
			this.nodemap.put("entityLivingClass", new ObfuscationEntry("net/minecraft/entity/EntityLivingBase", "sv"));
			this.nodemap.put("entityItemClass", new ObfuscationEntry("net/minecraft/entity/item/EntityItem", "xk"));
			this.nodemap.put("entityRendererClass", new ObfuscationEntry("net/minecraft/client/renderer/EntityRenderer", "blt"));
			this.nodemap.put("worldRendererClass", new ObfuscationEntry("net/minecraft/client/renderer/WorldRenderer", "blo"));
			this.nodemap.put("renderGlobalClass", new ObfuscationEntry("net/minecraft/client/renderer/RenderGlobal", "bma"));
			this.nodemap.put("tessellatorClass", new ObfuscationEntry("net/minecraft/client/renderer/Tessellator", "bmh"));
			this.nodemap.put("renderManagerClass", new ObfuscationEntry("net/minecraft/client/renderer/entity/RenderManager", "bnn"));
			this.nodemap.put("tileEntityRendererClass", new ObfuscationEntry("net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "bmk"));
			this.nodemap.put("containerPlayer", new ObfuscationEntry("net/minecraft/inventory/ContainerPlayer", "aap"));
			this.nodemap.put("minecraft", new ObfuscationEntry("net/minecraft/client/Minecraft", "bao"));
			this.nodemap.put("session", new ObfuscationEntry("net/minecraft/util/Session", "bbs"));
			this.nodemap.put("guiScreen", new ObfuscationEntry("net/minecraft/client/gui/GuiScreen", "bdw"));
			this.nodemap.put("itemRendererClass", new ObfuscationEntry("net/minecraft/client/renderer/ItemRenderer", "bly"));
			this.nodemap.put("vecClass", new ObfuscationEntry("net/minecraft/util/Vec3", "azw"));
			this.nodemap.put("entityClass", new ObfuscationEntry("net/minecraft/entity/Entity", "sa"));
			this.nodemap.put("guiSleepClass", new ObfuscationEntry("net/minecraft/client/gui/GuiSleepMP", "bdi"));
			this.nodemap.put("effectRendererClass", new ObfuscationEntry("net/minecraft/client/particle/EffectRenderer", "bkn"));
			this.nodemap.put("forgeHooks", new ObfuscationEntry("net/minecraftforge/client/ForgeHooksClient"));
			// this.nodemap.put("customPlayerMP", new
			// ObfuscationEntry("micdoodle8/mods/galacticraft/core/entities/player/GCEntityPlayerMP"));
			// this.nodemap.put("customPlayerSP", new
			// ObfuscationEntry("micdoodle8/mods/galacticraft/core/entities/player/GCEntityClientPlayerMP"));
			// this.nodemap.put("customEntityOtherPlayer", new
			// ObfuscationEntry("micdoodle8/mods/galacticraft/core/entities/player/GCEntityOtherPlayerMP"));
			this.nodemap.put("packetSpawnPlayer", new ObfuscationEntry("net/minecraft/network/play/server/S0CPacketSpawnPlayer", "gb"));
			this.nodemap.put("entityOtherPlayer", new ObfuscationEntry("net/minecraft/client/entity/EntityOtherPlayerMP", "bll"));
			this.nodemap.put("minecraftServer", new ObfuscationEntry("net/minecraft/server/MinecraftServer"));
			this.nodemap.put("worldServer", new ObfuscationEntry("net/minecraft/world/WorldServer", "mt"));
			this.nodemap.put("worldClient", new ObfuscationEntry("net/minecraft/client/multiplayer/WorldClient", "bjf"));
			this.nodemap.put("tileEntityClass", new ObfuscationEntry("net/minecraft/tileentity/TileEntity", "aor"));
			this.nodemap.put("musicTicker", new ObfuscationEntry("net/minecraft/client/audio/MusicTicker", "btg"));
			this.nodemap.put("chunkProviderServer", new ObfuscationEntry("net/minecraft/world/gen/ChunkProviderServer", "ms"));
			this.nodemap.put("netHandlerLoginServer", new ObfuscationEntry("net/minecraft/server/network/NetHandlerLoginServer", "nn"));

			this.nodemap.put("thePlayer", new FieldObfuscationEntry("thePlayer", "h"));
			this.nodemap.put("glRenderList", new FieldObfuscationEntry("glRenderList", "z"));
			this.nodemap.put("cps_worldObj", new FieldObfuscationEntry("worldObj", "i"));

			this.nodemap.put("createPlayerMethod", new MethodObfuscationEntry("createPlayerForUser", "f", "(L" + getNameDynamic("gameProfileClass") + ";)L"
					+ getNameDynamic("PlayerMP") + ";"));
			this.nodemap.put("respawnPlayerMethod", new MethodObfuscationEntry("respawnPlayer", "a", "(L" + getNameDynamic("PlayerMP") + ";IZ)L"
					+ getNameDynamic("PlayerMP") + ";"));
			this.nodemap.put("createClientPlayerMethod", new MethodObfuscationEntry("func_147493_a", "a", "(L" + getNameDynamic("worldClass") + ";L"
					+ getNameDynamic("statFileWriterClass") + ";)L" + getNameDynamic("playerClient") + ";"));
			this.nodemap.put("moveEntityMethod", new MethodObfuscationEntry("moveEntityWithHeading", "e", "(FF)V")); //1
			this.nodemap.put("onUpdateMethod", new MethodObfuscationEntry("onUpdate", "h", "()V")); //1
			this.nodemap.put("updateLightmapMethod", new MethodObfuscationEntry("updateLightmap", "i", "(F)V"));
			this.nodemap.put("renderOverlaysMethod", new MethodObfuscationEntry("renderOverlays", "b", "(F)V"));
			this.nodemap.put("updateFogColorMethod", new MethodObfuscationEntry("updateFogColor", "j", "(F)V"));
			this.nodemap.put("getFogColorMethod", new MethodObfuscationEntry("getFogColor", "f", "(F)L" + getNameDynamic("vecClass") + ";"));
			this.nodemap.put("getSkyColorMethod", new MethodObfuscationEntry("getSkyColor", "a", "(L" + getNameDynamic("entityClass") + ";F)L"
					+ getNameDynamic("vecClass") + ";"));
			this.nodemap.put("wakeEntityMethod", new MethodObfuscationEntry("func_146418_g", "f", "()V"));
			this.nodemap.put("orientBedCamera", new MethodObfuscationEntry("orientBedCamera", "(L" + getNameDynamic("minecraft") + ";L"
					+ getNameDynamic("entityLivingClass") + ";)V"));
			this.nodemap.put("renderParticlesMethod", new MethodObfuscationEntry("renderParticles", "a", "(L" + getNameDynamic("entityClass") + ";F)V"));
			this.nodemap.put("customPlayerMPConstructor", new MethodObfuscationEntry("<init>", "(L" + getNameDynamic("minecraftServer") + ";L"
					+ getNameDynamic("worldServer") + ";L" + getNameDynamic("gameProfileClass") + ";L" + getNameDynamic("itemInWorldManagerClass") + ";)V"));
			this.nodemap.put("customPlayerSPConstructor", new MethodObfuscationEntry("<init>", "(L" + getNameDynamic("minecraft") + ";L"
					+ getNameDynamic("worldClass") + ";L" + getNameDynamic("session") + ";L" + getNameDynamic("netHandlerPlayClientClass") + ";L"
					+ getNameDynamic("statFileWriterClass") + ";)V"));
			this.nodemap.put("handleSpawnPlayerMethod",
					new MethodObfuscationEntry("handleSpawnPlayer", "a", "(L" + getNameDynamic("packetSpawnPlayer") + ";)V"));
			this.nodemap.put("orientCamera", new MethodObfuscationEntry("orientCamera", "h", "(F)V"));
			this.nodemap.put("renderManagerMethod", new MethodObfuscationEntry("func_147939_a", "a", "(L" + getNameDynamic("entityClass") + ";DDDFFZ)Z"));
			this.nodemap.put("setupGLTranslationMethod", new MethodObfuscationEntry("setupGLTranslation", "f", "()V"));
			this.nodemap.put("preRenderBlocksMethod", new MethodObfuscationEntry("preRenderBlocks", "b", "(I)V"));
			this.nodemap.put("setPositionMethod", new MethodObfuscationEntry("setPosition", "a", "(III)V"));
			this.nodemap.put("updateRendererMethod", new MethodObfuscationEntry("updateRenderer", "a", "(L" + getNameDynamic("entityLivingClass") + ";)V"));
			this.nodemap.put("loadRenderersMethod", new MethodObfuscationEntry("loadRenderers", "a", "()V"));
			this.nodemap.put("renderGlobalInitMethod", new MethodObfuscationEntry("<init>", "(L" + getNameDynamic("minecraft") + ";)V"));
			this.nodemap.put("sortAndRenderMethod", new MethodObfuscationEntry("sortAndRender", "a", "(L" + getNameDynamic("entityLivingClass") + ";ID)I"));
			this.nodemap.put("addVertexMethod", new MethodObfuscationEntry("addVertex", "a", "(DDD)V"));
			this.nodemap.put("renderTileAtMethod", new MethodObfuscationEntry("renderTileEntityAt", "a", "(L" + getNameDynamic("tileEntityClass") + ";DDDF)V"));
			this.nodemap.put("startGame", new MethodObfuscationEntry("startGame", "ag", "()V"));
			this.nodemap.put("canRenderOnFire", new MethodObfuscationEntry("canRenderOnFire", "aA", "()Z"));
			this.nodemap.put("CGSpopulate", new MethodObfuscationEntry("populate", "a", "(Lapu;II)V"));
			this.nodemap.put("attemptLoginMethodBukkit", new MethodObfuscationEntry("attemptLogin", "attemptLogin", "(L"
					+ getNameDynamic("netHandlerLoginServer") + ";L" + getNameDynamic("gameProfileClass") + ";Ljava/lang/String;)L"
					+ getNameDynamic("PlayerMP") + ";"));
		}

		try {
			this.isServer = (Launch.classLoader.getClassBytes(getNameDynamic("renderGlobalClass")) == null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {

		String testName = name.replace('.', '/');

		if (this.deobfuscated) {
			return transformVanillaDeobfuscated(testName, bytes);
		}
		if (testName.length() <= 3) {
			return transformVanillaObfuscated(testName, bytes);
		}

		return bytes;
	}

	private byte[] transformVanillaDeobfuscated(String testName, byte[] bytes) {
		if (testName.equals(getName("entityLivingClass"))) {
			return transformEntityLiving(bytes);
		}
		if (testName.equals(getName("entityItemClass"))) {
			return transformEntityItem(bytes);
		}
		return bytes;
	}

	private byte[] transformVanillaObfuscated(String testName, byte[] bytes) {

		if (testName.equals(((ObfuscationEntry) this.nodemap.get("entityLivingClass")).obfuscatedName)) {
			return transformEntityLiving(bytes);
		}
		if (testName.equals(((ObfuscationEntry) this.nodemap.get("entityItemClass")).obfuscatedName)) {
			return transformEntityItem(bytes);
		}
		return bytes;
	}

	public byte[] transformEntityLiving(byte[] bytes) {
		ClassNode node = startInjection(bytes);

		operationCount = 1;

		MethodNode method = getMethod(node, "moveEntityMethod");

		if (method != null) {
			for (int count = 0; count < method.instructions.size(); count++) {
				AbstractInsnNode list = method.instructions.get(count);

				if ((list instanceof LdcInsnNode)) {
					LdcInsnNode nodeAt = (LdcInsnNode) list;

					if (nodeAt.cst.equals(Double.valueOf(0.08D))) {
						VarInsnNode beforeNode = new VarInsnNode(25, 0);
						MethodInsnNode overwriteNode = new MethodInsnNode(184, "com/badday/ss/core/utils/WorldUtils", "getGravityForEntity", "(L"
								+ getNameDynamic("entityClass") + ";)D");

						method.instructions.insertBefore(nodeAt, beforeNode);
						method.instructions.set(nodeAt, overwriteNode);
						injectionCount += 1;
					}
				}
			}
		}

		return finishInjection(node);
	}

	public byte[] transformEntityItem(byte[] bytes) {
		ClassNode node = startInjection(bytes);

		operationCount = 1;

		MethodNode method = getMethod(node, "onUpdateMethod");

		if (method != null) {
			for (int count = 0; count < method.instructions.size(); count++) {
				AbstractInsnNode list = method.instructions.get(count);

				if ((list instanceof LdcInsnNode)) {
					LdcInsnNode nodeAt = (LdcInsnNode) list;

					if (nodeAt.cst.equals(Double.valueOf(0.03999999910593033D))) {
						VarInsnNode beforeNode = new VarInsnNode(25, 0);
						MethodInsnNode overwriteNode = new MethodInsnNode(184, "com/badday/ss/core/utils/WorldUtils", "getItemGravity", "(L"
								+ getNameDynamic("entityItemClass") + ";)D");

						method.instructions.insertBefore(nodeAt, beforeNode);
						method.instructions.set(nodeAt, overwriteNode);
						injectionCount += 1;
					}
				}
			}
		}

		return finishInjection(node);
	}

	// =======================================

	private boolean mcVersionMatches(String testVersion) {
		return VersionParser.parseRange(testVersion).containsVersion(this.mcVersion);
	}

	public static class FieldObfuscationEntry extends ObfuscationEntry {
		public FieldObfuscationEntry(String name, String obfuscatedName) {
			super(obfuscatedName);
		}
	}

	private MethodNode getMethod(ClassNode node, String keyName) {
		for (MethodNode methodNode : node.methods) {
			if (methodMatches(keyName, methodNode)) {
				return methodNode;
			}
		}

		return null;
	}

	private boolean methodMatches(String keyName, MethodInsnNode node) {
		return (node.name.equals(getNameDynamic(keyName))) && (node.desc.equals(getDescDynamic(keyName)));
	}

	private boolean methodMatches(String keyName, MethodNode node) {
		return (node.name.equals(getNameDynamic(keyName))) && (node.desc.equals(getDescDynamic(keyName)));
	}

	public String getName(String keyName) {
		return ((ObfuscationEntry) this.nodemap.get(keyName)).name;
	}

	public String getObfName(String keyName) {
		return ((ObfuscationEntry) this.nodemap.get(keyName)).obfuscatedName;
	}

	public String getNameDynamic(String keyName) {
		try {
			if (this.deobfuscated) {
				return getName(keyName);
			}

			return getObfName(keyName);
		} catch (NullPointerException e) {
			System.err.println("Could not find key: " + keyName);
			throw e;
		}
	}

	public String getDescDynamic(String keyName) {
		return ((MethodObfuscationEntry) this.nodemap.get(keyName)).methodDesc;
	}

	private boolean classPathMatches(String keyName, String className) {
		return className.replace('.', '/').equals(getNameDynamic(keyName));
	}

	private void printLog(String message) {
		System.out.println(message);
	}

	private ClassNode startInjection(byte[] bytes) {
		ClassNode node = new ClassNode();
		ClassReader reader = new ClassReader(bytes);
		reader.accept(node, 0);
		injectionCount = 0;
		operationCount = 0;
		return node;
	}

	private byte[] finishInjection(ClassNode node) {
		return finishInjection(node, true);
	}

	private byte[] finishInjection(ClassNode node, boolean printToLog) {
		ClassWriter writer = new ClassWriter(1);
		node.accept(writer);

		if (printToLog) {
			printResultsAndReset(node.name);
		}

		return writer.toByteArray();
	}

	public static class MethodObfuscationEntry extends ObfuscationEntry {
		public String methodDesc;

		public MethodObfuscationEntry(String name, String obfuscatedName, String methodDesc) {
			super(obfuscatedName);
			this.methodDesc = methodDesc;
		}

		public MethodObfuscationEntry(String commonName, String methodDesc) {
			this(commonName, commonName, methodDesc);
		}
	}

	public static class ObfuscationEntry {
		public String name;
		public String obfuscatedName;

		public ObfuscationEntry(String name, String obfuscatedName) {
			this.name = name;
			this.obfuscatedName = obfuscatedName;
		}

		public ObfuscationEntry(String commonName) {
			this(commonName, commonName);
		}
	}

	private void printResultsAndReset(String nodeName) {
		if (operationCount > 0) {
			if (injectionCount >= operationCount) {
				printLog("SSInject successfully injected bytecode into: " + nodeName + " (" + injectionCount + " / " + operationCount + ")");
			} else {
				System.err.println("Potential problem: SSInjectcticraft did not complete injection of bytecode into: " + nodeName + " (" + injectionCount
						+ " / " + operationCount + ")");
			}
		}
	}

}
