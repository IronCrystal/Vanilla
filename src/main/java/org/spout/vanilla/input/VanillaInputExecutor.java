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
package org.spout.vanilla.input;

import org.spout.api.component.impl.CameraComponent;
import org.spout.api.component.impl.SceneComponent;
import org.spout.api.entity.Player;
import org.spout.api.entity.state.PlayerInputState;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.input.InputExecutor;
import org.spout.api.math.QuaternionMath;
import org.spout.api.math.Vector3;

public class VanillaInputExecutor implements InputExecutor {
	private Player player;
	private CameraComponent camera;
	private float speed = 50;

	public VanillaInputExecutor(Player player) {
		this.player = player;
		camera = player.get(CameraComponent.class);
	}

	@Override
	public void execute(float dt, Transform playerTransform) {
		PlayerInputState inputState = player.input();
		SceneComponent sc = player.getScene();
		Transform ts = sc.getTransform(); //TODO: Maybe need getTransformLive?

		Vector3 offset = Vector3.ZERO;
		if (inputState.getForward()) {
			offset = offset.subtract(ts.forwardVector().multiply(speed).multiply(dt));
		}
		if (inputState.getBackward()) {
			offset = offset.add(ts.forwardVector().multiply(speed).multiply(dt));
		}
		if (inputState.getLeft()) {
			offset = offset.subtract(ts.rightVector().multiply(speed).multiply(dt));
		}
		if (inputState.getRight()) {
			offset = offset.add(ts.rightVector().multiply(speed).multiply(dt));
		}
		if (inputState.getJump()) {
			offset = offset.add(ts.upVector().multiply(speed).multiply(dt));
		}
		if (inputState.getCrouch()) {
			offset = offset.subtract(ts.upVector().multiply(speed).multiply(dt));
		}

		ts.translateAndSetRotation(offset, QuaternionMath.rotation(inputState.pitch(), inputState.yaw(), ts.getRotation().getRoll()));
		sc.setTransform(ts);
	}
}
