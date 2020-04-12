package org.omnaest.vector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PolarVectorTest
{

    @Test
    public void testGetRadius() throws Exception
    {
        assertEquals(1, Vector.of(1, 0, 0)
                              .asPolarVector()
                              .getRadius(),
                     0.01);
        assertEquals(1, Vector.of(0, 1, 0)
                              .asPolarVector()
                              .getRadius(),
                     0.01);
        assertEquals(1, Vector.of(0, 0, 1)
                              .asPolarVector()
                              .getRadius(),
                     0.01);
    }

    @Test
    public void testGetTheta() throws Exception
    {
        assertEquals(0.0, Vector.of(0, 0, 1)
                                .asPolarVector()
                                .getThetaInDegree(),
                     0.01);
        assertEquals(90.0, Vector.of(1, 1, 0)
                                 .asPolarVector()
                                 .getThetaInDegree(),
                     0.01);
    }

    @Test
    public void testGetPhi() throws Exception
    {
        assertEquals(0.0, Vector.of(0, 0, 1)
                                .asPolarVector()
                                .getPhiInDegree(),
                     0.01);
        assertEquals(90.0, Vector.of(0, 1, 0)
                                 .asPolarVector()
                                 .getPhiInDegree(),
                     0.01);
        assertEquals(00.0, Vector.of(1, 0, 0)
                                 .asPolarVector()
                                 .getPhiInDegree(),
                     0.01);
    }

    @Test
    public void testAddPhiInDegree() throws Exception
    {
        Vector vector = Vector.of(1, 0, 0)
                              .asPolarVector()
                              .addPhiInDegree(90)
                              .asCartesianVector();
        assertEquals(0, vector.getX(), 0.01);
        assertEquals(1, vector.getY(), 0.01);
        assertEquals(0, vector.getZ(), 0.01);
    }

    @Test
    public void testAddThetaInDegree() throws Exception
    {
        Vector vector = Vector.of(0, 0, 1)
                              .asPolarVector()
                              .addThetaInDegree(90)
                              .asCartesianVector();
        assertEquals(1, vector.getX(), 0.01);
        assertEquals(0, vector.getY(), 0.01);
        assertEquals(0, vector.getZ(), 0.01);
    }

}
