/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.DriveSubsystem;

import static frc.robot.RobotContainer.driveSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  private LightSensor blightsensor = new LightSensor();
  private DriveSubsystem m_encoderobject = new DriveSubsystem();
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    m_colorSensor.sensorInit();
    initCamera();
  }
  private void initCamera() {
    CameraServer cs = CameraServer.getInstance();
    UsbCamera frontCamera = cs.startAutomaticCapture();
    UsbCamera backCamera = cs.startAutomaticCapture();

    frontCamera.setResolution(256, 144);
    frontCamera.setFPS(30);
    backCamera.setResolution(256, 144);
    backCamera.setFPS(30);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    //m_colorSensor.testFunction();
    //blightsensor.printOut();
    if(ConfigureBed.getInstance().configBedInit()== ConfigureBed.Jumper.ONE){
      System.out.println("this is a test; 1");
    }
    else if(ConfigureBed.getInstance().configBedInit()== ConfigureBed.Jumper.TWO){
      System.out.println("this is a test; 2");
    }
    else if(ConfigureBed.getInstance().configBedInit()== ConfigureBed.Jumper.THREE){
      System.out.println("this is a test; 3");
    }
    else{
      System.out.println("this is a test; 4");
    }

  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }
  private final ColorSensor m_colorSensor = new ColorSensor();

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }



  }

  /**
   * This function is called periodically during operator control.
   */

  @Override
  public void teleopPeriodic() {




  }





  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
  public static void initializeSparkDefaults(CANSparkMax... sparks) {
    for (CANSparkMax spark : sparks) {
      spark.setSmartCurrentLimit(RobotMap.MAX_MOTOR_STALL_AMPS, RobotMap.MAX_MOTOR_FREE_AMPS);

    }
  }

}
