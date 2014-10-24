package com.badday.ss.inject;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class SSInjectBytecode implements IClassTransformer {

	HashMap<String, String> nodemap = new HashMap();
	private boolean deobfuscated = true;
	private boolean optifinePresent;

	public SSInjectBytecode() {
		try
	    {
	      URLClassLoader loader = new LaunchClassLoader(((URLClassLoader)getClass().getClassLoader()).getURLs());
	      URL classResource = loader.findResource(String.valueOf("net.minecraft.world.World").replace('.', '/').concat(".class"));
	      if (classResource == null)
	      {
	        this.deobfuscated = false;
	      }
	      else
	      {
	        this.deobfuscated = true;
	      }

	      classResource = loader.findResource(String.valueOf("CustomColorizer").replace('.', '/').concat(".class"));
	      if (classResource == null)
	      {
	        this.optifinePresent = false;
	      }
	      else
	      {
	        this.optifinePresent = true;
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }

	    if (this.deobfuscated)
	    {
	      this.nodemap.put("respawnPlayerMethod", "respawnPlayer");
	      this.nodemap.put("worldClass", "net/minecraft/world/World");
	      this.nodemap.put("playerMP", "net/minecraft/entity/player/EntityPlayerMP");
	      this.nodemap.put("netLoginHandler", "net/minecraft/network/NetLoginHandler");
	      this.nodemap.put("confManagerClass", "net/minecraft/server/management/ServerConfigurationManager");
	      this.nodemap.put("createPlayerMethod", "createPlayerForUser");
	      this.nodemap.put("createPlayerDesc", "(Ljava/lang/String;)L" + (String)this.nodemap.get("playerMP") + ";");
	      this.nodemap.put("respawnPlayerDesc", "(L" + (String)this.nodemap.get("playerMP") + ";IZ)L" + (String)this.nodemap.get("playerMP") + ";");
	      this.nodemap.put("itemInWorldManagerClass", "net/minecraft/src/ItemInWorldManager");

	      this.nodemap.put("attemptLoginMethodBukkit", "");
	      this.nodemap.put("attemptLoginDescBukkit", "");

	      this.nodemap.put("playerControllerClass", "net/minecraft/client/multiplayer/PlayerControllerMP");
	      this.nodemap.put("playerClient", "net/minecraft/client/entity/EntityClientPlayerMP");
	      this.nodemap.put("netClientHandler", "net/minecraft/client/multiplayer/NetClientHandler");
	      this.nodemap.put("createClientPlayerMethod", "func_78754_a");
	      this.nodemap.put("createClientPlayerDesc", "(L" + (String)this.nodemap.get("worldClass") + ";)L" + (String)this.nodemap.get("playerClient") + ";");

	      this.nodemap.put("entityLivingClass", "net/minecraft/entity/EntityLivingBase");
	      this.nodemap.put("moveEntityMethod", "moveEntityWithHeading");
	      this.nodemap.put("moveEntityDesc", "(FF)V");

	      this.nodemap.put("entityItemClass", "net/minecraft/entity/item/EntityItem");
	      this.nodemap.put("onUpdateMethod", "onUpdate");
	      this.nodemap.put("onUpdateDesc", "()V");

	      this.nodemap.put("entityRendererClass", "net/minecraft/client/renderer/EntityRenderer");
	      this.nodemap.put("updateLightmapMethod", "updateLightmap");
	      this.nodemap.put("updateLightmapDesc", "(F)V");

	      this.nodemap.put("player", "net/minecraft/entity/player/EntityPlayer");
	      this.nodemap.put("containerPlayer", "net/minecraft/inventory/ContainerPlayer");
	      this.nodemap.put("invPlayerClass", "net/minecraft/entity/player/InventoryPlayer");

	      this.nodemap.put("minecraft", "net/minecraft/client/Minecraft");
	      this.nodemap.put("session", "net/minecraft/util/Session");
	      this.nodemap.put("guiPlayer", "net/minecraft/client/gui/inventory/GuiInventory");
	      this.nodemap.put("thePlayer", "thePlayer");
	      this.nodemap.put("displayGui", "displayGuiScreen");
	      this.nodemap.put("guiScreen", "net/minecraft/src/GuiScreen");
	      this.nodemap.put("displayGuiDesc", "(L" + (String)this.nodemap.get("guiScreen") + ";)V");
	      this.nodemap.put("runTick", "runTick");
	      this.nodemap.put("runTickDesc", "()V");
	      this.nodemap.put("clickMiddleMouseButton", "clickMiddleMouseButton");
	      this.nodemap.put("clickMiddleMouseButtonDesc", "()V");

	      this.nodemap.put("itemRendererClass", "net/minecraft/client/renderer/ItemRenderer");
	      this.nodemap.put("renderOverlaysMethod", "renderOverlays");
	      this.nodemap.put("renderOverlaysDesc", "(F)V");

	      this.nodemap.put("updateFogColorMethod", "updateFogColor");
	      this.nodemap.put("updateFogColorDesc", "(F)V");
	      this.nodemap.put("getFogColorMethod", "getFogColor");
	      this.nodemap.put("getSkyColorMethod", "getSkyColor");
	      this.nodemap.put("vecClass", "net/minecraft/util/Vec3");
	      this.nodemap.put("entityClass", "net/minecraft/entity/Entity");
	      this.nodemap.put("getFogColorDesc", "(F)L" + (String)this.nodemap.get("vecClass") + ";");
	      this.nodemap.put("getSkyColorDesc", "(L" + (String)this.nodemap.get("entityClass") + ";F)L" + (String)this.nodemap.get("vecClass") + ";");

	      this.nodemap.put("guiSleepClass", "net/minecraft/client/gui/GuiSleepMP");
	      this.nodemap.put("wakeEntityMethod", "wakeEntity");
	      this.nodemap.put("wakeEntityDesc", "()V");

	      this.nodemap.put("orientCameraDesc", "(L" + (String)this.nodemap.get("minecraft") + ";L" + (String)this.nodemap.get("entityLivingClass") + ";)V");

	      this.nodemap.put("blockClass", "net/minecraft/block/Block");
	      this.nodemap.put("breakBlockMethod", "breakBlock");
	      this.nodemap.put("breakBlockDesc", "(L" + (String)this.nodemap.get("worldClass") + ";IIIII)V");
	    }
	    else
	    {
	      String mcVersion = (String)cpw.mods.fml.relauncher.FMLInjectionData.data()[4];

	      if (VersionParser.parseRange("[1.6.4]").containsVersion(new DefaultArtifactVersion(mcVersion)))
	      {
	        this.nodemap.put("worldClass", "abw");

	        this.nodemap.put("playerMP", "jv");
	        this.nodemap.put("netLoginHandler", "jy");
	        this.nodemap.put("confManagerClass", "hn");
	        this.nodemap.put("createPlayerMethod", "a");
	        this.nodemap.put("createPlayerDesc", "(Ljava/lang/String;)L" + (String)this.nodemap.get("playerMP") + ";");
	        this.nodemap.put("respawnPlayerMethod", "a");
	        this.nodemap.put("respawnPlayerDesc", "(L" + (String)this.nodemap.get("playerMP") + ";IZ)L" + (String)this.nodemap.get("playerMP") + ";");
	        this.nodemap.put("itemInWorldManagerClass", "jw");

	        this.nodemap.put("attemptLoginMethodBukkit", "attemptLogin");
	        this.nodemap.put("attemptLoginDescBukkit", "(L" + (String)this.nodemap.get("netLoginHandler") + ";Ljava/lang/String;Ljava/lang/String;)L" + (String)this.nodemap.get("playerMP") + ";");

	        this.nodemap.put("playerControllerClass", "bdc");
	        this.nodemap.put("playerClient", "bdi");
	        this.nodemap.put("netClientHandler", "bcw");
	        this.nodemap.put("createClientPlayerMethod", "a");
	        this.nodemap.put("createClientPlayerDesc", "(L" + (String)this.nodemap.get("worldClass") + ";)L" + (String)this.nodemap.get("playerClient") + ";");

	        this.nodemap.put("entityLivingClass", "of");
	        this.nodemap.put("moveEntityMethod", "e");
	        this.nodemap.put("moveEntityDesc", "(FF)V");

	        this.nodemap.put("entityItemClass", "ss");
	        this.nodemap.put("onUpdateMethod", "l_");
	        this.nodemap.put("onUpdateDesc", "()V");

	        this.nodemap.put("entityRendererClass", "bfe");
	        this.nodemap.put("updateLightmapMethod", "h");
	        this.nodemap.put("updateLightmapDesc", "(F)V");

	        this.nodemap.put("player", "uf");
	        this.nodemap.put("containerPlayer", "vv");
	        this.nodemap.put("invPlayerClass", "ud");

	        this.nodemap.put("minecraft", "atv");
	        this.nodemap.put("session", "aus");
	        this.nodemap.put("guiPlayer", "axv");
	        this.nodemap.put("thePlayer", "h");
	        this.nodemap.put("displayGui", "a");
	        this.nodemap.put("guiScreen", "awe");
	        this.nodemap.put("displayGuiDesc", "(L" + (String)this.nodemap.get("guiScreen") + ";)V");
	        this.nodemap.put("runTick", "k");
	        this.nodemap.put("runTickDesc", "()V");
	        this.nodemap.put("clickMiddleMouseButton", "W");
	        this.nodemap.put("clickMiddleMouseButtonDesc", "()V");

	        this.nodemap.put("itemRendererClass", "bfj");
	        this.nodemap.put("renderOverlaysMethod", "b");
	        this.nodemap.put("renderOverlaysDesc", "(F)V");

	        this.nodemap.put("updateFogColorMethod", "i");
	        this.nodemap.put("updateFogColorDesc", "(F)V");
	        this.nodemap.put("getFogColorMethod", "f");
	        this.nodemap.put("getSkyColorMethod", "a");
	        this.nodemap.put("vecClass", "atc");
	        this.nodemap.put("entityClass", "nn");
	        this.nodemap.put("getFogColorDesc", "(F)L" + (String)this.nodemap.get("vecClass") + ";");
	        this.nodemap.put("getSkyColorDesc", "(L" + (String)this.nodemap.get("entityClass") + ";F)L" + (String)this.nodemap.get("vecClass") + ";");

	        this.nodemap.put("guiSleepClass", "avm");
	        this.nodemap.put("wakeEntityMethod", "g");
	        this.nodemap.put("wakeEntityDesc", "()V");

	        this.nodemap.put("orientCameraDesc", "(L" + (String)this.nodemap.get("minecraft") + ";L" + (String)this.nodemap.get("entityLivingClass") + ";)V");
	      }
	      
	    }
	  }

	  public byte[] transform(String name, String transformedName, byte[] bytes)
	  {
	    /*if (name.replace('.', '/').equals(this.nodemap.get("confManagerClass")))
	    {
	      bytes = transform1(name, bytes, this.nodemap);
	    }
	    else if (name.replace('.', '/').equals(this.nodemap.get("playerControllerClass")))
	    {
	      bytes = transform2(name, bytes, this.nodemap);
	    }
	    else*/ if (name.replace('.', '/').equals(this.nodemap.get("entityLivingClass")))
	    {
	      bytes = transform3(name, bytes, this.nodemap);
	    }
	    else if (name.replace('.', '/').equals(this.nodemap.get("entityItemClass")))
	    {
	      bytes = transform4(name, bytes, this.nodemap);
	    }/*
	    else if (name.replace('.', '/').equals(this.nodemap.get("entityRendererClass")))
	    {
	      bytes = transform5(name, bytes, this.nodemap);
	    }
	    else if (name.replace('.', '/').equals(this.nodemap.get("itemRendererClass")))
	    {
	      bytes = transform14(name, bytes, this.nodemap);
	    }
	    else if (name.replace('.', '/').equals(this.nodemap.get("guiSleepClass")))
	    {
	      bytes = transform7(name, bytes, this.nodemap);
	    }
	    else if (name.equals("net.minecraftforge.client.ForgeHooksClient"))
	    {
	      bytes = transform8(name, bytes, this.nodemap);
	    }

	    if (name.contains("galacticraft"))
	    {
	      bytes = transform9(name, bytes, this.nodemap);
	    }*/

	    return bytes;
	}

	  public byte[] transform3(String name, byte[] bytes, HashMap<String, String> map)
	  {
	    ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(bytes);
	    reader.accept(node, 0);

	    int operationCount = 1;
	    int injectionCount = 0;

	    Iterator methods = node.methods.iterator();

	    while (methods.hasNext())
	    {
	      MethodNode methodnode = (MethodNode)methods.next();

	      if ((methodnode.name.equals(map.get("moveEntityMethod"))) && (methodnode.desc.equals(map.get("moveEntityDesc"))))
	      {
	        for (int count = 0; count < methodnode.instructions.size(); count++)
	        {
	          AbstractInsnNode list = methodnode.instructions.get(count);

	          if ((list instanceof LdcInsnNode))
	          {
	            LdcInsnNode nodeAt = (LdcInsnNode)list;

	            if (nodeAt.cst.equals(Double.valueOf(0.08D)))
	            {
	              VarInsnNode beforeNode = new VarInsnNode(25, 0);
	              MethodInsnNode overwriteNode = new MethodInsnNode(184, "ss/core/utils/WorldUtils", "getGravityForEntity", "(L" + (String)map.get("entityLivingClass") + ";)D");

	              methodnode.instructions.insertBefore(nodeAt, beforeNode);
	              methodnode.instructions.set(nodeAt, overwriteNode);
	              injectionCount++;
	            }
	          }
	        }
	      }
	    }
	    
	    ClassWriter writer = new ClassWriter(1);
	    node.accept(writer);
	    bytes = writer.toByteArray();

	    System.out.println("SS successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

	    return bytes;
	  }
	  
	  public byte[] transform4(String name, byte[] bytes, HashMap<String, String> map)
	  {
	    ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(bytes);
	    reader.accept(node, 0);

	    int operationCount = 2;
	    int injectionCount = 0;

	    Iterator methods = node.methods.iterator();

	    while (methods.hasNext())
	    {
	      MethodNode methodnode = (MethodNode)methods.next();

	      if ((methodnode.name.equals(map.get("onUpdateMethod"))) && (methodnode.desc.equals(map.get("onUpdateDesc"))))
	      {
	        for (int count = 0; count < methodnode.instructions.size(); count++)
	        {
	          AbstractInsnNode list = methodnode.instructions.get(count);

	          if ((list instanceof LdcInsnNode))
	          {
	            LdcInsnNode nodeAt = (LdcInsnNode)list;

	            if (nodeAt.cst.equals(Double.valueOf(0.03999999910593033D)))
	            {
	              VarInsnNode beforeNode = new VarInsnNode(25, 0);
	              MethodInsnNode overwriteNode = new MethodInsnNode(184, "ss/core/utils/WorldUtils", "getItemGravity", "(L" + (String)map.get("entityItemClass") + ";)D");

	              methodnode.instructions.insertBefore(nodeAt, beforeNode);
	              methodnode.instructions.set(nodeAt, overwriteNode);
	              injectionCount++;
	            }

	            if (nodeAt.cst.equals(Double.valueOf(0.9800000190734863D)))
	            {
	              VarInsnNode beforeNode = new VarInsnNode(25, 0);
	              MethodInsnNode overwriteNode = new MethodInsnNode(184, "ss/core/utils/WorldUtils", "getItemGravity2", "(L" + (String)map.get("entityItemClass") + ";)D");

	              methodnode.instructions.insertBefore(nodeAt, beforeNode);
	              methodnode.instructions.set(nodeAt, overwriteNode);
	              injectionCount++;
	            }
	          }
	        }
	      }
	    }

	    ClassWriter writer = new ClassWriter(1);
	    node.accept(writer);
	    bytes = writer.toByteArray();

	    System.out.println("SS successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

	    return bytes;
	  }  
}
