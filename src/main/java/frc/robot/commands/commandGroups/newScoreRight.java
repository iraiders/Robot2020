package frc.robot.commands.commandGroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.oldMoveCommands.moveBackward;
import frc.robot.commands.oldMoveCommands.moveForward;
import frc.robot.commands.oldMoveCommands.turnLeft45;
import frc.robot.commands.oldMoveCommands.turnRight45;
import frc.robot.commands.outputGateCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class newScoreRight extends SequentialCommandGroup {
  public newScoreRight(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
    addCommands(
      new moveForward(9.92, driveSubsystem),

      new turnRight45(driveSubsystem, 30),

      new outputGateCommand(intakeSubsystem),

      new turnLeft45(driveSubsystem, 30),

      new moveBackward(8, driveSubsystem)
    );
  }
}
