package com.badday.ss.core.utils;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.badday.ss.SS;



public class SSSettings {

	public static boolean getBooleanFor(Configuration config, String heading,
			String item, boolean value) {
		if (config == null) {
			return value;
		} else {
			try {
				Property e = config.get(heading, item, value);
				return e.getBoolean(value);
			} catch (Exception var5) {
				System.out
						.println("["+SS.MODNAME+"] Error while trying to add Integer, config wasn\'t loaded properly!");
				return value;
			}
		}
	}

	public static boolean getBooleanFor(Configuration config, String heading,
			String item, boolean value, String comment) {
		if (config == null) {
			return value;
		} else {
			try {
				Property e = config.get(heading, item, value);
				e.comment = comment;
				return e.getBoolean(value);
			} catch (Exception var6) {
				System.out
						.println("["+SS.MODNAME+"] Error while trying to add Integer, config wasn\'t loaded properly!");
				return value;
			}
		}
	}

	public static int getIntFor(Configuration config, String heading,
			String item, int value) {
		if (config == null) {
			return value;
		} else {
			try {
				Property e = config.get(heading, item, value);
				return e.getInt(value);
			} catch (Exception var5) {
				System.out
						.println("["+SS.MODNAME+"] Error while trying to add Integer, config wasn\'t loaded properly!");
				return value;
			}
		}
	}

  public static float getFloatFor(Configuration config, String heading,
                              String item, float value) {
    if (config == null) {
      return value;
    } else {
      try {
        Property e = config.get(heading, item, value);
        return (float) e.getDouble(value);
      } catch (Exception var5) {
        System.out
            .println("["+SS.MODNAME+"] Error while trying to add Integer, config wasn\'t loaded properly!");
        return value;
      }
    }
  }

	public static int getIntFor(Configuration config, String heading,
			String item, int value, String comment) {
		if (config == null) {
			return value;
		} else {
			try {
				Property e = config.get(heading, item, value);
				e.comment = comment;
				return e.getInt(value);
			} catch (Exception var6) {
				System.out
						.println("["+SS.MODNAME+"] Error while trying to add Integer, config wasn\'t loaded properly!");
				return value;
			}
		}
	}

}
