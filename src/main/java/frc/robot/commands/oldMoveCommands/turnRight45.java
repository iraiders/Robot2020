package frc.robot.commands.oldMoveCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class turnRight45 extends CommandBase {
  double originalDist = 0;
  double newDist = 0;
  double accumulatedDist = 0;
  double leftSpeed = 0;
  double rightSpeed = 0;
  double targetAngle = 0;

  private final DriveSubsystem m_driveSubsystem;

  public turnRight45(DriveSubsystem driveSubsystem, double Angle) {
    m_driveSubsystem = driveSubsystem;
    targetAngle = Angle;
    addRequirements(driveSubsystem); //establishes drive subsystem as subsystem used
    accumulatedDist = 0;
  }

  @Override
  public void initialize() {
    originalDist = m_driveSubsystem.toFeet(m_driveSubsystem.getEncoder(1).getPosition());
    newDist = originalDist;
    m_driveSubsystem.resetEncoder(m_driveSubsystem.getEncoder(1));
    accumulatedDist = 0;
  }

  @Override
  public void execute() {
    System.out.println("This got to execute");
    leftSpeed = 0.25;
    rightSpeed = -0.25;
    m_driveSubsystem.getRoboDrive().tankDrive(leftSpeed, rightSpeed);
    newDist = m_driveSubsystem.getEncoder(1).getPosition();
    accumulatedDist += -(m_driveSubsystem.toFeet(m_driveSubsystem.encoderDistance(m_driveSubsystem.getEncoder(1)))); //adds old distance to encoder input, translated to feet;
    System.out.println("It should be moving right now");
    System.out.println("Traveled " + accumulatedDist + "Feet");
  }

  @Override
  public boolean isFinished() {
    if (accumulatedDist > targetAngle/45) { //largely arbitrary, guess-and-check
      m_driveSubsystem.getRoboDrive().stopMotor();
      Timer.delay(0.25);
      return true;
    } else return false;
  }

}
