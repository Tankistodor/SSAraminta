package com.badday.ss.inject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class SSInjectContainer extends DummyModContainer {

	public SSInjectContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "SSInject";
		meta.name = "SSInject Core";
		meta.updateUrl = "http://none/";
		meta.description = "Provides core features of SS's mods";
		meta.authorList = Arrays.asList(new String[] { "ss" });
		meta.url = "http://none/";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Override
	public List<ArtifactVersion> getDependencies() {
		LinkedList deps = new LinkedList();
		deps.add(VersionParser.parseVersionReference("required-after:Forge@[10.12.2.1148,)"));
		return deps;
	}

	@Override
	public VersionRange acceptableMinecraftVersionRange() {
		return VersionParser.parseRange("[1.7.10]");
	}

}
