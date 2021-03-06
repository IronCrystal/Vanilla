/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.util;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.data.RedstonePowerMode;
import org.spout.vanilla.material.block.redstone.IndirectRedstoneSource;
import org.spout.vanilla.material.block.redstone.RedstoneSource;

public class RedstoneUtil {
	/**
	 * Checks if the block given is a redstone conductor
	 * @param block to check
	 * @return True if it is a redstone conductor
	 */
	public static boolean isConductor(Block block) {
		return isConductor(block.getMaterial());
	}

	/**
	 * Checks if the block material given is a redstone conductor
	 * @param mat to check
	 * @return True if it is a redstone conductor
	 */
	public static boolean isConductor(BlockMaterial mat) {
		return mat instanceof IndirectRedstoneSource && ((IndirectRedstoneSource) mat).isRedstoneConductor();
	}

	/**
	 * Gets if the given block receives Redstone power or not
	 * @param block to get it of
	 * @return True if it receives power, False if not
	 */
	public static boolean isReceivingPower(Block block) {
		return getReceivingPowerLocation(block) != null;
	}

	/**
	 * Gets the block that is powering this block, or null if none
	 * @param block to get source of power of
	 * @return Source of redstone power, or null if none
	 */
	public static Block getReceivingPowerLocation(Block block) {
		for (BlockFace face : BlockFaces.NESWBT) {
			Block b = block.translate(face);
			if (isEmittingPower(b, face.getOpposite())) {
				return b;
			}
		}
		return null;
	}

	/**
	 * Gets if the given block is emitting power to surrounding blocks
	 * @param block to check
	 * @param to the face it is powering
	 * @return True if emitting power, False if not
	 */
	public static boolean isEmittingPower(Block block, BlockFace to) {
		return isEmittingPower(block, to, RedstonePowerMode.ALL);
	}

	/**
	 * Gets if the given block is emitting power to surrounding blocks
	 * @param block to check
	 * @param powerMode to use when reading power
	 * @return True if emitting power, False if not
	 */
	public static boolean isEmittingPower(Block block, RedstonePowerMode powerMode) {
		return isEmittingPower(block, BlockFace.THIS, powerMode);
	}

	/**
	 * Gets if the given block is emitting power to surrounding blocks
	 * @param block to check
	 * @param to the face it is powering
	 * @param powerMode to use when reading power
	 * @return True if emitting power, False if not
	 */
	public static boolean isEmittingPower(Block block, BlockFace to, RedstonePowerMode powerMode) {
		BlockMaterial mat = block.getMaterial();
		if (!(mat instanceof IndirectRedstoneSource)) {
			return false;
		}
		// Powered itself?
		if (to == BlockFace.THIS) {
			return ((IndirectRedstoneSource) mat).hasRedstonePower(block, powerMode);
		}
		// Use direction for direct sources
		if (mat instanceof RedstoneSource && ((RedstoneSource) mat).hasDirectRedstonePower(block, to, powerMode)) {
			return true;
		}
		// Fall-back to indirect redstone sources
		return ((IndirectRedstoneSource) mat).hasIndirectRedstonePower(block, to, powerMode);
	}
}
