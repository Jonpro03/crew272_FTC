package org.firstinspires.ftc.teamcode.Movement;

public class Utility {
    public static Vector calcPath(double curX, double curY, double curAngle, double destX, double destY) {
        double a = destX - curX;
        double b = destY - curY;
        double c = Math.sqrt((a * a) + (b * b));

        double destAngle = Math.atan2(b, a);
        double angleCorrection = destAngle - curAngle;
        return new Vector(c, angleCorrection);
    }
}
