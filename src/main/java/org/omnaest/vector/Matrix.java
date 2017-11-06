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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @see Vector
 * @see #builder()
 * @author Omnaest
 */
public class Matrix
{
	protected static final Matrix NULL = new Matrix(new double[0][0]);

	private double[][] data;

	public Matrix(double[]... data)
	{
		super();
		this.data = data;
	}

	public Matrix(Vector vector)
	{
		super();
		double[] coordinates = vector.getCoordinates();
		this.data = new double[coordinates.length][1];

		int ii = 0;
		for (double value : coordinates)
		{
			this.data[ii++] = new double[] { value };
		}
	}

	public Vector multiply(Vector vector)
	{
		Matrix matrixB = new Matrix(vector);
		Matrix result = this.multiply(matrixB);
		int dimensionY = result.getDimensions()[1];
		double[] coordinates = new double[dimensionY];
		for (int ii = 0; ii < dimensionY; ii++)
		{
			coordinates[ii] = result.getRaw(0, ii);
		}
		return new Vector(coordinates);
	}

	public Matrix multiply(double scalar)
	{
		Matrix matrix = Matrix	.builder()
								.addRows(this.getRows())
								.build();

		for (int ii = 1; ii <= this.getRowCount(); ii++)
		{
			for (int jj = 1; jj <= this.getColumnCount(); jj++)
			{
				matrix.setValue(ii, jj, matrix.getValue(ii, jj) * scalar);
			}
		}

		return matrix;
	}

	public Matrix multiply(Matrix matrixB)
	{
		int[] dimensionsA = this.getDimensions();
		int[] dimensionsB = matrixB.getDimensions();

		if (dimensionsA[0] != dimensionsB[1])
		{
			throw new IllegalArgumentException("x dimension of A must be equal to y dimension of B");
		}

		int freeDimension = dimensionsA[0];

		double[][] data2 = new double[dimensionsA[1]][dimensionsB[0]];

		for (int y = 0; y < data2.length; y++)
		{
			for (int x = 0; x < data2[y].length; x++)
			{
				double sum = 0;

				for (int ii = 0; ii < freeDimension; ii++)
				{
					double a = this.getRaw(ii, y);
					double b = matrixB.getRaw(x, ii);
					sum += a * b;
				}
				data2[y][x] = sum;
			}
		}

		return new Matrix(data2);
	}

	protected double getRaw(int x, int y)
	{
		return this.data[y][x];
	}

	/**
	 * Returns the value of the row i and column j.<br>
	 * <br>
	 * i = 1,2,3,... and j = 1,2,3,...
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public double getValue(int i, int j)
	{
		return this.getRaw(j - 1, i - 1);
	}

	/**
	 * Returns the dimension as array in form: [columns,rows]
	 * 
	 * @see Matrix#getRowCount()
	 * @see #getColumnCount()
	 * @return
	 */
	public int[] getDimensions()
	{
		return new int[] { this.data[0].length, this.data.length };
	}

	public int getRowCount()
	{
		return this.getDimensions()[1];
	}

	public int getColumnCount()
	{
		return this.getDimensions()[0];
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		int[] dimensions = this.getDimensions();
		for (int y = 0; y < dimensions[1]; y++)
		{
			for (int x = 0; x < dimensions[0]; x++)
			{
				double value = this.getRaw(x, y);
				sb.append(String.format(Locale.ENGLISH, "% 6.2f", value) + " ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public Matrix getSubMatrix(int x1, int y1, int x2, int y2)
	{
		double[][] data2 = new double[y2 - y1 + 1][x2 - x1 + 1];
		for (int y = y1; y <= y2; y++)
		{
			for (int x = x1; x <= x2; x++)
			{
				data2[y - y1][x - x1] = this.getRaw(x, y);
			}
		}
		return new Matrix(data2);
	}

	protected Matrix getSubMatrixModulo(int x1, int y1, int x2, int y2)
	{
		int[] dimensions = this.getDimensions();
		double[][] data2 = new double[y2 - y1 + 1][x2 - x1 + 1];
		for (int y = y1; y <= y2; y++)
		{
			for (int x = x1; x <= x2; x++)
			{
				int xMod = x % dimensions[0];
				int yMod = y % dimensions[1];
				data2[y - y1][x - x1] = this.getRaw(xMod, yMod);
			}
		}
		return new Matrix(data2);
	}

	public double determinant()
	{
		return this.determinantOf(this);
	}

	protected double determinantOf(Matrix matrix)
	{
		//
		double retval = 0.0;

		//
		int dimension = matrix.getRowCount();
		if (dimension != matrix.getColumnCount())
		{
			throw new IllegalStateException("Matrix must be square");
		}
		if (dimension > 2)
		{
			for (int ii = 0; ii < dimension; ii++)
			{
				double factor = matrix.getRaw(0, ii);
				double determinant = matrix	.getSubMatrixModulo(1, ii + 1, dimension - 1, dimension - 1 + ii)
											.determinant();
				retval += factor * determinant;
			}
		}
		else if (dimension == 2)
		{
			retval = matrix.getRaw(0, 0) * matrix.getRaw(1, 1) - matrix.getRaw(1, 0) * matrix.getRaw(0, 1);
		}
		else if (dimension == 1)
		{
			retval = matrix.getRaw(0, 0);
		}
		else
		{
			throw new IllegalArgumentException("Matrix dimension has to be at least 2");
		}

		//
		return retval;
	}

	protected Matrix transposed()
	{
		int[] dimensions = this.getDimensions();

		double[][] data = new double[dimensions[0]][dimensions[1]];
		for (int x = 0; x < dimensions[0]; x++)
		{
			for (int y = 0; y < dimensions[1]; y++)
			{
				data[x][y] = this.data[y][x];
			}
		}

		return new Matrix(data);
	}

	/**
	 * Removes a row from the {@link Matrix}, starting with i=1,2,3,...
	 * 
	 * @param i
	 * @return
	 */
	public Matrix reducedByRow(int i)
	{
		Builder builder = Matrix.builder();

		for (int ii = 1; ii <= this.getRowCount(); ii++)
		{
			if (ii != i)
			{
				builder.addRow(this.getRow(ii));
			}
		}

		return builder.build();
	}

	/**
	 * Returns the row i as {@link Vector}. i = 1,2,3,...
	 * 
	 * @param i
	 * @return
	 */
	private Vector getRow(int i)
	{
		return new Vector(Arrays.copyOf(this.data[i - 1], this.data[i - 1].length));
	}

	/**
	 * Returns the column j as {@link Vector}. j = 1,2,3,...
	 * 
	 * @param j
	 * @return
	 */
	private Vector getColumn(int j)
	{
		return this	.transposed()
					.getRow(j);
	}

	/**
	 * Removes a column of the {@link Matrix}, starting with j=1,2,3,...
	 * 
	 * @param j
	 * @return
	 */
	public Matrix reducedByColumn(int j)
	{
		Builder builder = Matrix.builder();

		for (int jj = 1; jj <= this.getColumnCount(); jj++)
		{
			if (jj != j)
			{
				builder.addColumn(this.getColumn(jj));
			}
		}

		return builder.build();
	}

	public static interface Builder extends RowBuilder, ColumnBuilder
	{

	}

	public static interface ColumnBuilder
	{
		ColumnBuilder addColumn(Vector vector);

		ColumnBuilder addColumn(double... values);

		Matrix build();
	}

	public static interface RowBuilder
	{
		RowBuilder addRow(Vector vector);

		RowBuilder addRow(double... values);

		RowBuilder addRows(Vector[] rows);

		Matrix build();
	}

	public static Builder builder()
	{
		return new Builder()
		{
			private List<double[]>	rows	= new ArrayList<>();
			private List<double[]>	columns	= new ArrayList<>();

			@Override
			public Matrix build()
			{
				Matrix retval = Matrix.NULL;

				if (!this.columns.isEmpty())
				{
					retval = new Matrix(this.columns.toArray(new double[0][0])).transposed();
				}
				else if (!this.rows.isEmpty())
				{
					retval = new Matrix(this.rows.toArray(new double[0][0]));
				}

				return retval;
			}

			@Override
			public RowBuilder addRow(double... values)
			{
				this.rows.add(values);
				return this;
			}

			@Override
			public ColumnBuilder addColumn(double... values)
			{
				this.columns.add(values);
				return this;
			}

			@Override
			public RowBuilder addRow(Vector vector)
			{
				return this.addRow(vector.getCoordinates());
			}

			@Override
			public ColumnBuilder addColumn(Vector vector)
			{
				return this.addColumn(vector.getCoordinates());
			}

			@Override
			public RowBuilder addRows(Vector[] rows)
			{
				Arrays	.asList(rows)
						.stream()
						.forEach(row -> this.addRow(row));
				return this;
			}
		};
	}

	public Matrix reducedByRowAndColumn(int i, int j)
	{
		return this	.reducedByRow(i)
					.reducedByColumn(j);
	}

	/**
	 * Returns the adjunct of this {@link Matrix}.
	 * 
	 * @see <a href="https://de.wikipedia.org/wiki/Adjunkte">wikipedia</a>
	 * @return
	 */
	public Matrix adjunct()
	{
		Matrix retval = Matrix	.builder()
								.addRows(this.getRows())
								.build();
		for (int ii = 1; ii <= this.getRowCount(); ii++)
		{
			for (int jj = 1; jj <= this.getColumnCount(); jj++)
			{
				double sign = Math.pow(-1, ii + jj);
				double determinant = this	.reducedByRowAndColumn(ii, jj)
											.determinant();
				retval.setValue(ii, jj, sign * determinant);
			}
		}

		return retval.transposed();
	}

	public Matrix inverse()
	{
		return this	.adjunct()
					.multiply(1.0 / this.determinant());
	}

	/**
	 * Sets the given value at row i and column j.<br>
	 * <br>
	 * i = 1,2,3,... and j = 1,2,3,...
	 * 
	 * @param i
	 * @param j
	 * @param value
	 * @return
	 */
	private Matrix setValue(int i, int j, double value)
	{
		this.data[i - 1][j - 1] = value;
		return this;
	}

	private Vector[] getRows()
	{
		List<Vector> rows = IntStream	.range(1, this.getRowCount() + 1)
										.mapToObj(ii -> this.getRow(ii))
										.collect(Collectors.toList());
		return rows.toArray(new Vector[rows.size()]);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(this.data);
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
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(this.data, other.data))
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns a identity matrix in the given dimension.<br>
	 * <br>
	 * Example:<br>
	 * 
	 * <pre>
	 * 1 0 0
	 * 0 1 0
	 * 0 0 1
	 * </pre>
	 * 
	 * @param dimension
	 * @return
	 */
	public static Matrix identity(int dimension)
	{
		Builder builder = Matrix.builder();

		for (int ii = 0; ii < dimension; ii++)
		{
			double[] row = new double[dimension];
			row[ii] = 1.0;
			builder.addRow(row);
		}

		return builder.build();
	}

	/**
	 * Adds another {@link Matrix}
	 * 
	 * @param other
	 * @return
	 */
	public Matrix add(Matrix other)
	{
		Matrix retval = Matrix.clone(this);
		for (int ii = 1; ii <= this.getRowCount(); ii++)
		{
			for (int jj = 1; jj <= this.getColumnCount(); jj++)
			{
				retval.setValue(ii, jj, this.getValue(ii, jj) + other.getValue(ii, jj));
			}
		}
		return retval;
	}

	private static Matrix clone(Matrix matrix)
	{
		return Matrix	.builder()
						.addRows(matrix.getRows())
						.build();
	}

}
