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
package org.spout.vanilla.protocol.entity.creature;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.entity.living.passive.Sheep;

public class SheepEntityProtocol extends CreatureProtocol {
	public static int SHEAR_COLOR_INDEX = 16; // The MC metadata index for determining the color of the sheap and if it's been sheared.

	public SheepEntityProtocol() {
		super(CreatureType.SHEEP);
	}

	@Override
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		List<Parameter<?>> parameters = super.getSpawnParameters(entity);
		Sheep sheep = entity.add(Sheep.class);
		byte data = 0;
		data |= (sheep.isSheared() ? 1 : 0) << 4;
		data |= sheep.getColor().getData() & 0x0F;
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, SHEAR_COLOR_INDEX, data));
		return parameters;
	}
}
