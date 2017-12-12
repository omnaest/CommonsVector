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

public class ModifiableVector extends Vector
{
	public ModifiableVector(double x, double y, double z)
	{
		super(x, y, z);
	}

	public ModifiableVector(double x, double y)
	{
		super(x, y);
	}

	public ModifiableVector(double... coordinates)
	{
		super(coordinates);
	}

	public ModifiableVector(double x)
	{
		super(x);
	}

	@Override
	public ModifiableVector setX(double x)
	{
		super.setX(x);
		return this;
	}

	@Override
	public ModifiableVector setY(double y)
	{
		super.setY(y);
		return this;
	}

	@Override
	public ModifiableVector setZ(double z)
	{
		super.setZ(z);
		return this;
	}

	@Override
	public ModifiableVector setCoordinate(int index, double value)
	{
		super.setCoordinate(index, value);
		return this;
	}

	public ModifiableVector addX(double x)
	{
		return this.setX(this.getX() + x);
	}

	public ModifiableVector addY(double y)
	{
		return this.setY(this.getY() + y);
	}

	public ModifiableVector addZ(double z)
	{
		return this.setZ(this.getZ() + z);
	}

	public static ModifiableVector of(double... coordinates)
	{
		return new ModifiableVector(coordinates);
	}
}
