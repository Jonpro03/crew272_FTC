package org.firstinspires.ftc.teamcode.Movement.Models;

import org.firstinspires.ftc.teamcode.Movement.Models.Route;

import java.util.List;

public class AutonomousRoute {
    public List<? extends Route> routeItems;
    public int pauseMS;

    public AutonomousRoute(List<? extends Route> routes, int pauseMS) {
        routeItems = routes;
        this.pauseMS = pauseMS;
    }

    public AutonomousRoute(List<? extends Route> routes) {
        routeItems = routes;
        pauseMS = 100;
    }
}
