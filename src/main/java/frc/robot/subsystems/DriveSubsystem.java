package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ConfigureBed;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.SMDrive;

public class DriveSubsystem extends SubsystemBase {
  /**
   * Creates a new DriveSubsystem.
   */


  /*Creates motors, getting motor controller (CANSparkMax) ports from RobotMap
  * MAKE SURE TEST BED IS SET TO BRUSHED*/
  private CANSparkMax frontLeft = new CANSparkMax(RobotMap.frontLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);
  private CANSparkMax frontRight = new CANSparkMax(RobotMap.frontRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);
  private CANSparkMax backLeft = new CANSparkMax(RobotMap.backLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);
  private CANSparkMax backRight = new CANSparkMax(RobotMap.backRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);

  CANEncoder encoder1;
  CANEncoder encoder2;
  CANEncoder encoder3;
  CANEncoder encoder4;
  //Differential drive coordinates motors, used for tank + arcade drive
  public SMDrive driveCommand;
  public DifferentialDrive roboDrive;
  double current_E_Value = 0;
  double old_E_Value = 0;
  double traveledInches = 0;
  double traveledFeet = 0;
  double i = 0;
  double printIterator = 5;
  public double distanceToDrive = 0;
  public static double gyroTurnConstant() {return 0.9; } //Multiplies angles by constant
  ADXRS450_Gyro gyro = new ADXRS450_Gyro();

  public DifferentialDrive getRoboDrive () {
    return roboDrive;
  }

  public CANEncoder getEncoder(int number) {
    if (number == 1) {
      return encoder1;
    }
    else if (number == 2) {
      return encoder2;
    }
    else if (number == 3) {
      return encoder3;
    }
    else if (number == 4) {
      return encoder4;
    }
    else return null;
  }

  public boolean printIterator() {
    if (i == printIterator) { return true; }
    else { return false;}
  }

  public ADXRS450_Gyro getGyro() {
    return gyro;
  }

  public DriveSubsystem() {

    if(ConfigureBed.getInstance().configBedInit()== ConfigureBed.Jumper.ONE || ConfigureBed.getInstance().configBedInit()== ConfigureBed.Jumper.THREE){
      //System.out.println("this is a test; 1");
      frontLeft = new CANSparkMax(RobotMap.frontLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
      frontRight = new CANSparkMax(RobotMap.frontRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
      backLeft = new CANSparkMax(RobotMap.backLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
      backRight = new CANSparkMax(RobotMap.backRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);

    }
    else if(ConfigureBed.getInstance().configBedInit()== ConfigureBed.Jumper.TWO){
      //System.out.println("this is a test; 2");
      frontLeft = new CANSparkMax(RobotMap.frontLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);
      frontRight = new CANSparkMax(RobotMap.frontRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);
      backLeft = new CANSparkMax(RobotMap.backLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);
      backRight = new CANSparkMax(RobotMap.backRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushed);

    }
    else{
      System.out.println("An error has occurred with the jumper");
      //System.exit(-1);
      //frontLeft = new CANSparkMax(RobotMap.frontLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
      //frontRight = new CANSparkMax(RobotMap.frontRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
      //backLeft = new CANSparkMax(RobotMap.backLeftMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
      //backRight = new CANSparkMax(RobotMap.backRightMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
    }
    Robot.initializeSparkDefaults(frontLeft, frontRight);

    backLeft.follow(frontLeft);
    backRight.follow(frontRight);

    encoder1 = frontLeft.getEncoder();
    encoder2 = frontRight.getEncoder();
    encoder3 = backLeft.getEncoder();
    encoder4 = backRight.getEncoder();

    driveCommand = new SMDrive(this);
    roboDrive = new DifferentialDrive(frontLeft, frontRight);

    roboDrive.setDeadband(RobotMap.DEADBAND);
    setDefaultCommand(driveCommand);
  }

  @Override
  public void periodic() {
    double value = encoder1.getPosition();
    double value2 = encoder2.getPosition();
    double value3 = encoder3.getPosition();
    double value4 = encoder4.getPosition();
    i++;
    if (i == 5) {i = 0;}
    //System.out.println("The value of encoder 1 is: " + value);
    //System.out.println("The value of encoder 2 is: " + value2);
    //System.out.println("The value of encoder 3 is: " + value3);
    //System.out.println("The value of encoder 4 is: " + value4);

    // This method will be called once per scheduler run
  }

  public void resetEncoder(CANEncoder encoder) {
    old_E_Value = encoder.getPosition();
    traveledInches = 0;
    traveledFeet = 0;
  }

  public double encoderDistance(CANEncoder encoder) {
    double traveledInches = 0;
    current_E_Value = encoder.getPosition();
    double traveledUnits = (current_E_Value - old_E_Value);
    traveledInches = toInches(traveledUnits);
    old_E_Value = current_E_Value;
    if (printIterator() == true) {
      System.out.println("Traveled " + traveledInches + "Inches");
    }
    return traveledInches;
  }

  public double improvedEncoderDist(CANEncoder encoder) { //Now a constantly saved number (rather than constantly recalculated from 0), and returns distance in feet, making autonomous code look a lot nicer
    current_E_Value = encoder.getPosition();
    double traveledUnits = (current_E_Value - old_E_Value);
    traveledFeet = toFeet(traveledUnits);
    if (printIterator() == true) {
      System.out.println("Improved encoderDistance method is working. Output: " + traveledFeet + " Feet since last reset.");
    }

    return traveledFeet;
  }

  private double toInches(double encoderValue)  {
    return (encoderValue * RobotMap.getEncoderConstant());
  }

  public double toFeet(double encoderValue) {
    return (toInches(encoderValue) / 12);
  }

  /**
   * Given a target number, current number, and increment, adjust current number by increment until we reach target
   * This is useful particularly in {@link frc.robot.commands.SMDrive} where we need to ramp up to user input to avoid jerkiness
   *
   * @param target The number you eventually want to get to (ie. joystick speed)
   * @param current The current number you are at (so we know what to start at for the increment)
   * @param increment How much to increase current by until current = target
   *
   * @see <a href="https://en.wikipedia.org/wiki/Slew_rate">Wikipedia article on Slew rates</a>
   * @return Adjusted target
   */
  public static double slewLimit(double target, double current, double increment) {
    increment = Math.abs(increment); // Professionally validating user input right here 👌
    double change = target - current;
    if (Math.abs(current) > Math.abs(target)) return target; // Always slow down immediately for safety concerns
    if (change > increment) { change = increment; }
    else if (change < -increment) { change = -increment; }
    return current + change;
  }

}
