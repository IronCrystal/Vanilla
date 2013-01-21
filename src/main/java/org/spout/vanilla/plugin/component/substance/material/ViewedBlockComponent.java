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
package org.spout.vanilla.plugin.component.substance.material;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.material.block.BlockFace;

public abstract class ViewedBlockComponent extends VanillaBlockComponent {
	protected final Set<Player> viewers = new HashSet<Player>();

	/**
	 * Opens a window for the given player.
	 * 
	 * @param player
	 * @return true if the component opened successfully
	 */
	public abstract boolean open(Player player);

	/**
	 * Closes the window for the given player
	 * 
	 * @param player
	 * @return true if the window was closed successfully
	 */
	public boolean close(Player player) {
		viewers.remove(player);
		return true;
	}

	public void closeAll() {
		for (Player player : viewers) {
			close(player);
		}
	}

	public Set<Player> getViewers() {
		return viewers;
	}

	@Override
	public void onInteractBy(Entity entity, Action action, BlockFace face) {
		super.onInteractBy(entity, action, face);
		if (action == Action.RIGHT_CLICK && entity instanceof Player) {
			Player player = (Player) entity;
			if (open(player)) {
				viewers.add(player);
			}
		}
	}
}
