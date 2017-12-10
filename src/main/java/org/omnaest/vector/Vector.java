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

import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.DoubleStream;

import org.omnaest.vector.Matrix.Builder;

public class Vector
{
	/**
	 * (0,0,0) vector in 3D
	 *
	 * @see #as2DVector()
	 * @see #asVectorWithDimension(int)
	 */
	public static final Vector	NULL	= new Vector(0, 0, 0);
	public static final Vector	E_X		= new Vector(1, 0, 0);
	public static final Vector	E_Y		= new Vector(0, 1, 0);;
	public static final Vector	E_Z		= new Vector(0, 0, 1);

	private double[] coordinates;

	public Vector(double x, double y)
	{
		super();
		this.coordinates = new double[] { x, y };
	}

	public Vector(double x, double y, double z)
	{
		super();
		this.coordinates = new double[] { x, y, z };
	}

	public Vector(double x)
	{
		super();
		this.coordinates = new double[] { x };
	}

	public Vector(double... coordinates)
	{
		super();
		this.coordinates = coordinates;
	}

	public double getX()
	{
		return this.getCoordinate(0);
	}

	public double getY()
	{
		return this.getCoordinate(1);
	}

	public double getZ()
	{
		return this.getCoordinate(2);
	}

	public double getCoordinate(int dimension)
	{
		return dimension < this.coordinates.length ? this.coordinates[dimension] : 0.0;
	}

	public double[] getCoordinates()
	{
		return this.coordinates;
	}

	public DoubleStream getCoordinatesStream()
	{
		return DoubleStream.of(this.coordinates);
	}

	public Vector subtract(Vector vector)
	{
		return this.add(vector.multiply(-1.0));
	}

	public Vector add(Vector vector)
	{
		int commonDimension = this.determineCommonDimension(this, vector);
		double[] addedCoordinates = new double[commonDimension];
		for (int ii = 0; ii < commonDimension; ii++)
		{
			addedCoordinates[ii] = this.getCoordinate(ii) + vector.getCoordinate(ii);
		}
		return new Vector(addedCoordinates);
	}

	private int determineCommonDimension(Vector vector1, Vector vector2)
	{
		return Math.max(vector1.getDimension(), vector2.getDimension());
	}

	public double multiplyScalar(Vector vector)
	{
		double retval = 0.0;
		for (int ii = 0; ii < this.determineCommonDimension(this, vector); ii++)
		{
			retval += this.getCoordinate(ii) * vector.getCoordinate(ii);
		}
		return retval;
	}

	public Vector multiplyCross(Vector vector)
	{
		int dimension = Math.max(this.getDimension(), vector.getDimension());
		double[] values = new double[dimension];
		for (int ii = 0; ii < dimension; ii++)
		{
			double[] identityCoordinates = new double[dimension];
			identityCoordinates[ii] = 1.0;
			Vector identityVector = new Vector(identityCoordinates);
			values[ii] = Matrix	.builder()
								.addColumn(identityVector)
								.addColumn(this)
								.addColumn(vector)
								.build()
								.determinant();
		}
		return new Vector(values);
	}

	public double absolute()
	{
		return Math.sqrt(this.multiplyScalar(this));
	}

	public Vector normVector()
	{
		double scalarValue = this.absolute();
		double range = 0.000000001;
		return scalarValue > range || scalarValue < -range ? this.divide(scalarValue) : new Vector(0, 0);
	}

	public Vector multiply(double multiplier)
	{
		double[] multiplyedCoordinates = new double[this.coordinates.length];
		for (int ii = 0; ii < this.coordinates.length; ii++)
		{
			multiplyedCoordinates[ii] = this.coordinates[ii] * multiplier;
		}
		return new Vector(multiplyedCoordinates);
	}

	public Vector divide(double divider)
	{
		return this.multiply(1.0 / divider);
	}

	/**
	 * Rotates in degree around the z-axis
	 *
	 * @param angle
	 * @return
	 */
	public Vector rotateZ(double angle)
	{
		return this.rotate(0, 0, angle);
	}

	public Vector rotateY(double angle)
	{
		return this.rotate(0, angle, 0);
	}

	public Vector rotateX(double angle)
	{
		return this.rotate(angle, 0, 0);
	}

	/**
	 * An active rotation around the given angles
	 * 
	 * @see #rotatePassive(double, double, double)
	 * @param angleX
	 * @param angleY
	 * @param angleZ
	 * @return
	 */
	public Vector rotate(double angleX, double angleY, double angleZ)
	{
		boolean extrinsic = false;
		boolean passive = false;
		return this.rotate(angleX, angleY, angleZ, extrinsic, passive);
	}

	public Vector rotate(double angleX, double angleY, double angleZ, boolean extrinsic, boolean passive)
	{
		int dimension = this.getDimension();

		UnaryOperator<Matrix> singleRotationMatrixModifier = m -> passive ? m.inverse() : m;

		//		List<UnaryOperator<Matrix>> rotations = extrinsic ? Arrays.asList((m) ->
		//		{
		//			Vector ex = m	.inverse()
		//							.multiply(new Vector(1, 0, 0));
		//			return this.getRotationMatrix(ex, angleX);
		//		}, (m) ->
		//		{
		//			Vector ey = m	.inverse()
		//							.multiply(new Vector(0, 1, 0));
		//			return this.getRotationMatrix(ey, angleY);
		//		}, (m) ->
		//		{
		//			Vector ez = m	.inverse()
		//							.multiply(new Vector(0, 0, 1));
		//			return this.getRotationMatrix(ez, angleZ);
		//		})
		//
		//				: Arrays.asList((m) -> this.getRotationMatrixZ(angleZ), (m) -> this.getRotationMatrixY(angleY), (m) -> this.getRotationMatrixX(angleX));
		//
		//		Collections.reverse(rotations);
		//
		//		AtomicReference<Matrix> previous = new AtomicReference<>(Matrix.identity(3));
		//		Matrix rotationMatrix = rotations	.stream()
		//											.map(ms -> ms.apply(previous.get()))
		//											.peek(m -> previous.set(previous.get()
		//																			.multiply(m)))
		//											.map(singleRotationMatrixModifier)
		//											.reduce(Matrix::multiply)
		//											.get();

		Vector ex = new Vector(1, 0, 0);
		Matrix rotationMatrixX = this.getRotationMatrix(ex, angleX);
		Matrix inverseRotationMatrixX = this.getRotationMatrix(ex, -angleX);

		Vector ey = inverseRotationMatrixX.multiply(new Vector(0, 1, 0));
		Matrix rotationMatrixY = this.getRotationMatrix(ey, angleY);
		Matrix inverseRotationMatrixY = this.getRotationMatrix(ey, -angleY);

		Vector ez = inverseRotationMatrixX	.multiply(inverseRotationMatrixY)
											.multiply(new Vector(0, 0, 1));
		Matrix rotationMatrixZ = this.getRotationMatrix(ez, angleZ);
		Matrix inverseRotationMatrixZ = this.getRotationMatrix(ez, -angleZ);

		Matrix rotationMatrix = rotationMatrixX	.multiply(rotationMatrixY)
												.multiply(rotationMatrixZ);

		Matrix reducedRotationMatrix = rotationMatrix.getSubMatrix(0, 0, dimension - 1, dimension - 1);
		return reducedRotationMatrix.multiply(this);

		//		double r = this.absolute();
		//		double gamma = Math.atan(Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY()) / this.getZ());
		//		double teta = Math.atan(this.getY() / this.getX());
		//
		//		gamma += angleX;
		//		teta += angleY;
		//
		//		return new Vector(r * Math.sin(gamma) * Math.cos(teta), r * Math.sin(gamma) * Math.sin(teta), r * Math.cos(teta));
	}

	/**
	 * A passive rotation around the given angles
	 * 
	 * @param angleX
	 * @param angleY
	 * @param angleZ
	 * @return
	 */
	public Vector rotatePassive(double angleX, double angleY, double angleZ)
	{
		boolean passive = false;
		boolean extrinsic = true;
		return this.rotate(angleX, angleY, angleZ, extrinsic, passive);
	}

	/**
	 * Returns the outer product / tensor product of this and another {@link Vector}
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Outer_product">wikipedia</a>
	 * @param other
	 * @return
	 */
	public Matrix outerProduct(Vector other)
	{
		Builder builder = Matrix.builder();

		for (int ii = 0; ii < this.getDimension(); ii++)
		{
			double[] row = new double[other.getDimension()];
			for (int jj = 0; jj < other.getDimension(); jj++)
			{
				double value = this.getCoordinate(ii);
				double otherValue = other.getCoordinate(jj);
				row[jj] = value * otherValue;
			}
			builder.addRow(row);
		}

		return builder.build();
	}

	public Matrix crossProductMatrix()
	{
		double z = this.getZ();
		double y = this.getY();
		double x = this.getX();
		return Matrix	.builder()
						.addRow(0, -z, y)
						.addRow(z, 0, -x)
						.addRow(-y, x, 0)
						.build();
	}

	public int getDimension()
	{
		return this.coordinates.length;
	}

	private Matrix getRotationMatrix(Vector u, double angleU)
	{
		double cos = Math.cos(angleU / 180.0 * Math.PI);
		double sin = Math.sin(angleU / 180.0 * Math.PI);
		return Matrix	.identity(3)
						.multiply(cos)
						.add(u	.crossProductMatrix()
								.multiply(sin))
						.add(u	.multiply(1 - cos)
								.outerProduct(u));
	}

	private Matrix getRotationMatrixX(double angleX)
	{
		double cos = Math.cos(angleX / 180.0 * Math.PI);
		double sin = Math.sin(angleX / 180.0 * Math.PI);
		return Matrix	.builder()
						.addRow(1, 0, 0)
						.addRow(0, cos, -sin)
						.addRow(0, sin, cos)
						.build();
	}

	private Matrix getRotationMatrixY(double angleY)
	{
		double cos = Math.cos(angleY / 180 * Math.PI);
		double sin = Math.sin(angleY / 180 * Math.PI);
		return Matrix	.builder()
						.addRow(cos, 0, sin)
						.addRow(0, 1, 0)
						.addRow(-sin, 0, cos)
						.build();
	}

	private Matrix getRotationMatrixZ(double angleZ)
	{
		double cos = Math.cos(angleZ / 180 * Math.PI);
		double sin = Math.sin(angleZ / 180 * Math.PI);
		return Matrix	.builder()
						.addRow(cos, -sin, 0)
						.addRow(sin, cos, 0)
						.addRow(0, 0, 1)
						.build();
	}

	/**
	 * Be aware that this calculation is limited to 2D and 3D Vectors
	 *
	 * @param pointOnLine
	 * @param lineDirection
	 * @return
	 */
	public double closestDistanceToLine(Vector pointOnLine, Vector lineDirection)
	{
		//		Vector delta = this	.as3DVector()
		//							.subtract(pointOnLine.as3DVector());
		//		return delta.multiplyCross(lineDirection.as3DVector())
		//					.absolute()
		//				/ lineDirection	.as3DVector()
		//								.absolute();
		return this	.closestDirectionToLine(pointOnLine, lineDirection)
					.absolute();
	}

	/**
	 * Be aware that this calculation is limited to 2D and 3D Vectors
	 *
	 * @param pointOnLine
	 * @param lineDirection
	 * @return
	 */
	public Vector closestDirectionToLine(Vector pointOnLine, Vector lineDirection)
	{
		Vector a = pointOnLine;
		Vector n = lineDirection.normVector();
		Vector p = this;
		Vector delta = a.subtract(p);
		return delta.subtract(n.multiply(delta.multiplyScalar(n)));
	}

	/**
	 * @see #asVectorWithDimension(int)
	 * @return
	 */
	public Vector as3DVector()
	{
		return this.asVectorWithDimension(3);
	}

	/**
	 * @see #asVectorWithDimension(int)
	 * @return
	 */
	public Vector as2DVector()
	{
		return this.asVectorWithDimension(2);
	}

	/**
	 * @see #as2DVector()
	 * @see #as3DVector()
	 * @param targetDimension
	 * @return
	 */
	public Vector asVectorWithDimension(int targetDimension)
	{
		final int sourceDimension = this.getDimension();
		double[] coordinates = new double[targetDimension];
		for (int ii = 0; ii < Math.min(targetDimension, sourceDimension); ii++)
		{
			coordinates[ii] = this.coordinates[ii];
		}
		return new Vector(coordinates);
	}

	@Override
	public String toString()
	{
		return Arrays.toString(this.coordinates);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.coordinates);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		Vector other = (Vector) obj;
		if (!Arrays.equals(this.coordinates, other.coordinates))
		{
			return false;
		}
		return true;
	}

	public double distanceTo(Vector vector)
	{
		return this	.subtract(vector)
					.absolute();
	}

	public boolean equals(Vector other, double delta)
	{
		boolean retval = true;

		if (this.getDimension() != other.getDimension())
		{
			retval = false;
		}
		else
		{
			for (int ii = 0; ii < this.getDimension(); ii++)
			{
				retval &= Math.abs(this.getCoordinate(ii) - other.getCoordinate(ii)) < delta;
			}
		}

		return retval;
	}

	public static double translatePIToDegree(double angleInPi)
	{
		double retval = 180.0 * angleInPi / Math.PI;
		while (retval >= 360)
		{
			retval -= 360;
		}
		return retval;
	}

	public static double translateDegreeToPi(double angleInDegree)
	{
		double retval = Math.PI * angleInDegree / 180.0;
		while (retval >= Math.PI * 2)
		{
			retval -= Math.PI * 2;
		}
		return retval;
	}

	public double determineAngle(Vector other)
	{
		double cosValue = this	.normVector()
								.multiplyScalar(other.normVector());
		double acos = Math.acos(cosValue);
		return translatePIToDegree(acos);
	}

	public double determineAngleToXAxis()
	{
		return this.determineAngle(Vector.E_X);
	}

}
