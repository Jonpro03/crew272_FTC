package org.firstinspires.ftc.teamcode.Movement;

import org.firstinspires.ftc.teamcode.Movement.Models.Location;
import org.firstinspires.ftc.teamcode.Movement.Models.Point2D;
import org.firstinspires.ftc.teamcode.Movement.Models.PolarCoordinate;

public class Utility {
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int STRAIGHT = 0;

    public static PolarCoordinate calcPolarCoordinate(Location currentLocation, Point2D destinationPoint) {
        double a = destinationPoint.x - currentLocation.position.x;
        double b = destinationPoint.y - currentLocation.position.y;
        double c = Math.sqrt((a * a) + (b * b));

        double destAngle = Math.atan2(b, a);
        double correctedAngle = destAngle - currentLocation.rotation;
        return new PolarCoordinate(c, correctedAngle);
    }
}
