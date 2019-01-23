package org.firstinspires.ftc.teamcode.Movement.Models;

public class Route {
    public double leftIn;
    public double rightIn;
    public double speed;
    public double timeout;

    public Route(double leftIn, double rightIn, double speed, double timeout) {
        this.leftIn = leftIn;
        this.rightIn = rightIn;
        this.speed = speed;
        this.timeout = timeout;
    }
}
