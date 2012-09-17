/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.inventory.util;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.util.Grid;
import org.spout.api.inventory.util.GridIterator;

public class InventoryGridConverter extends InventoryConverter {
	private final Grid grid;
	private final int offset;

	public InventoryGridConverter(Inventory inventory, int length, int offset) {
		super(inventory, new int[inventory.getGrid(length).getSize()]);
		grid = inventory.getGrid(length);
		this.offset = offset;
		Grid grid = inventory.getGrid(length);
		GridIterator i = grid.iterator();
		while (i.hasNext()) {
			int y = i.getY();
			int next = i.next();
			if (y == grid.getWidth() - 1) {
				slots[next] = i.getIndex() + offset;
				continue;
			}
			slots[next] = i.getX() + grid.getSize() - (offset * y);
		}
	}

	public InventoryGridConverter(Inventory inventory, int length) {
		this(inventory, length, 0);
	}

	public InventoryGridConverter translate(int offset) {
		return new InventoryGridConverter(inventory, grid.getLength(), this.offset + offset);
	}

	public Grid getGrid() {
		return grid;
	}

	public int getOffset() {
		return offset;
	}
}
