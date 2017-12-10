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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VectorTest
{
	@Test
	public void testRotate() throws Exception
	{
		{
			Vector rotatedAroundZ = new Vector(1, 0, 0).rotateZ(90);
			assertTrue(new Vector(0, 1, 0).equals(rotatedAroundZ, 001));
		}
		{
			Vector rotatedAroundZ = new Vector(1, 0, 0).rotateY(90);
			assertTrue(new Vector(0, 0, -1).equals(rotatedAroundZ, 001));
		}
		{
			Vector rotatedAroundZ = new Vector(0, 1, 0).rotateX(90);
			assertTrue(new Vector(0, 0, 1).equals(rotatedAroundZ, 001));
		}

		{
			Vector rotatedAroundZandX = new Vector(1, 0, 0)	.rotateZ(90)
															.rotateX(90);
			assertTrue(new Vector(0, 0, 1).equals(rotatedAroundZandX, 001));
		}
	}

	@Test
	public void testRotatePassive() throws Exception
	{
		//System.out.println(rotatedVector);
		//		{
		//			Vector rotated = new Vector(0, 1, 0).rotatePassive(90, 0, 0);
		//			System.out.println(rotated);
		//			assertTrue(new Vector(0, 0, 1).equals(rotated, 0.001));
		//		}
		{
			Vector rotated = new Vector(0, 1, 0).rotatePassive(90, 90, 0);
			System.out.println(rotated);
			assertTrue(new Vector(1, 0, 0).equals(rotated, 0.001));
		}
		//		{
		//			Vector rotated = new Vector(0, 0, 1).rotatePassive(90, 0, 90);
		//			System.out.println(rotated);
		//			assertTrue(new Vector(1, 0, 0).equals(rotated, 0.001));
		//		}

		//		for (int angleX = 0; angleX <= 90; angleX++)
		//		{
		//			Vector rotated = new Vector(0, 0, 1).rotatePassive(angleX, 0, 0);
		//			System.out.println(rotated);
		//			//assertTrue(new Vector(1, 0, 0).equals(rotated, 0.001));
		//		}

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
	public void testEqualsVectorDouble() throws Exception
	{
		assertTrue(new Vector(1, 0, 1).equals(new Vector(1.01, 0, 1.01), 0.1));
		assertFalse(new Vector(1, 0, 1).equals(new Vector(1.01, 0, 1.01), 0.0001));
	}

	@Test
	public void testOuterProduct() throws Exception
	{
		Matrix outerProduct = new Vector(1, 2, 3, 4).outerProduct(new Vector(2, 3, 5));
		//System.out.println(outerProduct);
		assertEquals(	Matrix.builder()
							.addRow(2.00, 3.00, 5.00)
							.addRow(4.00, 6.00, 10.00)
							.addRow(6.00, 9.00, 15.00)
							.addRow(8.00, 12.00, 20.00)
							.build(),
						outerProduct);
	}

	@Test
	public void testDetermineAngleToXAxis() throws Exception
	{
		assertEquals(46.3, new Vector(1, 2, 3).determineAngle(new Vector(-7, 8, 9)), 0.01);
		assertEquals(90.0, Vector.E_Y.determineAngleToXAxis(), 0.01);
		assertEquals(0.0, Vector.E_X.determineAngleToXAxis(), 0.01);
		assertEquals(90.0, Vector.E_Z.determineAngleToXAxis(), 0.01);
	}

}
