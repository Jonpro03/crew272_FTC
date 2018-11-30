package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.Movement.AutonomousRoute;
import org.firstinspires.ftc.teamcode.Movement.Rotation;
import org.firstinspires.ftc.teamcode.Movement.StraightRoute;

import java.util.Arrays;

class Routes {
    static final StraightRoute MOVE_TO_NEXT_ORE = new StraightRoute( 14.5, 0.2, 2);
    static final StraightRoute DRIVE_TO_CRATER = new StraightRoute(112, 0.5, 10);

    static final AutonomousRoute DEPARTURE = new AutonomousRoute(Arrays.asList(
            new StraightRoute(23.15, 0.6, 4), // drive forward from the lander
            new Rotation(-90, 0.6, 2), // Turn left in front of the ores
            new StraightRoute(-18.5, 0.3, 2)// Reverse to position in front of the first ore
    ));

    static final AutonomousRoute CRATER_SIDE_ORE_TO_DEPOT = new AutonomousRoute(Arrays.asList(
            new StraightRoute(34.5, 0.8, 4),
            new Rotation(-45, 0.3, 2),
            new StraightRoute(46,0.8,5)
    ));

    static final AutonomousRoute DEPOT_SIDE_ORE_TO_DEPOT = new AutonomousRoute(Arrays.asList(
            new StraightRoute(17, 0.6, 4),
            new Rotation(45, 0.4, 2),
            new StraightRoute(12,0.6,5),
            new Rotation(90, 0.4, 2),
            new StraightRoute(38,1, 5)
    ));

    static final AutonomousRoute ORE_FOUND = new AutonomousRoute(Arrays.asList(
            new StraightRoute(-3, 0.2, 1),
            new StraightRoute(3, 0.2, 1)
    ), 250);
}