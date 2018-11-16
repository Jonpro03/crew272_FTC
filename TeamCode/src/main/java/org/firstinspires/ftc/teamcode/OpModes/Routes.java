package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.Movement.AutonomousRoute;
import org.firstinspires.ftc.teamcode.Movement.Rotation;
import org.firstinspires.ftc.teamcode.Movement.StraightRoute;

import java.util.Arrays;

class Routes {
    static final StraightRoute MOVE_TO_NEXT_ORE = new StraightRoute( 14.5, 0.6, 2);
    static final StraightRoute REVERSE_INTO_CRATER = new StraightRoute(-110, 1, 10);

    static final AutonomousRoute DEPARTURE = new AutonomousRoute(Arrays.asList(
            new StraightRoute(25.15, 0.8, 3), // drive forward from the lander
            new Rotation(-90, 0.8, 2), // Turn left in front of the ores
            new StraightRoute(-18.5, 1, 2)// Reverse to position in front of the first ore
    ));

    static final AutonomousRoute CRATER_SIDE_ORE_TO_DEPOT = new AutonomousRoute(Arrays.asList(
            new StraightRoute(34.5, 1, 4),
            new Rotation(-45, 0.8, 2),
            new StraightRoute(44,1,5)
    ));

    static final AutonomousRoute DEPOT_SIDE_ORE_TO_DEPOT = new AutonomousRoute(Arrays.asList(
            new StraightRoute(17, 1, 4),
            new Rotation(45, 0.8, 2),
            new StraightRoute(12,1,5),
            new Rotation(90, 0.8, 2),
            new StraightRoute(36,1, 5)
    ));

    static final AutonomousRoute ORE_FOUND = new AutonomousRoute(Arrays.asList(
            new StraightRoute(-3, 0.5, 1),
            new StraightRoute(3, 0.5, 1)
    ), 250);
}