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

public class MatrixTest
{

	@Test
	public void testMultiply() throws Exception
	{
		Matrix matrixA = new Matrix(new double[] { 3, 2, 1 }, new double[] { 1, 0, 2 });
		Vector vector = new Vector(1, 0, 4);

		Vector result = matrixA.multiply(vector);

		//		System.out.println(matrixA);
		//		System.out.println();
		//		System.out.println(vector);
		//		System.out.println();
		//		System.out.println(result);

		assertEquals(7.0, result.getX(), 0.001);
		assertEquals(9.0, result.getY(), 0.001);
	}

	@Test
	public void testGetRawSubMatrix() throws Exception
	{
		Matrix matrixA = new Matrix(new double[] { 3, 2, 1 }, new double[] { 1, 0, 2 }, new double[] { 4, 4, 4 });

		Matrix subMatrix = matrixA.getSubMatrix(0, 0, 2 - 1, 3 - 1);
		assertEquals(2, subMatrix.getDimensions()[0]);
		assertEquals(3, subMatrix.getDimensions()[1]);
		assertEquals(3, subMatrix.getRaw(0, 0), 0.001);
		assertEquals(0, subMatrix.getRaw(1, 1), 0.001);
		assertEquals(4, subMatrix.getRaw(1, 2), 0.001);
	}

	@Test
	public void testReducedByRowAndColumn() throws Exception
	{
		Matrix reducedMatrix = Matrix	.builder()
										.addRow(new double[] { 1, 3, 7 })
										.addRow(new double[] { 11, 13, 17 })
										.addRow(new double[] { 19, 23, 31 })
										.build()
										.reducedByRowAndColumn(2, 2);

		assertEquals(1.0 * 31 - 7 * 19, reducedMatrix.determinant(), 0.001);
	}

	@Test
	public void testAdjunct() throws Exception
	{
		Matrix matrix = Matrix	.builder()
								.addRow(new double[] { 1, 3 })
								.addRow(new double[] { 11, 13 })
								.build();
		Matrix adjunct = matrix.adjunct();

		//		System.out.println(matrix);
		//		System.out.println(adjunct);

		assertEquals(	Matrix.builder()
							.addRow(new double[] { 13, -3 })
							.addRow(new double[] { -11, 1 })
							.build(),
						adjunct);
	}

	@Test
	public void testInverse() throws Exception
	{
		{
			Matrix matrix = Matrix	.builder()
									.addRow(new double[] { 1, 2 })
									.addRow(new double[] { 3, 4 })
									.build();
			Matrix inverse = matrix.inverse();

			//		System.out.println(matrix);
			//		System.out.println(inverse);

			assertEquals(	Matrix.builder()
								.addRow(new double[] { -2, 1 })
								.addRow(new double[] { 1.5, -0.5 })
								.build(),
							inverse);
		}
		{
			Matrix matrix = Matrix	.builder()
									.addRow(2, -1, 0)
									.addRow(-1, 2, -1)
									.addRow(0, -1, 2)
									.build();
			Matrix inverse = matrix.inverse();

			//			System.out.println(matrix);
			//			System.out.println(inverse);

			assertEquals(	Matrix.builder()
								.addRow(3, 2, 1)
								.addRow(2, 4, 2)
								.addRow(1, 2, 3)
								.build()
								.multiply(0.25),
							inverse);
		}
	}

	@Test
	public void testIdentity() throws Exception
	{
		assertEquals(	Matrix.builder()
							.addRow(1, 0, 0)
							.addRow(0, 1, 0)
							.addRow(0, 0, 1)
							.build(),
						Matrix.identity(3));
	}

	@Test
	public void testAdd() throws Exception
	{
		Matrix matrix = Matrix	.builder()
								.addRow(1, 2)
								.addRow(3, 4)
								.build()
								.add(Matrix	.builder()
											.addRow(1, 2)
											.addRow(3, 4)
											.build());
		assertEquals(	Matrix.builder()
							.addRow(1 * 2, 2 * 2)
							.addRow(3 * 2, 4 * 2)
							.build(),
						matrix);
	}

}
