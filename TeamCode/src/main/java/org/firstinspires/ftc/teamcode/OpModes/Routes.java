package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.Movement.AutonomousRoute;
import org.firstinspires.ftc.teamcode.Movement.Rotation;
import org.firstinspires.ftc.teamcode.Movement.StraightRoute;

import java.util.Arrays;

class Routes {
    static final StraightRoute MOVE_TO_NEXT_ORE = new StraightRoute( 14.5, 0.2, 2);
    static final StraightRoute DRIVE_TO_CRATER = new StraightRoute(112, 0.9, 10);

    static final AutonomousRoute DEPARTURE = new AutonomousRoute(Arrays.asList(
            //new Rotation(5, 1, 1), // Turn left in front of the ores
            new StraightRoute(24, 0.6, 4), // drive forward from the lander
            new Rotation(-86, 0.3, 3), // Turn left in front of the ores
            new StraightRoute(-20.0, 0.3, 2)// Reverse to position in front of the first ore
    ));

    static final AutonomousRoute CRATER_SIDE_ORE_TO_DEPOT = new AutonomousRoute(Arrays.asList(
            new StraightRoute(35.5, 0.8, 4),
            new Rotation(-44, 0.3, 3),
            new StraightRoute(46,0.8,5)
    ));

    static final AutonomousRoute DEPOT_SIDE_ORE_TO_DEPOT = new AutonomousRoute(Arrays.asList(
            new StraightRoute(18, 0.6, 4),
            new Rotation(43, 0.3, 3),
            new StraightRoute(10,0.6,5),
            new Rotation(75, 0.3, 3),
            new StraightRoute(38,1, 5)
    ));


    static final AutonomousRoute ORE_FOUND = new AutonomousRoute(Arrays.asList(
            new Rotation(25, 0.3, 2),
            new Rotation(-25, 0.3, 2)
    ), 250);
}