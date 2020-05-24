package org.omnaest.vector;

public class PolarVector
{
    private Vector vector;

    protected PolarVector(Vector vector)
    {
        super();
        this.vector = vector;
    }

    public double getRadius()
    {
        return Math.sqrt(this.vector.getCoordinatesStream()
                                    .map(v -> v * v)
                                    .sum());
    }

    public PolarVector setRadius(double radius)
    {
        return this.vector.normVector()
                          .multiply(radius)
                          .asPolarVector();
    }

    public double getTheta()
    {
        return Math.acos(this.vector.getZ() / this.getRadius());
    }

    public double getThetaInDegree()
    {
        return Math.toDegrees(this.getTheta());
    }

    public double getPhi()
    {
        return Math.atan2(this.vector.getY(), this.vector.getX());
    }

    public double getPhiInDegree()
    {
        return Math.toDegrees(this.getPhi());
    }

    public PolarVector addThetaInDegree(double thetaDelta)
    {
        return this.addTheta(Math.toRadians(thetaDelta));
    }

    public PolarVector addTheta(double thetaDelta)
    {
        double theta = this.getTheta() + thetaDelta;
        double x = this.getRadius() * Math.sin(theta) * Math.cos(this.getPhi());
        double y = this.getRadius() * Math.sin(theta) * Math.sin(this.getPhi());
        double z = this.getRadius() * Math.cos(theta);
        return Vector.of(x, y, z)
                     .asPolarVector();
    }

    public PolarVector addPhiInDegree(double phiDelta)
    {
        return this.addPhi(Math.toRadians(phiDelta));
    }

    public PolarVector addPhi(double phiDelta)
    {
        double phi = this.getPhi() + phiDelta;
        double x = this.getRadius() * Math.sin(this.getTheta()) * Math.cos(phi);
        double y = this.getRadius() * Math.sin(this.getTheta()) * Math.sin(phi);
        double z = this.getRadius() * Math.cos(this.getTheta());
        return Vector.of(x, y, z)
                     .asPolarVector();
    }

    public Vector asCartesianVector()
    {
        return this.vector;
    }

    @Override
    public String toString()
    {
        return "PolarVector [vector=" + this.vector + "]";
    }

}
