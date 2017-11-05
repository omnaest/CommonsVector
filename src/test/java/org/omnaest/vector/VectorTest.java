/*

	Copyright 2017 Danny Kunz

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.


*/
package org.omnaest.vector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest
{
	@Test
	public void testRotate() throws Exception
	{
		assertEquals(	1.0, new Vector(1, 0, 0).rotateZ(90)
												.getY(),
						0.0001);
		assertEquals(	1.0, new Vector(1, 0, 0).rotateY(90)
												.getZ(),
						0.0001);
		assertEquals(	1.0, new Vector(0, 0, 1).rotateX(90)
												.getY(),
						0.0001);
	}

	@Test
	public void testMultiplyCross() throws Exception
	{
		Vector cross = new Vector(1, 2, 3).multiplyCross(new Vector(-7, 8, 9));
		assertEquals(-6.0, cross.getX(), 0.001);
		assertEquals(-30.0, cross.getY(), 0.001);
		assertEquals(22.0, cross.getZ(), 0.001);
	}

	@Test
	public void testClosestDistanceToLine() throws Exception
	{
		double distance = new Vector(0, 100).closestDistanceToLine(new Vector(0, 0), new Vector(1, 0));
		assertEquals(100, distance, 0.001);
	}

	@Test
	public void testAbsolute() throws Exception
	{
		assertEquals(Math.sqrt(2), new Vector(1, 1).absolute(), 0.001);
		assertEquals(1, new Vector(1, 0).absolute(), 0.001);
		assertEquals(1, new Vector(0, 1).absolute(), 0.001);
	}

	@Test
	public void testMultiplyScalar() throws Exception
	{
		assertEquals(1 + 9 + 49, new Vector(1, 3, 7, 0).multiplyScalar(new Vector(1, 3, 7)), 0.001);
	}

	@Test
	public void testAdd() throws Exception
	{
		Vector vector = new Vector(1, 3).add(new Vector(1, 3, 7));
		assertEquals(2, vector.getX(), 0.001);
		assertEquals(6, vector.getY(), 0.001);
		assertEquals(7, vector.getZ(), 0.001);
	}

	@Test
	public void testClosestDirectionToLine() throws Exception
	{
		double distance = new Vector(0, 100).closestDirectionToLine(new Vector(0, 0), new Vector(1, 0))
											.absolute();
		assertEquals(100, distance, 0.001);
	}

	@Test
	public void testRotatePassive() throws Exception
	{
		double angleX = 0;
		double angleY = -90;
		double angleZ = 90;
		Vector rotatedVector = new Vector(0, 100, 0).rotatePassive(angleX, angleY, angleZ);
		System.out.println(rotatedVector);
		assertEquals(0, rotatedVector.getX(), 0.001);
		assertEquals(0, rotatedVector.getY(), 0.001);
		assertEquals(100, rotatedVector.getZ(), 0.001);
	}

}
