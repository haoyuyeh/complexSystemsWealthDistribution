
/**
 * this class using GUI to obtain values of parameters and run the simulation of
 * wealth distribution with different extensions
 * 
 * group members: 782692 Hao Yu Yeh coding 
 * 				  741805 Zheng Chai 
 *				  734507 Jiaying Li report
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author haoyu
 *
 */
@SuppressWarnings("serial")
public class Simulation extends JFrame {

	/**
	 * GUI variable used to get the related parameters
	 */
	private JPanel mainPane;
	private String[] functionNames = { "original", "inheritable feature" };
	private JTextField tickTextField;
	private JTextField peopleTextField;
	private JTextField visionTextField;
	private JTextField metabolismTextField;
	private JTextField lifeMinTextField;
	private JTextField lifeMaxTextField;
	private JTextField bestLandTextField;
	private JTextField grainGrowthIntervalTextField;
	private JTextField numGrainGrowthTextField;
	private JTextField inheritedRateTextField;
	private JLabel inheritedRate;
	private JSlider inheritedRateSlider;
	private boolean isSetup = false;

	/**
	 * simulation parameters
	 */
	// numbers of people participate in the simulation
	public int PEOPLE;
	// current tick
	public int ticks = 0;
	// how many ticks the simulation will run
	public int numOfTicks = 0;
	// maximum amount a land can hold
	public static int MAX_GRAIN = 50;
	// the percent of best land
	public static double percent_best_land;
	// 2D array with all locations
	public Land[][] all_Location;
	// array of people
	public Person[] all_People;
	// Size of the grid
	private int NUM_COLUMNS = 50;
	private int NUM_ROWS = 50;
	// spread the grains at beginning
	public OriginalGrainSpreadMethod diffuseMethod;
	// deciding the amount of diffusing grains
	public static double DIFFUSE_PERCENT = 25;
	// indicating current setting
	private String currentFunction = "original";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulation frame = new Simulation();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("rawtypes")
	public Simulation() {
		/**
		 * GUI part
		 */
		// frame setting
		super("Wealth Distribution Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 788, 598);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new GridLayout(0, 2, 0, 0));
		setContentPane(mainPane);
		// deciding the which setting is applied in simulation
		JLabel modelSetting = new JLabel("model setting:");
		modelSetting.setHorizontalAlignment(SwingConstants.CENTER);
		modelSetting.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		mainPane.add(modelSetting);

		@SuppressWarnings("unchecked")
		JComboBox functionComboBox = new JComboBox(functionNames);
		functionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				currentFunction = (String) cb.getSelectedItem();
				// only showing the parameters needed by the setting
				if (currentFunction.equals("inheritable feature")) {
					inheritedRate.setVisible(true);
					inheritedRateTextField.setVisible(true);
					inheritedRateSlider.setVisible(true);
				} else if (currentFunction.equals("original")) {
					inheritedRate.setVisible(false);
					inheritedRateTextField.setVisible(false);
					inheritedRateSlider.setVisible(false);
				}
			}
		});
		functionComboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		functionComboBox.setMaximumRowCount(2);
		mainPane.add(functionComboBox);

		// deciding how many tick it will run
		JPanel tickPanel = new JPanel();
		mainPane.add(tickPanel);
		tickPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel tick = new JLabel("Ticks: ");
		tick.setHorizontalAlignment(SwingConstants.RIGHT);
		tickPanel.add(tick);
		tick.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));

		tickTextField = new JTextField();
		tickTextField.setBorder(null);
		tickTextField.setForeground(Color.BLACK);
		tickTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		tickTextField.setHorizontalAlignment(SwingConstants.LEFT);
		tickTextField.setEditable(false);
		tickPanel.add(tickTextField);
		tickTextField.setColumns(10);

		JSlider tickSlider = new JSlider();
		tickSlider.setPaintTicks(true);
		tickSlider.setValue(1000);
		tickSlider.setMajorTickSpacing(1000);
		tickSlider.setPaintLabels(true);
		tickSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				tickTextField.setText(Integer.toString(source.getValue()));
			}
		});
		tickSlider.setMinimum(10);
		tickSlider.setMaximum(10000);
		mainPane.add(tickSlider);

		// deciding how many people will be involved
		JPanel peoplePanel = new JPanel();
		mainPane.add(peoplePanel);
		peoplePanel.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel numPeople = new JLabel("num-people: ");
		numPeople.setHorizontalAlignment(SwingConstants.RIGHT);
		peoplePanel.add(numPeople);
		numPeople.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));

		peopleTextField = new JTextField();
		peopleTextField.setHorizontalAlignment(SwingConstants.LEFT);
		peopleTextField.setBorder(null);
		peopleTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		peopleTextField.setEditable(false);
		peoplePanel.add(peopleTextField);
		peopleTextField.setColumns(10);

		JSlider peopleSlider = new JSlider();
		peopleSlider.setValue(250);
		peopleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				peopleTextField.setText(Integer.toString(source.getValue()));
			}
		});
		peopleSlider.setMajorTickSpacing(100);
		peopleSlider.setPaintLabels(true);
		peopleSlider.setMaximum(1000);
		peopleSlider.setMinimum(2);
		peopleSlider.setPaintTicks(true);
		mainPane.add(peopleSlider);

		// deciding the max vision a person can have
		JPanel visionPanel = new JPanel();
		mainPane.add(visionPanel);
		visionPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel maxVision = new JLabel("max-vision: ");
		maxVision.setHorizontalAlignment(SwingConstants.RIGHT);
		visionPanel.add(maxVision);
		maxVision.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));

		visionTextField = new JTextField();
		visionTextField.setHorizontalAlignment(SwingConstants.LEFT);
		visionTextField.setBorder(null);
		visionTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		visionTextField.setEditable(false);
		visionPanel.add(visionTextField);
		visionTextField.setColumns(10);

		JSlider visionSlider = new JSlider();
		visionSlider.setValue(5);
		visionSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				visionTextField.setText(Integer.toString(source.getValue()));
			}
		});
		visionSlider.setPaintLabels(true);
		visionSlider.setMajorTickSpacing(5);
		visionSlider.setMinimum(1);
		visionSlider.setMaximum(15);
		visionSlider.setPaintTicks(true);
		mainPane.add(visionSlider);

		// deciding the max metabolism a person can have
		JPanel metabolismPanel = new JPanel();
		mainPane.add(metabolismPanel);
		metabolismPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel metabolismMax = new JLabel("metabolism-max: ");
		metabolismMax.setHorizontalAlignment(SwingConstants.RIGHT);
		metabolismPanel.add(metabolismMax);
		metabolismMax.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));

		metabolismTextField = new JTextField();
		metabolismTextField.setHorizontalAlignment(SwingConstants.LEFT);
		metabolismTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		metabolismTextField.setBorder(null);
		metabolismTextField.setEditable(false);
		metabolismPanel.add(metabolismTextField);
		metabolismTextField.setColumns(10);

		JSlider metabolismSlider = new JSlider();
		metabolismSlider.setValue(15);
		metabolismSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				metabolismTextField
						.setText(Integer.toString(source.getValue()));
			}
		});
		metabolismSlider.setPaintLabels(true);
		metabolismSlider.setMajorTickSpacing(5);
		metabolismSlider.setMaximum(25);
		metabolismSlider.setMinimum(1);
		metabolismSlider.setPaintTicks(true);
		mainPane.add(metabolismSlider);

		// deciding the minimum life expectancy of a person
		JPanel lifePanel = new JPanel();
		mainPane.add(lifePanel);
		lifePanel.setLayout(new GridLayout(2, 0, 0, 0));

		JLabel lifeExpectancyMin = new JLabel("life-expectancy-min: ");
		lifeExpectancyMin.setHorizontalAlignment(SwingConstants.RIGHT);
		lifePanel.add(lifeExpectancyMin);
		lifeExpectancyMin.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));

		lifeMinTextField = new JTextField();
		lifeMinTextField.setEditable(false);
		lifeMinTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lifeMinTextField.setHorizontalAlignment(SwingConstants.LEFT);
		lifeMinTextField.setBorder(null);
		lifePanel.add(lifeMinTextField);
		lifeMinTextField.setColumns(10);

		// deciding the maximum life expectancy of a person
		JLabel lifeExpectancyMax = new JLabel("life-expectancy-max: ");
		lifeExpectancyMax.setHorizontalAlignment(SwingConstants.RIGHT);
		lifeExpectancyMax.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lifePanel.add(lifeExpectancyMax);

		lifeMaxTextField = new JTextField();
		lifeMaxTextField.setHorizontalAlignment(SwingConstants.LEFT);
		lifeMaxTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lifeMaxTextField.setEditable(false);
		lifeMaxTextField.setBorder(null);
		lifePanel.add(lifeMaxTextField);
		lifeMaxTextField.setColumns(10);

		JPanel lifeSliderPanel = new JPanel();
		mainPane.add(lifeSliderPanel);
		lifeSliderPanel.setLayout(new GridLayout(2, 0, 0, 0));

		JSlider lifeMinSlider = new JSlider();
		lifeMinSlider.setValue(1);
		lifeMinSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				lifeMinTextField.setText(Integer.toString(source.getValue()));
			}
		});
		lifeMinSlider.setMajorTickSpacing(10);
		lifeMinSlider.setPaintLabels(true);
		lifeMinSlider.setMinimum(1);
		lifeMinSlider.setPaintTicks(true);
		lifeSliderPanel.add(lifeMinSlider);

		JSlider lifeMaxSlider = new JSlider();
		lifeMaxSlider.setValue(83);
		lifeMaxSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				lifeMaxTextField.setText(Integer.toString(source.getValue()));
			}
		});
		lifeMaxSlider.setPaintLabels(true);
		lifeMaxSlider.setMajorTickSpacing(10);
		lifeMaxSlider.setMinimum(1);
		lifeSliderPanel.add(lifeMaxSlider);
		lifeMaxSlider.setPaintTicks(true);

		// deciding the amount of best land
		JPanel bestLandPanel = new JPanel();
		mainPane.add(bestLandPanel);
		bestLandPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel bestLandPercent = new JLabel("percent-best-land(%): ");
		bestLandPercent.setHorizontalAlignment(SwingConstants.RIGHT);
		bestLandPercent.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		bestLandPanel.add(bestLandPercent);

		bestLandTextField = new JTextField();
		bestLandTextField.setEditable(false);
		bestLandTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		bestLandTextField.setHorizontalAlignment(SwingConstants.LEFT);
		bestLandTextField.setBorder(null);
		bestLandPanel.add(bestLandTextField);
		bestLandTextField.setColumns(10);

		JSlider bestLandSlider = new JSlider();
		bestLandSlider.setValue(10);
		bestLandSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				bestLandTextField.setText(Integer.toString(source.getValue()));
			}
		});
		bestLandSlider.setMaximum(25);
		bestLandSlider.setMajorTickSpacing(5);
		bestLandSlider.setPaintLabels(true);
		bestLandSlider.setMinimum(5);
		bestLandSlider.setSnapToTicks(true);
		bestLandSlider.setPaintTicks(true);
		mainPane.add(bestLandSlider);

		// deciding the growing interval of grain
		JPanel grainPanel = new JPanel();
		mainPane.add(grainPanel);
		grainPanel.setLayout(new GridLayout(2, 0, 0, 0));

		JLabel grainGrowthInterval = new JLabel("grain-growth-interval: ");
		grainGrowthInterval.setHorizontalAlignment(SwingConstants.RIGHT);
		grainGrowthInterval.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		grainPanel.add(grainGrowthInterval);

		grainGrowthIntervalTextField = new JTextField();
		grainGrowthIntervalTextField.setEditable(false);
		grainGrowthIntervalTextField
				.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		grainGrowthIntervalTextField
				.setHorizontalAlignment(SwingConstants.LEFT);
		grainGrowthIntervalTextField.setBorder(null);
		grainPanel.add(grainGrowthIntervalTextField);
		grainGrowthIntervalTextField.setColumns(10);

		// deciding the amount of grain being grown within an interval
		JLabel numGrainGrowth = new JLabel("num-grain-growth: ");
		numGrainGrowth.setHorizontalAlignment(SwingConstants.RIGHT);
		numGrainGrowth.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		grainPanel.add(numGrainGrowth);

		numGrainGrowthTextField = new JTextField();
		numGrainGrowthTextField.setEditable(false);
		numGrainGrowthTextField.setHorizontalAlignment(SwingConstants.LEFT);
		numGrainGrowthTextField
				.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		numGrainGrowthTextField.setBorder(null);
		grainPanel.add(numGrainGrowthTextField);
		numGrainGrowthTextField.setColumns(10);

		JPanel grainSliderPanel = new JPanel();
		mainPane.add(grainSliderPanel);
		grainSliderPanel.setLayout(new GridLayout(2, 0, 0, 0));

		JSlider grainGrowthIntervalSlider = new JSlider();
		grainGrowthIntervalSlider.setValue(1);
		grainGrowthIntervalSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				grainGrowthIntervalTextField
						.setText(Integer.toString(source.getValue()));
			}
		});
		grainGrowthIntervalSlider.setPaintLabels(true);
		grainGrowthIntervalSlider.setMajorTickSpacing(1);
		grainGrowthIntervalSlider.setMaximum(10);
		grainGrowthIntervalSlider.setMinimum(1);
		grainGrowthIntervalSlider.setSnapToTicks(true);
		grainGrowthIntervalSlider.setPaintTicks(true);
		grainSliderPanel.add(grainGrowthIntervalSlider);

		JSlider numGrainGrowthSlider = new JSlider();
		numGrainGrowthSlider.setValue(4);
		numGrainGrowthSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				numGrainGrowthTextField
						.setText(Integer.toString(source.getValue()));
			}
		});
		numGrainGrowthSlider.setMinimum(1);
		numGrainGrowthSlider.setMajorTickSpacing(1);
		numGrainGrowthSlider.setMaximum(10);
		numGrainGrowthSlider.setPaintLabels(true);
		numGrainGrowthSlider.setSnapToTicks(true);
		numGrainGrowthSlider.setPaintTicks(true);
		grainSliderPanel.add(numGrainGrowthSlider);

		// press this button to set up all parameters
		JButton setupButton = new JButton("setup");
		setupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PEOPLE = Integer.parseInt(peopleTextField.getText());

				Person.vision_max = Integer.parseInt(visionTextField.getText());

				Person.metabolism_max =
						Integer.parseInt(metabolismTextField.getText());

				Person.life_expectancy_min =
						Integer.parseInt(lifeMinTextField.getText());

				Person.life_expectancy_max =
						Integer.parseInt(lifeMaxTextField.getText());

				percent_best_land =
						Integer.parseInt(bestLandTextField.getText());

				Land.grain_grow_interval = Integer
						.parseInt(grainGrowthIntervalTextField.getText());

				Land.num_grain_grown =
						Integer.parseInt(numGrainGrowthTextField.getText());

				numOfTicks = Integer.parseInt(tickTextField.getText());

				if (currentFunction.equals("inheritable feature")) {
					InheritablePerson.inheritedRate =
							Integer.parseInt(inheritedRateTextField.getText());
				}
				isSetup = true;
				JOptionPane.showMessageDialog(null, "setup finished.");
			}
		});

		// deciding how many percent of feature a person can inherit
		JPanel inheritedPanel = new JPanel();
		mainPane.add(inheritedPanel);
		inheritedPanel.setLayout(new GridLayout(0, 2, 0, 0));

		inheritedRate = new JLabel("inherited rate: ");
		inheritedRate.setVisible(false);
		inheritedRate.setHorizontalAlignment(SwingConstants.RIGHT);
		inheritedRate.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		inheritedPanel.add(inheritedRate);

		inheritedRateTextField = new JTextField();
		inheritedRateTextField.setVisible(false);
		inheritedRateTextField.setHorizontalAlignment(SwingConstants.LEFT);
		inheritedRateTextField.setForeground(Color.BLACK);
		inheritedRateTextField
				.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		inheritedRateTextField.setEditable(false);
		inheritedRateTextField.setColumns(10);
		inheritedRateTextField.setBorder(null);
		inheritedPanel.add(inheritedRateTextField);

		inheritedRateSlider = new JSlider();
		inheritedRateSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				inheritedRateTextField
						.setText(Integer.toString(source.getValue()));
			}
		});
		inheritedRateSlider.setVisible(false);
		inheritedRateSlider.setSnapToTicks(true);
		inheritedRateSlider.setPaintTicks(true);
		inheritedRateSlider.setPaintLabels(true);
		inheritedRateSlider.setMinimum(10);
		inheritedRateSlider.setMajorTickSpacing(5);
		mainPane.add(inheritedRateSlider);
		setupButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		mainPane.add(setupButton);

		// pressing this button to start simulation
		JButton runButton = new JButton("run");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isSetup) {
					// preparing the grid
					setupLocations(NUM_COLUMNS, NUM_ROWS);

					// adding person to the grid
					setupPersons(PEOPLE);

					// using to save statistics in a file
					String strPoor = "";
					String strMiddle = "";
					String strRich = "";

					// the main loop to run the simulation
					for (int i = 0; i < numOfTicks; i++) {
						update();

						// the code next counts the amount of rich, middle and
						// poor
						// people after each tick
						int rich = 0, rm = 0, rw = 0, rv = 0;
						int poor = 0, pm = 0, pw = 0, pv = 0;
						int middle = 0, mm = 0, mw = 0, mv = 0;
						for (Person p : all_People) {
							if (p.myClass == 'r') {
								rich++;
								if (currentFunction
										.equals("inheritable feature")) {
									switch (((InheritablePerson) p).inheritedFeature) {
									case METABOLISM:
										rm++;
										break;
									case WEALTH:
										rw++;
										break;
									case VISION:
										rv++;
										break;
									default:
									}
								}
							}
							if (p.myClass == 'm') {
								middle++;
								if (currentFunction
										.equals("inheritable feature")) {
									switch (((InheritablePerson) p).inheritedFeature) {
									case METABOLISM:
										mm++;
										break;
									case WEALTH:
										mw++;
										break;
									case VISION:
										mv++;
										break;
									default:
									}
								}
							}
							if (p.myClass == 'p') {
								poor++;
								if (currentFunction
										.equals("inheritable feature")) {
									switch (((InheritablePerson) p).inheritedFeature) {
									case METABOLISM:
										pm++;
										break;
									case WEALTH:
										pw++;
										break;
									case VISION:
										pv++;
										break;
									default:
									}
								}
							}
						}
						// recording the values of each tick
						if (currentFunction.equals("inheritable feature")) {
							strPoor +=
									poor + "," + pm + "," + pw + "," + pv + ",";
							strMiddle += middle + "," + mm + "," + mw + "," + mv
									+ ",";
							strRich +=
									rich + "," + rm + "," + rw + "," + rv + ",";
						} else {
							strPoor += poor + ",";
							strMiddle += middle + ",";
							strRich += rich + ",";
						}
					}
					// Saving the files
					savetoFile(strPoor.substring(0, strPoor.length() - 1),
							"poor");
					savetoFile(strMiddle.substring(0, strMiddle.length() - 1),
							"middle");
					savetoFile(strRich.substring(0, strRich.length() - 1),
							"rich");
					isSetup = false;
					JOptionPane.showMessageDialog(null, "program end");
					// Program End
				} else {
					JOptionPane.showMessageDialog(null, "setup not finished.");
				}
			}
		});
		runButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		mainPane.add(runButton);

		/**
		 * simulation part
		 */
		diffuseMethod = new OriginalGrainSpreadMethod();
		MovingMethod.NUM_COLUMNS = this.NUM_COLUMNS;
		MovingMethod.NUM_ROWS = this.NUM_ROWS;
	}

	/**
	 * saving statistics generated from simulation
	 * 
	 * @param text
	 *            string to save
	 * @param name
	 *            file name
	 */
	public void savetoFile(String text, String name) {
		String path = Simulation.class.getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		String OS = System.getProperty("os.name").toLowerCase();

		if (path.contains("!")) {
			path = path.substring(0, path.indexOf("!"));
		}

		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		path = path.substring(0, path.lastIndexOf("/") + 1) + name + ".csv";
		// for windows os
		path = path.substring(1).replaceAll("%20", " ");
		// for mac os
		if (OS.indexOf("mac") >= 0) {
			path = "/" + path;
		}
		try {
			File file = new File(path);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(text);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * rules of simulation to be updated for each tick First,
	 */
	public void update() {
		// each person look around for the direction to move next depending of
		// the vision
		for (Person p : all_People) {
			p.updateLocation(all_Location);
		}

		// Checking for all lands. if no person is in the land, keep the number
		// of grains; if more than one person is in the land, the grains are
		// distributed evenly
		for (int i = 0; i < NUM_COLUMNS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {
				all_Location[i][j].harvest();
			}
		}

		// Checking if people are alive, if death reset parameters
		for (Person p : all_People) {
			p.Update();
		}

		// Update the class of each person, determining poor, middle and rich
		Person.updateClass(all_People);

		// growing grains on the land depending on the frequency defined by
		// grain_grow_interval.
		if ((ticks % Land.grain_grow_interval) == 0) {
			for (int i = 0; i < NUM_COLUMNS; i++) {
				for (int j = 0; j < NUM_ROWS; j++) {
					all_Location[i][j].grow_grain();
				}
			}
		}
		// update the number of ticks
		ticks = ticks + 1;
	}

	/**
	 * creating persons and locating them on the grid randomly
	 */
	public void setupPersons(int numPeople) {
		// different setting uses different type of person
		if (currentFunction.equals("inheritable feature")) {
			all_People = new InheritablePerson[numPeople];
			for (int i = 0; i < all_People.length; i++) {
				all_People[i] = new InheritablePerson();
			}
		} else {
			all_People = new Person[numPeople];
			for (int i = 0; i < all_People.length; i++) {
				all_People[i] = new Person();
			}
		}

		for (Person p : all_People) {
			p.col = randomInt(0, NUM_COLUMNS - 1);
			p.row = randomInt(0, NUM_ROWS - 1);
		}
	}

	/**
	 * starting a new 2D array of lands and creating new object for each land as
	 * well as dispersing grains over the grid
	 * 
	 * @param col
	 * @param row
	 */
	public void setupLocations(int col, int row) {
		all_Location = new Land[col][row];
		for (int i = 0; i < col; i++) {
			for (int j = 0; j < row; j++) {
				all_Location[i][j] = new Land();
			}
		}
		diffuseMethod.spreadGrains(all_Location, col, row);
	}

	/**
	 * Return a random int between min and max including both
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		int range = max - min;
		return (int) (Math.random() * (range + 1) + min);
	}
}
