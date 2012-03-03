/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.generator.normal.biome;

import java.util.ArrayList;

import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.generator.VanillaBiomeType;
import org.spout.vanilla.generator.normal.decorator.BeachDecorator;
import org.spout.vanilla.generator.normal.decorator.CaveDecorator;
import org.spout.vanilla.generator.normal.decorator.DungeonDecorator;
import org.spout.vanilla.generator.normal.decorator.FlowerDecorator;
import org.spout.vanilla.generator.normal.decorator.GrassDecorator;
import org.spout.vanilla.generator.normal.decorator.PondDecorator;
import org.spout.vanilla.generator.normal.decorator.TreeDecorator;

import net.royawesome.jlibnoise.module.source.Perlin;

public class PlainBiome extends VanillaBiomeType {
	Perlin layerCount = new Perlin(), heightMap = new Perlin();
	ArrayList<Perlin> layers = new ArrayList<Perlin>();

	protected PlainBiome(int id) {
		super(id, new CaveDecorator(), new FlowerDecorator(), new GrassDecorator(), new PondDecorator(), new BeachDecorator(), new TreeDecorator(), new DungeonDecorator());
		layerCount.setOctaveCount(5);
		heightMap.setOctaveCount(5);
	}

	public PlainBiome() {
		this(1);
	}

	public Perlin getLayer(int seed, int layer) {
		if (layer >= 0) {
			if (layer < layers.size()) {
				return layers.get(layer);
			} else {
				Perlin p = new Perlin();
				p.setSeed(seed + layer);
				p.setOctaveCount(5);
				layers.add(p);
				return p;
			}
		} else {
			return null;
		}
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		layerCount.setSeed((int) blockData.getWorld().getSeed() + 10);
		heightMap.setSeed((int) blockData.getWorld().getSeed());

		final int y = chunkY * 16;
		final int height = (int) ((heightMap.GetValue(x / 16.0 + 0.005, 0.05, z / 16.0 + 0.005) + 1.0) * 4.0 + 60.0);

		for (int dy = y; dy < y + 16; dy++) {
			blockData.set(x, dy, z, getBlockId(height, dy));
		}

		if (height < 64) {
			generateWateredStack(blockData, x, y, z, 64);
		}
	}

	protected void generateWateredStack(CuboidShortBuffer blockData, int x, int y, int z, int maxSeaLevel) {
		for (int dy = y + 15; dy >= y; dy--) {
			if (dy < maxSeaLevel && blockData.get(x, dy, z) == VanillaMaterials.AIR.getId()) {
				blockData.set(x, dy, z, VanillaMaterials.WATER.getId());
			} else {
				break;
			}
		}
	}

	protected short getBlockId(int top, int dy) {
		short id;
		if (dy > top) {
			id = VanillaMaterials.AIR.getId();
		} else if (dy == top && dy >= 63) {
			id = VanillaMaterials.GRASS.getId();
		} else if (dy + 4 >= top) {
			id = VanillaMaterials.DIRT.getId();
		} else if (dy != 0) {
			id = VanillaMaterials.STONE.getId();
		} else {
			id = VanillaMaterials.BEDROCK.getId();
		}
		return id;
	}
}
