package iguanaman.iguanatweakstconstruct;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import iguanaman.iguanatweakstconstruct.leveling.Leveling;
import iguanaman.iguanatweakstconstruct.leveling.commands.IguanaCommandLevelUpTool;
import iguanaman.iguanatweakstconstruct.leveling.commands.IguanaCommandToolXP;
import iguanaman.iguanatweakstconstruct.leveling.commands.debug;
import iguanaman.iguanatweakstconstruct.proxy.CommonProxy;
import iguanaman.iguanatweakstconstruct.reference.IguanaConfig;
import iguanaman.iguanatweakstconstruct.reference.IguanaReference;
import iguanaman.iguanatweakstconstruct.util.IguanaLog;
import mantle.pulsar.config.ForgeCFG;
import mantle.pulsar.control.PulseManager;
import mantle.pulsar.pulse.PulseMeta;
import net.minecraft.item.Item;
import tconstruct.tools.TinkerTools;

import java.util.Arrays;
import java.util.List;

@Mod(modid= IguanaReference.MOD_ID, name=IguanaReference.MOD_NAME, version="1.6.X-1p",
dependencies = "required-after:" + IguanaReference.TCON_MOD_ID + ";after:*")
public class IguanaTweaksTConstruct {

	// The instance of your mod that Forge uses.
	@Instance(IguanaReference.MOD_ID)
	public static IguanaTweaksTConstruct instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide=IguanaReference.PROXY_CLIENT_CLASS, serverSide=IguanaReference.PROXY_SERVER_CLASS)
	public static CommonProxy proxy;

	public static List<Item> toolParts = null;

    // TODO: decide wether or not the same cfg as tcon should be used
    // use the PulseManager. This allows us to separate the different parts into independend modules and have stuff together. yay.
    private ForgeCFG pulseCFG = new ForgeCFG("TinkersModules", "Addon: Iguana Tweaks for Tinkers Construct");
    private PulseManager pulsar = new PulseManager(IguanaReference.MOD_ID, pulseCFG);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		IguanaConfig.init(event.getSuggestedConfigurationFile());

		/*toolParts = Arrays.asList (
				TinkerTools.toolRod, TinkerTools.pickaxeHead, TinkerTools.shovelHead, TinkerTools.hatchetHead,
				TinkerTools.binding, TinkerTools.toughBinding, TinkerTools.toughRod, TinkerTools.largePlate,
				TinkerTools.swordBlade, TinkerTools.wideGuard, TinkerTools.handGuard, TinkerTools.crossbar,
				TinkerTools.knifeBlade, TinkerTools.fullGuard, TinkerTools.frypanHead, TinkerTools.signHead,
				TinkerTools.chiselHead, TinkerTools.scytheBlade, TinkerTools.broadAxeHead, TinkerTools.excavatorHead,
				TinkerTools.largeSwordBlade, TinkerTools.hammerHead, TinkerTools.bowstring, TinkerTools.fletching,
				TinkerTools.arrowhead );
*/

        pulsar.registerPulse(new Leveling());
        pulsar.preInit(event);
	}


	@EventHandler
	public void load(FMLInitializationEvent event) {
        pulsar.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
        pulsar.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
        // TODO: change this to a proper isModuleLoaded or something in Pulsar 0.4+ (when released/implemented)
        PulseMeta meta = new PulseMeta(IguanaReference.PULSE_LEVELING, "", false, false);
		if (pulseCFG.isModuleEnabled(meta))
		{
            IguanaLog.debug("Adding command: leveluptool");
            event.registerServerCommand(new IguanaCommandLevelUpTool());
            IguanaLog.debug("Adding command: toolxp");
            event.registerServerCommand(new IguanaCommandToolXP());
		}
	}


}